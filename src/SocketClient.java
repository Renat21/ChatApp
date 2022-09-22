import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class SocketClient {

    static Scanner in;

    public static void listenForMessage()
    {
        new Thread(() -> {
            String messageFromGroupChat;
            while (true)
            {
                messageFromGroupChat=in.nextLine();
                System.out.println(messageFromGroupChat);
            }
        }).start();
    }
    public static void main(String[] args) throws
            Exception {
        try (var socket = new Socket("127.0.0.1", 59898)) {
            System.out.println("Вы в чате");
            var scanner = new Scanner(System.in);
            in = new Scanner(socket.getInputStream());
            var out = new
                    PrintWriter(socket.getOutputStream(), true);

            listenForMessage();
            while (true) {
                if (scanner.hasNextLine())
                    out.println(scanner.nextLine());
            }

        }
    }
}