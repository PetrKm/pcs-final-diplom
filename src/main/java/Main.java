import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

            try (ServerSocket serverSocket = new ServerSocket(8989)) {
                BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         BufferedReader reader = new BufferedReader
                                 (new InputStreamReader(clientSocket.getInputStream()))) {
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        List<PageEntry> searchingResult = engine.search(reader.readLine());
                        writer.println(new Gson().toJson(searchingResult));
                    }
                }
            } catch (IOException e) {
                System.out.println("Не могу стартовать сервер");
                e.printStackTrace();
            }
        }

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    }
}