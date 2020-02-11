import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT = 5555;
//    private static final String IP = "127.0.0.1";

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter server ip: ");
        String ip = scanner.nextLine();

        try(Socket s = new Socket(ip, PORT);
            BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true)) {
            System.out.println("Connection established -> " + "[" + s.getInetAddress().getHostAddress() + "]");
            String query, sResponse;
            while(true) {
                System.out.print(">>> ");
                query = keyboardInput.readLine();
                if(query.equalsIgnoreCase("exit")) break;

                out.println(query);
                sResponse = in.readLine();
                if(sResponse.startsWith("1")){
                    System.out.println("!!!No email address found on the page!!!");
                } else if(sResponse.startsWith("2")){
                    System.out.println("!!!Server couldn't find the web page!!!");
                } else if(sResponse.startsWith("0")) {
                    sResponse = in.readLine();
                    while (sResponse != null && !sResponse.equals("end")) {
                        System.out.println("<<< " + sResponse);
                        sResponse = in.readLine();
                    }
                }

            }


        }
        System.out.println("GOOD BYE!!");
        System.out.println("Connection terminated");
        scanner.close();
    }
}
