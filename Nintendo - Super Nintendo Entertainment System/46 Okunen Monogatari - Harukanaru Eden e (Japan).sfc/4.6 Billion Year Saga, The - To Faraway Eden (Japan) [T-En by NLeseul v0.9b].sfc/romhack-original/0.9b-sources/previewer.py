import csv, os

from kivy.app import App
from kivy.properties import BooleanProperty, ListProperty, ObjectProperty

from pyy_chr.core import Buffer, BitplaneInterpreter, BufferInterpreter, TileMapper

import text_util


class PreviewerApp(App):
    dirty = BooleanProperty(False)
    pixel_provider = ObjectProperty()
    text_banks = ListProperty()

    def __init__(self):
        super(PreviewerApp, self).__init__()

        self._text_bank_info = [
            {
                "display": "None",
                "window_size": (22, 6)
            },
            {
                "display": "Dialog 1",
                "path": "assets/text/dialog_bank_1.csv",
                "window_size": (22, 6)
            },
            {
                "display": "Dialog 2",
                "path": "assets/text/dialog_bank_2.csv",
                "window_size": (22, 6)
            },
            {
                "display": "Dialog 3",
                "path": "assets/text/dialog_bank_3.csv",
                "window_size": (22, 6)
            },
            {
                "display": "EVO options",
                "path": "assets/text/evo_options.csv",
                "window_size": (17, 20)
            },
            {
                "display": "Area names",
                "path": "assets/text/area_names.csv",
                "window_size": (22, 2)
            },
        ]

        self._reverse_font_map = None
        self._font_buffer = None
        self._text_buffer = None
        self._available_text = None
        self._encoded_text = None
        self._current_bank = 0
        self._current_page = 0
        self._current_text_index = 0

    def build_config(self, config):
        config.setdefaults("previewer", {
            "data_path": "."
        })

    def build_settings(self, settings):
        settings_json = """
        [
            { "type": "path",
              "title": "Data path",
              "desc": "The root path of the 46BYS project data to use.",
              "section": "previewer",
              "key": "data_path" }
        ]
        """

        settings.add_json_panel('Previewer settings', self.config, data=settings_json)

    def build(self):

        self.text_banks = [info['display'] for info in self._text_bank_info]

        palette_source = BufferInterpreter(1, "RGB",
                                           Buffer(bytearray(b'\x00\x00\x73\x00\x00\x00\x7d\x7d\x7d\xff\xff\xff')))

        mapper = TileMapper(None, None, palette_source)

        self.pixel_provider = mapper

        self._load_common_assets()
        self._load_text_bank()

    def on_config_change(self, config, section, key, value):
        self._load_common_assets()

    def on_bank_changed(self, bank_name):
        bank_index = None
        for i, bank_info in enumerate(self._text_bank_info):
            if bank_info['display'] == bank_name:
                bank_index = i

        if bank_index is not None:
            self._current_bank = bank_index
            self._load_text_bank()

    def on_text_changed(self, text):

        if self._available_text is not None and self._available_text[self._current_text_index][4] != text:
            self._available_text[self._current_text_index][4] = text
            self.dirty = True

        if self._reverse_font_map is not None and self._window_size is not None:
            self._encoded_text = text_util.encode_text(text, self._reverse_font_map,
                                                       pad_to_line_count=self._window_size[1]).split(b'\xfe')

        self._redraw_text()

    def on_save(self):
        data_path = self.config.get('previewer', 'data_path')
        bank_info = self._text_bank_info[self._current_bank]

        with open(os.path.join(data_path, bank_info['path']), 'w',
                  encoding='shift-jis') as in_file:
            writer = csv.writer(in_file, lineterminator='\n')
            writer.writerows(self._available_text)

        self.dirty = False

    def on_display_touch_up(self, display, touch):
        if display.collide_point(*touch.opos):
            x_delta = touch.pos[0] - touch.opos[0]
            y_delta = touch.pos[1] - touch.opos[1]

            if abs(x_delta) > abs(y_delta):
                if x_delta < 0 and self._current_text_index < len(self._available_text) - 1:
                    self._set_current_text(self._current_text_index + 1)
                elif x_delta > 0 and self._current_text_index > 0:
                    self._set_current_text(self._current_text_index - 1)
            else:
                if y_delta > 0:
                    self._set_current_page(self._current_page + 1)
                elif y_delta < 0 < self._current_page:
                    self._set_current_page(self._current_page - 1)

    def on_cursor_position_changed(self, new_pos):
        if self._window_size is not None:
            new_page = new_pos[1] // self._window_size[1]
            if new_page != self._current_page:
                self._set_current_page(new_page)

    def _load_common_assets(self):
        data_path = self.config.get('previewer', 'data_path')

        try:
            self._reverse_font_map = text_util.load_map_reverse(os.path.join(data_path, 'assets/text/font.tbl'))
            self._font_buffer = Buffer(open(os.path.join(data_path, 'assets/gfx/font.bin'), 'rb').read())

            self.pixel_provider.tile_source = BitplaneInterpreter(self._font_buffer, 2, 1)

        except FileNotFoundError:
            self._reverse_font_map = None
            self._font_buffer = None

            self.pixel_provider.tile_source = None

        self._redraw_text()

    def _load_text_bank(self):
        bank_info = self._text_bank_info[self._current_bank]

        self._available_text = None
        self.dirty = False

        if self.pixel_provider is None:
            return

        self._window_size = bank_info['window_size']
        empty_window = bytearray(b'\x01' + b'\x02' * self._window_size[0] + b'\x03' +
                                 (b'\x08' + b'\x00' * self._window_size[0] + b'\x0a') * self._window_size[1] +
                                 b'\x04' + b'\x05' * self._window_size[0] + b'\x06')
        self._text_buffer = Buffer(empty_window)
        self.pixel_provider.map_source = BufferInterpreter(self._window_size[0] + 2, 'P', self._text_buffer)

        if 'path' not in bank_info:
            return

        data_path = self.config.get('previewer', 'data_path')

        try:
            with open(os.path.join(data_path, bank_info['path']), 'r',
                      encoding='shift-jis') as in_file:
                reader = csv.reader(in_file, lineterminator='\n')
                self._available_text = [row for row in reader]
        except FileNotFoundError:
            pass

        self._set_current_text(0)

    def _set_current_text(self, text_index):
        self._current_text_index = text_index

        text_input = self.root.ids['text_input']
        text_input.text = self._available_text[self._current_text_index][4]
        text_input.cursor = 0, 0

    def _set_current_page(self, page):
        self._current_page = page
        self._redraw_text()

    def _redraw_text(self):
        if self._encoded_text is None or self._text_buffer is None:
            return

        current_line = self._current_page * self._window_size[1]

        writer = self._text_buffer.begin_write()
        for line_index, line in enumerate(self._encoded_text):
            if line_index < current_line:
                continue
            if line_index >= current_line + self._window_size[1]:
                break
            writer.write((line_index - current_line + 1) * (self._window_size[0] + 2) + 1, line.ljust(self._window_size[0], b'\x00')[:self._window_size[0]])
        writer.end_write()

        self.root.canvas.ask_update()

if __name__ == '__main__':
    PreviewerApp().run()
    pass
