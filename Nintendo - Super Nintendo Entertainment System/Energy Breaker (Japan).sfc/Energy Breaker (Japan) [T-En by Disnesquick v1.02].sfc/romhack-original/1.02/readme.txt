ENERGY BREAKER
English Translation ver. 1.02 - 26/09/2012
http://eb.yuudachi.net/
==============================================================================
1. About Energy Breaker
2. Version History
3. Credits
4. History/Retrospective
5. Getting Started
6. Known Issues
7. Patching Info

==============================================================================
1. ABOUT ENERGY BREAKER
==============================================================================

	Energy Breaker is an isometric strategy RPG developed by Neverland Co. and
released by Taito Corporation in 1996. You play as Myra, an amnesiac young
woman with a thirst for adventure.
	The story begins with Myra being visited in a dream by a mysterious woman 
calling herself Selphia, only to meet her in real life the next day. On the
advice of Selphia, Myra heads to Eltois, the Wind Forest, hoping to catch up
with a man who may know about her past. Shortly after arriving at the Wind
Forest, Myra joins up with Lenardo, an old scientist who's hunting for the
Reincarnation, a legendary flower whose scent is said to have the power to
bring the dead back to life...

==============================================================================
2. VERSION HISTORY
==============================================================================

1.02 - 26 September 2012
- Corrects a bug introduced by the dialogue compression.

1.01 - 25 September 2012
- Corrects the game destroying bug properly.
- Smaller patch sized due to enhanced dialogue compression.
- Easter egg corrected.
- Typos fixed
- Lenardo less of an arse

1.00.1 BANDAID - 24 September 2012
- Stops a potential game destroying bug but doesn't correct the underlying cause.
  A proper fix will be forthcoming but use this is the meantime.

1.00 - 23 September 2012
- First release
- Full English translation

If you come across any bugs, crashes or spelling/grammar mistakes, it would be
greatly appreciated if you could report them. Screenshots, save states and
save files (SRAM) are particularly helpful additions, especially in the case
of bugs or crashes. (For bugs/crashes, please provide a save before the bug
occurs.) You can report bugs at [http://yuudachi.net/bbs/].

==============================================================================
3. CREDITS
==============================================================================

Reprogramming/graphics:
	Disnesquick

Translation/editing:
	satsu

Translation assistance:
	Avicalendriya, Filler, shivalva

Beta testing:
	Ballz, Gideon Zhi, RadicalR, Sheex

Energy Breaker TM, © 1996 Taito Corporation. All rights reserved.
This fan translation is provided for free and should not be sold.

==============================================================================
4. HISTORY/RETROSPECTIVE
==============================================================================

History, by Disnesquick:

Back in the day, I was working on Romancing Saga 3, in a translation group
with TigerClaw, along with a few other things, like Tales of Phantasia. I was
pretty green to SNES hacking, although I was familiar with 6502, the precursor
to the 65816 in the SNES, from my childhood computer, the BBC Micro 32k, manufactured
by Acorn.

Romancing Saga 3 was proving to be a hell of a lot harder than I had expected, due
to the copious amounts of complex subcode mixed in with the text. I had also just
found out that DeJap were close to finishing Tales of Phantasia, rendering my work
on that game fairly pointless. It was around that time that I heard that a game
called Energy Breaker had just been dumped so I checked it out.
Playing through the first few scenes in Eltois, I thought the game looked and
sounded absolutely beautiful. I need a new high-profle project, since things weren't
going so well with the other projects I decided to have a crack at Energy Breaker. I
was glad to leave the subcode madness of RS3 behind...

Early work progressed quite quickly. Menus and 8x8 were quickly dumped and
Avicalendriya agreed to do translation work. Assembler work was also pretty quick
back then, I managed to get some pretty buggy VWF hacks in on the menu and dual-line
the "chat" menu, though keeping the fixed-width font.

I also got a VWF in on the dialogue windows and then looked into dumping the script.
There was a reasonably simple LZ compression scheme, with which I was quite familiar
from ToP... and then the actual dialogue dump started. Quite a few strange
opcodes mixed in the dialogue...

Subcode...

Crap...

I had invested so much time in the hack by then that I decided to just go with it
and proceeded, for the best part of a year (my final year of high-school) to document
all the subcode so that it could be decompiled and recompiled with the dialogue. Avi
persuaded me to split the dialogue text from the surrounding control code and so the
inserter was born. Sadly I lost my reference to the subcode somewhere along the way
so there will never be an EB editor.

It was around this time that real-life hit Avi and so I was out of a translator. I
started to look at the other text hacks I would have to do. It slowly became apparent
that Energy Breaker was probably the most complicated game to hack that I could have
ever chosen. Every little thing had its own text routine: Names next to characters,
notification text, the chat menu, battle text, world map text... Everything needed
re-coding and my "easier than RS3" project was definitely anything but.

I decided I needed a better workflow and started dumping entire routines, rationalizing
them and THEN doing the actual hacking work. The first example of this was for
the character name entry menu, which took a month of the summer after school to
disassemble and recode. I was pretty careful to avoid the mistakes that had
plagued the project, like doing small assembler hacks directly with a hex editor
(I didn't have an assembler for ToP, so I worked with a 65816 reference and an editor.
I therefore ended up learning all the hex codes for the 65816 instructions and can
still program a reasonable routine directly with a hex editor, though this results
in buggy kludge-code). I would still say the "ring menu" hack is one of the cleanest
replacement routines I've coded and the ASM hack in EB that I'm proudest of. The sheer
number of other routines was really daunting and I was alone at this point, so work
was pretty slow.

There wasn't much interest in the project from translators until Shivalva came on board
and started to do some dialogue work. It was great to finally see some dialogue in the
game but it showed up quite a lot of bugs so work on anything but dialogue stalled
for a long time. I'm not quite sure what happened to Shivalva but after doing a small
portion of the script, we kinda lost contact. I think he became demotivated by how slowly
work was going on the hacking side of things, since I was grappling with about 10
different text routines.

After a while, I released a "teaser" patch with the buggy work to date, to try and attract
attention. Filler and satsu responded and work resumed. Coordinating two translators
proved to be difficult and satsu started complaining that the process of sending
me the script for every revision was slow and tedious. I agreed and started to put
together a maketool: A single program that would assemble all the code and text and put
them all into the expanded area of the ROM. With two clicks, the entire ROM
could be updated at once! Bearing in mind that I started in a romhacking era when using
Thingy to hack text in-situ was pretty normal, I think this is a reasonable achievment.

satsu quickly took over as sole translator and persuaded me to make a few changes to the
inserter, like moving all the dialogue into one file. Although the new code that I was
writing was quite decent, there was still a lot of legacy left in the ROM and the new was
built on the old. To this date, I still don't know exactly how many old hacks are in the
ROM, absolutely essential but equally mysterious...

Progress continued in spurts from 2004 through to 2006: I did some hacking, left it for a
while, satsu did some translating, got distracted by Filena. During this time, I ended up
meeting satsu IRL, since he was studying pretty close to me. We still meet up once or twice
a year, to get ruinously drunk, to this day. Around 2006, two things happened: Firstly, we
decided that it was time to do beta testing, and got RadicalR, Sheex, Gideon Zhi and ballz on
board, and secondly I moved to Switzerland to begin my PhD. I carried on the bug-fixing for
a few months before getting completely crushed by the work-load in my new position; Hacking
work basically ceased, although I would still meet with satsu and he would complain at me.

Finally in the middle of August 2012. satsu decided enough was enough and he was
going to do a final release, bugs and all but perhaps there were "a couple" of small hacks
I could do that would take no time. I did these small hacks and my sense of perfectionism
drove me to do a few more. Before I knew it, I was back in the saddle and started smashing
bugs left, right and centre. We decided on a release-date of September 1st and this
definite deadline moved me to get everything ready to a profesional standard, hack-wise.
Quite a few minor bugs were found in the days leading up to release and these too were
crushed by the power of assembler hacking.

Disnesquick - Retrospective

Energy Breaker is probably the longest running ROM hack there is. The reason for
this is that it is probably the hardest ROM hack in history. The menus in Energy
Breaker are incredibly dynamic and every tiny piece of text gets its own routine.
I have come to loathe the programmers for this and its pretty clear that the
game was a rush job.

Coming up to release, I spent about a month past my announced release date, "fixing
up the menus" to remove "a bit of flicker". This turned into a complete reprogramming
of the menus because the original code was so godawful. The menus are now cleaner and
faster than they were in the original game!

Looking back on Energy Breaker there is a lot I could have done differently, to get a
faster release but I was a kid when I began work: I started the project when I was 17
and will be 30 in under a year.

It would not be an exageration to say that this translation is my life's work. Wherever
I went, the files went with me and I've always been paranoid about losing them, knowing
"some day" I would finish the project off. That day has now come: I feel happy and regret
nothing. For those who have waited, thanks for your patience, if I ever do another hack
then it won't take as long. Romancing Saga 3 would be a god damn walk in the park
compared to this monster...

I am incredibly glad to have worked on this game, and to have met a number of friends
through it, most obviously satsu. Thanks to everyone who lent their support during
its difficult birth!

Translator's Notes - Retrospective

	It's hard to believe that the time to write this has finally come. I've
been working on this project for about eight years now, so it's hard to
imagine that it's finally being released; or better said, it's hard to believe
 that I'm finally letting go of it.

	What kind of reception will this translation get? What kind of reception
will Energy Breaker get in the English-speaking world? I can only hope that at
least someone out there will come to have as fond memories of the game as I do
now, having gone through every nook and cranny of it.

	On a personal level, this translation is very important to me. I started
working on it as a university student, and continued to work on it through my
career as a professional translator to date. I learnt a great deal about the
art of translating games over that time, and gained a great deal of valuable
experience. I would always pass on the knowledge I'd acquired to the Energy
Breaker project in one way or another. There are several strata of translation
here: namely my work as a student with a few gaps in my Japanese abilities, as
a fresh-faced graduate, as translator who'd finally broken into professional
work and finally, as a localisation veteran. No wonder it took so much editing.

	Disnesquick mentioned in his retrospective that others had participated in
the translation, but despite that, I still find it a very personal work. There
is actually very little of the translations provided by others left: most of
it was unusable due to the difficulties in translating the script, and I 
retranslated or heavily rewrote nearly everything. I hope that this shows in
the final product - that the script feels like a cohesive, polished and
consistent work rather than a patchwork of translation.

	I mentioned the difficulty of translating the script. I suppose these
notes are still "translator's notes", so I should probably write about what
kind of grief this project gave me. Let me quickly list up a few reasons why
translating the script was a rather difficult task:

	1.) It takes a long time to get used to the way the script is written, if
you look at it purely from a textual angle. The script is sorted into blocks
for each location, then into events, conversations and search text. Rarely was
it in chronological order. To make things worse, there is loads of unused text.
A lot of it is developer text, like dummy text for characters or conversations,
events or places that were removed, or goofy debug comments about a staff
member's pubes. No, really.

	2.) Almost all the dialogue is given in speech bubbles. The style of
writing is extremely conversational, and as a result, elements like the page
breaks and flow of conversation are based on how people talk in Japanese. It
was a considerable effort trying to get the speech bubbles to work for English,
and took a great deal of editing and rewriting. Though we could cut down on
some speech bubbles or add them in places, there were places where we had to
obey what was provided for the Japanese script, such as heavily scripted
scenes with lots of character actions.

	3.) You had to work out who was talking based on the way the lines were
written, which proved very difficult at times as many lines were very short or
were written in a way that didn't provide much in the way of clues.

	4.) Because generic responses to Myra's attitude or responses to keywords
were mixed in with all the other text in NPC conversations, it was very
difficult to work out what was what at first.

	5.) At times, the story is just plain confusing or poorly explained, and
there are some parts that are obviously rushed. This made it hard to translate,
and even harder to edit in order to improve this for the English version.

	I hope that with the above in mind, the other translators understand why I
retranslated and rewrote so much of the text that I feel like I can
confidently call this my translation. It was a very rewarding challenge, and I
hope that the script that I've delivered in the end is good enough for me to
go around talking about it at great length like this.

	This may be one of my last big fan translations, and certainly one of the
last big SNES translations. I certainly don't think I'll ever get to work on a
project that means so much to me and that has so much personal history bound
up in it ever again.

																- satsu

==============================================================================
5. GETTING STARTED
==============================================================================

Energy Breaker has two main gameplay modes: the field, where you can freely
wander around and talk to characters, and battle, where you are constrained
by certain rules and can't interact with the environment.

For a list of controls, you can refer to the Controls option on the settings
menu, accessible by pressing START. Some on-screen prompts are also given
on menus.

= Basic Controls: Field =

D-Pad: Move Myra
A Button: Talk/examine
B Button: Jump up or down a level
X Button: Open menu
Y Button: --
L/R Buttons: Change direction Myra is facing

= Basic Controls: Battle =

D-Pad: Move cursor/navigate menus
A Button: Confirm selection
B Button: Cancel
X Button: Open menu
Y Button: --
L/R Buttons: Change direction character is facing

= Balance =

During battle, every action you take costs Balance points, or "Bal". When you
select a skill from the list, you'll see the amount of Bal points required to
use that move. Moving your character will cost 5 Bal. Bal is also required to
use items in your inventory. You can use items like Robalran Powder to restore
your Bal, or allow it to recover automatically at the start of a new turn.
However, please note that you will recover Bal more slowly if you've taken
damage.

= Skills and Energy =

In order to use skills, you'll need to channel the right amounts of energy for
each element: wind (green), water (blue), fire (red) and earth (yellow).
The amount of energy you have for each element will be given in values like
"2/3" or "0/3". The first number is the amount of energy you have assigned to
that element, while the second number is the maximum possible amount. You can
increase the latter value by levelling up or using certain items. The former,
you can freely adjust depending on how many Pp points your character has. For
example, if you have 5 Pp, you can distribute 5 points across the elements.
Incidentally, your energy balance subtly affects your stats.

Over the course of your adventure, you'll pick up items called Grimoires that
show you the balance of elements you'll need to learn and use skills. Once
you've assigned the right balance of elements, you'll automatically learn the
skill during the course of battle. (Note, however, that one character learns
skills by defeating certain enemies, rather than from Grimoires.)

= General Advice =

- Don't forget to buy a weapon.
- Check EVERYTHING. Items are hiding practically everywhere.
- Really, check everything. The game rewards this.
- Once you've cleared a stage (battle), the Run option will become available
  the next time you play that stage.
- If you make the moral decision to CHEAT, please be aware that battle-related
cheats like infinite Bal will cause the game to crash during scenes outside of
battle.
- Naturally-occurring items like Roelby Berries and Robalran Powder don't run
out, so you can keep picking them up. However, you can't pick them up if you
already have one in your inventory.

==============================================================================
6. KNOWN ISSUES
==============================================================================

- The Bin Bug
For some reason, your Bin (where all the items you throw away go) can get
filled with random items. This can affect the balance of the game, because
you can sell your Bin's contents, and make quite a lot of money in the process.

==============================================================================
7. PATCHING INFORMATION
==============================================================================

This patch is designed for use on an unheadered ROM. If you don't know how to
check whether your ROM has a header or not, you can check using a utility like
TUSH, available at [http://www.romhacking.net/utilities/608/].

= When Using Emulators =
Most modern emulators feature the ability to automatically, temporarily patch
the ROM for you, so you shouldn't have to manually patch it yourself.
For more information on automatically patching the ROM, check the readme for
the emulator you're using.

= When Using Copiers =
You'll need to hard patch the ROM. Some suggested utilities:

- Windows users: Lunar IPS
	http://www.romhacking.net/utilities/240/
- OS X users: MultiPatcher
	http://www.romhacking.net/utilities/746/
- Linux users: Lazy IPS
	http://www.romhacking.net/utilities/844/
	
==============================================================================
																2012 DNQ/satsu