*= $cf90

rdline:         REP #$30            ; Load destination address
                LDA $1f0000,x   
                INX             
                INX             
                ASL ;A          
                CLC             
                ADC $32         
                TAY             
                
                SEP #$20        
                
                LDA #$ff            ; Reset focus
                STA $3c         
                
                LDA #$30            ; Reset tile bank
                STA $3b
                                
rdchar:         LDA $1f0000,x       ; Read char from source
                INX             
                CMP #$ff        
                BNE output      
                                
escape:         LDA $1f0000,x       ; Handle escape sequence
                INX             
                CMP #$ff        
                BEQ term        
                CMP #$fe        
                BEQ nl          
                CMP #$bb        
                BEQ setctl      
                CMP #$dd
                BEQ settb
                CMP #$cc
                BEQ embed
                JMP rdchar      
                                
term:           LDA #$08
                TRB $10c0
                JMP $d0c5       
                                
nl:             JMP rdline      
                
setctl:         LDA $1f0000,x   
                INX             
                STA $3c         
                JMP rdchar      
                
settb:          LDA $1f0000,x
                INX
                STA $3b
                JMP rdchar
                
embed:          REP #$30
                .al
                LDA $1f0000,x
                INX
                PHX
                
                AND #$00ff
                TAX
                LDA $10ac,x
                AND #$00ff
                CMP #$00ff
                BEQ exit_embed
                
                ASL
                TAX
                LDA $1fa660,x
                TAX
                
embed_loop:     .as
                SEP #$20
                LDA $1f0000,x
                INX
                CMP #$ff
                BEQ exit_embed
                STA $e000,y
                INY
                LDA $3b
                STA $e000,y
                INY
                JMP embed_loop
                
                
exit_embed:     PLX
                .as
                SEP #$20
                JMP rdchar
                                
output:         STA $3a             ; Save char to write
                

                LDA $3c             ; Check if control is current focus
                CMP $38         
                BNE write_a
                
                LDA $36             ; Check the blink timer
                BIT #$10
                BEQ write_a_real
                
                LDA #$00            ; Write a blank
                JMP write_char
                
write_a:        LDA $10a5           ; Some flags that prevent us from writing?
                BIT #$0020
                BEQ write_a_real
                LDA $10c0
                BIT #$0008
                BNE write_a_real
                INY                 ; If we're not writing this character, we still need to advance the output pointer
                INY
                JMP rdchar
write_a_real:   LDA $3a
write_char:     STA $e000,y
                INY
                

                
output_pal:     LDA $3b             ; Figure out how to write the palette/tile bank
                CMP #$20
                BNE write_tilebk
                
                                    ; Special tile bank logic
                LDA $3c             ; Is control current focus?
                CMP $38             
                BEQ write_30
                
                PHX                 ; Load navigation map and see if this control is disabled
                REP #$30
                .al
                LDA $10ba
                AND #$00ff
                ASL
                TAX
                
                LDA $1f94ee,x
                CLC
                ADC $3c
                TAX
                
                LDA $1f0000,x
                
                PLX
                SEP #$20
                .as
                CMP #$FF
                BEQ write_24

                LDA $3b
                JMP write_tilebk
                
write_30:       LDA #$30
                JMP write_tilebk
                
write_24:       LDA #$24
                             
write_tilebk:   STA $e000,y
                INY
                
                JMP rdchar