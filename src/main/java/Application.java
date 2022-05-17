import java.io.Console;
import java.util.Scanner;

public class Application {
    public static void main(String []args)  {
        Scanner sc = new Scanner(System.in);
        Main main = new Main();
        boolean running = true;
        System.out.print("Enter Password: ");
        String password =sc.nextLine();
        if(password.equals("aadesh")){
            System.out.println("Welcome To the AmaterasuSQL.\nNote:Don't use any quotation marks while writing query\n And query must end with semicolon");
            System.out.println("1)Enter help to see available commands.\n2)Enter exit to exit.");
            while(running){
                System.out.print("<AmaterasuSQL># ");
                String input = sc.nextLine();
                String query = " ";
                if(input.length()>1 && !input.equals("exit")){
                    query = input;
                    input = "start";
                }
                switch (input) {
                    default -> {
                    }
                    case "exit" -> running = false;
                    case "start" -> main.evaluateStatement(query);
                }

            }
        }
        else{
            System.out.println("Incorrect Password");
        }


    }
}
