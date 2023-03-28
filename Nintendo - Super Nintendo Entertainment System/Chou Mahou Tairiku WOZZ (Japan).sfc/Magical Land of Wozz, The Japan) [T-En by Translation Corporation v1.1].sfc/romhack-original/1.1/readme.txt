  _______________                          ________                   
 / _____   _____ \                        / ______ \               
/ /     | |     \ \    Nightcrawler's    / /      \ \
\/      | |      \/                     / /        \ \
        | |                            / /      ____\/ /\___  /\_____
        | |  /\___   ___/\ /\___    /\ | |     //--\\  |/--\\ |/----\\
        | |  |/--\\ //--|| ||--\\  / / | |     ||  ||  ||   ` ||    || 
        | |  ||   ` ||  || ||  || / /  | |     ||__||  ||     ||____|| 
        | |  ||     ||__|| ||  || \ \  | |     \____/  ||     ||_____/
        | |  ||     \___|| ||  ||  \ \  \ \          /\\/     || 
        | |  \/         \/ \/  \/  / /   \ \        / /       ||
        | |                        \/     \ \______/ /        ||  
        \ /  (c)2014 All rights reserved.  \________/         \/
         `                                  
***********************************************************************
           Chou-Mahou Tairiku Wozz/The Magical Land of Wozz(c)
                        English Translation Patch
                             presented by:

                             Nightcrawler's
                         Translation Corporation
                      http://transcorp.romhacking.net


                       Version 1.1 October 30th 2014
***********************************************************************



Table of Contents

1.0 Information
	1.1 Patch Information
	1.2 Distribution and Licensing Information
	1.3 Copyright Information
	1.4 Disclaimer
2.0 The Project
	2.1 History of the Project
	2.2 The Team
3.0 About the Game
4.0 Credits of who worked on this Patch
5.0 What you will find in the Patch
6.0 Known Issues in the Patch
7.0 Special Thanks



1.0	Information


1.1	****Patch Information****

	This patch when hard patched correctly will result in a VALID checksum.

	This patch was created for use on an original ROM that has:

	  * NO header
	  * CRC32: 0xB3258F38
	  * NSRT (http://www.romhacking.net/utilities/400/) identified as
	    Chou Mahou Tairiku Wozz, Version 1.0, 0x515d Checksum

	The 'TransPatch.exe' custom patching application can be used to ensure 
	you have the correct ROM and patch, and apply the patch to headered or 
	non-headered ROMs automatically. This is the recommended way to apply 
	the patch for novice patchers.

	The patcher should work on Win95/98/ME/2000/XP/Vista/7/8, as well as 
	Linux under Wine. It should be self explanatory to use. You will be 
	asked for your ROM file and your IPS patch file. The program will alert 
	you if any errors are found or upon a successful patching job. 

	An xDelta format patch is also provided for reliable patching in all other
	cases. xDelta patchers are available on all platforms. Popular choices are:
		
	  * MultiPatch (MacOS X)  - http://www.romhacking.net/utilities/746/
	  * Delta Patcher (Win/Linux) - http://www.romhacking.net/utilities/704/
	  * xdelta UI (Win .NET/Linux Mono) - http://www.romhacking.net/utilities/598/
		
	Patch History:
	
	  * 1.0 - December 3rd 2006 - Initial Release
	  * 1.1 - October 30th 2014 - Bug fix release addressing all reported
	                              issues to date.

1.2	****Distribution and Licensing Information****

	This patch is licensed under the Creative Commons Deed as:
		
	Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)

	This means you are free to share, copy, or distribute the work under the
	following conditions: 
	 
	  * Attribution:
		You must give appropriate credit and provide a link to the work's
		website.

	  * NonCommercial:
		You may not use the material for commercial purposes.

	  * NoDerivatives:
		You may not alter, remix, transform, or build upon the work and
		distribute the modified work.
	
	A full copy of the license (CC BY-NC-ND 4.0) can be found at:

	http://creativecommons.org/licenses/by-nc-nd/4.0/
		
	The only files found in the official distribution archive are 
	'readme.txt', 'wozz11.ips', 'wozz11.xdelta', and 'TransPatch.exe'. If 
	anything else has been found in this archive, this is not an official 
	distribution. Only official distributions may be distributed freely for 
	public use per the license.
	

1.3	****Copyright Information****

	Chou-Mahou Tairiku Wozz is a 1995 trademarked, copyrighted 
	property of B.P.S (Bullet Proof Software) and RED company. 
	Nightcrawler's Translation Corporation is not affiliated with any of 
	the above companies in any way.

	Nightcrawler's Translation Corporation title and all logos are 
	not to be used under any circumstances without the expressed written 
	consent of the original creator/s. 

1.4	****Disclaimer****

	No warranties are given. The license may not give you all of the 
	permissions necessary for your intended use. Nightcrawler's Translation 
	Corporation, Nightcrawler, nor any affiliates of the aforementioned shall 
	be held liable or responsible for any losses, damages, injury, and/or 
	legal consequences due to the misuse, or illegal use, of any of the files 
	contained within this archive. 


2.0	Project History and Words about the Team


2.1 Project History

	I can't believe this day has finally come. I'm not sure I can 
	really put into words how I feel about this. I have some very mixed 
	feelings about this project. It has had a long history and has been a long 
	time in the making, not to mention way overdue. As with all stories, we 
	may as well just start at the beginning.

	This translation was officially announced in February 2003 
	although work began sometime before that. I have always practiced never 
	officially announcing a project unless I am committed to finishing it. 
	It's been so long, I can't even remember when I first worked on it 
	anymore. Suffice to say; we're somewhere around the 4-year mark now.

	This project is one that I have to admit, I really 
	underestimated, and I don't do that very often being a 10 year veteran. 
	This game was just a monster of hard coded values and inconsistent ways 
	of doing the same thing in different parts of the game. I can't count 
	how many menu lines (That's right lines not screens)and pointer values 
	I had to trace out individually because they were hard coded and done 
	differently than anything else in the game. While the game had it's 
	share of technical challenges for an SNES game, it wasn't the technical 
	difficulty that was the problem. It was the sheer amount of time it 
	took to deal with all these inconsistencies and hard coded values. 

	One good example was the battle text strings in the game. Each 
	one had hard coded pointers and was usually assembled from sub string 
	pieces in many cases. There were times where I thought I'd never find 
	them all as I couldn't come up with any easy fool proof way to locate 
	all of them as some strings were normal, some were assembled, all were 
	hard coded, and few were consistent in the way called in code. One by one 
	though, they were taken down and were all successfully dealt with.

	Beyond that, the project suffered a huge setback in 2005 as a 
	result of a failed partnership with RPGOne. I'm not going to dig up old 
	drama, but long story short, the project was set back by almost one 
	year while unfinished, buggy, sub par left over work from the failed 
	partnership was fixed up and redone. Some problems as a result of this 
	weren't discovered until sometime in 2005 when the first round of real 
	testing was being done. Having to back track at that point in the project
	really caused a set back. This is the primary reason expanded item names 
	never made it into the hack. 

	At long last, after 4 long years this game has been completed. I 
	think it is a hell of a translation that many of you will enjoy. I for 
	one am happy to put this project behind me because I spent way too much 
	of my life working on it! My only regret is I never had the time to 
	give a proper expansion hack to the 8x8 in the game. I estimate it 
	would probably have delayed the project another 6 months to a year 
	because so many parts of the game rely on 8 character limits. It 
	would break a lot of code including battle text, battle arena text, 
	treasure chests, inventions, menus, vehicles, robots, and more since 
	Wozz loves to hard code everything. Beyond that, after 4 years, I just 
	don't have it in me to work on this game anymore. I don't want Wozz on 
	my tombstone as my life's work and legacy! It's better that my talents 
	were focused on other projects now. So, I hope you can forgive this one 
	flaw in an otherwise wonderful piece of work in my opinion.

	I really hope you'll enjoy 4 years worth of my passion for and 
	dedication to this hobby and community. I freely and proudly 
	give you the opportunity to play Wozz in English. Thank you to all who 
	have given me words of support, all who have given a helping hand to 
	the project, and the fans of TransCorp and our community. Take it, love 
	it, cherish it!
	    
2.1 The Team

	Jonny:

	One thing that did come out of the RPGOne alliance and make it's 
	way into the game was some phenomenal graphics hacking and art work by 
	Jonny. It's a pretty rare talent to be a good hacker AND a good artist, 
	but Jonnny seems to pull it off nicely with several graphical hacks 
	throughout the game from the concept of the new English title screen to 
	the final screen of the game. Thanks goes out to you for a great job Jonny, 
	wherever you are. Your work was appreciated!

	Akujin:
		 
	Akujin, the same translator who did Dual Orb 2 for us, did the 
	script for Wozz. He did an initial rough draft of the main script and 
	then disappeared. He pulls more disappearing acts than the best magicians,
	but we couldn't have done it without him.

	Liana:

	After Akujin disappeared, a new friend of mine, Liana took up the 
	slack and did the remainder of the misc. translations and missed text 
	from the initial dump that needed to be done. This project may never 
	have been finished without her help. A great big 'Thank You' goes out 
	to Liana. =)

	Misty:

	After the script was translated, it was still in a fairly rough 
	form. It was time to do a good edit for spit and polish sake. That's when 
	Misty stepped up to the plate. Not only did she give it one edit, she 
	gave it several! In fact, we were STILL editing and improving minor 
	things in the script right on up to last night! She was very serious 
	with her work and was a pleasure to work with. I commend her efforts to 
	help make the Wozz script the best that it could be. She also deserves 
	a great big 'Thank You'! ;)

	Beta Testers:

	Finally earlier this year the project hit final beta. I would 
	like to thank the beta testing team of Gideon Zhi, Princess, King Mike, 
	Trunkz0r, Liana, and Misty for finding and filing 207 bugs and issues 
	into the bug tracker! Wozz had a very complex and delicate scripting 
	engine and nearly 4 years worth of compounded work in it and I was 
	happy to have such a great team to track down all of those issues. 
	Thank you beta testing team!


3.0 	About the Game


	This game is a 16-bit masterpiece. It was way ahead of its time 
	with great depth to the game play and innovative features. Along with 
	the typical RPG fare, Wozz adds an invention system, vehicles, robots, 
	8 playable characters, and three different branches of story depending on 
	which of the three main characters you choose. To top it off, it has 
	three different endings to go with it.

	This game is a light-hearted, humorous spoof on all the typical RPG 
	clichés. It may not have the epic serious storyline of FFVI, but it 
	doesn't have to. Wozz is there to be anti-RPG, to make you laugh, and 
	more importantly to make you have FUN. I think it excels at being a fun 
	game. It was a fun game to play for me from beginning to end, and had me 
	laughing out loud in several parts!

	The graphics are at first glance average, but a lot of attention 
	to character detail and animations can soon be seen throughout the 
	game. They definitely aid in bringing the world of Wozz to life. One
	quick example is each character has his/her own eye bugging/popping,
	laughing, and anger animations. It was really a nice touch to make
	the game more colorful and bring the script to life.

	Following that up, the game has a pretty nice soundtrack and I 
	found myself humming a few tunes at night while trying to get to sleep. 
	Many tunes were great, some were average, but hardly any were bad.
	

4.0	Credits


	Here are the people that helped make this translation possible. 
	Be thankful to all. ;)

	Hacking:    	Nightcrawler, Jonny
	Translators:	Akujin, Liana
	Script Editing:	Misty
	Beta Testers:	Gideon Zhi, KingMike, Trunkz0r, Liana, Misty, Princess 


5.0  What you will find in this patch:


	It's complete now, so you SHOULD find everything 100% translated! 

	Because the game is different depending on which character you start 
	with, it was very hard to test everything. If you find any bugs, please 
	report them at the TransCorp message board.

	Easter Egg:

	You will also find myself, Nightcrawler, hidden in the game 
	somewhere. I used the custom purple sprite seen in the intro, so you 
	will know it when you find me. I will also have a good explanation for 
	wearing such a ridiculous outfit!

	Beyond that, Wozz has quite a few of it's own secrets, so there 
	was no need to add any others. Kudos to those who find and get everything
	in this game. 


6.0   Known Issues:


	As of right now, there are no known issues, but you may want to 
	be aware of one or two things.

	If you don't activate the Telepa-Box in Journey, you will get no 
	text when you get to other boxes in the game because there is nothing 
	to connect to. This is in the original game, but may seem like a bug.

	The item 'OkuParts' may seem to not work sometimes when used in 
	the over world from the Item menu. Move this item further up in your 
	inventory and it will work properly again. This appears to be a bug in 
	the original game.

	Most versions of ZSNES show the battle damage numbers behind
	the sprite characters. This is due to an emulator bug. Only the most
	recent ZSNES WIP(Nov 12, 2006) versions can display this correctly.
	Either obtain a WIP version from the ZSNES message board (at your own
	risk) or use a different emulator such as SNES9x or BSNES that does 
	not have this problem.


7.0	Special Thanks


	Special Thanks go out to these people for contribution and help 
	in this project.  Without you guys, this patch may not have been 
	possible!

	(in no particular order)

	Additonal Special Thanks to:

	Bongo' 
	Byuu
	Feitan
	Gemini
	Ryusui
	Koitsu

	TransCorp message board supporters
	ROMhacking.net message board supporters
	Former 'The Whirlpool' staff
	And of course Mom and Dad! = ) For without them I wouldn't be here! :P

	I'm terribly sorry if I forgot anyone. I didn't mean to! I hope 
	you can forgive me! I'll be sure to put you in the next patch release! 
	: ) 

