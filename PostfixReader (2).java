import java.io.*;

/**
 * The program PostfixReader.java takes a postfix expression, checks its validity 
 * and states if it is an invalid expression, otherwise prints the infix form of the 
 * expression as well as the result after processing it
 * @author tabid
 *
 */

public class PostfixReader {
	public static void main(String[] args) {
		PostfixReader myAnswer = new PostfixReader();
		myAnswer.doConversion();
	}

	/**
	 * converts Postfix from input into infix and prints out the result
	 */
	public void doConversion() {
		// TODO: read Postfix from input using readPostfix(), then convert it to infix and
		// print it out
		
		String Postfix[] = readPostfix();
		//check if expression valid, if not print error and end
		if(!isValid(Postfix)) {
			System.out.println("Error: Invalid postfix");
		}
		else {		
			Stack myStack = new Stack();
			//adding everything into stack
			int i = Postfix.length - 1;
			while(i >= 0) {
				myStack.push(Postfix[i]);
				i--;
			}
			
			String output_infinix[] = new String[100];
			int index = 0;  //to track last index of output_infinix. it always points to null
			
			while(!myStack.isEmpty()) { //while stack is not empty
				if(!isOperator(myStack.top())){  //if not an operator then simply add to output
					output_infinix[index] = myStack.pop();
					index++;
				}
				else {  //its an operator, retrieve last 2 from output, reduce its index by 2, form new and add
					String retrieve2 = output_infinix[index-1];
					String retrieve1 = output_infinix[index-2];
					output_infinix[index-2] = "( " + retrieve1 + " " + myStack.top() + " " + retrieve2 + " )";
					output_infinix[index-1] = null;
					index--;
					myStack.position--;
				}
			}
			System.out.println("Infix: " + output_infinix[index-1]);
			evalInfix(output_infinix[index-1]);
		}
	}
	
	/**
	 * checks a string variable and determines if it is an operator 
	 * @param s string to check if it is an operator or not
	 * @return true if operator, false otherwise
	 */
	public boolean isOperator(String s) {
		char cmp = s.charAt(0);
		if(s.length() > 1) {
			return false;
		}
		if(cmp == '+' || cmp == '-' || cmp == '*' || cmp == '/' || cmp == '^') {
			return true;
		}
		return false;
	}
	
	/**Method to check validity of postfix
	 * keep a counter.
	When you see a literal, increment the counter.
	When you see an operator, decrement the counter twice, then increment it.
	At the end of the string, if the counter is 1, and if it never went below 0, 
	the string is valid.
	*/
	
	/**
	 * performs a check if the expression is a valid postfix expression through the above algorithm
	 * @param postfix_arr String array which comprises of the postfix expression broken up 
	 * after using space as a delimiter.
	 * @return returns true if the postfix expression is valid.
	 */
	public boolean isValid(String postfix_arr[]) {
		int i = 0; 
		int counter = 0;
		boolean validity = true;
		while(i < postfix_arr.length && validity == true) {
			String test_val = postfix_arr[i];
			
			if(isOperator(test_val)) {
				counter -= 2;
				if(counter < 0) {
					validity = false;
				}
				counter++;
			}
			else {
				counter++;
			}
			i++;
		}
		if(counter == 1 && validity == true) {
			return true;
		}
		return false;
	}

	/**
	 * method to evaluate the result of a given infix expression
	 * @param infix, a string representation of the infix form
	 */
	public void evalInfix(String infix) {
		// TODO: evaluate the infix representation of the input arithmetic expression, 
		// and then print the result of the evaluation of the expression on the next 
		// line.
		
		/**PROCEDURE
		 * Pop-out two values from the operand stack, let’s say it is A and B.
		Pop-out operation from operator stack. let’s say it is ‘+’.
		Do  A + B and push the result to the operand stack.
		
		Algorithm:
		Iterate through given expression, one character at a time
		Once the expression iteration is completed and the operator stack is not empty,do
		Process until the operator stack is empty.The values left in the 
		operand stack is our final result.
		 * */
		Stack operand_Stack = new Stack();
		Stack operator_Stack = new Stack();
		String retrieved[] = infix.split(" ");
		for(int i = 0; i < retrieved.length; i ++) {
			char cmp = retrieved[i].charAt(0);
			
			// If character is operand push on the operand stack, OR if character is '(', 
			// push on the operator stack.
			if(retrieved[i].length() > 1 || 
					(!isOperator(retrieved[i]) && cmp != '(' && cmp != ')')) {
				operand_Stack.push(retrieved[i]);
			}
			else if(cmp == '(') {
				operator_Stack.push(retrieved[i]);
			}
			//If the character is “)”, then do Process (as explained above) until the 
			//corresponding “(” is encountered in operator stack. Now just pop out the “(“.
			else if(cmp == ')') {
				while((operator_Stack.top()).charAt(0) != '(') {
					String A2 = operand_Stack.pop();
					String A1 = operand_Stack.pop();
					String operation = operator_Stack.pop();
					String part_Main_equation = A1 + " " + operation + " " + A2;
					operand_Stack.push(Evaluate(part_Main_equation));

				}
				operator_Stack.pop();  // to pop the top '('
			}
			/**If the character is an operator,
			If the operator stack is empty then push it to the operator stack OR If 
			the character’s precedence is greater than or equal to the precedence of the 
			stack top of the operator stack, then push the character to the operator stack.
			If the character’s precedence is less than the precedence of the stack top of 
			the operator stack then do Procedure(as above) until character’s precedence is 
			less or stack is not empty.
			*/
			else {
				if(operator_Stack.isEmpty() ||
						precedence(retrieved[i]) >= precedence(operator_Stack.top())) {
					operator_Stack.push(retrieved[i]);
				}
				else {
					while(precedence(retrieved[i]) < precedence(operator_Stack.top()) && 
							!operator_Stack.isEmpty()) {
						String A2 = operand_Stack.pop();
						String A1 = operand_Stack.pop();
						String operation = operator_Stack.pop();
						String part_Main_equation = A1 + " " + operation + " " + A2;
						operand_Stack.push(Evaluate(part_Main_equation));
					}
				}
			}
		}
		//iteration completed so check if anything left in operator stack
		if(!operator_Stack.isEmpty()) {
			String A2 = operand_Stack.pop();
			String A1 = operand_Stack.pop();
			String operation = operator_Stack.pop();
			String part_Main_equation = A1 + " " + operation + " " + A2;
			operand_Stack.push(Evaluate(part_Main_equation));
		}
		System.out.println("Result: " + operand_Stack.top());
	}

	/**calculates the result of tokens of the infix expression. Note the expression is a 
	 * string but it is processed accordingly.
	 * @param expression, a token of the original infix expression being processed in bits.
	 * @return the result of the calculation after being converted back to string.
	 */
	public String Evaluate(String expression) {
		String equation[] = expression.split(" ");
		int A = Integer.parseInt(equation[0]);
		char action = equation[1].charAt(0);
		int B = Integer.parseInt(equation[2]);
		int result = 0 ;
		
		switch(action) {
		case '+':
			result = A + B;
			break;
        case '-':
            result = A - B;
            break;
        case '*':
        	result = A * B;
        	break;
        case '/':
            result = A / B;
            break;
        case '^':
        	result = (int)Math.pow(A, B);
        	break;
		}
		return Integer.toString(result);
	}
	
	/**
	 * method to read a space delimited postfix expression input
	 * @return returns a string array composing of the separated postfix
	 */
	public String[] readPostfix() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		try {
			System.out.print("Input Postfix: ");
			inputLine = input.readLine();
			return inputLine.split(" ");
		} catch (IOException e) {
			System.err.println("Input ERROR.");
		}

		// return empty array if error occurs
		return new String[] {};
	}
	
	/**
	 * a method to determine the precedence of the different operators
	 * @param x, the operator to check the precedence level of
	 * @return the integer associated with the precedence of each operator.
	 */
	public int precedence(String x){
		char foo = x.charAt(0);
        switch (foo){
        case '+':
        case '-':
            return 1;
        case '*':
        case '/':
            return 2;
        case '^':
            return 3;
    }
        return -1;
	}

}

/**
 * Implementation of a custom stack class with support of features like push, pop etc.
 * @author tabid
 */
class Stack {
	// TODO: implement Stack in this class	
	
	String stack_array[] = new String[100];
	int position = 0;
	/**
	 * Method to check whether the stack is empty
	 * @return returns a true value if the stack is empty 
	 */
	public boolean isEmpty() {
		return position == 0;
	}
	
	/**
	 * method to push values into the stack
	 * @param val, the value to insert in the stack.
	 */
	public void push(String val){
		stack_array[position] = val;
		position++;
	}
	
	/**
	 * Method to remove the top element from the stack
	 * @return returns the element that was at the top of the stack
	 */
	public String pop() {
		String temp = stack_array[position-1];
		stack_array[position-1] = null;
		position--;
		return temp;
	}
	
	/**
	 * a peek method to get the top most element in a stack
	 * @return returns the last element which was pushed into the stack.
	 */
	public String top() {
		return stack_array[position-1];
	}
}