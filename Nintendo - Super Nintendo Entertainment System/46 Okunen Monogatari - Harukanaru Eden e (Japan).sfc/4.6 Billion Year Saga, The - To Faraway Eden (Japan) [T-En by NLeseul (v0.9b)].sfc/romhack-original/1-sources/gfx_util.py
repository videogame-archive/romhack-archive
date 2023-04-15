def find_pattern_match_length(input, input_offset, match_offset, max_length):
    length = 0
    
    while length <= max_length and input_offset + length < len(input) and match_offset + length < len(input) and input[input_offset + length] == input[match_offset + length]:
        length += 1
        
    return length

def find_pattern(input, input_offset, max_distance, max_length):
    match_offset = input_offset - 1
    
    best_offset = input_offset
    best_length = 0
    
    while match_offset >= 0 and input_offset - match_offset < max_distance:
        length_found = find_pattern_match_length(input, input_offset, match_offset, max_length)
        if length_found > 0 and length_found > best_length:
            best_offset = match_offset
            best_length = length_found
        match_offset -= 1
    
    return (best_offset, best_length) if best_length > 0 else None

def compress(input):
    
    base_size = 3
    
    output = bytearray()
    header = (0x80 + 3)
    output += header.to_bytes(1, byteorder='little') # Header, 1 byte
    
    output += len(input).to_bytes(2, byteorder='little')
    
    input_offset = 0
    
    current_bitmask_bit = 8
    current_bitmask_offset = 0
    
    while input_offset < len(input):
    
        if current_bitmask_bit >= 8:
            #print('Committing bitmask at {0:x} as {1:x}'.format(current_bitmask_offset, output[current_bitmask_offset]))

            current_bitmask_bit = 0
            current_bitmask_offset = len(output)
            output += (0x0).to_bytes(1, byteorder='little')
            #print('New bitmask at {0:x}'.format(current_bitmask_offset))
            
        best_match = find_pattern(input, input_offset, 0xfff, base_size + 0xf + 0xff - 1)
        
        if best_match and best_match[1] >= base_size:
            #print('Pattern found: ' + str(best_match))
            
            run_size = best_match[1] - base_size
            overflow = 0
            if run_size >= 0xf:
                overflow = best_match[1] - 0xf - base_size
                run_size = 0xf
                #print('NEED MOAR {0} {1}'.format(best_match[1], overflow))
            
            offset = (input_offset - best_match[0] - 1)
            #print('Run size will be {0:x} with {1:1} overflow'.format(run_size, overflow))
            pattern_word = ((run_size & 0xf) << 12) + (offset & 0xfff)
            #print('{0:x}'.format(pattern_word))
            output += pattern_word.to_bytes(2, byteorder='little')
            
            if run_size == 0xf:
                output += overflow.to_bytes(1, byteorder='little')
            
            input_offset += best_match[1]
        else:
            #print('Verbatim {0:x}'.format(input[input_offset]))
            output += input[input_offset].to_bytes(1, byteorder='little')
            input_offset += 1
            
            output[current_bitmask_offset] = output[current_bitmask_offset] | (1 << current_bitmask_bit)
            
        current_bitmask_bit += 1
    
    return bytes(output)