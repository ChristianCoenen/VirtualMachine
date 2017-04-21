package environment;

public class Main {

	public static void main(String[] args) {
		VM tinyVM = new VM(16, 4096);
		Compiler.compile("fibonacci");
		tinyVM.run("fibonacci.txt");

	}

}
