package environment;

import java.util.Stack;

/**
 * A virtual machine with registers, memory address, stacks and a program counter.
 *
 * @author Christian, Luca
 *
 */
public class VM implements Globals {
	int pC = 0;
	short register[];
	short memoryAddress[];
	Stack<Short> stack = new Stack<>();
	Stack<Integer> srStack = new Stack<>();

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
		Interpreter.interpret(this);
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
	 * Print intern Debug information on the console
	 * @param rx
	 * 			 a register
	 */
	public void registerDebug(short rx) {
		if (DEBUG) {
			System.out.println("Wert von Register " + rx + ": " + register[rx]);
		}
	}
}
