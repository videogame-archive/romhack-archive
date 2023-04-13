Credits:
    Original Translation:
        RPGuy96 - Rom hacking
        VincentASM - Translationsï»¿
    Updated Translation:
        Quirino
    Current Project:
        Robert of Normandy - Rom hacking & bug fixing.
    Special Thanks to:
        Neill Corlett - For the translated title screenï»¿
        
Changelog:
    beta 0.22:
        - Fixed the text used when vendors/secret shops sell items to the convoy.
    beta 0.21:
        - Fixed a mistaken song code that caused Gazzack's boss quote to break sound if animations were off.
    beta 0.20:
        - Fix a mistake in Bantus B1 join dialogue.
        - Correct some naming inconsistencies.
    beta 0.19:
        - Fixed the hidden treasure popup location for 1.1 ROMs.
    beta 0.18:
        - On the item list in the preparations, the item slot number no longer overflows the boundary box.
    beta 0.17:
        - Fixed the dialogue at the end of Chapter 2-3.
        - Corrected the description of the Boots to show the correct stat gain.
    beta 0.16:
        - Fixed line overflow in Lefcandith Gauntlet death ending.
        - Fixed line overflows in Chapter 1-16 opening.
        - Fixed line overflows in Chapter 2-3 opening.
        - Fixed a line overflow in the dialogue for the devil axe village in Chapter 1-3.
        - Fixed Bantu's recruitment dialogue in Book 1.
        - Fixed a line overflow in Gotoh's dialogue in Chapter 1-12.
        - Fixed a line overflow in Gotoh's dialogue for failing to gather all of the stars shards in Chapter 2-14
        - Fixed a line overflow in Michalis's dialogue in Chapter F-1.
        - Fixed Rickard's death quote not displaying fully.
        - Fixed missing terrain bonus data for Arena tiles.
        - Fixed save data not displaying properly after copying or erasing files.
        - Fixed issue on unit select where the last character in long names was always highlighted.
        - Fixed issue on in-chapter unit screen where the last character in long names would stay visible which switching pages.
        - Fixed issue on in-chapter unit screen where the last character in long names would always be highlighted.
        - Fixed the right bar of the in-chapter menu not disappearing if it was opened on the right side of the screen.
        - Fixed cursor in "Binding Shield emits a mysterious light!" not aligning with text box.
        - Fixed trailing A in "BindShld Restored" message, and fit the text inside the display box.
        - Fixed "Book 2 War of Heroes:" in Book 1 epilogue not being terminated properly.
        - Fixed highlighting on in-chapter supply Take/Store/Discard interface
        - Adjusted Unit Select menu so that the cursor doesn't cover part of the "Start" text.
        - Messages for map/disabled animations are now in English.
        - Made class names in battle screen and class roll consistent with stat screen names.
        - All chapter titles are now fully visible on the world map.
        - Chapter titles on the Status Screen now match titles on world map/save files.
        - Overhauled the v1.1 patch to not overwrite the underlying differences between v1.1 and v1.0.

Instructions:
    The ZIP file includes a transition path from v0.14 of the Quirino patch. No 1.1 transition patch was included because the Quirino 1.1 patch converts the ROM to 1.0.

    The from_jp folder includes .ips patches for Japanese headered and unheadered ROMs of both v1.0 and v1.1 of Fire Emblem: Monshou no Nazo.
    To see which version of FE3 you have, open the ROM in SNES9x and check the Revision under File -> Show Rom Info.
    
    Apply the patch to an untranslated FE3 ROM. You can use a patcher like Lunar IPS or Flips, or put the patch file in the 
    same directory as your ROM and give it the same name as your ROM. Most modern SNES emulators should support softpatching.
    
    This is a beta release, so please report any issues you find. Reporting issues with the 1.1 patch would be
    especially appreciated. Please do not request any script changes or report anything else besides technical mistakes.
