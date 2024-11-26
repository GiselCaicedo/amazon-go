package aquality.appium.mobile.template.amazongo.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Arrays;

public class MainController implements Initializable {
    @FXML
    private WebView mapView;
    
    @FXML
    private ListView<String> statusList;
    
    private ObservableList<String> estados = FXCollections.observableArrayList(
        "✓ Pedido realizado",
        "✓ Pago confirmado",
        "✓ Procesando pedido",
        "✓ Repartidor asignado",
        "• En camino hacia ti",
        "○ Entregado"
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar la lista de estados
        statusList.setItems(estados);
        statusList.setMouseTransparent(true);
        statusList.setFocusTraversable(false);
        
        // Inicializar el mapa
        initializeMap();
        
        // Simular un repartidor en movimiento (para demostración)
        simularMovimientoRepartidor();
    }

    private void initializeMap() {
        StringBuilder mapHTML = new StringBuilder();
        mapHTML.append("<!DOCTYPE html>\n");
        mapHTML.append("<html>\n");
        mapHTML.append("<head>\n");
        mapHTML.append("    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.css\"/>\n");
        mapHTML.append("    <script src=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.js\"></script>\n");
        mapHTML.append("    <style>\n");
        mapHTML.append("        #map { height: 100vh; width: 100%; }\n");
        mapHTML.append("    </style>\n");
        mapHTML.append("</head>\n");
        mapHTML.append("<body style=\"margin:0;\">\n");
        mapHTML.append("    <div id=\"map\"></div>\n");
        mapHTML.append("    <script>\n");
        mapHTML.append("        var map = L.map('map').setView([19.4326, -99.1332], 15);\n");
        mapHTML.append("        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n");
        mapHTML.append("            attribution: '© OpenStreetMap contributors'\n");
        mapHTML.append("        }).addTo(map);\n");
        mapHTML.append("        var markers = {};\n");
        mapHTML.append("        function addMarker(id, lat, lng, title, isDelivery) {\n");
        mapHTML.append("            if (markers[id]) {\n");
        mapHTML.append("                markers[id].setLatLng([lat, lng]);\n");
        mapHTML.append("            } else {\n");
        mapHTML.append("                var icon = isDelivery ? L.divIcon({\n");
        mapHTML.append("                    html: '<div style=\"background-color: #FF9900; border-radius: 50%; width: 20px; height: 20px; border: 2px solid white;\"></div>',\n");
        mapHTML.append("                    className: 'delivery-marker'\n");
        mapHTML.append("                }) : null;\n");
        mapHTML.append("                markers[id] = L.marker([lat, lng], {icon: icon}).addTo(map)\n");
        mapHTML.append("                    .bindPopup(title);\n");
        mapHTML.append("            }\n");
        mapHTML.append("        }\n");
        mapHTML.append("        function updateDeliveryPath(points) {\n");
        mapHTML.append("            if (window.deliveryPath) {\n");
        mapHTML.append("                map.removeLayer(window.deliveryPath);\n");
        mapHTML.append("            }\n");
        mapHTML.append("            window.deliveryPath = L.polyline(points, {color: '#FF9900'}).addTo(map);\n");
        mapHTML.append("        }\n");
        mapHTML.append("    </script>\n");
        mapHTML.append("</body>\n");
        mapHTML.append("</html>");
        
        mapView.getEngine().loadContent(mapHTML.toString());
        mapView.getEngine().setJavaScriptEnabled(true);
        
        // Agregar marcador del destino
        Platform.runLater(() -> {
            addMarker("destino", 19.4326, -99.1332, "Punto de entrega", false);
            // Simular la ubicación inicial del repartidor
            addMarker("repartidor", 19.4300, -99.1350, "Repartidor", true);
        });
    }

    private void simularMovimientoRepartidor() {
        Thread simulationThread = new Thread(() -> {
            double lat = 19.4300;
            double lng = -99.1350;
            
            try {
                while (true) {
                    final double currentLat = lat;
                    final double currentLng = lng;
                    
                    Platform.runLater(() -> {
                        addMarker("repartidor", currentLat, currentLng, "Repartidor", true);
                    });
                    
                    // Mover hacia el destino
                    lat += (19.4326 - lat) * 0.1;
                    lng += (-99.1332 - lng) * 0.1;
                    
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        simulationThread.setDaemon(true);
        simulationThread.start();
    }
    
    private void addMarker(String id, double lat, double lng, String title, boolean isDelivery) {
        String script = String.format(
            "addMarker('%s', %f, %f, '%s', %b);",
            id, lat, lng, title, isDelivery
        );
        mapView.getEngine().executeScript(script);
    }

    @FXML
    private void handleNuevoPedido() {
        // TODO: Implementar lógica para nuevo pedido
    }

    @FXML
    private void handleActualizarEstado() {
        // TODO: Implementar lógica para actualizar estado
    }
}