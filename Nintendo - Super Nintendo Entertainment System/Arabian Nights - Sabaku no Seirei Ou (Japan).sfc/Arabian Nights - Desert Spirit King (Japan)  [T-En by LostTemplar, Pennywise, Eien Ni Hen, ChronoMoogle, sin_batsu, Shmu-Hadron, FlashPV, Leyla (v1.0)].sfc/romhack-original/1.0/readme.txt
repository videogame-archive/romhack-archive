                                Arabian Nights
                           English Translation v1.0

--[ credits ]-----------------------------------------------------------------

Project Management - Pennywise

Reprogramming      - LostTemplar

Translation        - Eien ni Hen

Graphics           - ChronoMoogle (titlescreen refinement, credits, various)
                     sin_batsu (titlescreen refinement, anti-repro screen)
                     Shmu-Hadron (titlescreen prototype)				     
                     FlashPV
                     Leyla (various battle icons)

Betatesting        - ChronoMoogle
                     LostTemplar
                     Eien ni Hen
                     Pennywise

--[ version history ]---------------------------------------------------------

12/25/2012  v1.0 - first public release

--[ feature list ]------------------------------------------------------------

The game has been rigorously reworked to allow for a pleasing experience in
English:

 - usage of proportional fonts (dialog, menus, battle, diary)
 - up to 16 letters for the heroine's name
 - layout changes (menu, battle)
 - icons/symbols instead of Chinese characters where appropriate

In addition, several bugs from the original game, reaching from minor to
game-breaking, have been fixed (not exhaustive):

 - game-breaking bug at the Thieves' hideout
 - many minor annoyances at the Royal Palace
 - donations in Egypt
 - broken tiles on the world map
 - Thief's Clothes
 - typos in the credits roll
 
For details and for comments on the actual implementation visit

                http://www.au-ro-ra.net/index.php?page=antech

--[ patching ]----------------------------------------------------------------

Source ROM

  Size    2,621,440 bytes (2.5MB), no header
  SHA-1   4c06a9a1f5598869852b9f008136e38e2a39baab
  MD5     eb1ad755da3310f392cf8bc94962612e
  CRC32   b6dbf57b
	
Patch

  Size    389,243 bytes
  SHA-1   c5e8dbda0b1a99492a4f043ac4b9e73e6a78ed32
  MD5     adbe1127c3992e6d813bc94232d0e3db
  CRC32   2144df1c
	
Patched ROM

  Size    3,145,728 bytes (3MB), no header
  SHA-1   6528f90602edb8772698c082a1cb3e0c9a033165
  MD5     fb6227cd750c6fc4beade6db8a55e3b8
  CRC32   be092358

To apply our patch to the ROM you have to use beat, which you can download
from here:

                  http://www.romhacking.net/utilities/893/

The patching process should be self-explanatory; just choose the patch, the
source ROM, and the destination.

BPS was deliberately chosen over IPS because it simply is the superior format;
if, however, you would like to have an IPS patch (e.g. for soft-patching), it
is very easy to create one yourself. Just apply the patch with beat while
keeping a copy of the source ROM, and then create an IPS file, for instance
with Lunar IPS:

                  http://www.romhacking.net/utilities/240/
				 
--[ getting started ]---------------------------------------------------------

We have left the actual gameplay of Arabian Nights unmodified in order to stay
true to the original. However, The game isn't perfect and can get annoying at
times due to the high encounter rate. The Japanese version had two cheat names
that, if you named Shukran after them, made the game easier and/or more
enjoyable. We have of course translated them:

  "Takara!!"  - you start with your levels maxed out, however can't learn
                Harty's skills
  "Pandora!!" - all enemies yield twice the experience and money
				 
Give Shukran one of the two names (without the quotes) to receive the
mentioned bonus. Another tip: Before leaving Bazaar, the first town, go and
buy some armor for Shukran!

PS: Pandora Box is the developer and Takara the publisher of Arabian Nights.
				 
--[ known problems ]----------------------------------------------------------

- Very seldomly the animation of the transition to the battle screen
  (the "whirl") is bugged and displays random content of the VRAM (mostly
  numbers). This bug is part of the original, appears very sporadically and
  seems unreproducible. So if anyone has a savestate just before this happens
  I'd happily try to fix it. - LostTemplar

- Sometimes there is flicker just above the dialog box. This also happens in
  the original.
  
--[ historical remarks / comments ]-------------------------------------------

Pennywise (initiator, management)

  Around 2008/2009 I contacted Kammedo, the guy who ran Yonin no Translators, 
  about helping with script editing for the translation of Arabian Nights. He 
  responded and things got started with the help of one other person. The
  script quite frankly was in pretty bad shape. It had been translated by
  several people over the years with varying degrees of substandard mastery of
  the English language. A lot of it didn't even make sense as it was probably
  translated out of context to boot. 
  
  After an initial proof to fix all the errors and really awkward stuff, I
  began playtesting to polish the script. Unfortunately pretty much everyone
  involved in the translation was using ZSNES and other terribly outdated
  emulators to test it and never noticed that there were several game breaking
  bugs relating to the text routines and transferring to VRAM outside of
  vblank. I tried my best to get the people involved to solve the issue by
  getting some outside help and advice, but ultimately nothing ever came of
  it. There were also issues with the toolset that was written for the game
  and various other stability issues. Time went by and slowly, but surely most
  everyone involved with the project went away, never to be heard from again,
  and the project faded away. I was left with a buggy toolset, a WIP ROM with
  no source, and a translation that was still in pretty bad shape. 
  
  Eventually I started looking to find someone to finish the hacking for the
  game, but given that I didn't really have all that much aside from the ROM,
  it was hard finding someone to finish it. There is a saying that the last
  10% is 90% of the work and this would have been the case if I found someone.
  Sure people were interested, but no one really had the time to devote to the
  project and so years passed with nothing really ever happening. 
  
  It wasn't until 2012 when I contacted LostTemplar about finishing the
  translation and like a miracle he agreed. Given the state of the project,
  it was agreed that we would scrap the previous work and start from scratch.
  It was much easier to do that instead of trying to figure out what went
  wrong with no source. Finally, I asked the translator Eien ni Hen if she
  would do the honors of retranslating and she agreed. After several years of
  inactivity things came together in a matter of months.
  
LostTemplar (reprogramming):

  I had been working on a translation of Far East of Eden Zero when some time
  around April 2012 I handed the project off to byuu, author of higan
  (formerly bsnes). Itching to get my hands dirty on another Super Famicom
  game I was pleasantly surprised when Pennywise asked me whether I would like
  to take over Arabian Nights. That's because not only did he have access to
  YnT's old work, but he had also already asked Eien ni Hen to redo the
  translation. And for the record, her translation work is marvelous!
  
  However, soon it was decided to redo the hacking work from scratch,
  because, quite frankly, YnT's work just wasn't up to par regarding today's
  standards in ROM hacking, we had no source code, and it was terribly
  bug-ridden to boot.
  
  In order to up the ante I then went out of my way to do some advanced
  reprogramming. Visit the link under "feature list" for details! I wish to
  inspire future ROM hackers to do the same, all the while entertaining the
  faint hope that this will even contribute to the overall quality of ROM
  hacking (especially translations) in general.
  
Eien ni Hen (translation):
  
  I remember hearing a few years ago how difficult Arabian Nights was to
  translate. I also recall that it had gone through several hackers and lord
  knows how many translators. I'm proud to be the translator that saw the
  project to completion. When I got the script, it was a complete mess and 
  very evidently the work of multiple translators. The diary entries in
  particular were translated quite poorly. I completely redid the translation
  and brought a strong feeling of consistency to the script.

  The most fun part for me was tailoring the Djinns' various lines to their
  personalities. For example, Jambia speaks like an English butler, and Githil
  a little like Bart Simpson. Ifrit's speech is also kind of odd, fluctuating
  between regal king and tough guy; I tried to strike a good balance between
  the two in the English version. Lastly, I wish the Djinn of Darkness had
  more screen time in the game. She was quite an interesting character but
  I don't think the developers had enough time to develop her backstory. Most
  of the Djinns have a ton of lines, but she only gets a few. 

  The script was surprisingly easy (except for maybe the enemy names), and the
  only overt change I made was localizing 邪教団 [jakyōdan] as "Evil Cult of
  Evil". (The actual translation is more like "Heresy Group".) I felt this
  name fit in better with the light, humorous tone of the game. I did also
  change a couple nonsensical item names (namely ト－イエ－ [tōiei] to "Cleanser"),
  but other than that the　translation is pretty straightforward.

  Anyway, this was a great project for me, and I hope everyone enjoys playing
  it!  
  
ChronoMoogle (betatesting, graphics):

  When I got into the SNES-scene in 2005 I primarily did it for the great RPGs
  of the system. I eventually ended up being curious about the many great
  titles which never made it out of Japan and snagged a copy of the promising
  looking title "Arabian Nights". In my playthrough of the Japanese version
  back in 2008 I already noticed some small and even game-breaking bugs in
  this otherwise very enjoyable and good RPG. It was fun, but unfortunately
  felt rushed.

  Fast forward to 2012. LostTemplar and I are talking a little bit about the
  game in IRC and he eventually mentions he just started working on the
  hacking of this translation. So I volunteered for beta testing and
  fortunately got into the team. I could reproduce a lot of the bugs and
  errors I remembered and even a bunch more, and it really was a blast that
  LostTemplar actually managed to fix them all. Aside from the bug testing
  I provided some graphical rework on the butchered titlescreen, the
  error-heavy and engrish credits and some other small titbits.

  I found it rather fun and enjoyable to test and help with this project, 
  especially because the communication beetween the involved members worked
  out so incredibly good.

---------------------------[ have fun playing! ]------------------------------