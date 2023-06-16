# Compilador
Dadas las producciones:

| | |
| ---| ---|
| 0) | PROGP -> PROG |
1) PROG -> DATASEC PRIN
2) DATSEC -> datos VARS fin_datos
3) DATSEC -> epsilon
4) VARS -> TIPO : LISTA VARS
5) VARS -> TIPO : LISTA
6) LISTA -> id , LISTA
7) LISTA -> id
8) PRIN -> { BLQ }
9) TIPO -> entero
10) TIPO -> decimal
11) BLQ -> INST BLQ
12) BLQ -> INST
13) INST -> COND
14) INST -> ASIG
15) INST -> CICLO
16) COND -> cierto ( EXP ) haz BLQ falso BLQ fin_cond
17) COND -> cierto ( EXP ) haz BLQ fin_cond
18) CICLO -> mientras ( EXP ) BLQ fin_mientras
19) ASIG -> id asig E
20) EXP -> E OP E
21) E -> E + F
22) E -> E - F
23) E -> F
24) F -> F * S
25) F -> F / S
26) F -> S
27) S -> ent
28) S -> dec
29) S -> id
30) S -> ( E )
31) OP -> may
32) OP -> men
33) OP -> mayi
34) OP -> meni
35) OP -> igual
36) OP -> dif

La clase `*Al.java` recibe un archivo `*.prg` como entrada. Esta realiza un analisis lexico del archivo y genera un archivo `*.sal`. Este ultimo archivo es la entrada de la clase `*SLR1.java`, la cual realiza un analisis sintatico y generacion de codigo (a Ensamblador).
