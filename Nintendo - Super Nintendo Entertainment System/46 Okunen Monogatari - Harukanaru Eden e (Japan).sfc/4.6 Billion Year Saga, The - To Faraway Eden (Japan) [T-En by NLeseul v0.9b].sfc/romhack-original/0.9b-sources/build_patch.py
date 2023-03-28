#!/usr/bin/python3

import os
import subprocess
import sys

import csv
from ips_util import Patch

import text_util
import gfx_util

class StringPool:
    def __init__(self, address, capacity):
        self.address = address
        self.capacity = capacity

        self.pool = bytearray()

    def can_add(self, bytes):
        return len(self.pool) + len(bytes) < self.capacity

    def add(self, bytes):
        start = len(self.pool) + self.address

        self.pool += bytes

        return start

    def free_space(self):
        return self.capacity - len(self.pool)

    def get_bytes(self):
        return self.pool

def num_8bit(num):
    return num.to_bytes(1, byteorder='little')

def num_16bit(num):
    return num.to_bytes(2, byteorder='little')

def num_24bit(num):
    return num.to_bytes(3, byteorder='little')

def write_with_size_check(patch, address, available_length, data, fill_byte=b'\x00'):
    difference = available_length - len(data)
    if difference < 0:
        raise Exception('Not enough space for data! Received {0} bytes, but only have space allocated for {1}.'.format(len(data), available_length))

    patch.add_record(address, data)

    if difference > 0:
        patch.add_rle_record(address + len(data), fill_byte, difference)


def write_strings_from_csv(patch, filename, reverse_font_map, pointer_table_address, pointer_table_length,
                           string_pool_address, string_pool_length, overflow_pool_address = None, overflow_pool_length = None,
                           column_to_encode=4, newline=b'\xfe', terminator=b'\xff', pad_to_line_count=1, pad_final_line=False, interleaved=False):
    print('Writing strings from {0}...'.format(filename))


    pointer_table_out = bytearray()
    previously_encoded = {}

    pools = [StringPool(string_pool_address, string_pool_length)]

    if overflow_pool_address is not None and overflow_pool_length is not None:
        pools.append(StringPool(overflow_pool_address, overflow_pool_length))

    with open(filename, 'r', encoding='shift-jis') as in_file:
        reader = csv.reader(in_file, lineterminator='\n')
        for i, row in enumerate(reader):
            if interleaved:
                # This is only used for area names, which have some special flags that need to be set, except for index 15.
                flag_map = {7: 0x2, 9: 0x4, 10: 0x8, 16: 0x8}
                encoded_string = text_util.encode_text_interleaved(row[4], reverse_font_map, i != 15, flag_map[i] if i in flag_map else 0x1)
            else:
                encoded_string = text_util.encode_text(row[column_to_encode], reverse_font_map,
                                                       pad_to_line_count=pad_to_line_count, pad_final_line=pad_final_line,
                                                       newline=newline, terminator=terminator)

            is_tiny = (len(encoded_string) < 100)

            string_address = None
            if encoded_string in previously_encoded:
                string_address = previously_encoded[encoded_string]
            else:
                for pool in (reversed(pools) if is_tiny else pools):
                    if pool.can_add(encoded_string):
                        string_address = (0xffff & pool.add(encoded_string))
                        break

                if string_address is not None:
                    previously_encoded[encoded_string] = string_address

            if string_address is None:
                print('Text {0} didn\'t fit! Size was {1}'.format(row[4], len(row[4])))
                for poolIndex, pool in enumerate(pools):
                    print('Pool {0}: {1} free'.format(poolIndex, pool.free_space()))
                pointer_table_out += (0xffff).to_bytes(2, byteorder='little')
            else:
                pointer_table_out += string_address.to_bytes(2, byteorder='little')

    write_with_size_check(patch, pointer_table_address, pointer_table_length, pointer_table_out)
    for poolIndex, pool in enumerate(pools):
        write_with_size_check(patch, pool.address, pool.capacity, pool.get_bytes(), fill_byte=b'\xff')
        print('Remaining in pool at {0:x}: {1}'.format(pool.address, pool.free_space()))

def write_gfx(patch, data, address, length):
    write_with_size_check(patch, address, length, gfx_util.compress(data))

def write_gfx_from_file(patch, filename, address, length):
    with open(filename, 'rb') as f:
        write_gfx(patch, f.read(), address, length)

def write_code(patch, filename, address, length):
    tmp_filename = 'build/_tmp.a65'
    result = subprocess.run(['xa', '-o', tmp_filename, '-w', filename], shell=True, stderr=subprocess.PIPE)
    if result.returncode == 0:
        with open(tmp_filename, 'rb') as tmp_file:
            write_with_size_check(patch, address, length, tmp_file.read(), fill_byte=b'\xea')
        os.remove(tmp_filename)
    else:
        raise Exception('Assembler failed on {0} with error code {1}:\n\nErrors:\n{2}'.format(filename, result.returncode, result.stderr.decode(sys.stderr.encoding)))

def write_dialog_choice_entry(patch, address, dialog_index=None, page_index=None, options=None, dest1=None, dest2=None, dest3=None, first_option=None):
    # Dialog choice data consists of 7 words:
    #  0: Dialog index.
    #  1: Line index. Should be a multiple of 6 for the window height.
    #  2: Number of options. If this is 0, it simply redirects the dialog to the first destination automatically.
    #  3: Destination line for option 1. Note that the destination lines need to be 6 lines (1 page) before the intended displayed line, as the line counter still gets advanced by 6 after the redirect.
    #  4: Destination line for option 2.
    #  5: Destination line for option 3, probably. Unused.
    #  6: Index of first option. If this is 1, the space allocated for the first option is assumed to be static text instead and becomes unselectable.

    if dialog_index is not None:
        patch.add_record(address,     num_16bit(dialog_index))
    if page_index is not None:
        patch.add_record(address + 2, num_16bit(page_index * 6))
    if options is not None:
        patch.add_record(address + 4, num_16bit(options))
    for index, dest in enumerate([dest1, dest2, dest3]):
        if dest is not None:
            patch.add_record(address + 6 + (index * 2), b'\xff\xff' if dest == 0xffff else num_16bit((dest - 1) * 6))
    if first_option is not None:
        patch.add_record(address + 12, num_16bit(first_option))

def write_hdma_table_entry(patch, base_address, entry_index, row_count=None, left_value=None, right_value=None):
    # The HDMA tables we care about all modify the window registers to form the shapes of window
    # backgrounds. Each entry is three bytes: Number of rows affected, left bound, right bound.
    entry_address = base_address + 3 * entry_index
    if row_count is not None:
        patch.add_record(entry_address,     num_8bit(row_count))
    if left_value is not None:
        patch.add_record(entry_address + 1, num_8bit(left_value))
    if right_value is not None:
        patch.add_record(entry_address + 2, num_8bit(right_value))

if __name__ == '__main__':
    os.makedirs('build', exist_ok=True)

    reverse_font_map = text_util.load_map_reverse('assets/text/font.tbl')

    patch = Patch()

    # New tiles for digits in font.
    patch.add_record(0x488a, b'\xB5\xB6\xB7\xB8')

    # Some code that gets executed to get the evolution menu into a clean state after making a selection...
    # it ensures that the "Yes" option is visible if it was in the middle of blinking. But because the "Yes"
    # text is longer than the original, no easy way to expand it. Just NOP it out; it'll look fine.
    patch.add_rle_record(0x4ade, b'\xea', 12)

    # Evolution options...
    # These instructions write blank to each possible location of the arrow.
    # Nudge each one up by 0x40...
    patch.add_record(0x626b, b'\x06')
    patch.add_record(0x626f, b'\x86')
    patch.add_record(0x6273, b'\x06')
    patch.add_record(0x6277, b'\x86')
    patch.add_record(0x627B, b'\x06')

    # Do the same with a table of pointers used for writing the actual arrow.
    patch.add_record(0x6325, b'\x06')
    patch.add_record(0x6327, b'\x86')
    patch.add_record(0x6329, b'\x06')
    patch.add_record(0x632b, b'\x86')
    patch.add_record(0x632d, b'\x06')

    write_code(patch, 'assets/code/menu text.asm', 0x4f90, 309)

    # Code for dialog choices starts at 0x1b6f8... the arrows all need to shift left and up.

    # First, all three possible arrow spots are blanked out. Update those.
    patch.add_record(0x1b752, num_24bit(0x7ee9ca))
    patch.add_record(0x1b756, num_24bit(0x7eea4a))
    patch.add_record(0x1b75a, num_24bit(0x7eeaca))

    # Then, the base location to which the arrow actually gets written. (Gets offset by the current focus index.)
    patch.add_record(0x1b76d, num_24bit(0x7ee9ca))

    # The name entry window used by the fossil record...

    # This is the check for the index of "End," which happens way before most of the name entry code for some reason.
    patch.add_record(0x1a822, num_8bit(0x56))

    # Remove a multiplication by 4 (two ASLs) when fetching the character to store.
    patch.add_rle_record(0x1b9ff, b'\xea', 2)

    # "Space" is now at index 0x55.
    patch.add_record(0x1ba0d, num_8bit(0x55))

    # We only write one row, and set the palette ourselves. This inserts "AND #$00ff: OR #$3000: NOP" in place of an extra write to the top row.
    patch.add_record(0x1ba20, b'\x29\xff\x00\x09\x00\x30\xea')

    # Code to read the characters in the name entry window is rewritten to use 1 byte per character instead of 4.
    write_code(patch, 'assets/code/name entry grid.asm', 0x1ba69, 289)

    # Scrolling arrows on name entry... goal is only one page of characters, so scrolling should never be supported. For this
    # block that draws the arrows, just skip comparing to 0xb for the up arrow; this has the effect of doing the comparison
    # against 0 instead.
    patch.add_rle_record(0x1bb93, b'\xea', 3)

    # Instructions to check a few bounds on the current index (after navigating down?).
    patch.add_record(0x1bcff, num_16bit(85)) # Compare to index of "Space"
    patch.add_record(0x1bd04, num_16bit(88)) # Compare to index of "End"
    patch.add_record(0x1bd09, num_16bit(90)) # Compare to first overflow index
    patch.add_record(0x1bd1b, num_16bit(85)) # Force to the index of "Space"
    patch.add_record(0x1bd24, num_16bit(86)) # Force to the index of "End"

    # When navigating up from "End," it needs to shift the current index forward a bit to align visually. This checks that value.
    patch.add_record(0x1bd73, num_16bit(86 - 10))

    # Handle wrapping when navigating right from "End."
    patch.add_record(0x1bc51, num_16bit(87))
    patch.add_record(0x1bc56, num_16bit(80))

    # Handle wrapping to "End" when navigating left on the last row.
    patch.add_record(0x1bca1, num_16bit(79))
    patch.add_record(0x1bca6, num_16bit(86))

    # At 0x1bdcc, there's a list of indices in the character grid that should be skipped over. We don't need most of them.
    write_with_size_check(patch, 0x1bdcc, 14, num_8bit(28) + num_8bit(58), fill_byte=b'\xff')

    # The characters used in the name entry. Control characters go immediately after them.
    with open('assets/text/name entry grid.txt', 'r', encoding='shift-jis') as f:
        grid_start = 0x1c6b3
        data = text_util.encode_text(f.read(), reverse_font_map, newline=b'', terminator=b'')
        ctrl_start = grid_start + len(data)
        data += text_util.encode_text('Space End', reverse_font_map, newline=b'', terminator=b'')
        write_with_size_check(patch, grid_start, 798, data)

    # This assembly code sets the height of the area name window. Make it shorter.
    patch.add_record(0x1c2af, num_16bit(4))

    # Assembly code to render text for save slots. The text used comes from the area names, I think?

    # NOP out some instructions that skip the first few bytes of the text.
    patch.add_rle_record(0x1c441, b'\xea', 4)

    # I rewrote one whole block for simplicity... I think the changes are still basically just constants where
    # the chapter digits come from. I'm honestly not sure what exactly this does differently, though; the file
    # I originally compiled it from seems to have disappeared.
    write_code(patch, 'assets/code/save slot text.asm', 0x1c46f, 52)

    # These are the destination offsets for the number of areas cleared and total areas in the chapter, respectively.
    # I'm just shifting them left by one character (2 bytes) to accomodate the translated string.
    patch.add_record(0x1c4b4, num_16bit(0x004a))
    patch.add_record(0x1c4de, num_16bit(0x0051))

    write_strings_from_csv(patch, 'assets/text/area_names.csv', reverse_font_map, 0x1c9db, 108 * 2, 0x1cab3, 2048, interleaved=True)

    write_strings_from_csv(patch, 'assets/text/dialog_bank_1.csv', reverse_font_map, 0x1d2b3, 29 * 2, 0x1d2ed, 6766, pad_to_line_count=6, pad_final_line=True)
    write_strings_from_csv(patch, 'assets/text/dialog_bank_2.csv', reverse_font_map, 0xfb719, 81 * 2, 0xfb7bb, 18185, pad_to_line_count=6, pad_final_line=True)
    write_strings_from_csv(patch, 'assets/text/dialog_bank_3.csv', reverse_font_map, 0xedfc1, 33 * 2, 0xee011, 6684, pad_to_line_count=6, pad_final_line=True)

    # And then, the dialogs have a data table starting at 0x1ed5b. See the helper method for notes on that.
    write_dialog_choice_entry(patch, 0x1ed5b, page_index=2, dest1=4, dest2=3)                 # 0x11 - Ichthyostega elder's story
    write_dialog_choice_entry(patch, 0x1ed69, page_index=3, dest1=1)
    write_dialog_choice_entry(patch, 0x1ed77, page_index=8, dest1=2, dest2=0xffff)            # 0x23 - Styracosaur's story
    write_dialog_choice_entry(patch, 0x1ed85, page_index=3, dest1=6, dest2=4)                 # 0x2f - Tyrannosaurs
    write_dialog_choice_entry(patch, 0x1ed93, page_index=5)
    write_dialog_choice_entry(patch, 0x1eda1, page_index=6, dest1=2, dest2=7)
    write_dialog_choice_entry(patch, 0x1edaf, page_index=4, dest1=5, dest2=6)                 # 0x35 - Mammal evolution
    write_dialog_choice_entry(patch, 0x1edbd, page_index=5)
    write_dialog_choice_entry(patch, 0x1edcb, page_index=6)
    write_dialog_choice_entry(patch, 0x1edd9, page_index=6, dest1=7, dest2=8)                 # 0x39 - Avian King
    write_dialog_choice_entry(patch, 0x1ede7, page_index=7)
    write_dialog_choice_entry(patch, 0x1edf5, page_index=5, dest1=6, dest2=7)                 # 0x3b - Yeti Lord
    write_dialog_choice_entry(patch, 0x1ee03, page_index=6)
    write_dialog_choice_entry(patch, 0x1ee11, page_index=1, dest1=2, dest2=5)                 # 0x3f - Hidden glade stegosaur
    write_dialog_choice_entry(patch, 0x1ee1f, page_index=4)
    write_dialog_choice_entry(patch, 0x1ee2d, page_index=1, dest1=2, dest2=3)                 # 0x42 - Visitors above condor mountain
    write_dialog_choice_entry(patch, 0x1ee3b, page_index=2)
    write_dialog_choice_entry(patch, 0x1ee49, page_index=3, dest1=4, dest2=5)                 # 0x48 - Lagon Commander
    write_dialog_choice_entry(patch, 0x1ee57, page_index=4)

    # At 0x1ef27, there's a routine that does several checks for special things that happen after dialog lines. 0x1ef65
    # checks the line index for the mammal evolution one; that needs to be updated. (Happens on page 5, from above.)
    patch.add_record(0x1ef66, num_8bit(5 * 6))

    # At 0x1f010, there's some code that wants to draw a fake progress meter of ellipses for a "test" that was once part of
    # the mammal dialog. It has constants against which the dialog index (0x1f016) and the line index (0x1f1e)
    # are checked.
    patch.add_record(0x1f01f, num_16bit(2 * 6))    # Make sure the line index is correct.
    patch.add_record(0x1f04f, num_8bit(0xbf))      # change the character written to the nicer-looking four-dot ellipsis.
    patch.add_record(0x1f052, num_24bit(0x7eea8a)) # And place it at the left edge of the window.

    # Somewhere in the vicinity of 0x1f0d8, there's another set of checks for dialog events which handles the ones that load cut scenes.
    patch.add_record(0x1f0e3, num_8bit(7 * 6))  # 0x2f - Tyrannosaurs
    patch.add_record(0x1f0f4, num_8bit(7 * 6))  # 0x39 - Avian King
    patch.add_record(0x1f105, num_8bit(4 * 6))  # 0x48 - Lagon Commander
    patch.add_record(0x1f116, num_8bit(2 * 6))  # 0x42 - Visitors above condor mountain
    patch.add_record(0x1f127, num_8bit(4 * 6))  # 0x3f - Hidden glade stegosaur


    # Before the pointer table for each of these menus, there's a block of 8 bytes per entry describing the size of the window.
    # Starting address, width, height. The fourth word is a flag of some kind, but I'm not sure what it does.

    # Area menu
    patch.add_record(0xf80da, num_16bit(17) + num_16bit(7))  # Main window (3 options) is wider and shorter.
    patch.add_record(0xf810a, num_16bit(23))                 # Name entry grid is narrower.
    patch.add_record(0xf8110, num_16bit(7))                  # "Name" window starts earlier
    patch.add_record(0xf8112, num_16bit(17))                 #   and is wider.
    patch.add_record(0xf811c, num_16bit(4))                  # "Fossil recorded" is shortened.
    patch.add_record(0xf8122, num_16bit(17) + num_16bit(9))  # Window for the main window is a bit bigger.
    patch.add_record(0xf815a, num_16bit(17) + num_16bit(5))  # Main window (2 options) is wider and shorter.
    patch.add_record(0xf8164, num_16bit(5))                  # Main window (1 option) is shorter.
    write_strings_from_csv(patch, 'assets/text/menu_area.csv', reverse_font_map, 0xf8170, 19 * 2, 0xf8196, 1378, newline=b'\xff\xfe', terminator=b'\xff\xff')

    # Evolution menu
    patch.add_record(0xf8708, num_16bit(0x01ef) + num_16bit(15)) # "Are you sure?" stretches to the left.
    patch.add_record(0xf8710, num_16bit(0x01ef) + num_16bit(15)) # "Not enough EP!" does the same.
    patch.add_record(0xf871c, num_16bit(9))                      # "Time flows by rapidly" gets shorter...
    patch.add_record(0xf872c, num_16bit(9))                      #   same for "An unfamiliar environment"
    patch.add_record(0xf8734, num_16bit(9))                      #   and "Crystal's power is depleted"
    patch.add_record(0xf873c, num_16bit(9))                      #   and "Crystal's power accelerates your evolution"
    patch.add_record(0xf8744, num_16bit(9))                      #   and "evolve into a bird.
    write_strings_from_csv(patch, 'assets/text/menu_evo.csv', reverse_font_map, 0xf8748, 10 * 2, 0xf875c, 1008, newline=b'\xff\xfe', terminator=b'\xff\xff')

    # Map menu
    patch.add_record(0xf8b4e, num_16bit(17) + num_16bit(9))                     # Window for the main menu is a bit bigger.
    patch.add_record(0xf8b5e, num_16bit(15))                                    # "Are you sure?" (for saving) gets wider.
    patch.add_record(0xf8b68, num_16bit(4))                                     # "Save data recorded" gets shorter.
    patch.add_record(0xf8b94, num_16bit(0x0147))                                # "There are no records" moves up several rows
    patch.add_record(0xf8b98, num_16bit(4))                                     #   and get shorter.
    patch.add_record(0xf8bae, num_16bit(15))                                    # "Are you sure?" (for deleting) gets wider.
    patch.add_record(0xf8bb4, num_16bit(0x010b) + num_16bit(11) + num_16bit(4)) # "Save data deleted" moves down, gets longer, and gets shorter.
    patch.add_record(0xf8bce, num_16bit(15))                                    # "Are you sure?" (for deleting record entries) gets shorter.
    patch.add_record(0xf8bd4, num_16bit(0x010b) + num_16bit(11) + num_16bit(4)) # "Entry deleted" moves down, gets longer, and gets shorter.
    write_strings_from_csv(patch, 'assets/text/menu_map.csv', reverse_font_map, 0xf8bdc, 18 * 2, 0xf8c00, 1366, newline=b'\xff\xfe', terminator=b'\xff\xff')

    # Prologue and title screen strings... no window borders associated with these.
    write_strings_from_csv(patch, 'assets/text/menu_prologue.csv', reverse_font_map, 0xf9156, 5 * 2, 0xf9160, 288, newline=b'\xff\xfe', terminator=b'\xff\xff')
    write_strings_from_csv(patch, 'assets/text/menu_title.csv', reverse_font_map, 0xf9280, 3 * 2, 0xf9286, 214, newline=b'\xff\xfe', terminator=b'\xff\xff')

    # Load menu
    patch.add_record(0xf9366, num_16bit(15)) # "Are you sure?" gets wider.
    patch.add_record(0xf9370, num_16bit(4))  # "Save data loaded" gets shorter.
    write_strings_from_csv(patch, 'assets/text/menu_load.csv', reverse_font_map, 0xf9374, 3 * 2, 0xf937a, 206, newline=b'\xff\xfe', terminator=b'\xff\xff')

    # "Inserted text" is only used for storing classification text like "Fish", "Amphibian", etc. that gets inserted into the status displays.
    # The original ROM has some other unused text there, but we just blank it all out to save room.
    # Note that the space saved here gets used for overflow in the 0xfb719 dialog block.
    # If for some reason the inserted text ever gets any bigger, make sure to update the overflow block's start address and size too.
    write_strings_from_csv(patch, 'assets/text/menu_inserted_text.csv', reverse_font_map, 0xfa660, 55 * 2, 0xfa6e0, 96, newline=b'\xff\xfe', terminator=b'\xff\xff')

    write_strings_from_csv(patch, 'assets/text/evo_options.csv', reverse_font_map, 0xfaae0, 28 * 2, 0xfab20, 3065)

    # Credits... odd format here, and I'm not entirely sure how it works.
    with open('assets/text/credits.txt', 'r') as f:
        write_with_size_check(patch, 0x13f516, 1949, text_util.encode_text(f.read(), reverse_font_map, newline=b'\x0d', terminator=b''))

    # The health and EP displays... 16-bit tiles.
    patch.add_record(0xf8060, b'\x87\x30\x8f\x30\xdc')                             # "HP:"
    patch.add_record(0xf8078, b'\x84\x30\x8f\x30\xdc\x30\x00\x30\x00\x30\x00\x30') # "EP:   " (Note three spaces)

    # "Pause" text... just constants embedded in assembly.
    patch.add_record(0x1c67d, text_util.map_char('P', reverse_font_map))
    patch.add_record(0x1c684, text_util.map_char('a', reverse_font_map))
    patch.add_record(0x1c68b, text_util.map_char('u', reverse_font_map))
    patch.add_record(0x1c692, text_util.map_char('s', reverse_font_map))
    patch.add_record(0x1c699, text_util.map_char('e', reverse_font_map))

    # And HDMA tables...
    write_hdma_table_entry(patch, 0x1158b, 1, row_count=0x32, right_value=0x8d) # 3-option root menu - Wider on right and shorter.
    write_hdma_table_entry(patch, 0x11595, 2, row_count=0x22)                   # Status window (2 options) - Menu shorter.
    write_hdma_table_entry(patch, 0x11595, 4, row_count=0x48)                   #                             Next region(s) wider to compensate.
    write_hdma_table_entry(patch, 0x115d1, 1, row_count=0x22, right_value=0x8d) # 2-option root menu - Wider on right and shorter.

    patch.add_record(0x1160f, b'\x7b') # Standalone yes/no confirmation on evo menu; make slightly wider on the left.
    patch.add_record(0x11618, b'\x42') # Some window a little shorter; might be one of the evolution messages.
    patch.add_record(0x11622, b'\x42') # I think this is the red crystal message. Make it a bit shorter.

    patch.add_record(0x11657, b'\x3b') # Name entry screen... "Name" window wider on the left,
    patch.add_record(0x1165b, b'\xd5') #                      and text entry window narrower on right.
    patch.add_record(0x1165e, b'\xd5') #                      Why is it split up into three identical
    patch.add_record(0x11661, b'\xd5') #                      blocks? I don't know.
    patch.add_record(0x11669, b'\x1e') # "Save data recorded." Wider on the left.
    patch.add_record(0x11679, b'\x8d') # "Where will you record your save data?" - Menu window wider on right
    patch.add_record(0x1167a, b'\x26') #                                         - and taller on bottom.
    patch.add_record(0x1167d, b'\x30') #                                         - Shorten the save window to compensate.
    patch.add_record(0x11689, b'\x8d') # "Are you sure?" (for saving) - Menu window wider on right
    patch.add_record(0x1168a, b'\x26') #                                and taller on bottom.
    patch.add_record(0x1168d, b'\x30') #                                Shorten the save window to compensate.
    patch.add_record(0x11692, b'\xb5') #                                Wider confirmation window.
    patch.add_record(0x116ad, b'\x40') # "There are no records!" - Menu window shorter
    patch.add_record(0x116af, b'\x8d') #                           and wider on right.
    patch.add_record(0x116b0, b'\x1a') #                           Shorter message window.
    patch.add_record(0x116bc, b'\x8d') # "This will overwrite..." (for saving) - Menu window wider on right
    patch.add_record(0x116bd, b'\x26') #                                         and taller on bottom.
    patch.add_record(0x116c0, b'\x30') #                                         Shorten the save window to compensate.
    patch.add_record(0x116d3, b'\x1c') # Believe this to be the area name window.
    patch.add_record(0x116ff, b'\x40') # Used by both the area and map root menus. - Window becomes taller
    patch.add_record(0x11701, b'\x8d') #                                             and wider.
    patch.add_record(0x11709, b'\x40') # "Proceed with green crystal?" - Menu becomes shorter
    patch.add_record(0x1170b, b'\x8d') #                                 and wider.
    patch.add_record(0x1170d, b'\xff\x00') #                             Then a gap between windows.
    patch.add_record(0x1170f, b'\x49') #                                 Shorten next region to compensate.
    patch.add_record(0x11719, b'\x40') # "Which entry to restore?" - Window becomes shorter
    patch.add_record(0x1171b, b'\x8d') #                             and wider.
    patch.add_record(0x1171d, b'\xff\x00') #                         Then a gap between windows.
    patch.add_record(0x1171f, b'\x5b') #                             Shorten next region to compensate.
    patch.add_record(0x11729, b'\x40') # "Restore--are you sure? / Cannot restore" - Window becomes shorter
    patch.add_record(0x1172b, b'\x8d') #                                             and wider.
    patch.add_record(0x1172d, b'\xff\x00') #                                         Then a gap between windows.
    patch.add_record(0x1172f, b'\x2e') #                                             Shorten next region to compensate.
    write_hdma_table_entry(patch, 0x1173c, 2, row_count=0x32) # Status window (3 options) - Menu shorter.
    write_hdma_table_entry(patch, 0x1173c, 4, row_count=0x38) #                             Extend next region(s) to compensate.
    write_hdma_table_entry(patch, 0x1174f, 1, row_count=0x30, right_value=0x8d) # "Record where?" (3 opt) - Menu wider and shorter.
    write_hdma_table_entry(patch, 0x1174f, 2, row_count=0x08, left_value=0xff, right_value=0x00) #          Gap between windows.
    write_hdma_table_entry(patch, 0x1174f, 3, row_count=0x58)                   #                           Extend final region to compensate.
    write_hdma_table_entry(patch, 0x1175f, 1, row_count=0x30, right_value=0x8d) # "Are you sure?" (3 opt) - Menu wider and shorter.
    write_hdma_table_entry(patch, 0x1175f, 2, row_count=0x08, left_value=0x0f, right_value=0x00) #          Gap between windows.
    write_hdma_table_entry(patch, 0x1175f, 3, row_count=0x2f) #                                             Extend next region to compensate.
    write_hdma_table_entry(patch, 0x11785, 2, row_count=0x42) # Status window (4 options) - Menu shorter.
    write_hdma_table_entry(patch, 0x11785, 3, row_count=0x68) #                             Extend next region to compensate.
    patch.add_record(0x1179a, b'\x8d') # "Which entry to preserve?" - Window becomes wider
    patch.add_record(0x1179b, b'\x0d') #                              and taller.
    patch.add_record(0x1179e, b'\x4d') #                              Shorten next region to compensate.
    write_hdma_table_entry(patch, 0x117a5, 1, right_value=0x8d) # "Preserve--are you sure?" (4 options) - Menu wider
    write_hdma_table_entry(patch, 0x117a5, 2, row_count=0x0a)   #                                         and shorter.
    write_hdma_table_entry(patch, 0x117a5, 3, row_count=0x26)   #                                         Extend next to compensate.
    patch.add_record(0x117e0, b'\x8d') # "Which entry would you like to view?" - Menu window wider on right
    patch.add_record(0x117e1, b'\x0c') #                                         and taller on bottom.
    patch.add_record(0x117e4, b'\x4e') #                                         Shorten next region to compensate.
    patch.add_record(0x1180e, b'\x8d') # "Which save data will you delete?" - Menu window wider on right
    patch.add_record(0x1180f, b'\x1e') #                                      and taller on bottom.
    patch.add_record(0x11812, b'\x38') #                                      Shorten next region to compensate.
    patch.add_record(0x1181e, b'\x8d') # "Are you sure?" (for deleting) - Menu window wider on right
    patch.add_record(0x1181f, b'\x1e') #                                  and taller on bottom.
    patch.add_record(0x11822, b'\x38') #                                  Shorten next region to compensate.
    patch.add_record(0x11827, b'\xb5') #                                  Wider confirmation window.
    patch.add_record(0x1182c, b'\x41') # "Save data/entry deleted" - Window starts earlier on top,
    patch.add_record(0x1182f, b'\x1d') #                             isn't as tall,
    patch.add_record(0x11830, b'\x5b') #                             starts later on the left,
    patch.add_record(0x11831, b'\xac') #                             and stops earlier on the right.
    patch.add_record(0x11839, b'\x40') # "Which entry will you delete?" - Menu becomes shorter
    patch.add_record(0x1183b, b'\x8d') #                                  and wider on right.
    patch.add_record(0x1183c, b'\x08\xff\x00') #                          Then a gap between windows.
    patch.add_record(0x1183f, b'\x58') #                                  And the bottom window needs to be taller.
    patch.add_record(0x11849, b'\x40') # "Are you sure?" (for deleting records) - Menu becomes shorter
    patch.add_record(0x1184b, b'\x8d') #                                          and wider on right.
    patch.add_record(0x1184c, b'\x08\xff\x00') #                                  Then a gap between windows.
    patch.add_record(0x1184f, b'\x2e') #                                          Lengthen next region to compensate.
    patch.add_record(0x11854, b'\xc5') #                                          Wider confirmation window
    patch.add_record(0x11857, b'\xc5') #                                          continuing to next region.

    # Dialog window dimensions... mostly constants in assembly.
    # All I'm doing is changing the width of text to 22 characters to fill the existing window and shifting the text over by
    # one tile to compensate. Bunch of redundant constants need to get touched for that.
    dialog_width, dialog_height,  = 22, 6
    dialog_start_addr_prologue, dialog_start_addr_area = 0xe08a, 0xe1ca

    patch.add_record(0x1b63f, (dialog_width * 2).to_bytes(2, byteorder='little'))                 # Offset in bytes between lines in the dialog buffer,
    patch.add_record(0x1b695, (dialog_width * 2).to_bytes(2, byteorder='little'))                 #   and again for scrolling.
    patch.add_record(0x1b6c1, (dialog_width * dialog_height * 2).to_bytes(2, byteorder='little')) # Length of a page in bytes.
    patch.add_record(0x1b6c9, dialog_width.to_bytes(2, byteorder='little'))                       # Width of dialog window in characters.
    patch.add_record(0x1b6d4, (64 - dialog_width * 2).to_bytes(2, byteorder='little'))            # Offset in bytes from end of line in the tilemap to start of the next.
    patch.add_record(0x1efca, dialog_start_addr_area.to_bytes(2, byteorder='little'))             # Start address of dialog text on initial page,
    patch.add_record(0x1f000, dialog_start_addr_area.to_bytes(2, byteorder='little'))             #   and on subsequent pages.
    patch.add_record(0x1f2df, dialog_start_addr_prologue.to_bytes(2, byteorder='little'))         #   Same thing for the prologue text.
    patch.add_record(0x1f31f, dialog_start_addr_prologue.to_bytes(2, byteorder='little'))         #

    # Change the palette for the prologue dialog windows to gray/dark gray instead of white/red.
    prolog_dialog_palette = 0x2000
    patch.add_record(0x1f2d1, prolog_dialog_palette.to_bytes(2, byteorder='little')) # Used for text.
    patch.add_record(0x1f36d, prolog_dialog_palette.to_bytes(2, byteorder='little')) # Used for the arrow.

    # Instructions in the area of 0x1f1ae are responsible for loading the prologue text. Update some constants there.
    patch.add_record(0x1f1af, b'\x02') # Start music at index 2.
    patch.add_record(0x1f1dc, b'\x05') # End the prologue and move on to the sun scene at index 5.

    # Starting at 0xf51df is a table of timings for each page in the intro dialog text. We have
    # fewer pages than the original, so we're shifting a chunk of that array back by two entries.
    patch.add_record(0xf51eb, b'\x00\x03\x00\x01\x00\x06\x01\xff\xff\xff\xff\xff')

    # Tilemap for the chapter graphics and possibly some other things.
    write_gfx_from_file(patch, 'assets/gfx/chapter_tilemap.bin', 0x4efec, 1488)

    # There's a section of the font tiles that gets replaced with the evolution buttons while that menu is
    # open, and then reloaded from a different compressed image. Write both of those from the source asset.
    with open('assets/gfx/font.bin', 'rb') as f:
        font_data = f.read()
        write_gfx(patch, font_data, 0x79358, 2578)
        write_gfx(patch, font_data[0x200:0x600], 0x77c7e, 711)

    # Evolution menu buttons
    write_gfx_from_file(patch, 'assets/gfx/evo_buttons.bin', 0x7efe0, 860)

    # Title image
    write_gfx_from_file(patch, 'assets/gfx/title.bin', 0x11acb2, 3990)

    # Chapter title graphics
    write_gfx_from_file(patch, 'assets/gfx/chapter.bin', 0x110c06, 2022)

    # "Triconodon" image from chapter 4 intro
    write_gfx_from_file(patch, 'assets/gfx/triconodon.bin', 0x1247e2, 2248)

    # Now let's finish up by updating the SNES ROM header.
    write_with_size_check(patch, 0x7fc0, 21, b'46BYS TO FARAWAY EDEN', 0x20) # ROM title
    patch.add_record(0x7fde, num_16bit(0x63c4))                              # Checksum
    patch.add_record(0x7fdc, num_16bit(0xffff ^ 0x63c4))                     # Complement of checksum

    # All done! Build the patch now...
    with open('build/The 4.6 Billion Year Saga - To Faraway Eden.ips', 'w+b') as f:
        f.write(patch.encode())

    # Apply the patch to a ROM file, if one was specified at the command line.
    if len(sys.argv) > 1:
        rom_data = bytes()
        with open(sys.argv[1], 'rb') as f:
            rom_data = f.read()

        rom_data = patch.apply(rom_data)

        with open('build/The 4.6 Billion Year Saga - To Faraway Eden.sfc', 'wb') as f:
            f.write(rom_data)
