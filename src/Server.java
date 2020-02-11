import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static void main(String[] args) {

        try (ServerSocket ss = new ServerSocket(5555)) {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            while (true) {
                pool.execute(new ClientThread(ss.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class ClientThread implements Runnable {
        Socket cs;
        ClientThread(Socket cs) {
            this.cs = cs;
        }
        @Override
        public void run() {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()), true)) {
                System.out.println("Connection established to "+ cs.getInetAddress());
                String request;

                while ((request = in.readLine()) != null) {
                    EmailExtractor emailExtractor = new EmailExtractor(request);
                    emailExtractor.extract();
                    switch(emailExtractor.responseNum) {
                        case 0:
                            out.println(0);
                            out.println(emailExtractor.toString());
                            out.println("end");
                            break;
                        case 1:
                            out.println(1);
//                            out.println("!!!No email address found on the page!!!");
                            break;
                        case 2:
                            out.println(2);
//                            out.println("!!!Server couldn’t find the web page!!!");
                            break;
                    }

                    emailExtractor.flushEmails();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
