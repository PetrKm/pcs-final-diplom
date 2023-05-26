import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try (Socket client = new Socket("localhost", 8989);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(
                     new BufferedWriter(
                             new OutputStreamWriter(client.getOutputStream())), true)) {
            System.out.println("Введите слово для поиска:");
            Scanner scanner = new Scanner(System.in);
            String request = scanner.nextLine();
            out.println(request);
            String s;
            for (int i = 0; (s = in.readLine()) != null; i++) {
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}