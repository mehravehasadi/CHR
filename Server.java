package chatroom;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
    private static final int PORT = 1234;
    private static final List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + " connected");
                clients.add(clientSocket);

                Thread thread = new Thread(() -> handleClient(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Welcome to this chatroom!");

            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break; // Connection closed
                }

                System.out.println("<" + clientSocket.getInetAddress() + "> " + message);

                String messageToSend = "<" + clientSocket.getInetAddress() + "> " + message;
                broadcast(messageToSend, clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(clientSocket);
            System.out.println(clientSocket.getInetAddress() + " disconnected");
        }
    }

    private static void broadcast(String message, Socket sender) {
        for (Socket client : clients) {
            if (client != sender) {
                try {
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    writer.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
