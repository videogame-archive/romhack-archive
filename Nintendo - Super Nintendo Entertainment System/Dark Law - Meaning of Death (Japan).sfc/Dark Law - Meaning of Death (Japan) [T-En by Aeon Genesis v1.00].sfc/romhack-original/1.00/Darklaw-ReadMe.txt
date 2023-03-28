DARK LAW ~ MEANING OF DEATH
ENGLISH TRANSLATION V1.00
Copyright 2007 by Aeon Genesis
http://agtp.romhack.net

ToC

0. Before You Begin
1. About Dark Law
2. Patch History
3. Patch Credits and Contributors
4. Known issues
5. Application Instructions
6. Troubleshooting

------------------
0.Before You Begin
------------------
Dark Law has evidenced a few issues with emulators. These include the
corruption of sprites after a battle in the "King of the Abyss" scenario
and the corruption of the palette during "The Search." These were
duplicatable in the Japanese version, and as such are probably not caused
by the patch.

----------------
1.About Dark Law
----------------
Dark Law is a rather nontraditional RPG for the SNES. It focuses on
exploration and storytelling over combat; some of the skills that the
player's characters can learn are even useful for the adventure mode.
It is told through a series of short vignettes ("Scenarios.") Some are
interrelated, but most are essentially standalone. It is a very dark
and macabre game, probably not good to play if you're depressed, but it
does some very interesting things with the genre as a whole. It feels
like Dark Law was trying to be a pencil & paper game, but was coated with
a layer of J-RPG paint. It's quite good.

---------------
2.Patch History
---------------
Dark Law was started... oh god, I don't even remember when. Years ago,
certainly; it originally belonged to Haeleth, who passed it off to me
last July, and I promptly sat on it and did nothing. I picked it up
just after Christmas, spending much of the State of the Union address
working on the first (of many) variable-width font routines that the
game would receive.

It currently stands at 15 font routine hacks: 3 8x16s, 2 8x8s, and
TEN VWFs.

In retrospect, the project was probably more trouble that it was worth,
but hey, it's done and I'm not complaining.

March 7, 2007 - Initial Version 1.00 release

---------------
3.Patch Credits
---------------
THE DARK LAW TEAM
Gideon Zhi - Project leader, romhacker, assembly hacker
Haeleth - Script Translation

SPECIAL THANKS
Huge props to my betatesters for not only completing the game in such a
timely fashion, but also for being so dilligent in reporting the numerous
issues that remained in the beta patch. I can only hope we've squashed
everything...

--------------
4.Known Issues
--------------
--We encountered an issue during playtesting in which some NPC data ended
  up getting corrupted. Specifically, McStarr's and Antoniana's stats, name,
  and equipment got mucked up pretty badly... McStarr had 99/0 Aim and
  Antoniana's equipment would cause the game to crash. I BELIEVE this to be
  a side-effect of another issue we squashed in the meantime, but I can't
  be sure. If you encounter anything like this, PLEASE let us know.
--Character sprites can get corrupted after a battle in the "King of the Abyss"
  scenario. This is an emulator issue and is not my responsibility.
--The palette can get corrupted during the "The Search" scenario. This is an
  emulator issue and is not my responsibility.

Please report any other bugs, spelling errors, and
such on The Pantheon (http://donut.parodius.com/agtp)
Screenshots are preferred, as are (ZSNES format) savestates.

--------------------------
6.Application Instructions
--------------------------
If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "darklaw.smc" make sure the patch
is "darklaw.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM DOES NOT HAVE
A HEADER!. If you right-click the ROM and select Properties, it
should read "Size: 4.00 MB (4,194,304 bytes)". SNESTool will remove all
of your headers for you easily, and you can find it at
http://www.romhacking.net
In the Utilities section, simply search for snestool.

An easy way to tell if the game has a header or not is that if you
apply the patch and the game does not boot, your game very likely does
not have a header.

-----------------
6.Troubleshooting
-----------------
--If the game does not run at all, read the above section on
  application instructions (specifically on header removal.)
  Make sure your ROM is not read-only when you remove its header.
--If McStarr or Antoniana are corrupted, LET ME KNOW. Thank you.
--If the game crashes due to a sprite corruption after a battle in "King of
  the Abyss" it's probably an emulator issue. Sorry.
--If the palette gets corrupted during "The Search" it's probably an emulator
  issue. Sorry.