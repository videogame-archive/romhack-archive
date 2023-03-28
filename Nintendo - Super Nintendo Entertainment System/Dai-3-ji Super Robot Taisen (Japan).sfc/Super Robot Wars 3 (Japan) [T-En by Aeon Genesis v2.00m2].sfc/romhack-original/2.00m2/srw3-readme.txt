SUPER ROBOT WARS 3
ENGLISH TRANSLATION V2.00m2
Copyright 2019 by Aeon Genesis
http://agtp.romhack.net

ToC

1.  About Super Robot Wars 3
2.  Patch History
3.  Patch Credits and Contributors
4.  Known issues
5.  Application Instructions
6.  Optional Patches

--------------------------
1.About Super Robot Wars 3
--------------------------
Super Robot Wars 3 presents the series in a moment of awkward
lanky teenaged growth. Quality-of-life mechanics that are
standard in later entries, such as upgrade carryover and
per-battle counterattack options, are not yet present, and
the difficulty is far higher than is reasonable in a lot of
moments. It nonetheless presents an important milestone within
the series` growth, being the first game to have branching
scenarios, unit upgrades, and pilot reassignment. It also
marks the first real instance where licensed series fiction
(mostly UC Gundam stuff) is integrated well into the overall
plot instead of just being one-off stages here and there,
and the final boss is one of the series` most memorable
(if only for being absurdly overpowered.)

---------------
2.Patch History
---------------
The SRW3 translation was originally released in 2002.
This was before there were a lot of English resources on the
included properties, and the patch reflected that: more than
a few names were mistranslated. It also reflected my skill
as a romhacker at the time; while several menus were expanded,
more than a few things had to be truncated, the font was
simply too big for what it needed to display, and my sense
of proper aesthetics in things like chapter title displays
needed refinement. The new version fixes all that, and looks
much, much closer to the Super Robot Wars EX translation
released a few months ago. In fact, since 3 and EX share
an engine, I was able to port much of EX`s code to SRW3
without a whole lot of difficulty.

June 24, 2019     - Version 2.00m2 Release
--Fixes to the music persistence patch
June 24, 2019     - Version 2.00 Release
December 25, 2002 - Initial version 1.00 Release

---------------
3.Patch Credits
---------------
THE SUPER ROBOT WARS 3 TEAM
Main Team:
Gideon Zhi - Project leader, romhacker
TheMajinZenki - Translator
Akujin - Translator
Fei - Editor
Mugi - Title Screen

Special Thanks, in no particular order:
Klarth, LordTech, Dark Force, Jair, Anus P,
the MO Board Crowd, Bongo`, Neill Corlett,
Taskforce, Nightcrawler, BMF54123

--------------
4.Known Issues
--------------
There are no known issues.

--------------------------
5.Application Instructions
--------------------------
Quick ROM Info:
1.50 MB (12mbit LoROM) WITHOUT header. (*exactly* 1,572,864 bytes)
No header.

If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "srw3.smc" make sure the patch
is "srw3.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM DOES NOT
HAVE A HEADER!. If you right-click the ROM and select Properties, it
should read "1.50MB (1,572,864 bytes)". TUSH will remove all
of your headers for you easily, and you can find it at
http://www.romhacking.net/utilities/608/

An easy way to tell if the game has a header or not is that if you
apply the patch and the game does not boot, your game very likely does
have a header.

------------------
6.Optional Patches
------------------
Included with the translation is one optional patch, and an additional
"undo" patch which will remove the patch's effects from your ROM should you
decide you don't like it. The patch is entirely standalone and does not
require the translation, so if for instance you decide you want Music
Persistence in your Japanese copy of the game, you can apply that one patch
and be on your way.

MUSIC PERSISTENCE
Super Robot War titles are strategy games, characterized by units moving
around a map and attacking one another in special animated sequences, during
which music plays from the player's (or boss's/Elzam's) unit's TV show,
movie, OVA, etc. It's usually a theme song from the show's opening sequence,
but since Masoukishin does not contain any licensed properties, they're all
various theme songs written for the original characters that appear in the
game.

In later SRW titles, the unit's theme music will continue to play
after the animated sequence ends and the game returns you to the map. This
tends to make the games feel more exciting, as the music is generally
designed to get the viewer invested in the show. Older SRW titles, however,
would stop the theme music as soon as the animated sequence ends, and would
start the map music over from the beginning. As a direct result, you never
heard much of the robot's themes OR the map music unless you deliberately
stuck around and waited to listen.

The Music Persistence patch changes the music behavior in SRW3 to be more
like the later games in the series. Once an attack sequence begins and a
theme song starts playing, the theme song will continue to play until either
the turn ends or a unit from a different series takes part in a battle
sequence.
