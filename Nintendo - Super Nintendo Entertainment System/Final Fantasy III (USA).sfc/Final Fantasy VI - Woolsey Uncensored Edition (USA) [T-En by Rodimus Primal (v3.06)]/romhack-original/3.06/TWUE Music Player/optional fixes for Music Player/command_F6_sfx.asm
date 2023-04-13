; Event Command $F6 and map SFX playing Fix for music player    
; version 1.0
; released on 06/28/2017

hirom
;header         ; uncomment if ROM has an header


org $C002D3     ; from C0/4D02, C0/4D08, C0/4DFE, C0/B856, C0/C0F8
STA $1301       ; save song or SFX to play
LDA #$18        ; SPC flags
STA $1300       ; save flags
JSR fixA        ; save previous volume and play song / SFX
LDA $1A         ; load previous volume
STA $1302       ; restore previous volume
RTS

org $C0B889     ; event command $F6
LDA $EB         ; load flags
STA $1300       ; save as SPC flags
LDA $EC         ; load song ID
STA $1301       ; save as song to play
JSR fixB        ; save previous volume and play song
LDA $1A         ; load previous volume
STA $1302       ; restore previous volume
LDA #$04        ; event command is 4 bytes
JMP $9B5C       ; advance event queue

org $C0D613     ; can be any free space in bank $C0, you need 19 bytes
fixA:           ; save previous volume and play song / SFX
LDA #$80        ; new volume
STA $ED         ; save in RAM
fixB:           ; event command $F6 jumps here
LDA $1302       ; load previous volume
STA $1A         ; save in temp RAM
LDA $ED         ; load new volume
STA $1302       ; save it
JSL $C50004     ; play song / SFX
RTS

