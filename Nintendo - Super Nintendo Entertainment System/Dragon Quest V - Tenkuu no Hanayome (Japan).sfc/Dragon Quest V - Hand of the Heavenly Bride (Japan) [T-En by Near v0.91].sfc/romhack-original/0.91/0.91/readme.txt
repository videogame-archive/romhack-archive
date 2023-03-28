***************************
* Dragon Quest V          *
* English Translation     *
* Version 0.91 [11/20/01] *
* Translation By: spSpiff *
* Programming By: byuu    *
* http://byuu.cjb.net/    *
***************************

What's New:
Version 0.91
-Translated all 9 status ailments [special thanks to Neil for this]
-Fixed typos: stll, recieved, don'y, more...
-Fixed auto formatter with monster names
-Fixed text overwrites with menus
-Rewrote battle text system, it now auto scrolls and increments only 1 line at a time, and the cursor is also fixed
-Fixed name wrapping in battle text
-Re-added 2 line monster names to fit some of the longer names, and to fix a few text spills/misalignments
-Fixed casino battle monster list
-Translated Save/Continue menu
-Translated X-Ray menu
-Fixed crashing bug when your inventory was full, and you opened a chest with a key game item
-Translated Bianca and Flora strings that were missing
-Fixed text spilling with 8 char names
-Fixed all instances with known issues with 8 char naming, now any screen that can use 8 char names, does
-A few text windows that were hard to understand have been retranslated... but _much_ is left to do still
-Much, much more...
Version 0.90
-Initial release

Index:
 1) What is this?
 2) How to use this patch
 3) What's Done/Not Done/Known Bugs
 4) Notes about the translation
 5) Credits
 6) Contact

1) What is this?
This is a translation patch for the super famicom RPG Dragon Quest V. It is for use with the original japanese rom,
and it will convert the rom to english. You will need the original, unmodified, unpatched, crc checksum passed,
Dragon Quest V ROM to use this patch, and an emulator/copier.
One of the best free emulators is at http://www.zsnes.com/, you may also try http://www.snes9x.com/
As for the rom, i cannot, and will not tell you where to find this, so dont even ask. If you do not own the original
japanese game, then it is illegal to own a rom image of the game, and for that reason, i will not provide you with
this file. So dont even ask me, or you will be ignored.

2) How to use this patch
Using zsnes [or snes9x], extract the japanese rom [if it is zipped], you should now have a file with a name such as
dq5.smc, or dragonq5.smc, or something very similar. Now, the name of the ips patch is dq5vXXX.ips, you need to rename
one or both of these files so that the name of the rom matches. Here are some examples:
Works:
 dq5.smc and dq5.ips
 dq5e.smc and dq5e.ips
Does not work:
 dragonq5.smc and dq5.ips
 dq5.smc and dq5vXXX.ips
Once both of the file names match, minus the extensions, you need to make sure that both of these files reside in
the same directory, and with zsnes, they can even reside in the same zip file.
You cannot use any saves from the original japanese rom, or from any other translation project. Saves from this patch,
however, will be compatible with future versions of this patch. The reason you cannot use the japanese or other
translated saves on this game is because the names will not work. The A-Z and a-z will not align properly and your name
will end up sounding very strange. If you are famaliar with a hex editor you can fix this, but i will do no such thing
for you.
Note: A=0x10, a=0x30, and you can only use A-Z and a-z for names.

3) What's Done/Not Done/Known Bugs
Done:
 Everything, but what is listed below
Not Done:
 Self Formatting Text.

 Script clarifications.

 Fix code to work with Super UFO-8 and other copiers. Please, if anyone can be of any assistance with this,
 please email me at byuu_@hotmail.com, not having a copier, i cant debug the problems.

 Huffman decompression isnt that fast. This was sort of a problem with the original game, but now it is slightly
 worse due to the increased size of the english text in the rom, and my custom decompression routines. Its really
 not that bad, and you usually cant tell on most windows...

 Other. There are some other very minor things, the script has been revised numerous times, but when dealing with ~400kb
 of raw text, it is possible that things were overlooked. If you see some text that sounds really strange, or you cant
 figure out the meaning, save a snapshot in anything but BMP format, and send it to me, and tell me what sounds strange
 about it, and we will fix it.

4) Notes about the translation
The translation was done by spSpiff. He has revised the scripts several times, and i have revised it once to remove
the few remaining typos. This is a very literal translation, it is done in a way to preverse as much of the original
meaning as possible, while still allowing the script to be readable, and contain individual character emotions.
Some things we could not leave in their original japanese form, the only _main_ thing was the spell names. In japan,
dragon quest has its own unique names for spells that are made up, they have no english translation. an example is
hoimi. it is the Heal spell in english dragon warrior games, and everyone recognizes the name heal, but if we were
to leave hoimi, it could get difficult for one not famaliar with the series to understand what the spells were.
Therefore, the english dragon warrior spell names were used here. everything else was done as literally as possible.

5) Credits
The translation was done _in full_ by spSpiff.
The programming was done _in full_ by byuu.
This was done to minimalize inconsistencies and code/text syncing. This minimizes bugs, and gives the best overall
feel, most would agree that this is the perfect combination for any translation. And i would have to agree, looking
at the current state of the translation.
Absolutely no work was used from anyone else. This was 100% our work [spSpiff and byuu].
I have released my programming sources i have used for this translation as well.
Even though absolutely no one else contributed anything at all to this patch, i would like to give my personal thanks
to the following people, in alphabetical order!!:
Nightcrawler for support and looking over my patch
[http://transcorp.parodius.com/]
NoPrgress for info on the 21/3 bit pointers for the huffman compression, and of course the awesome dq6e patch
[http://www.geocities.com/noprgress]
Pendy for being the first person to update about my translation
[http://dqnn.alefgard.com]
RPGd and the Whirlpool for posting about my updates, and dealing with me/not banning me on their boards ^^'
[http://rpgd.emulationworld.com and http://donut.parodius.com]
Tomato for the excellent dialogue font
[http://www.starmen.net/tomato]
Neil for the generous list of status ailments
[http://pt.parodius.com]
Everyone who submitted bug reports, and has given my patch a chance

6) Contact
 byuu:    byuu_@hotmail.com
 spSpiff: spSpiff@go.com

7) Distribution
You may distribute this patch, so long as it remains _free_, not applied to an original japanese game [see index:2 for
how to distribute the rom with the ips patch not applied, and still have the game play in english.], and that you
_include this file_ in the distribution so that others may find my website in case of important bug fixes, updates,
or any other information they would need to know.

Thanks, and enjoy the patch!