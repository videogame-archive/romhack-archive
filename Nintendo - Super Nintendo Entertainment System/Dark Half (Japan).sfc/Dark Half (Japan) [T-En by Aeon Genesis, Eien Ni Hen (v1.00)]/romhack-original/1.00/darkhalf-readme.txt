DARK HALF
ENGLISH TRANSLATION V1.00
Copyright 2015 by Aeon Genesis
http://agtp.romhack.net

ToC

1.  About Dark Half
2.  Patch History
3.  Features
4.  Patch Credits and Contributors
5.  Known issues
6.  Application Instructions

-----------------
1.About Dark Half
-----------------
Dark Half begins with the Fallen One being released from his
thousand-year-old prison. The player then takes control of
the Fallen One as he wanders the earth and absorbs the souls
of the living. When the morning dawns, the player takes
control of Falco, a knight tasked with protecting the human
race. Each night, control switches back to the Fallen One,
and each morning control switches back to Falco.

The plot is actually really well-written. It's very much
black-and-white at the start, but by the time Judgment Day
rolls around and the game gives you the option to pick
sides, the choice is much less stark. The game itself does
some interesting things with RPG conventions too. 

---------------
2.Patch History
---------------
Dark Half has a really rather varied history,  and I suppose
if I were to start at the beginning, but the problem is that
I'm not sure exactly where tha beginning is! This projcet
originated with another group, SirYoink's Klepto Translations,
back in... god, 1999? 2000? I don't even know. I honestly
don't have a lot of information on the history of Yoink's
version of the patch, just that it seemed to progress in
brief fits and starts. He managed to get the script dumped,
after a fashion, and Eien ni Hen translated. The last version
released in 2011 and featured some noticeable improvements
over previous versions, most visibly including a half-width
font. Yoink reports this has containing roughly half of the
game's dialog. 

Fastforward to August of 2014. Pennywise of Stardust Crusaders
(http://yojimbo.eludevisibility.org/) interviewed Eien ni Hen
for a podcast, ad she talked about Dark Half for a while.
This was one of the games that got me into romhacking in the
first place, and every few years I followed up on the status
of the project and thought vaguely about starting up my own
version, but Yoink was an old friend and I didn't really want
to step on any toes. But after the podcast went up I decided
to see about dumping the script from the game and sent a
tentative email to Yoink, giving him full veto power over
the project. After he gave his blessing the project moved
forward into full gear and I passed the new script dump over
to Eien ni Hen, who dropped her earlier translation into it.
The rest, as they say, is history. While not without its
challenges (this was not a simple hack!) the project was
still one of the smoothest I've been involved in.

May 24, 2015 - Initial Version 1.0 Release

---------------
3.Patch Credits
---------------
THE DARK HALF TEAM
Gideon Zhi  - Project leader, lead romhacker, assembly work,
              script edit
Eien ni Hen - Translation

--------------
5.Known Issues
--------------
Spells, when cast, display their name as a circle of sprites
around the caster. A few of these do no match their English
translations exactly, but all are fairly close.

Please report any other bugs, spelling errors, and such on
on The Pantheon (http://agtp.romhack.net/pantheon/)
Screenshots are preferred, as are SRAM saves.

--------------------------
6.Application Instructions
--------------------------
Quick ROM Info:
3.00 MB (24mbit LoROM) WITHOUT header. (*exactly* 3,145,728 bytes)
No header. Patch will expand the rom to 4.00 MB.

If using ZSNES, make sure that the patch has the same name as your ROM.
In other words, if your ROM is called "darkhalf.smc" make sure the patch
is "darkhalf.ips" okay? If you're using a Mac, a Mac IPS patcher is
available. Check the AGTP Links page. If you're using a copier, you
probably already know how to patch the ROM :) Be sure to apply the
patch to a clean copy of the ROM, and make sure your ROM DOES NOT
HAVE A HEADER!. If you right-click the ROM and select Properties, it
should read "3.00 MB (3,145,728 bytes)". TUSH will remove all
of your headers for you easily, and you can find it at
http://www.romhacking.net/utilities/608/

An easy way to tell if the game has a header or not is that if you
apply the patch and the game does not boot, your game very likely does
have a header.