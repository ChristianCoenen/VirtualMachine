package environment;

public class Main {

	public static void main(String[] args) {
		VM tinyVM = new VM(16, 4096);
		tinyVM.run("fibonacci.asm");

	}

}
