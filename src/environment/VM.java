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
	 *            size of the memory addresses
	 */
	VM(int registerSize, int memoryAddressSize) {
		this.register = new short[registerSize];
		this.memoryAddress = new short[memoryAddressSize];
	}

	/**
	 * Starts the vm with a given assembler file.
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

	private void load(String filename) {

	}

	private void simulateLoad(short[] test) {
		memoryAddress = test;

	}

	private void execute() {
		while (pC < 2000) {
			executeLine(memoryAddress[pC]);
		}
	}

	private void executeLine(short opCode) {
		byte cmd = (byte) (opCode & 15);
		short data = (short) (opCode >> 4);
		byte rx = (byte) ((opCode >> 4) & 15);
		byte ry = (byte) ((opCode >> 8) & 15);
		if (DEBUG) {
			System.out.println("Command NR: " + cmd);
		}
		switch (cmd) {
		case NOP: {
			pC++;
			break;
		}
		case LOAD: {
			register[0] = data;
			pC++;
			if (DEBUG) {
				System.out.println("Wert von Register 0: " + register[0]);
			}
			break;
		}
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
		case ADD: {
			register[rx] += register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		case SUB: {
			register[rx] -= register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		case MUL: {
			register[rx] *= register[ry];
			pC++;
			registerDebug(rx);
			break;
		}
		case DIV: {
			if(register[ry] != 0) {
				register[rx] /= register[ry];
			}
			else {
				System.out.println("Division nicht ausgeführt, da Ry = 0!");
			}
			pC++;
			registerDebug(rx);
			break;
		}
		case PUSH: {
			stack.push(register[rx]);
			pC++;
			break;
		}
		case POP: {
			register[rx] = stack.pop();
			pC++;
			break;
		}
		case JMP: {
			pC = data;
			break;
		}
		case JIZ: {
			if (register[0] == 0) {
				pC = data;
			}
			break;
		}
		case JIH: {
			if (register[0] > 0) {
				pC = data;
			}
			break;
		}
		case JSR: {
			srStack.push(pC++);
			pC = data;
			break;
		}
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

	private void registerDebug(short rx) {
		if (DEBUG) {
			System.out.println("Wert von Register " + rx + ": " + register[rx]);
		}
	}
}
