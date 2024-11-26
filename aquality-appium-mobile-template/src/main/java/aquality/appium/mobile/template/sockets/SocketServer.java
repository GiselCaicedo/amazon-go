package aquality.appium.mobile.template.sockets;

import aquality.appium.mobile.template.models.Pedido;
import aquality.appium.mobile.template.models.PedidoUpdateMessage;
import aquality.appium.mobile.template.models.Location;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServer {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, SocketClientHandler> clients;
    private ConcurrentHashMap<String, Pedido> pedidosActivos;
    private final Gson gson;

    public SocketServer() {
        clients = new ConcurrentHashMap<>();
        pedidosActivos = new ConcurrentHashMap<>();
        gson = new Gson();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de Delivery iniciado en el puerto " + PORT);
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

    public void handleMessage(String clientId, String message) {
        try {
            PedidoUpdateMessage updateMessage = gson.fromJson(message, PedidoUpdateMessage.class);
            Pedido pedido = pedidosActivos.get(updateMessage.getPedidoId());
            
            if (pedido != null) {
                switch (updateMessage.getTipo()) {
                    case "UBICACION":
                        pedido.setUbicacionActual(updateMessage.getNuevaUbicacion());
                        break;
                    case "ESTADO":
                        pedido.setEstado(updateMessage.getNuevoEstado());
                        break;
                }
                notifyPedidoUpdate(pedido);
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private void notifyPedidoUpdate(Pedido pedido) {
        String updateJson = gson.toJson(pedido);
        notifyClient(pedido.getClienteId(), updateJson);
        if (pedido.getRepartidorId() != null) {
            notifyClient(pedido.getRepartidorId(), updateJson);
        }
    }

    private void notifyClient(String clientId, String message) {
        SocketClientHandler handler = clients.get(clientId);
        if (handler != null) {
            handler.sendMessage(message);
        }
    }

    public void addClient(String clientId, SocketClientHandler clientHandler) {
        clients.put(clientId, clientHandler);
    }

    public void addPedido(Pedido pedido) {
        pedidosActivos.put(pedido.getId(), pedido);
    }
}