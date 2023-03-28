
*= $c46f

                     .al
                     LDA $701000,x
                     AND #$00FF
                     STA $44
                     CLC
                     ADC #$30B6
                     STA $0846,y
                     LDA $32
                     STA $46
                     LDA $34
                     STA $48
                     LDA $36
                     STA $4A
                     PHX
                     PHB
                     REP #$10
                     SEP #$20
                     .as
                     LDA #$00
                     PHA
                     PLB
                     REP #$30
                     .al
                     LDA $701001,x
                     AND #$00FF
                     STA $32
                     JSR $B825