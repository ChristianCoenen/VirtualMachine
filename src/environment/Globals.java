package environment;

public interface Globals {

	byte NOP = 0;
	byte LOAD = 1;
	byte MOV = 2;
	byte ADD = 3;
	byte SUB = 4;
	byte MUL = 5;
	byte DIV = 6;
	byte PUSH = 7;
	byte POP = 8;
	byte JMP = 9;
	byte JIZ = 10;
	byte JIH = 11;
	byte JSR = 12;
	byte RTS = 13;
	short TOMEM= 0b100000000000;
	short FROMMEM=0b100000000000;
	short R0=0000000000000000;

	// variable to enable/disable the DEBUG-Mode
	boolean DEBUG = true;
}
