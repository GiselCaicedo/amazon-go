// SocketClient.java
package aquality.appium.mobile.template.sockets;

import aquality.appium.mobile.template.models.Location;
import aquality.appium.mobile.template.models.PedidoUpdateMessage;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String clientId;
    private final Gson gson;
    private boolean isConnected;

    public SocketClient(String clientId) {
        this.clientId = clientId;
        this.gson = new Gson();
        this.isConnected = false;
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Enviar ID del cliente como primer mensaje
            out.println(clientId);
            isConnected = true;

            // Iniciar thread para escuchar mensajes del servidor
            startListening();
        } catch (IOException e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }
    }

    private void startListening() {
        new Thread(() -> {
            try {
                String message;
                while (isConnected && (message = in.readLine()) != null) {
                    handleServerMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error al leer mensajes del servidor: " + e.getMessage());
                disconnect();
            }
        }).start();
    }

    public void updateLocation(String pedidoId, Location location) {
        if (!isConnected) return;

        PedidoUpdateMessage updateMessage = new PedidoUpdateMessage(pedidoId, "UBICACION");
        updateMessage.setNuevaUbicacion(location);
        sendMessage(gson.toJson(updateMessage));
    }

    public void updatePedidoStatus(String pedidoId, String nuevoEstado) {
        if (!isConnected) return;

        PedidoUpdateMessage updateMessage = new PedidoUpdateMessage(pedidoId, "ESTADO");
        updateMessage.setNuevoEstado(nuevoEstado);
        sendMessage(gson.toJson(updateMessage));
    }

    private void sendMessage(String message) {
        if (out != null && isConnected) {
            out.println(message);
        }
    }

    private void handleServerMessage(String message) {
        // Aquí procesarás los mensajes que llegan del servidor
        System.out.println("Mensaje recibido del servidor: " + message);
        // TODO: Implementar lógica según el tipo de mensaje
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}