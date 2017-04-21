LOAD 1001; 	0	startaddr
MOV R3 R0;
LOAD 1;			1 zum rechnen
MOV R6 R0;
MOV (R3) R6;		1 auf 1001
MOV R4 R3; 	5	f n-2 sichern
MOV (R3) R6;		start + 1
ADD R6 R3;
MOV (R3) R6;		1 auf 1002
MOV R5 R4;		f n-1 sichern
ADD R4 R6;	10	R4 f n-2
ADD R3 R6;		R3 f n-1
LOAD 18;		schleifenzähler schreiben
MOV R15 R0;
JSR 18;			routinen aufruf
MOV R0 R15;	15	zähler hochholen
JIH 13;			spring zu 11 wenn R0>0
JMP 4095; 		spring zu programmende
MOV R12 R7;		temp1 löschen
MOV R13 R7;		temp2 löschen
MOV R12 (R4);	20	f n-2 hohlen
MOV R13 (R5);		f n-1 hohlen
ADD R12 R13;		f berechnen
MOV (R3) R12;		f beschreiben
ADD R3 R6;		addressen hochzählen
ADD R4 R6;	25
ADD R5 R6;
SUB R15 R6;		schleifenzähler--
RTS;