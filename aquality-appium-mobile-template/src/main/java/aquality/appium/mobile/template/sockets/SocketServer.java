/*
 * Copyright 2024 bjhisel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aquality.appium.mobile.template.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author bjhisel
 */

public class SocketServer {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, SocketClientHandler> clients;

    public SocketServer() {
        clients = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor Amazon Go Iniciando en el puerto " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress());
                SocketClientHandler clientHandler = new SocketClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastUpdate(String message) {
        clients.values().forEach(client -> client.sendMessage("Mensaje del servidor: " + message));
    }

    public void addClient(String clientId, SocketClientHandler clientHandler) {
        clients.put(clientId, clientHandler);
    }

    public static void main(String[] args) {
        new SocketServer().start();
    }
}