package environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
	private String[][] assemblerToMachine = new String[20][2];
	private String[] assemblerText = new String[2000];
	private short[] machineCode= new short[2000];

	public void compile(String filename) {
		try {
			read(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assemblerToMachine();
		write(filename);
	}

	public void read(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		assemblerText = lines.toArray(new String[lines.size()]);
	}

	public void assemblerToMachine() {
		for (int i = 0; i < assemblerText.length; i++) {
			String[] lineInformation = new String[5];
			String line = assemblerText[i].substring(0, assemblerText[i].indexOf(";"));
			lineInformation = line.split(" ");

			short machinecode = 0;
			for (int j = 0; j < line.length(); j++) {
				short value =Short.parseShort(assemblerToMachine[i][2]);
				machinecode= (short) (machinecode^ value);

			}

		}

	}

	public void write(String filename) {

	}

}
