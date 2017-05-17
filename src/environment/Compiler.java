package environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler implements Globals {
	// array which is used to store the assembler-test line by line
	private static String[] assemblerText = new String[1000];
	// array which is used to store the compiled assembler-code opCode by opCode
	private static short[] machineCode = new short[1000];

	/**
	 * handles the different parts of the compilation
	 *
	 * @param filename
	 *            the file where the assembler-code is
	 */
	public static void compile(String filename) {
		try {
			read(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		compileLine();
		try {
			write(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Basic input method which reads the given file and stores the code line by
	 * line into the correct array
	 *
	 * @param filename
	 *            which will be read-out
	 * @throws IOException
	 *             when the file is not correct
	 */
	private static void read(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename + ".asm");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		assemblerText = lines.toArray(new String[lines.size()]);
	}

	/**
	 * first we parse and split the text, then we compile every single part of
	 * it and then put them all together with an XOR
	 *
	 */
	private static void compileLine() {
		for (int i = 0; i < assemblerText.length; i++) {
			String[] lineInformation;
			// deleting unnecessary content(comments etc.)
			String line = assemblerText[i].substring(0, assemblerText[i].indexOf(';'));
			if (DEBUG) {
				System.out.println(line);
			}
			lineInformation = line.split(" ");

			short machinecodeParts[] = new short[lineInformation.length];
			// compile the command information of this line
			machinecodeParts[0] = compileWord(lineInformation[0]);
			// when the line has only two parts, there is only the data left to
			// compile, just left-shift with 4 and then parse it into short
			if (lineInformation.length == 2) {
				machinecodeParts[1] = (short) (Short.parseShort(lineInformation[1].replaceAll("[R]", "")) << 4);
			} else if (lineInformation.length == 3) {
				// When the line has three parts, its a MOV command.
				// Here we need to get the toMem and fromMem information first
				boolean isToMem = lineInformation[1].matches("\\(R.*\\)");
				boolean isFromMem = (lineInformation[2].matches("\\(R.*\\)"));
				// now delete the unnecessary characters
				lineInformation[1] = lineInformation[1].replaceAll("[()R]", "");
				lineInformation[2] = lineInformation[2].replaceAll("[()R]", "");
				if (DEBUG) {
					System.out.println("isToMem: " + isToMem + " isFromMem: " + isFromMem);
				}
				// for every combination of toMem and fromMem we add the correct
				// bits for this case
				machinecodeParts[1] = (short) (Short.parseShort(lineInformation[1]) << 4
						^ ((isToMem ? 2 : 0) + (isFromMem ? 1 : 0) << 12));
				machinecodeParts[2] = (short) (Short.parseShort(lineInformation[2]) << 8);
			}
			if (DEBUG) {
				for (int l = 0; l < machinecodeParts.length; l++) {
					System.out.print(machinecodeParts[l] + " ,");
				}
				System.out.println("machinecodeParts fertig");
			}
			short machinecodeFinish = 0;
			// combining the compiled parts of every assembler-line to finally
			// save the finished machine-code
			for (int k = 0; k < machinecodeParts.length; k++) {
				machinecodeFinish = (short) (machinecodeFinish ^ machinecodeParts[k]);
			}
			machineCode[i] = machinecodeFinish;
			if (DEBUG) {
				System.out.println(machinecodeFinish);
			}
		}

	}

	/**
	 * basic output function, to write the machine-code into the new file
	 *
	 * @param filename
	 *            target-file
	 * @throws IOException
	 */
	private static void write(String filename) throws IOException {
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(filename + ".txt"));
		for (int i = 0; i < machineCode.length; i++) {
			outputWriter.write(machineCode[i] + "");
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}

	/**
	 * give a command and get his byte-value
	 *
	 * @param assembler
	 * @return
	 */
	private static byte compileWord(String assembler) {
		String[] strings = {"NOP", "LOAD", "MOV", "ADD", "SUB", "MUL", "DIV", "PUSH", "POP", "JMP", "JIZ", "JIH", "JSR", "RTS"};
		for(int i = 0; i < strings.length; i++) {
			if(assembler.equals(strings[i])) {
				return (byte) i;
			}
		}
		return -1;
	}

}
