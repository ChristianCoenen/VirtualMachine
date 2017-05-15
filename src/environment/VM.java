package environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * A virtual machine with registers, memory address, stacks and a program
 * counter.
 *
 * @author Christian, Luca
 *
 */
public class VM implements Globals {
	int pC = 0;
	short register[];
	short memoryAddress[];
	double profileCounter[];
	int averageCounter = 0;
	Stack<Short> stack = new Stack<>();
	Stack<Integer> srStack = new Stack<>();
	double sum = 0;

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
		this.profileCounter = new double[memoryAddressSize];
	}

	/**
	 * Start the vm with a given assembler file.
	 *
	 * @param filename
	 *            an interpretable assembler file
	 */
	public void run(String filename) {
		try {
			load(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Interpreter.interpret(this);
	}

	public void registers() {
		for (int i = 1000; i < 1021; i++) {
			System.out.println("memoryAddress " + i + " " + this.memoryAddress[i]);
		}
		System.out.println();
	}

	/**
	 * Loads the assembler file in the virtual machine.
	 *
	 * @param filename
	 *            an interpretable assembler file
	 */
	private void load(String filename) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filename + ".txt"));
		String line;
		int counter = 0;
		while ((line = in.readLine()) != null) {
			memoryAddress[counter] = Short.parseShort(line);
			counter++;
		}
		in.close();
	}

	public void profiling() {
		sum = 0;
		for (int i = 0; i < profileCounter.length; i++) {
			profileCounter[i] = ((profileCounter[i] * 100) / averageCounter);
			sum += profileCounter[i];
			if (profileCounter[i]>0.0) {
				System.out.println("Prozentwert von Zeile " + (i+1) + " = " + profileCounter[i]);
			}
		}
		System.out.println("Summe der Prozente: " + sum);
	}

	/**
	 * Print intern Debug information on the console
	 *
	 * @param rx
	 *            a register
	 */
	public void registerDebug(short rx) {
		if (DEBUG) {
			System.out.println("Wert von Register " + rx + ": " + register[rx]);
		}
	}
}
