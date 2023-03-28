import itertools

def load_map(file_name):
    map = {}
    for line in open(file_name, 'rt', encoding='shift-jis').readlines():
        split_items = line.strip('\n').split('=')
        if len(split_items) > 1:
            map[bytes.fromhex(split_items[0])] = split_items[1]
    return map
        
def load_map_reverse(file_name):
    map = {}
    for line in open(file_name, 'rt', encoding='shift-jis').readlines():
        split_items = line.strip('\n').split('=')
        if len(split_items) > 1:
            map[split_items[1]] = bytes.fromhex(split_items[0])
    return map

def map_char(ch, map, unknown_chars = None):
    if ch in map:
        return map[ch]
    else:
        if unknown_chars is not None:
            unknown_chars.add(ch)
        return b'\x00'
        
def consume_char(line, map, unknown_chars = None):
    if line[0] == '[':
        end_index = line.find(']')
        if end_index < 0:
            return line[1:], b'\x00'

        ch = line[1:end_index]
        
        if ch.startswith('#'):
            return line[end_index + 1:], bytes.fromhex(ch[1:])
        else:
            return line[end_index + 1:], map_char(ch, map, unknown_chars)
    else:
        return line[1:], map_char(line[0], map, unknown_chars)
    
def encode_text_interleaved(text, reverse_map, include_header = True, other_flag = 0x1):
    if len(text) == 0:
        return b'\x00\x00\xff\xff' if include_header else b''

    out = bytearray()
    lines = text.splitlines()
    if len(lines) < 2:
        lines.append('')
        
    out_lines = []
    for line in lines:
        out_line = bytearray()
        while len(line) > 0:
            line, ch = consume_char(line, reverse_map)
            out_line += ch
        out_lines.append(bytes(out_line))
    
    if include_header:
        offset = 0xe5 + (22 - max(len(out_lines[0]), len(out_lines[1]))) // 2
        out += offset.to_bytes(2, byteorder='little')
        out += other_flag.to_bytes(2, byteorder='little')
            
    for top, bottom in itertools.zip_longest(out_lines[0], out_lines[1], fillvalue=0):
        out += top.to_bytes(1, byteorder='little')
        out += bottom.to_bytes(1, byteorder='little')
        
    out += b'\xff'
        
    return bytes(out)
    
def encode_text(text, reverse_map, newline=b'\xfe', terminator=b'\xff', pad_to_line_count=1, pad_final_line=False):
    out = bytearray()
    lines = text.splitlines()
    
    appended_lines = 0
    while len(lines) % pad_to_line_count != 0:
        lines.append('')
        appended_lines += 1
    if appended_lines > 0:
        print('Warning: Text beginning with \'{0}\' needed {1} newlines appended.'.format(lines[0] if len(lines) > 0 else '', appended_lines))
    
    unknown_chars = set()
    current_out_length = 0
    prev_out_length = 0
    for line in lines:
        prev_out_length = current_out_length
        current_out_length = 0

        if line != line.rstrip():
            print('Warning: Line \'{0}\' had trailing whitespace.'.format(line))
            line = line.rstrip()
        while len(line) > 0:
            line, ch = consume_char(line, reverse_map, unknown_chars)
            out += ch

            current_out_length += len(ch)

        out += newline
        
    if len(unknown_chars) > 0:
        print('Warning: Text beginning with \'{0}\' had unknown chars {1}'.format(lines[0] if len(lines) > 0 else '', unknown_chars))
    
    
        
    if len(out) > len(newline):
        out = out[0:len(out) - len(newline)]
        
        if pad_final_line and prev_out_length > current_out_length:
            print('Warning: Needed to pad final line of \'{0}\' from {1} to {2}.'.format(lines[0] if len(lines) > 0 else '', current_out_length, prev_out_length))
            
            for _ in range(prev_out_length - current_out_length):
                out += b'\x00'
    out += terminator
    
    return bytes(out)
