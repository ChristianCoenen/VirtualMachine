package environment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
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
		if (false) {
			short[] test = new short[memoryAddress.length];
			test[0] = 0b100001;
			test[1] = 0b000000010011;
			test[2] = 0b11001;
			simulateLoad(test);
		} else {
			try {
				load(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Interpreter.interpret(this);
		registers();
	}


	public void registers(){
		for(int i=1000; i<1021; i++){
			System.out.println("memoryAddress "+i+ " "+this.memoryAddress[i]);
		}
	}

	/**
	 * Loads the assembler file in the virtual machine.
	 *
	 * @param filename
	 *            an interpretable assembler file
	 */
	 private void load(String filename) throws IOException{
	 BufferedReader in = new BufferedReader(new FileReader(filename+".txt"));
	 String line;
	 int counter=0;
	 while((line = in.readLine()) != null) {
	 memoryAddress[counter]=Short.parseShort(line) ;
	 counter++;
	 }
	 in.close();
	 }
//	public void load(String filename) throws IOException, ClassNotFoundException {
//		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename + ".dat"));
//		memoryAddress = (short[]) in.readObject();
//		in.close();
//	}

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
