package environment;

import java.util.EmptyStackException;
import java.util.Stack;

public class VM implements Globals {
	// variable to enable/disable the DEBUG-Mode
	private final static boolean DEBUG = true;

	private int pC = 0;
	private short register[];
	private short memoryAddress[];
	private Stack<Short> stack = new Stack<>();
	private Stack<Integer> srStack = new Stack<>();

	/**
	 * Constructor to create a virtual machine.
	 *
	 * @param registerSize
	 *            size of the register array
	 * @param memoryAddressSize
	 *            size of the memory address array
	 */
	VM(int registerSize, int memoryAddressSize) {
		this.register = new short[registerSize];
		this.memoryAddress = new short[memoryAddressSize];
	}

	/**
	 * Start the vm with a given assembler file.
	 *
	 * @param filename
	 *            an interpretable assembler file
	 */
	public void run(String filename) {
		if (DEBUG) {
			short[] test = new short[memoryAddress.length];
			test[0] = 0b100001;
			test[1] = 0b000000010011;
			test[2] = 0b11001;
			simulateLoad(test);
		} else {
			load(filename);
		}
		execute();
	}

	/**
	 * Loads the assembler file in the virtual machine.
	 *
	 * @param filename
	 *            an interpretable assembler file
	 */
	private void load(String filename) {

	}

	/**
	 * Loads the assembler file in the virtual machine.
	 *
	 * @param test
	 *            an array with machinecode instructions
	 */
	private void simulateLoad(short[] test) {
		memoryAddress = test;

	}

	/**
	 * Execute and interpret the machinecode in the memoryAdresses
	 */
	private void execute() {
		while (pC < 2000) {
			executeLine(memoryAddress[pC]);
		}
	}

	/**
	 * Execute and interpret the machinecode in one memoryAddress
	 * @param opCode
	 * 			the machinecode
	 */
	private void executeLine(short opCode) {
		byte cmd = (byte) (opCode & 15);
		short data = (short) (opCode >> 4);
		byte rx = (byte) ((opCode >> 4) & 15);
		byte ry = (byte) ((opCode >> 8) & 15);
		if (DEBUG) {
			System.out.println("Command NR: " + cmd);
		}
		switch (cmd) {
		// do nothing for one command
		case NOP: {
			pC++;
			break;
		}
		// sets register zero to the given data
		case LOAD: {
			register[0] = data;
			pC++;
			if (DEBUG) {
				System.out.println("Wert von Register 0: " + register[0]);
			}
			break;
		}
		// To-DO
		case MOV: {
			boolean fromMem = ((opCode >> 12) & 1) == 1;
			boolean toMem = ((opCode >> 13) & 1) == 1;
			if (fromMem && toMem) {
				register[ry] = register[rx];
			}
			if (fromMem && toMem) {
				register[rx] = register[ry];
			}
			if (fromMem && toMem) {
				short temp = register[rx];
				register[rx] = register[ry];
				register[ry] = temp;
			}
			pC++;
			break;
		}
		// adds the value of register ry to the register rx
		case ADD: {
			register[rx] += register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		// subtracts the value of register ry from the value of register rx and
		// writes the solution into the rgister rx
		case SUB: {
			register[rx] -= register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		// multiplies the value of register rx with the value of register ry and
		// writes the solution into the rgister rx
		case MUL: {
			register[rx] *= register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		// divides the value of register rx with the value of register ry,
		// checks for divide-by-zero and writes the solution into the rgister rx
		case DIV: {
			if (register[ry] != 0) {
				register[rx] /= register[ry];
			} else {
				System.out.println("Division nicht ausgeführt, da Ry = 0!");
			}
			pC++;
			registerDebug(rx);
			break;
		}
		// pushes the value of register rx to the stack
		case PUSH: {
			stack.push(register[rx]);
			pC++;
			break;
		}
		// writes the lastly pushed number from the stack to the register rx
		case POP: {
			register[rx] = stack.pop();
			pC++;
			break;
		}
		// sets the programmcounter to the given address
		case JMP: {
			pC = data;
			break;
		}
		// sets the programmcounter to the given address if the value of
		// register zero is zero
		case JIZ: {
			if (register[0] == 0) {
				pC = data;
			}
			break;
		}
		// sets the programmcounter to the given address if the value of
		// register zero is bigger then zero
		case JIH: {
			if (register[0] > 0) {
				pC = data;
			}
			break;
		}
		// sets the programmcounter to the given address and pushes the last
		// programmcounter+1 to the srStack
		case JSR: {
			srStack.push(pC++);
			pC = data;
			break;
		}
		// sets the programmcounter to the lastly pushed value on the rsStack
		case RTS: {
			try {
				pC = srStack.pop();
			} catch (EmptyStackException e) {
				System.exit(0);
			}
			break;
		}

		}
	}

	/**
	 * Print intern Debug information on the console
	 * @param rx
	 * 			 a register
	 */
	private void registerDebug(short rx) {
		if (DEBUG) {
			System.out.println("Wert von Register " + rx + ": " + register[rx]);
		}
	}
}
