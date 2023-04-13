CYBER KNIGHT
ENGLISH TRANSLATION V1.01
Copyright 2001 by Aeon Genesis
http://agtp.romhack.net

ToC

1.About Cyber Knight
2.Patch History
3.Patch Credits
4.Known issues
5.Application Instructions

--------------------
1.About Cyber Knight
--------------------
Cyber Knight is a nifty little RPG for the SNES by Group SNE and Tonkinhouse. It's a very
different game and is probably not quite what you'd expect from an "RPG." Combat is more
strategic than most battle systems as you have freedom of movement, the method of boosting
your mechs' strength is not what you'd expect from an RPG either. and there are a _LOT_ of
planets you can visit. All in all it's a very neat game!

---------------
2.Patch History
---------------
The Cyber Knight project starte... dprobably around September of 2000 or so, when I found
the ROM, proclaimed that "This kicks ass!" and did a little preliminary work on it before
finding out that while the script wasn't compressed, it DID need a custom dumper to
accomodate for the control codes which draw the variably positioned, variably sized windows.
Round about March of 2001, Taskforce helped with a script dumper (which turned out to be a
very slightly modified Thingy) and the project was "officially" started. Sometime over the
summer, Sogabe popped up online and translated the 180K script in two days ^^; His english
isn't great though, so I gave the scripts to Cidolfas of RPGClassics.com who englishized
them in about three days time.

I sat down for the extremely arduous process of formatting the scripts for insertion in late
July. The window positions and sizes made this an especially large pain in the ass: each of
Cyber Kinght's two large dialogue blocks took about 15 hours of formatting time, whereas a
comparably large file of the same size without the funky windows would take about a fifth
of that time :P You can imagine how frustrated I was when I'd gotten it all about 75% done
and the motherboard on the computer I had everything on died. I still can't get anything off
of that machine; I had to RE-DO the entire format process. UGH. That's what led to my
burnout in August, if anyone's wondering :P

Anyways, a few months (and many hours of formatting) later, Cyber Knight's done!

December 17, 2001 - Version 1.01
-Fixes window overflow when examining planets of "medium gas giant" specification
-Fixes Pockets cavespeak for two or three of the modules
-Maybe fixes Japanese on the save screen, I forget if I fixed it or not.

November 17, 2001 - Initial version 1.00 Release

---------------
3.Patch Credits
---------------
THE CYBER KNIGHT TEAM
Gideon Zhi - Project leader, lead romhacker, assembly work
Sogabe - Translator
Cidolfas - Scriptwriter
Taskforce - Script dumper

--------------
4.Known Issues
--------------
-During combat attack cutscenes, enemies are not labelled
properly.
-The first two screens still contain Japanese.
-Occasionally, when refining neoparts, "g" will output instead
of the enemy's name. Damned if I know what causes this...
-The main character's Power statistic, when starting the game,
is out of alignment with the others' as is his name.
-Only four letters output for the character names in everything
BUT the main dialogue.
-The last four letters of Nehjena's name shows up on Shine's
status screen for some inexplicable reason.

This is NOT a bug:
Occasionally, party members who aren't in your active party will
speak. It was like this in the Japanese version, it ain't my fault.

--------------------------
5.Application Instructions
--------------------------
If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "cknight.smc" make sure the patch
is "cknight.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM HAS
a header. If you right-click the ROM and select Properties, it
should read "Size: 1.00MB (1,049,088 bytes)".
