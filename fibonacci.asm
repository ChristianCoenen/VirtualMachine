LOAD 20;  //Load the n for fib(n)
MOV R4 R0; //Set the value to the right register
PUSH R4; //Fibonacci
LOAD 0;
MOV R3 R0; //set register 3 to zero
MOV R0 R4; //get the n-value from the current recursion depth
JIZ 26; // if the current n value is zero this means that n==2 or higher
MOV R1 R0; // move the current n because we need to load
LOAD 1;
SUB R1 R0;//decrease current n by one
MOV R0 R1; //move current n back for the JIZ
JIZ 24; //if the current n value is zero this means that n==1
LOAD 1;
MOV R2 R1; //copy the current n for decrease
SUB R2 R0; //decrease current n by 1
PUSH R2; //save  n-1 for the next recursion-depth
MOV R4 R1; //move the current n to register 4 for the next subroutine-call
JSR 2;
POP R4; //get n-1 from the last recursion-depth
PUSH R0; //save the current n
JSR 2;
POP R3; //get the last n
ADD R3 R0; //set the solution from the last recursion-depth to register 3
JMP 26;
LOAD 1; //ADDRESSE: Jump when n==1
MOV R3 R0; //load and move a 1 for the deepest recursion-depths
LOAD 2;//ADDRESSE: Jump when n>0
POP R4; // get last n
LOAD 1000; //load 1000 for the saveadresses
ADD R4 R0; //calculate the memoryaddress for the current recursion-depth solution
MOV (R4) R3; //save the solution at the right memoryaddress
MOV R0 R3; //move it back to register 0 for one recursion-depth higher
RTS; // return to next command after subroutine call and ends the programm when the stack is empty
