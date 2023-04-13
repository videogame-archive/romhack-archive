          ____  
         / __/ 
        / /    
       / /    ___                                         ° |\ 
      | |    |   \   ___    ___  _  _    ___  _           _ | \   
      | |    | |\ \ / _ \  / __|| |/ /  / _ \\ \   /\  /\| ||  \ 
      | |    | |/ /| |_| || |   | | /  | | | |\ \ / / / /| ||   \
       \ \   | | / |  _  || |   |   \  | | | | \ \ /\/ / | || |\ \ 
        \ \_ | | \ | | | || |__ | |\ \ | |_| |  \ /\ \/  | || | \ \ 
         \__||_|\_\|_| |_| \___||_| \_\ \___/    \  \/   |_||_|  \/ 

								presents...



DRAGON QUEST I & II translation patch to English with ALL THE BUGS that froze the game FIXED.
v.1.053rtm (version with static splash screen,
and no misinformed amount of money stored in the vault when higher than 65535,
DQ1 bug that avoided saving the game with level 30 fixed.
One garbled "Heh heh heh" fixed)


Based on the RPGOne's translation patch from Japanese to English, v.2.0,
(c) 2002 RPGOne, (c) Evilteam

Bugfixes made by
(c) Rod Mérida, 2021.
  Betatesting:
	(Beranule & Nun's bugs)	Vicks Dg.
  	(Dragon Quest 1)	Lilpuddy31
	(Dragon Quest 2)	Dudejo 

		More information in: http://crackowia.gq

It must be applied on an unpatched unheadered "Dragon Quest I & II (Japan)" ROM.
Patching over an English-patched ROM (with RPGOne's 2.0 translation) may also be possible.


The aforementioned bugs include...
  - The freezing text in the inn of Beran / Beranule
  - Rolando cloning bug in Leftwyne / Liriza
  - An untranslated alternate variant of the ending of Dragon Quest 1 in Japanese, if you completed the game with the princess Laura in your arms, that we have translated from scratch from Japanese.
  - Occassional main characters' misnamings in Dragon Quest 2, in some dialogs.
  - A cut dialog in the middle of the final battle of Dragon Quest 1.
  - The randomized system of generation of names for the princess and for the prince of Sumaltria / Cannock in Dragon Quest 2 has been restored as it was in the Japanese original ROM.
  - The nun's endless looping dialog in Dragon Quest 2, that appears near the end, after having defeated Hargon, has been fixed.
  - The bug that occurred when using the cheat for customizing the names of the princess and the prince of Sumaltria in Dragon Quest 2, if you used a name bigger than 5 letters, has been fixed.
  - The bug that erased the 7th letter of the name of the princess, replaced by ENTER, and occassionally of the name of Rolando, has been fixed.
  - Wrongly informed price per night in the inn of Beranule.
  - Not necessary to press three times the A button when making field magic spells anymore.
  - "Ball of Light" replaced by "Light Orb" (as in the Game Boy Colour official English translation), that sounds more solemn.

  - The original possible random names for the princess of Moonbrook and the prince of Sumaltria have been restored as they were in the Japanese ROM (transcripted to their etymological Latin orthographs). They are, for the prince:
Rolando, Kain, Arthur, Conan, Kooky, Tonelat, Esgar, Paulos; and for the princess: 
Airin, Maria, Nana, Akina, Purine, Maiko, Linda, Samantha.


  - Mziab's pre-existing patch (2012) for fixing the broken title screen in Dragon Quest 1 has been added.

  v.1.03c
  - Splash screen replaced to a milder one.
  - Item name "Statue of Evil" in inventory is unified to "Eye of Malroth", how it's mentioned in dialogs, to avoid confusions.
  - Consequently, final boss name Shidor passes to Malroth.
  - Interrupted message "Oh, [name], it seems you've died... in battle!", that appeared like "Oh, [name], it seems you've di... in battle!" fixed, and erased the code that made its first line pass quickly.
  - Added double "L" to "Firebal" spell name, that passes to "Fireball".
  - Removed unnecessary spaces after spell names in battle, when learning new spells.
  - An ENTER code added in the messages that appear after using the field spells Heal and HealMore, to avoid the name "Samantha" makes text window be overflown.
  - Remove unnecessary spaces at the end of some few item names.
  - Untranslated enemy name Meda (shortened version of Medama, "Eye Ball") changed to Eyeba.
  - Enemy name "Kiss Dragon" fixed to "Kith Dragon".
  - Typo "mircale" fixed to "miracle" in "if you hold your statue over your head, it's said a miracle occurs."
  v.1.03d
  - Due to having removed the bug requiring to press an extra time A-Button when using field spells from the RPGOne's translation, there was a text overflow when using Antidote over someone healthy, as a field spell, too. This version fixes it.

  v.1.04
  - Fixed a bug that made the amount of money stored in the vault be misinformed, when this was bigger than 65535 gold pieces, giving the player the wrong idea the bank gold counter had been restarted and we had lost our money, that we could extract back anyway.
  - Many little errors of text windows overflow by one or to characters fixed.
  - Some garbage symbols added to the hero's name in two soldiers' dialogs at the end of DQ2, in the throne room, fixed.

  v.1.05
  - Fixed the bug in Dragon Quest 1 that didn't let you save the game after reaching level 30.
  - Fixed the bug in Dragon Quest 2 that blocked the game if the Descendant's Orb was used after having defeated Hargon.
  - Fixed a misnaming bug when using the Descendant's Orb after having defeated Hargon, that could make the name of the prince of Cannock or the princess be repeated twice, instead the Hero's name being mentioned; text overflow by 2 letters, in this dialog, also fixed.
  - Wrong pointer that made some Dragon Quest 2 villagers tell you "open that treasure chest" in the middle of a city, after having defeated Hargon, fixed.

  v.1.05a
  - Fixed the misnaming bug that made the king of Laurasia mention the hero's name normally, even though he was dead, after loading the game. Same for another couple of dialogs.
  - Two more little text overflows fixed.

  v.1.05w
  - Added one lacking intro after two dialogs of the King of Sumaltria, one is "He hasn't come back here." and another if you talk to him with the prince of Sumaltria in your party dead; so it doesn't mix up with the following experience and save messages.
  - Changed "here" by "there" in the dialog from the King of Laurasia: "But you were at Sumaltria, and he knew that you would be returning there." while you are still searching the prince.
  - Replaced "seek out my son Rolando" by "look after my son Rolando", when you talk to the King of Laurasia while the prince of Sumaltria is dead, in your party.

  v1.051w
  - Corrected "It looks far to important" by "It looks far too important", when trying to sell a special item (thanks to Bobza for reporting it).
  (- Now the optional cracktro-like spinning screen automatically stops also in real SNES hardware [or in very accurate emulators, like BSNES], and after resetting too)

  v1.052w
  - Seven more references from NPC to the hero even when he's dead, when they should be refering to the party's leader, corrected.

  v1.053rtm
  - Fixed a "Heh heh heh" that garbles a dialog, when talking to the old man that removes curses, while you're cursed by a curse that exceeds his knowledge (thanks to Felipe for this report).
__________________
INFO. ABOUT OLDER VERSIONS
	1.01 This was the first stable version of this patch. It fixes an issue when searching things with the Hero dead, that happened in the 1.0 version.

	1.02 Fixes one more bug left from the RPGOne's translation. If, after Rolando falls ill due to his curse in the inn of Beranule, the protagonist died, and we try to search something, the name of the princess appears like "do,,,R" instead "Linda", "Maria", "Airin" or whatever name she has assigned in that game.
	It also replaces one misnaming of the princess Laura, in the RPGOne's translation, as princess Gwaelin, at the beginning of DQ1.

	1.03 Not necessary to press three times the A button when making field magic spells anymore.
	1.03a  It replaces the key item name "Ball of Light" by "Light Orb" in the whole game.

__________________
For more ROMhacks,
translations, emulation
or romhacking utilities, and
bugfixes don't stop visiting our web.
	http://crackowia.gq

If you ever witness any other bug,
you may report it to: crackowia@gmail.com