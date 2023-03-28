*= $ba69
.xl

                     PHP                     
                     PHB                     
                     REP #$10                
                     SEP #$20                
                     .as
                     LDA #$7E                
                     PHA                     
                     PLB                     
                     REP #$30                
                     .al
                     LDA $10A8     
                     STA $3C       
                     LDY #$F10C              
                     STZ $32       
                     LDA $10E2  
                     AND #$00FF              
was_BA85:            BEQ was_BA9D
                     PHA                     
                     LDA $32    
                     CLC                     
                     ADC #$0028              
                     STA $32    
                     LDA $3C    
                     SEC                     
                     SBC #$000A              
                     STA $3C    
                     PLA                     
                     DEC ;A                   
                     JMP was_BA85
was_BA9D:            LDA $32    
                     TAX                     
                     LDA #$0009              ; Number of rows
                     STA $36    
was_BAA5:            DEC $36    
                     BPL was_BAAC
                     JMP was_BB87
was_BAAC:            LDA #$000A              ; Number of columns
                     STA $38    
was_BAB1:            DEC $38    
                     BMI was_BB2E
                     LDA $10A5
                     BIT #$0020              
                     BEQ draw_unfocus
                     LDA $3C    
                     BNE draw_space_after
                     
                     TYA                     
                     STA $10E4  
                     
                     LDA $03C6B3,x
                     AND #$00FF
                     ORA #$2C00
                     STA $0840,y
                     STA $10E6
                     JMP draw_space_after
                     
draw_unfocus:        LDA $03C6B3,x
                     AND #$00FF
                     ORA #$2000
                     STA $0840,y
                     
draw_space_after:    LDA #$3000              
                     STA $0842,y
                     DEC $3C    
                     INX                     
                     INY                     
                     INY                     
                     INY                     
                     INY                     
                     
                     LDA $38    
                     CMP #$0005              ; Check for middle character
                     BNE was_BAB1
                     LDA $36    
                     BNE was_BAB1            
                     LDA $10E2  
                     AND #$00FF              
                     CMP #$0000              
                     BEQ was_BB37
                     JMP was_BAB1
was_BB2E:            TYA                     ; Handling for advance to next row
                     CLC                     
                     ADC #$0058              ; Offset to start of next row
                     TAY                     
                     JMP was_BAA5
was_BB37:            LDX #$0000              
                     LDY #$F000              
                     LDA #$0009              
                     STA $3A    
was_BB42:            DEC $3A    
                     BMI was_BB87
                     LDA $10A5  
                     BIT #$0020              
                     BEQ was_BB6A
                     LDA $3C    
                     BNE was_BB77
                     TYA                     
                     CLC                     
                     ADC #$0D60              
                     STA $10E4  
                     LDA $03C708,x
                     AND #$00FF              
                     ORA #$2C00              
                     STA $0D60,y
                     JMP was_BB77
was_BB6A:            LDA $03C708,x
                     AND #$00FF              
                     ORA #$2000              
                     STA $0D60,y
was_BB77:            LDA $3A    
                     CMP #$0004              
                     BNE was_BB80
                     DEC $3C
was_BB80:            INX                     
                     INY                     
                     INY                     
                     JMP was_BB42
was_BB87:            PLB                     
                     PLP                     
                     RTS                     

; Ends at 03:bb89
