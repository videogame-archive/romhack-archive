![videogame archive](./docs/readme/videogame-archive.png "Videogame Archive")

# Romhack Archive

# Table of Contents
1. [Introduction](#Introduction)
2. [Objective](#Objective)
3. [Current Goals](#Current-Goals)
4. [Non Goals](#Non-Goals)
5. [How To Contribute](#How-To-Contribute)
6. [Contributor Guide](#Contributor-Guide)
7. [Other General Information](#Other-General-Information)

## Introduction
Romhacks are fan games or mods that run on the same hardware as the original games did.

These have only been able to be made by studding the original games meticulously, as a result thanks to them, we have today a better understanding of both the original games and systems they run on.

Moreover, they tell a lot of how different interest, culture and game design evolution made fans improve or change the games that inspired them.

Sadly, these have been distributed more often than not as patches, and more often than not using patch formats that don't allow for validation.

The lack of an open project providing database files to allow both archival and easier distribution was becoming increasingly worrying, as a result this project was born.

## Objective
This project follow the same kind of standards as No-Intro and Redump, so expect similar rules.

This repository contains in a 'structured' way, both 'information' and 'patches' related with romhacks.

The project goal is to 'normalize' the original patches distributed into a structure that is easier to process to deliver database (DAT) files. These database files are used in rom managers to help validate and archive collections.

**YOU WILL NOT FIND ROMS HERE**

## Current Goals

- [x] Write tool to patch roms on main formats
- [x] Write tool to create dat file from database
- [x] Write tool to create roms from database
- [x] Write tool to create a release template from input/output roms
- [ ] Porting romhacks/translations for SNES
- [ ] Porting romhacks/translations for NES
- [ ] Porting romhacks/translations for GB
- [ ] Porting romhacks/translations for GBC
- [ ] Porting romhacks/translations for GEN
- [ ] Porting romhacks/translations for N64
- [ ] Porting romhacks/translations for GBA

## Non Goals

- This project CANNOT replace the original community sites. This project is mainly an archival project.
- This project CANNOT take 'unique content' from community sites. Example: text descriptions or images.

## How To Contribute

⚠ ⚠ ⚠ Looking for Contributors ⚠ ⚠ ⚠

- Are you part of a romhacking community site and would like to help us build a relationship?

- Would like to help us build the database? Check the [Contributor Guide](./CONTRIBUTOR-GUIDE.md)

For these please best join our discord https://discord.gg/AEV88uDTeQ or email us at: vg-archive@protonmail.com

- Would you like to report that you found a lost hack/s? 

Please send email to vg-archive@protonmail.com with means to find them.

## Contributor Guide

Can be found next to this readme file, here: [Contributor guide](./CONTRIBUTOR-GUIDE.md).

## Other General Information

### Patch formats
There are many patch formats, some only used by certain communities to patch certain games, other more widely adopted.

Today IPS and BPS patch formats are considered De facto standards. Hence, the focus on these.

This archive tries to provide a single bps patch for every hack.

| **Format**                      | **Creation Date** | **Author** | **Description**                                                                         |
|---------------------------------|-------------------|------------|-----------------------------------------------------------------------------------------|
| [IPS](./docs/ips/ips_spec.md)   | >= 1993           | Unknown    | Original patch format, used to this day. Mainly used when validation is better avoided. |
| [UPS](./docs/ups1/ups-spec.pdf) | 2008              | Near       | Designed to replace IPS. Provides validation for input and output.                      |
| [BPS](./docs/bps1/bps_spec.md)  | 2012              | Near       | Designed to replace IPS. Provides validation for input, output and patch.               |

### Why only distribute patches?
The advantage over patched roms is that patches are normally very small and don't contain the original rom that is more likely copyrighted. This is currently the accepted approach as is used by all community sites.

### Why a git repository?
Initially it seems like a misuse of Git since the bulk of the repository is going to be binary data coming from patches.

Git is replacing:
- A website used to share content.
- An application used to submit content.

In this case Git provides instantly the infrastructure needed to bootstrap the project.

Most romhacks:
- Come from the first four console generations.
- Are created from very small patch files.

It is to acknowledge that to handle romhacks from fifth console generation onwards some extensions will be needed.