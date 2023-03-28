BB GUN: SURVIVAL GAME SIMULATION
ENGLISH TRANSLATION V1.00
Copyright 2004 Aeon Genesis
http://agtp.romhack.net

ToC

1.About BB Gun
2.Patch History
3.Patch Credits and Contributors
4.Known Issues
5.Application Instructions
6.Troubleshooting

--------
1.BB Gun
--------
BB Gun is a strategy game that simulates the Japanese survival game
hobby -- war games, basically, similar to strategic paintball with
non-paint-based guns. You assemble a team of warriors to take through
24 unique missions with varying objectives. You can hit enemies no
matter where they are, but if they're not in your direct line of sight
then they won't show up on the map. It's a pretty nifty game.

---------------
2.Patch History
---------------
This project, took two years. At the same time, it took two days. Weird,
eh? Basically, I spent an evening dumping the scripts and shovelling them
over to Tomato for translation (this was the last project he worked on
before his computer blew up or whatever) and then, not being skilled
enough to make the changes I'd wanted to the dialogue and text display
systems, the translated scripts sat idle for two years. Fast forward to
the middle of August, this year, at which point I took another stab at
it, figured it out, and got it all done in the space of two days. Whee!
Threw it through the usual gauntlet of tests, fixed a few issues,
and here's the result.

October 2, 2004 - Initial version 1.00 Release

---------------
3.Patch Credits
---------------
THE BB GUN TEAM
Main Team:
Gideon Zhi - Project leader, Romhacker
Tomato - Translation

Special thanks to Shih Tzu for helping with a few names that puzzled me.

--------------
4.Known Issues
--------------
--The inexplicably compressed hi-res popup windows have a few typos in
  them (that were in the Japanese version.) The reload option reads
  "Charge" and the two teas are "The Former" and "The Later" (instead
  of "The Latter") but neither of these are a very big deal.

Otherwise, there are no known issues. If you find any, please post
about them on The Pantheon (http://donut.parodius.com/agtp)

--------------------------
5.Application Instructions
--------------------------
Quick ROM Info:
1.50B (12mbit LoROM), Without Header (1,572,864 bytes).

If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "bbg.smc" make sure the patch
is "bbg.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM DOES NOT HAVE
A HEADER!. If you right-click the ROM and select Properties, it
should read "Size: 1.50 MB (1,572,864 bytes)". SNESTool can remove
your headers for you easily, and you can find it at
http://rpgd.emulationworld.com
In the utilities section, click on the IPS Tools link.
The answers to the questions it asks you do not matter unless you're
using a copier to play the game.

An easy way to tell if the game has a header or not is that if you do
the above and the game does not run, it probably has a header.
Use SNESTool to remove it from a clean Japanese ROM and try again.
And don't whine about SNESTool not working in Windows XP, it works
fine for me and I'm running on XP Pro.

-----------------
6.Troubleshooting
-----------------
--If the game does not run at all, read the above section on
  application instructions (specifically on header removal.)
  Make sure your ROM is not read-only when you remove its header.
  Also make sure that if you previously hard-patched the ROM and
  the game crashes as described, you will need to re-apply the
  patch a clean, Japanese original ROM.

--If some of the text appears horizontally stretched or distorted,
  please set your emulator to a display mode which supports hires.
  If the problem persists, disable any extra graphic filters you're
  using.
