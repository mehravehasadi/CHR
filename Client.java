package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Replace with the actual server IP
    private static final int SERVER_PORT = 1234; // Replace with the actual server port

    public static void main(String[] args) {
        try {
            Socket serverSocket = new Socket(SERVER_IP, SERVER_PORT);

            Thread readThread = new Thread(() -> readFromServer(serverSocket));
            Thread writeThread = new Thread(() -> writeToServer(serverSocket));

            readThread.start();
            writeThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromServer(Socket serverSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))) {
            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break; // Connection closed
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToServer(Socket serverSocket) {
        try (PrintWriter writer = new PrintWriter(serverSocket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                String message = scanner.nextLine();
                writer.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
