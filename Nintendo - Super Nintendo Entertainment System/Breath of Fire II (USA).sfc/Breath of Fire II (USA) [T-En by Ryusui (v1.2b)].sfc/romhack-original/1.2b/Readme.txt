========Breath of Fire 2: The Fated Child=======
==============Retranslation Project=============
==================Version 1.2b==================
======Copyright 2009 Watercrown Productions=====
========http://theryusui.googlepages.com========



==CONTENTS==

1.0: Introduction
	1.1: Version Log

2.0: Contents of this ZIP File

3.0: Requirements

4.0: Notes For Players Of Previous Versions

5.0: Known Issues

6.0: Et Cetera



==1.0: INTRODUCTION==



"When the world is engulfed in the fires of catastrophe,
heroes will rise, and a dragon shall lead them."


Let me begin this readme with an apology.

This is not the Breath of Fire 2 retranslation I would have created on my own.

It would have taken much longer; certainly it would be missing some of its nicer features. Most likely, it wouldn't have happened at all.

What can I say? Our visions may have diverged and conflicted, but it's d4s' code that's the heart and soul of this project. Yes, the end product is better thanks to my input (and the input of all my beta testers), but it's d4s' work at the core of it all. The amount of code that went into this project is staggering, and I've only tinkered with a small portion of it to get done what I needed done; the rest I can only guess at. So yes, I've earned my place in the credits, but it's d4s who deserves top billing here; never forget that.

A second apology is also due all of you: sorry for taking almost three freakin' years to get this thing out the door. Hopefully, the next one (Death Note? Another Sylvanian Families? Something completely diferent?) won't take nearly as long.



-1.1: Version Log-


Bugs Fixed in v1.1:
- The credits on the copyright screen have been rearranged so that the message displayed when the retranslation is played on actual SNES hardware is no longer overwritten.
-"Wisdom Fruit" and "Wisdom Stone" have been renamed to "Wisdom Seed" and "Wisdom Fruit", to better reflect the names used in Breath of Fire III.
-Fishing rod names no longer overprint the border of their box in the fishing interface. (This was a side effect of a fix I implemented to stop the names of fished-up treasure items getting truncated. Sorry!)
-Sten's Switch command no longer causes the screen to black out. (This was due to a minor quirk in d4s' code for handling HDMA effects at the start of battle.)
-The line that displays for Deis' Shed command is now missing its traditional stray "d". (I thought I had fixed this one before release. Whoops.)
-When returning to the Township/Nero's house after completing Colossea, Ryu's name will be changed back properly regardless of the party order. (This glitch was actually in the original version.)
-One of the three carpenters in Capitan will build a bar filled with NPCs that will let you know your hidden stats such as time spent playing, number of Healing Herbs purchased, number of times wiped out, and so forth. One of these NPCs tells you which character you've used the most and which character you've used the least; the proper names for each are now displayed.
-Numerous minor text issues have been rectified. (For one, it was brought to my attention that "que sera sera" is Spanish. Tapeta now says the more appropriate "c'est la vie".)

Bugs Fixed in v1.1b:
-Further minor text issues have been rectified.

Bugs Fixed in v1.2:
-Several item names have been changed for accuracy and consistency. The "Slash Brace" and "Bolt Brace" are now the "Slash Gloves" and "Bolt Gloves"; furthermore, "Knight Brace" turned out to be a mistranslation - I misread the original Japanese name as "warrior", not "war god" as it was supposed to be - and the new name is "Tyr's Gloves". (Yes, I am aware that Tyr is what Woolsey called Myria in Breath of Fire 1. More importantly, the real Tyr has only one hand. Can you say, "mindblow?") Also, the "Steel Plate" body armor is now the "Iron Plate" for consistency, and the "Plate" shields are now "Band" shields.
-The enemy "King Goo" has had its name changed to "Goo King" for consistency with later titles.
-The message displayed when trying to unequip an item when your inventory is full now displays properly.
-Further minor text issues have been rectified.

Bugs Fixed in v1.2b:
-The shaman selection menu under "Fuse" now works properly when there are gaps in the list. 
-Further minor text issues have been rectified.



==2.0: CONTENTS OF THIS ZIP FILE==



This ZIP file contains the following:

-Readme.txt (this file)
-Translator's Notes.txt (translator's notes)
-Breath of Fire 2 Retranslation (v1.2b).ips (the translation patch in IPS format)
-Extras.zip (extras)



==3.0: REQUIREMENTS==



The retranslation patch requires an unheadered U.S. Breath of Fire 2 ROM. If you can't find an unheadered ROM, you can remove the header from a U.S. ROM using SNESTool.

This patch was tested in Snes9x and ZSNES (primarily ZSNES), though it should run fine in any SNES emulator.



==4.0: NOTES FOR PLAYERS OF PREVIOUS VERSIONS==



If you've played the original U.S. version:

-You can now hold down the B button to run faster (does not work in all places).



If you've played the German version of the retranslation patch:

-You can now buy stacks of items at shops, up to 9 at a time (does not apply to all items).

-The magic menu in the battle screen now has a display on the right-hand side showing you how many AP the selected spell requires, as well as the ally's current AP.

-The item menu in the battle screen now has a display on the right-hand side showing your allies' current HP.



==5.0: KNOWN ISSUES==



-The original version of Breath of Fire 2 used the opaque text box to obscure the sudden appearance of NPCs in cutscenes. The transparent text box feature in this version makes these occurrences visible. There should be no such issue when the text box is set to opaque; the only solution would be to remove the transparent text box feature entirely (already done; however, I am awaiting input before proceeding with implementation in the beta or release patches).

-When tested using ZSNES, the NPCs during the sermon at the Cathedral would sometimes not scroll in sync with the background. This may be related to the transparent text box issue; further testing (and a save state from the relevant point in the game) is required.

-The upper text box in the battle screen that displays attack names and dialogue has no background or gradient in this version. Due to the differences between how the gradients were handled in the original version and how they are handled in this one, this is unlikely to be resolved without serious modifications.

-The 3D world effect implemented in the German version of this patch has been removed due to conflicts with the dialogue box (any dialogue requiring more than one page would cause the game to crash).

-The battle menu transparency effect implemented in the German version of this patch has been disabled due to the graphical errors it introduced (it created the apperance of a full-screen background by duplicating the bottom part of the actual background; this had awkward side effects in some cases).

-Talking to Daisy with anyone except Rand after the field has been plowed will display an erroneous message. This is an error in the original Breath of Fire 2 and is not an issue with the retranslation patch.



==6.0: ET CETERA==



Two-and-a-half years. Not even Sylvanian Families took me this long, but then again I had a much smaller script and happened to be my own hacker the whole way through.

When I first played Breath of Fire 2 on GBA, I couldn't help but think "I could write a better script than this". Then d4s came along with his retranslation project, and I decided it was time to put up or shut up. It's been a rocky road from there to here, but hopefully the end result will satisfy everyone waiting for a version of Breath of Fire 2 with a script written by someone who actually understands English. Again, I won't claim that this is the end-all, be-all definitive version of Breath of Fire 2 (that's for Capcom to make...someday, anyway), but I'd like to think it's a colossal improvement over what's currently available.

Special thanks to my beta testers, wertigon, SeraphSight, KingMike, Curt0195, NukaCola, Shadowsithe, RadicalR and Unartistic, and all the fans who have been waiting for this moment. Also a great big thank you to creaothceann for his contributions to my understanding of HDMA (it's thanks to him that the Dragon's Tear in the menu screen is in the foreground instead of behind the borders).

Special thanks also to vivify93, Lenophis, DarknessSavior, Eien Ni Hen and Kyuuen for ferreting out some further issues that crept into versions 1.0 and 1.1 (through no fault of my beta testers).

And special thanks to Tallgeese, who found yet another crop of typos and provided several useful suggestions; literally all the improvements in v1.2 are thanks to his sharp eye and constructive commentary. Special thanks also to snark, who showed me that "Mjollnir" is not only not a typo, it is actually the traditional spelling. Whoops.

Version 1.2b was a long time coming, but it was thanks to rpgmaster1 that I finally found an impetus to get it done: a glitch in the fusion interface where my vanishing cursor fix inadvertently left the game confused as to what Shaman is supposed to be selected if there are any gaps in the list. Also thanks to Treble0096 and his Let's Play, which helped me root out another batch of inadvertent nonsense and typos. I promise I'll get around to watching the whole thing sometime...which will probably prompt the release of version 1.2c.

But the biggest thanks of all are to, drumroll please...d4s. Yes, it took him a while to understand that an English translation would take more than simply dropping an English script into his German version. Yes, he sometimes left me waiting weeks for a response. Yes, his name magically transformed into a curse word every time I encountered a new glitch. But the bottom line is, occasional skewed priorities aside, the man is a genius. The game says "Watercrown Productions" now instead of "Project BoF2", but the fact remains that d4s is The Man Behind This Project, not me. Without him and his work, I doubt this project would have ever got off the ground and I'd have spent the past two-and-a-half years working my way through the Sylvanian Families series...not that there's anything wrong with Sylvanian Families, mind you, but come on: it's Breath of Fire 2 we're talking about here!


Breath of Fire 2: Shimei no Ko is copyright 1994 Capcom. This English retranslation patch version 1.2b was released on October 10th, 2009. This patch is not to be distributed without this readme attached; otherwise, distribute it however you like. Enjoy.