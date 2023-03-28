MYSTERIOUS DUNGEON 2: SHIREN THE WANDERER
ENGLISH TRANSLATION V1.00
Copyright 2006 Aeon Genesis
http://agtp.romhack.net

ToC

1.About Shiren
2.Patch History
3.Patch Credits and Contributors
4.Known Issues
5.Application Instructions
6.Troubleshooting

--------------------------------------------
1.Mysterious Dungeon 2 ~ Shiren the Wanderer
--------------------------------------------
Shiren is the simple story of a man following his friend's dying wish to
find the Land of the Sun at the top of Table Mountain in feudal Japan. It's
a rougelike - that is, the dungeon is randomly generated every time you visit
it, and you start at level 1 with little or no equipment every time you play.
The game has a few subplots you can advance by continually playing through,
which is nice, and all of them will have some bonus to you as a player at
their successful conclusion, be it a helper to follow you around or a new item
becoming available in the dungeon.

Once you complete the main (Impasse Valley) dungeon, as well as a few of the
subplots, you'll unlock a food-themed dungeon and a trap-themed dungeon for
extra gameplay. Once these are completed and all of Fei's puzzles are finished,
you'll unlock the ultimate challenge, a 100-floor dungeon (which, I might add,
none of my testers were able to complete.)

Good luck, and remember, just because the random number generator hates you,
it doesn't mean that the game's completely impossible!

---------------
2.Patch History
---------------
Shiren the Wanderer is the result of a number of years of work, and a number
of hurdles that needed to be overcome. Among these are a nasty system of
enscribable scrolls which needed to be reworked in English, a variable width
AND height font, 24x24 text, and a slew of other, smaller issues. Suffice it
to say that this release is quite pretty and, while not without a few minor
flaws, is very much a playable and enjoyable game.

March 7, 2006 - Initial version 1.00 Release

---------------
3.Patch Credits
---------------
THE SHIREN TEAM
Main Team:
Gideon Zhi - Project leader, Romhacker, Assembly work
Ian Kelley - Script translation and polish
z80gaiden et al - ASM help with blank scrolls and the Ranking
MKendora - Font utility

--------------
4.Known Issues
--------------

-The sign on the Wanderer Ranking is still in Japanese. I elected to
 leave it as such as a stylistic thing.
-The rankings for the three dungeons you can unlock after clearing
 the Impasse Valley game initialize to gibberish names.
-Very occasionally there will be a text spillover in the game progress
 window, usually when Shiren-as-a-monster attacks another monster. This
 is unavoidable, unfortunately.
-"Remove" in the Equip menu is slightly cut off. This seems unavoidable
 unfortunately.
-There may be a very few instances where text speeds by faster than you 
 can read. Not sure what to do about these.
If you find any problems, please post about them on The Pantheon
(http://donut.parodius.com/agtp)

--------------------------
5.Application Instructions
--------------------------
Quick ROM Info:
4.00MB (24mbit HiROM), WITHOUT Header (4,194,304 bytes).

If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "shiren.smc" make sure the patch
is "shiren.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM DOES NOT HAVE
A HEADER!. If you right-click the ROM and select Properties, it
should read "Size: 4.00 MB (4,194,304 bytes)". SNESTool can remove
your headers for you easily, and you can find it at
http://rpgd.emulationworld.com
In the utilities section, click on the IPS Tools link.

An easy way to tell if the game has a header or not is that if you do
the above and the game does not run, it probably has a header. Use
SNESTool to remove the header from a clean Japanese ROM and try again.
And don't whine about SNESTool not working in Windows XP, it works
fine for me and I'm running on XP Pro.

-----------------
6.Troubleshooting
-----------------
--If the game does not run at all, read the above section on
  application instructions (specifically on header removal.)
  Make sure your ROM is not read-only when you add its header.
  Also make sure that if you previously hard-patched the ROM and
  the game crashes as described, you will need to re-apply the
  patch a clean, Japanese original ROM.

  Also note that Shiren will not run on some versions of ZSNES.