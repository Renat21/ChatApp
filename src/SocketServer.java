import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;


public class SocketServer {

    private static final Timer timer = new Timer();
    private static final ArrayList<PrintWriter> clientList = new ArrayList<>();
    private static final List<String> messages = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898))
        {
            System.out.println("Сервер запущен...");
            var pool = Executors.newFixedThreadPool(20);
            allMessages();
            while (true) {
                ClientHandler s = new ClientHandler(listener.accept());
                clientList.add(s.out);
                pool.execute(s);
            }
        }
    }
    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public Scanner in;
        public PrintWriter out;
        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;

            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        @Override
        public void run() {
            System.out.println("Подключение: " + socket);
            try {

                while (in.hasNext()) {
                    String string = in.nextLine();
                    messages.add(string);
                }
            } catch (Exception e) {
                System.out.println("Ошибка:" + socket);
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Closed:" + socket);
            }
        }
    }

    public static void allMessages() {
        timer.schedule(new TimerTask() {
            public void run() {
                if (messages.size() > 0) {
                    for (PrintWriter printWriter : clientList)
                        for (String message : messages) printWriter.println(message);
                    messages.clear();
                }

            }
        }, 0, 5000);
    }

}