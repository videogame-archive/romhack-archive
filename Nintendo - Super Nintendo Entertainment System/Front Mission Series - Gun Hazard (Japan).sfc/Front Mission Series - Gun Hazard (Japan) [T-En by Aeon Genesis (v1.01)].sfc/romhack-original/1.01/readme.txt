FRONT MISSION: GUN HAZARD
ENGLISH TRANSLATION V1.01
Copyright 2004 Aeon Genesis
http://agtp.romhack.net

ToC

1.About Front Mission: Gun Hazard
2.Patch History
3.Patch Credits and Contributors
4.Known Issues
5.Application Instructions
6.Troubleshooting

---------------------------
1.Front Mission: Gun Hazard
---------------------------
FM:GH is a gaiden, of sorts, unrelated to the "traditional" Front
Mission timeline. It also plays unlike any of the others -- it is
a sidescroller, developed in concert with members of the team who
made Assault Suits: Valken. That does not mean that it doesn't have
Square's trademark RPG elements to it, rather, your character can
gain levels, upgrade his equipment, customize his Wanzer, and bring
allies into battle. It is a fairly long game, sporting just under
a full hundred levels to blast through, and it can get fairly
difficult at times.

---------------
2.Patch History
---------------
This project is four years old. I'm extremely happy to finally be
able to launch it, but in the description of the history, some
things are invariably going to fall by the wayside.

I started work identifying the storage method for the script
nearly four years ago. Since the game's alphabet was stored in an
optimized order, rather than an alphabetical order, I was forced
to made-do with a few letters that actually -were- in alphabetical
order and I based my conversion table off of that.

akujin came along, I don't remember when exactly, and agreed to help
dump and translate the script. It took a while, better part of a year
probably, and there was number of nasty issues with the script itself:
lots of conditional jumps (has-wanzer clauses, ally-present clauses,
event flag tests, etc) not to mention actual 65816 assembly code built
directly into the script. This obviously would require specialized
equipment to reinsert.

Enter Klarth, programmer extraordinaire, who sat down to write one of
the most versatile script insertion utilities ever created for the
purposes of romhacking. It was called "Atlas", in part because it allowed
the user to "map" out pointers in script files, but it was also named
after the orbital elevator in the game many of its features were built
for. The creation and tweaking of Atlas took quite a long time, and
development still continues to this day (I think.)

Afterwards, the script was insertable, but that did not necessarily mean
that it would fit back into the space allotted it. This was wonderfully
bad news, as even after rigging up some simple compression for the game's
text which granted us a tremendous amount of extra space, it still did not
fit.

This prompted a long-time fan of the game, M, to step forward and offer
to polish and condense the game's text. It was a long and grueling
process, and it was another good year before the game's text finally fit.
Some things inevitably ended up getting clipped, but the end result is,
I feel, much more refined due to the numerous revisions than it would have
been if we did not have the strict space constrains the ROM imposed upon
us.

Long story short, once the writing was done and M was available to bat
things back and forth off of, the remainder of the project went fairly
quickly, and here we are!

August 13, 2004 - Initial version 1.00 Release

August 13, 2004 - Initial version 1.01 Release
--Fixes a few text issues that got saved to the wrong file prior to the
  release.

---------------
3.Patch Credits
---------------
THE FRONT MISSION: GUN HAZARD TEAM
Main Team:
Gideon Zhi - Project leader, Romhacker
akujin - Translation
M - Lead script editor & writer
Klarth - Utility Programmer

--------------
4.Known Issues
--------------
--Some testers have experienced a glitch in a particular scene near
  the end of the game where the screen turns green and the game locks
  up. This has happened extremely sporadically and I have not been
  able to duplicate it; for all I know, it may have been a fluke.
  The game reportedly will unlock itself given a minute or two.
--The game will occasionally "drop" a letter from a string when it's
  writing text to the screen. As of this writing, this is beyond my
  control.

Otherwise, there are no known issues. If you find any, please post
about them on The Pantheon (http://donut.parodius.com/agtp)

--------------------------
5.Application Instructions
--------------------------
Quick ROM Info:
3.00MB (24mbit HiROM), With Header (3,146,240 bytes).

If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "fmgh.smc" make sure the patch
is "fmgh.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM HAS A
HEADER!. If you right-click the ROM and select Properties, it
should read "Size: 3.00 MB (3,146,240 bytes)". SNESTool can add
your headers for you easily, and you can find it at
http://rpgd.emulationworld.com
In the utilities section, click on the IPS Tools link.
The answers to the questions it asks you do not matter unless you're
using a copier to play the game.

An easy way to tell if the game has a header or not is that if you do
the above and the game does not run, it probably does not have a header.
Use SNESTool to add one. And don't whine about SNESTool not working
in Windows XP, it works fine for me and I'm running on XP Pro.

As of this writing, the version of ZSNES in my projects folder -will-
crash if the ROM is loaded and does not have a header. This is not
my problem.

-----------------
6.Troubleshooting
-----------------
--If the game does not progress past the Squaresoft splash screen at startup,
  read the above section on application instructions (specifically on
  header removal.) Make sure your ROM is not read-only when you add a header
  to it. Also make sure that if you previously hard-patched the ROM
  and the game crashes as described, you will need to re-apply the patch
  a clean, Japanese original ROM.
