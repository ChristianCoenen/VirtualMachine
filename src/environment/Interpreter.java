package environment;

import java.util.EmptyStackException;
/**
 * A interpreter class, which inteprets machinecode in the memory addresses of a virtual machine.
 *
 * @author Christian, Luca
 *
 */
public class Interpreter implements Globals {


	/**
	 * Private constructor, no interpreter object should be created
	 */
	private Interpreter() {};

	/**
	 * Execute and interpret the machinecode in the memoryAdresses
	 *
	 * @param v
	 * 		a virtual machine object
	 */
	public static void interpret(VM v) {
		while (v.pC < 2000) {
			v.averageCounter++;
			v.profileCounter[v.pC]=v.profileCounter[v.pC]+1;
			System.out.println("erhöhre wert von zeile: "+v.pC);
			interpretLine(v.memoryAddress[v.pC], v);
		}
	}

	/**
	 * Execute and interpret the machinecode in one memoryAddress
	 *
	 * @param opCode
	 * 			the machinecode
	 * @param v
	 * 			a virtual machine object
	 */
	private static void interpretLine(short opCode, VM v) {
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
			v.pC++;
			break;
		}
		// sets register zero to the given data
		case LOAD: {
			v.register[0] = data;
			v.pC++;
			if (DEBUG) {
				System.out.println("Wert von Register 0: " + v.register[0]);
			}
			break;
		}
		case MOV: {
			boolean fromMem = ((opCode >> 12) & 1) == 1;
			boolean toMem = ((opCode >> 13) & 1) == 1;
			// writes the value of the register ry into the memory with address given by the value in register rx
			if (toMem && !fromMem) {
				v.memoryAddress[v.register[rx]] = v.register[ry];
				v.registers();
			}
			// writes the value of the memory with address given by the value in register ry in the register rx
			else if (!toMem && fromMem) {
				v.register[rx] = v.memoryAddress[v.register[ry]];
			}
			// writes the value of the memory with address given by the value in register ry in the memory with address given by the value in register rx
			else if (toMem && fromMem) {
				v.memoryAddress[v.register[rx]] = v.memoryAddress[v.register[ry]];
			}
			// writes the value of register ry in the register rx
			else if(!toMem && !fromMem) {
				v.register[rx] = v.register[ry];
			}
			v.pC++;
			break;
		}
		// adds the value of register ry to the register rx
		case ADD: {
			v.register[rx] += v.register[ry];
			v.pC++;
			v.registerDebug(rx);
			break;
		}
		// subtracts the value of register ry from the value of register rx and
		// writes the solution into the register rx
		case SUB: {
			v.register[rx] -= v.register[ry];
			v.pC++;
			v.registerDebug(rx);
			break;
		}
		// multiplies the value of register rx with the value of register ry and
		// writes the solution into the register rx
		case MUL: {
			v.register[rx] *= v.register[ry];
			v.pC++;
			v.registerDebug(rx);
			break;
		}
		// divides the value of register rx with the value of register ry,
		// checks for divide-by-zero and writes the solution into the register rx
		case DIV: {
			if (v.register[ry] != 0) {
				v.register[rx] /= v.register[ry];
			} else {
				System.out.println("Division nicht ausgeführt, da Ry = 0!");
			}
			v.pC++;
			v.registerDebug(rx);
			break;
		}
		// pushes the value of register rx to the stack
		case PUSH: {
			v.stack.push(v.register[rx]);
			v.pC++;
			break;
		}
		// writes the lastly pushed number from the stack to the register rx
		case POP: {
			v.register[rx] = v.stack.pop();
			v.pC++;
			break;
		}
		// sets the programmcounter to the given address
		case JMP: {
			v.pC = data;
			break;
		}
		// sets the programmcounter to the given address if the value of
		// register zero is zero
		case JIZ: {
			if (v.register[0] == 0) {
				v.pC = data;
			}else{
				v.pC++;
			}
			break;
		}
		// sets the programmcounter to the given address if the value of
		// register zero is bigger then zero
		case JIH: {
			if (v.register[0] > 0) {
				v.pC = data;
			}
			break;
		}
		// sets the programmcounter to the given address and pushes the last
		// programmcounter+1 to the srStack
		case JSR: {
			v.pC++;
			v.srStack.push(v.pC);
			v.pC = data;
			break;
		}
		// sets the programmcounter to the lastly pushed value on the rsStack
		case RTS: {
			try {
				v.pC = v.srStack.pop();
			} catch (EmptyStackException e) {
				v.profiling();
				System.exit(0);
			}
			break;
		}

		}
	}
}
