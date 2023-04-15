# 46 Okunen Monogatari \~Harukanaru Eden e\~ - English translation patch

This is a translation patch for 46億年物語　～はるかなりエデンへ～, an "evolution action"
game released in 1992 for the Super Famicom; the translated title used by this
patch is *The 4.6 Billion Year Saga: To Faraway Eden*. 

This is the same title that was released in North America for the SNES under the
title *E.V.O.: The Search for Eden*. *E.V.O.* is infamous for having a very poor
translation—primarily, the names of many prehistoric animals, including familiar
dinosaur names, are mangled beyond recognition. ("Brosasaurus" and "Tritops"
are among the more familiar ones; "Omosaurus" and "Pronesaurus" are just
baffling.) Beyond that, the official translation isn't terrible, beyond a couple
of errors in converting large numbers and the final boss randomly claiming to
be a caveman. Apart from that, the text is just awkward in places and doesn't
express emotion very well.

I really just wanted an easy way to compare the English script with the original
text when I eventually play this game for YouTube, but the project kind of
escalated. So, here it is, for everyone else to enjoy, or whatever.

Please note that my Japanese is still pretty terrible, so there are likely still
some mistakes in the translations contributed by me. I won't object too much
if someone better at reading Japanese submits improved translations to this
project.

The main repository for this project is on
 [GitHub](https://github.com/nleseul/46bys_eden); any issues you find can be
reported there. You can also visit [my personal web
portal](http://nleseul.this-life.us/) or [email me
directly](mailto:nleseul@this-life.us).

I also include a translation of the manual in the repository
[here](https://github.com/nleseul/46bys_eden/blob/master/doc/manual_translation.txt).
This is mostly for my own reference, but it may be of use to someone.

Note that *はるかなるエデンへ* is a sequel or remake of a previous game, *46億年物語
～ｔｈｅ進化論～*, released only in Japan on PC-98. There was a recent translation of
*ｔｈｅ進化論* by [46 OkuMen](https://46okumen.com/) under the title *E.V.O.: The
Theory of Evolution*. This patch is completely unrelated to their work.

# Applying the patch

The patch ~~is~~will be released in IPS format, which is pretty standard for
console ROM patches. There are plenty of tools for applying IPS patches; a
popular one right now is [Lunar IPS](http://fusoya.eludevisibility.org/lips/).
You could also use [my command-line Python IPS tool](https://github.com/nleseul/ips_util),
if you really want to. 

The patch should be applied to the Japanese ROM. The CRC32 hash is `CA23CB27`.
I'm probably not supposed to link to a source for this ROM; just ask Google.

# Building the patch

If for some reason you need to build your own patch from these data files,
the command is:

```
python build_patch.py [<rom_path>]
```

This should produce a .ips file in the `build` subfolder. If you specify a path
to the original ROM as an argument, the patch will be automatically applied to
that ROM, and the resulting patched ROM will also be placed in the `build`
subfolder.

Note that you will need the `xa` assembler installed and available in your `PATH`
to assemble some bits of assembly source and insert them into the patch. This 
assembler is available [here](https://www.floodgap.com/retrotech/xa/).

There is also a visual editor for the main the in-game text data files that you
can try running if you want, with:

```
python previewer.py
```

You will need the [Kivy](https://kivy.org/#home) Python package installed for
rendering the GUI. You will also need my [`pyy_chr` utility](https://github.com/nleseul/pyy_chr),
which is used to help render the native SFC graphics data into on-screen images.