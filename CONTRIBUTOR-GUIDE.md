![videogame archive](./docs/readme/videogame-archive.png "Videogame Archive")

# Romhack Archive: Contributor Guide

# Table of Contents
1. [Introduction](#Introduction)
2. [Database Fields](#Database-Fields)
3. [Dealing With Abbreviations](#Dealing-With-Abbreviations)
4. [Dealing With Conflicting Information](#Dealing-With-Conflicting-Information)
5. [Current Labels](#Current-Labels)
6. [Romhack Rom Naming Convention](#Romhack-Rom-Naming-Convention)
7. [Romhack Source Code](#Romhack-Source-Code)
8. [Command Line Tools](#Command-Line-Tools)
9. [User Safety](#User-Safety)

## Introduction

The romhack archive consist of:

- ONE folder for every system: following No-Intro naming convention.
- ONE folder for every rom having romhacks: following No-Intro naming convention.
- ONE folder for every romhack 'version'. This makes possible to archive several romhack versions: Following the romhack archive extended naming convention.
- ONE 'romhack.bps' file, ONE 'romhack.json' file and ONE 'romhack-original' folder for every romhack rom.

This is best visualized with an example:
```
No-Intro System Name
    |- No-Intro Rom Name (region).extension
            |- Romhack Rom Name (region) [XYZ by Author (vVersion)] [Add by Author (vVersion)].extension
                |- romhack.bps
                |- romhack.json
                |- romhack-original
                    |- 1 
                        |-...
                    ...
```

Some will notice that the version is repeated, this is intentional and necessary.

The Romhack rom folder name contains the aggregation of the different patches versions used.

The romhack-original folder contains the originally distributed patch files 'exactly as they were delivered'. More often than not as a compressed archive.  For romhacks delivered as roms he generated romhack.bps is enough.

The numbers found under the 'romhack-original' are the order in what the individual patches where used to build the romhack rom.

## Database Fields

### romhack.json fields

| **Field**       | **Data Type**   | **Required** |     | **Purpose**                                                                                                                                                                                                        |
|-----------------|-----------------|--------------|:----|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Info**        | object          | Yes          |     |                                                                                                                                                                                                                    |
| name            | string          | No           |     | Storing the name of the romhack, when the name uses non ASCII characters, 'null' otherwise.                                                                                                                        |
| translatedTitle | boolean         | No           |     | 'true' if the romhack has translated the title screen of the original game, 'null' otherwise.                                                                                                                      |
| status          | string          | Yes          |     | 'Fully Playable' or 'Unfinished'.                                                                                                                                                                                  |
| adult           | boolean         | No           |     | 'true' if the game depicts explicit sexual content, 'null' otherwise.                                                                                                                                              |
| offensive       | boolean         | No           |     | 'true' if the game depicts offensive content, 'null' otherwise. Under gitHub [User Safety Guidelines](https://docs.github.com/en/site-policy/acceptable-use-policies/github-acceptable-use-policies).              |
| obsoleteVersion | boolean         | No           |     | 'true' if the version is no longer relevant, 'null' otherwise. Doesn't need to be older, just not relevant, an older version can be still be relevant if is actively played.                                       |
| backCatalog     | boolean         | No           |     | 'true' if the hack belongs to the back catalog, 'null' otherwise. Hacks only relevant on very particular conditions like mapper changes, or hacks just kept for historical reasons belong to this category.        |
| **Provenance**  | object          | Yes          |     |                                                                                                                                                                                                                    |
| retrievedBy     | string          | Yes          |     | Identifier of the curator submitting the information to the database.                                                                                                                                              |
| retrievedDate   | string          | Yes          |     | Date following the format YYYY-MM-DD. When the curator retrieved the patch.                                                                                                                                        |
| source          | string          | Yes          |     | 'Trusted' when source is trackable, null otherwise. Example: comes from a community site patch distribution or from the developer.                                                                                 |
| notes           | string          | No           |     | To be used sparingly for unpredictable relevant information the retriever find necessary. No opinions on hacks, no irrelevant thoughts, etc.                                                                       |
| **Rom**         | object          | Yes          |     |                                                                                                                                                                                                                    |
| size            | number          | Yes          |     | For the romhack rom: Size in bytes.                                                                                                                                                                                |
| crc32           | string          | Yes          |     | For the romhack rom: crc32 hash as an hex string in lowercase.                                                                                                                                                     |
| md5             | string          | Yes          |     | For the romhack rom: md5 hash as an hex string in lowercase.                                                                                                                                                       |
| sha1            | string          | Yes          |     | For the romhack rom: sha1 hash as an hex string in lowercase.                                                                                                                                                      |
| **Patches**     | array of object | Yes          |     |                                                                                                                                                                                                                    |
| authors         | array of string | Yes          |     | Authors of the patch, to avoid naming authors in different ways best is to use their name as indicated in community sites or forums including 'Anonymous'. In absence of any information 'Unknown' SHOULD be used. |
| url             | string          | No           |     | Url where the link to download the patch was found, 'null' otherwise.                                                                                                                                              |
| version         | string          | Yes          |     | Version, exactly as indicated by the author. Some authors will indicate '1.00', others '1.0', these are considered different.                                                                                      |
|                 |                 |              |     | If the author repeats a version an incremental numerical value SHOULD be added. For example '2.0 Final (1)' and '2.0 Final (2)'                                                                                    |
|                 |                 |              |     | In absence of a version the release date following the format YYYY.MM.DD SHOULD be used.                                                                                                                           |
|                 |                 |              |     | In absence of any versioning information, 'IF the last modification date of the rom or patch file IS RELIABLE' such date following the format YYYY.MM.DD SHOULD be used.                                           |
|                 |                 |              |     | In total absence of a reliable version 'Unknown' SHOULD be used.                                                                                                                                                   |            
| releaseDate     | string          | No           |     | Date that the current patch distribution was made available formatted YYYY-MM-DD. This date doesn't necessarily match the release or modification date of the community site.                                      |
|                 |                 |              |     | Is recommend to first scan text files on the patch distribution for this date when they are available.                                                                                                             |
|                 |                 |              |     | Also check the community site for the last date the author updated the patch when available. Sometimes the readme is outdated.                                                                                     |
| alternative     | string          | No           |     | When multiple alternative and/or optional patches for the same romhack are distributed together, string identifying the used patches separated by commas.                                                          |
| labels          | array of string | Yes          |     | Each patch SHOULD have AT LEAST one label. The first label of the first patch is the PRIMARY one and used as part of the romhack folder name.                                                                      |

As a general rule if a boolean value is not mandatory, null is used instead of false.

If you want to learn about JSON data types check: [w3schools Json data types](https://www.w3schools.com/js/js_json_datatypes.asp)

**romhack.json example:**
```json
{
  "info": {
    "name": null,
    "translatedTitle": null,
    "status": "Fully Playable",
    "adult": null,
    "offensive": null,
    "obsoleteVersion": null,
    "backCatalog": null
  },
  "provenance": {
    "retrievedBy": "JuMaFuSe",
    "retrievedDate": "2023-03-21",
    "source": "Trusted",
    "notes" : null
  },
  "rom": {
    "size": 2621440,
    "crc32": "e5313fb7",
    "md5": "7d285d74ab2975100462293b8e13d1a5",
    "sha1": "0ac7f6b0454538a1e87a44008e6ff996e01a4c8c"
  },
  "patches": [
    {
      "authors": [ "RPGe" ],
      "url": "https://www.romhacking.net/translations/353/",
      "version": "1.10",
      "releaseDate": "1998-10-17",
      "alternative": null,
      "labels": [ "T-En" ]
    },
    {
      "authors": [ "Spooniest" ],
      "url": "https://www.romhacking.net/translations/2600/",
      "version": "2.1",
      "releaseDate": "2021-03-02",
      "alternative": null,
      "labels": [ "Add" ]
    },
    {
      "authors": [ "noisecross" ],
      "url": "https://www.romhacking.net/translations/3499/",
      "version": "1.0",
      "releaseDate": "2018-03-25",
      "alternative": null,
      "labels": [ "Add" ]
    }
  ]
}
```

## Dealing With Abbreviations

If an object: title, version, etc...

1. If one object is a subset or abbreviation of other, use the longer one (ex. "Legend of Link" vs "Legend of Link: Adventure of Zelda", "Rockman 4 BCAS" vs "Rockman 4 - Burst Chaser X Air Sliding").

2. If one object is abbreviated and one is missing part of it, they should be combined if feasible (ex. "Link: Adventure of Zelda" vs "Legend of Link" can be combined as "Legend of Link: Adventure of Zelda").

## Dealing With Conflicting Information

If an object: title, version, etc...

1. If object information is conflicting, one should be chosen based on the following source priority: in-game title, readme, author's website, community site (with author input), filename of patch or zip containing patch.

2. Rare exceptions can be made if a higher priority title is clearly inadequate; requires maintainer confirmation.

## Current Labels
Labels will be added, modified or removed opportunistically as the project extends its catalogue.

Labels belonging to the first patch are the ones used to define a romhack.

Ideally and in general a romhack has only ONE label that defines it, but exceptions may be found.

The label system is based on the idea that romhacks fall in two big categories, the ones that enhance the original game and the ones that change it.

Each category additionally has two dimensions Gameplay/Cosmetic and Major/Minor.

| **Other/Enhance** | **Description**                                                                                                       |
|-------------------|-----------------------------------------------------------------------------------------------------------------------|
| T-\$              | $ is a language iso code in camelcase. See two letter codes at: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes |
| Performance       | Improves the overall performance of the game (ex. lag reduction, shorter transitions)                                 |
| Fix               | A Fixes one or more bugs, glitches, or graphical errors (NOT for intentional choices by the developer)                |
| Cheat             | Removes an aspect of the challenge (infinite lives, stage select, etc.) (NOT for more nuanced difficulty adjustments) |    

| **Enhance** | **Dimensions**             | **Description**                                                                                                                                                                                |
|-------------|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Overhaul    | All Major & Gameplay Major | Variety of gameplay improvements (and optionally cosmetic enhancements and bugfixes) that are intended to greatly improve the original experience (ex. rebalancing hacks, definitive editions) |
| Tweak       | Gameplay Minor & All Minor | Minor gameplay improvements intended to enhance the original experience (ex. minor difficulty adjustments, tweaking animation speeds)                                                          |
| Polish      | Cosmetic Major             | Visuals widely improved while maintaining the theme of the original game (ex. "Super Mario Bros Enhanced", most "enhanced color" hacks)                                                        |
| Retouch     | Cosmetic Minor             | Minor cosmetic improvements intended as a touchup to the original experience (ex. SMB1 mario replaced with SMB3 mario)                                                                         |

| **Change** | **Dimensions**             | **Description**                                                                                                                                                |
|------------|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Game       | All Mayor & Gameplay Mayor | Major gameplay changes that feel like a new game or sequel (ex. hacks with new levels, hacks that change the genre)                                            |
| Alternate  | Gameplay Minor & All Minor | Minor gameplay changes that depart from the original game (ex. "8 Eyes - Playing as Simon Belmont" with whip implemented)                                      |
| Themed     | Cosmetic Mayor             | Visuals widely replaced with another universe or theme that departs from the original game (ex. Bokosuka Wars "Hyrule Wars", "Bionic Commando Winter Edition") |
| Reskin     | Cosmetic Minor             | Minor cosmetic changes that depart from the original game (ex. player sprite replaced with another character, bosses arbitrarily redrawn to look like "bugs")  |

## Romhack Rom Naming Convention

This project naming convention is based on No-Intro naming convention.

| **Document**                                                                                                                           | **Date** | **Author** | **Description**                          |
|----------------------------------------------------------------------------------------------------------------------------------------|----------|------------|------------------------------------------|
| [The Official No-Intro Convention (PDF)](https://datomatic.no-intro.org/stuff/The%20Official%20No-Intro%20Convention%20(20071030).pdf) | 2007     | No-Intro   | Last official version of the convention. |
| [Naming Convention](https://wiki.no-intro.org/index.php?title=Naming_Convention)                                                       | ?        | No-Intro   | Updated version of the convention.       |

This project extends No-Intro naming convention to provide additional information about the romhack rom.

In particular, information about the patches used to build a romhack.

**Formal definition for a romhack rom name:**

Every romhack rom name contains a pair of brackets [] for every patch applied.

If a romhack was initially distributed as a physical cart the label "(Bootleg)" is added.

⚠ Author, Versions and Alternative qualifiers CANNOT contain parenthesis on the filename. If these are present on the json file they are removed when used on the filename.

```
NAME (REGION) [(Bootleg)] [PRIMARY by AUTHOR (vVERSION) (Alt ALTERNATIVE]] ... [Add by ADD_AUTHOR (vADD_VERSION) (Alt ADD_ALTERNATIVE)]
```

One of the advantages of our naming convention is that is unambiguous and can be calculated from the metadata allowing for validation.

This is best visualized with a few examples, links to their archive folder names are provided.

**Type 1:** One romhack with one patch, your most common case: 

Example 1.1:

Results in one rom folder.

3x3 Eyes - Juuma Houkan (Japan) \[T-En by Atomizer_Zero, FamilyGuy, AkiHizirino, mkwong98 (v1.01)\].sfc

**Type 2:** One romhack with one patch, that cones in various alternative versions:

Example 2.1:

A 'main' version and a 'alternative' version, results in two rom folders:

Amazing Spider-Man, The - Lethal Foes (Japan) \[T-En by gorgyrip, filler (v1.0)\].sfc

Amazing Spider-Man, The - Lethal Foes (Japan) \[T-En by gorgyrip, filler (v1.0) (Alt Caps)\].sfc

Please notice that to avoid duplicating the 'romhack-original' folder, a symlink is used.

Example 2.2:

Two 'alternative' versions, results in two rom folders:

Dragon Ball Z - Super Gokuu Den - Totsugeki Hen (Japan) \[T-En by Kakkoii Translations (v0.98) (Alt Manga)\].sfc

Dragon Ball Z - Super Gokuu Den - Totsugeki Hen (Japan) \[T-En by Kakkoii Translations (v0.98) (Alt Anime)\].sfc

Please notice that to avoid duplicating the 'romhack-original' folder, a symlink is used.

**Type 3:** One romhack with three patches, the main one and two Add:

The contributor on this case decided that the best way of experiencing the romhack is without bugs. 

Because of that the original hack without Add is not provided this results into a single rom folder.

Example 3.1:

Final Fantasy IV (Japan) (Rev 1) \[T-En by J2e Translations (v3.21)\] \[Add by Spooniest (v1.0)\] \[Add by Masaru (v1.2)\].sfc

**Type 4:** One romhack with one patch distribution with several patches:

On this case a single patch distribution comes with several patches, a main one and a myriad of optional ones.

Since all of them are distributed together this is still considered one patch, so only one pair of brackets '[]'.

From now, Link's Awakening Redux will be used as an example, if we only apply the main patch:

Legend of Zelda - Link's Awakening Redux (Usa, Europe) [Definitive Edition by ShadowOne333 (v1.3.5)].gbc

If we apply optional patches these are considered alternative versions.

Legend of Zelda - Link's Awakening Redux (Usa, Europe) [Definitive Edition by ShadowOne333 (v1.3.5) (Alt No-THIEF, Beep)].gbc

**Romhack with several labels:**

For the romhack folder name the first label on the list, also called PRIMARY label for that romhack is used.

Additional labels rom names will be auto generated by the tools when generating the dats or roms.

## Romhack Source Code

Sometimes we are lucky enough that source code for a romhack is made available.

This source code can be preserved together with the romhack patches under the 'romhack-original' folder.

```
...
|- romhack-original
    |- 1
    |- 1-source
    ...
```

A good example is: 

4.6 Billion Year Saga, The - To Faraway Eden (Japan) \[T-En by NLeseul (v0.9b)\].sfc

## Command Line Tools

Tools provided by the Romhack Archive.

### Dat creator

This tool can be used by anyone without needing to own any roms.

It builds database files from the information found on the git repository.

It can also optionally do a series of validation checks to ensure:
- romhack.json patch metadata matches the naming of the romhack folder.
- romhack.bps metadata matches what is found on romhack.json.
- romhack-original folder has the expected structure.
- rom hashes length are correct, some tools forget to add leading zeroes when needed.

```
%/>java -jar dat-creator.jar
usage: 
	java -jar dat-creator.jar "pathToArchiveRoot" ["validate"]
```

### Romset creator

This tool can be used by build the collections of roms documented by the archive.

It requires together with the git repository the original parent roms from No-Intro.

Input roms can be compressed using zip or not, output roms will be compressed using zip.

Zip format is autodetect by looking at the file's extension.

It can also optionally do a series of validation checks to ensure rom length and hashes are correct.

```
%/>java -jar romset-creator.jar
usage: 
	java -jar romset-creator.jar "pathToArchiveRoot" "pathToInputRomRoot" "pathToOutputRomRoot" ["validate"]
```

### Rom patcher

This tool can be used to patch individual roms.

Input and output roms can be compressed using zip or not.

Zip format is autodetect by looking at the file's extension.

```
%/>java -jar rom-patcher.jar
usage: 
	java -jar rom-patcher.jar "patch" "inputRom" "outputRom"
```

### Romhack 2 Archive

This tool is intended to be used by contributors.
Is important to have correctly named parent rom and romhack rom.

The roms should already be named following No-Intro character conventions.

This tool with generate:
- correct folder structure
- romhack.bps
- romhack.json 

The json can then be finish up filling manually before submission to the database.

Input and output roms can be compressed using zip or not.

Zip format is autodetect by looking at the file's extension.

```
%/>java -jar romhack2archive.jar
usage: 
	java -jar romhack2archive.jar "parentRom" "romhackRom" "outputDir"
```

## User Safety

Some may notice the fields 'adult' and 'offensive'.

Romhacks are fan games that have been released without the approval of any rating association.

Associations like [ESRB](https://www.esrb.org/ratings-guide/) on the US and [PEGI](https://pegi.info/) in Europe need to label each game release.

As a result romhacks have never passed any scrutiny before public consumption.

Following GitHub terms of service 'adult' and 'offensive' romhacks are excluded from the public repository.