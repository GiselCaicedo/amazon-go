package aquality.appium.mobile.template.amazongo.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class MainController implements Initializable {
    @FXML
    private WebView mapView;
    
    @FXML
    private ListView<String> statusList;
    
    @FXML
    private Label timeLabel;
    
    @FXML
    private Label orderIdLabel;
    
    @FXML
    private Label driverNameLabel;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    
    private final List<String> estadosBase = Arrays.asList(
        "✓ Pedido realizado                     ",
        "✓ Pago confirmado                      ",
        "✓ Procesando pedido                    ",
        "✓ Repartidor asignado                  ",
        "• En camino hacia ti                   ",
        "○ Entregado                            "
    );
    
    private ObservableList<String> estados;
    private int estadoActual = 4; // Inicia en "En camino hacia ti"
    private Timeline updateTimeline;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeStatusList();
        initializeMap();
        initializeUpdateTimeline();
        simularMovimientoRepartidor();
        
        // Configurar información inicial
        orderIdLabel.setText("Pedido #JDD23490F");
        driverNameLabel.setText("Juan Pérez");
        actualizarHoraEstimada();
    }

    private void initializeStatusList() {
        estados = FXCollections.observableArrayList();
        actualizarEstadosConHoras();
        
        statusList.setItems(estados);
        statusList.setMouseTransparent(true);
        statusList.setFocusTraversable(false);
        
        statusList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.startsWith("✓")) {
                        setStyle("-fx-text-fill: #00a65a; -fx-font-size: 14px;");
                    } else if (item.startsWith("•")) {
                        setStyle("-fx-text-fill: #FF9900; -fx-font-weight: bold; -fx-font-size: 14px;");
                    } else {
                        setStyle("-fx-text-fill: #999999; -fx-font-size: 14px;");
                    }
                }
            }
        });
    }

    private void initializeUpdateTimeline() {
        updateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(30), e -> actualizarHoraEstimada())
        );
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void actualizarEstadosConHoras() {
        estados.clear();
        LocalTime baseTime = LocalTime.now().minusMinutes(20);
        
        for (int i = 0; i < estadosBase.size(); i++) {
            if (i <= estadoActual) {
                LocalTime tiempo = baseTime.plusMinutes(i * 5);
                estados.add(estadosBase.get(i) + tiempo.format(TIME_FORMATTER));
            } else {
                estados.add(estadosBase.get(i) + "     --:--");
            }
        }
    }

    private void actualizarHoraEstimada() {
        LocalTime estimatedTime = LocalTime.now().plusMinutes(15);
        timeLabel.setText(estimatedTime.format(TIME_FORMATTER));
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
        mapHTML.append("        .delivery-marker { background: none; border: none; }\n");
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
        mapHTML.append("        var deliveryPath;\n");
        mapHTML.append("        function addMarker(id, lat, lng, title, isDelivery) {\n");
        mapHTML.append("            if (markers[id]) {\n");
        mapHTML.append("                markers[id].setLatLng([lat, lng]);\n");
        mapHTML.append("            } else {\n");
        mapHTML.append("                var icon = isDelivery ? L.divIcon({\n");
        mapHTML.append("                    html: '<div style=\"background-color: #FF9900; border-radius: 50%; width: 20px; height: 20px; border: 2px solid white; box-shadow: 0 0 10px rgba(0,0,0,0.3);\"></div>',\n");
        mapHTML.append("                    className: 'delivery-marker'\n");
        mapHTML.append("                }) : L.divIcon({\n");
        mapHTML.append("                    html: '<div style=\"background-color: #00a65a; border-radius: 50%; width: 16px; height: 16px; border: 2px solid white; box-shadow: 0 0 10px rgba(0,0,0,0.3);\"></div>',\n");
        mapHTML.append("                    className: 'delivery-marker'\n");
        mapHTML.append("                });\n");
        mapHTML.append("                markers[id] = L.marker([lat, lng], {icon: icon}).addTo(map)\n");
        mapHTML.append("                    .bindPopup(title);\n");
        mapHTML.append("            }\n");
        mapHTML.append("            if (isDelivery && markers['destino']) {\n");
        mapHTML.append("                updatePath([markers[id].getLatLng(), markers['destino'].getLatLng()]);\n");
        mapHTML.append("            }\n");
        mapHTML.append("        }\n");
        mapHTML.append("        function updatePath(points) {\n");
        mapHTML.append("            if (deliveryPath) map.removeLayer(deliveryPath);\n");
        mapHTML.append("            deliveryPath = L.polyline(points, {color: '#FF9900', weight: 3, opacity: 0.7}).addTo(map);\n");
        mapHTML.append("        }\n");
        mapHTML.append("    </script>\n");
        mapHTML.append("</body>\n");
        mapHTML.append("</html>");
        
        mapView.getEngine().loadContent(mapHTML.toString());
        mapView.getEngine().setJavaScriptEnabled(true);
        
        Platform.runLater(() -> {
            addMarker("destino", 19.4326, -99.1332, "Punto de entrega", false);
            addMarker("repartidor", 19.4300, -99.1350, "Juan Pérez - Repartidor", true);
        });
    }

    private void simularMovimientoRepartidor() {
        Thread simulationThread = new Thread(() -> {
            double lat = 19.4300;
            double lng = -99.1350;
            double velocidad = 0.1;
            
            try {
                while (true) {
                    final double currentLat = lat;
                    final double currentLng = lng;
                    
                    Platform.runLater(() -> {
                        addMarker("repartidor", currentLat, currentLng, "Juan Pérez - Repartidor", true);
                    });
                    
                    // Calcular distancia al destino
                    double distLat = 19.4326 - lat;
                    double distLng = -99.1332 - lng;
                    double distancia = Math.sqrt(distLat * distLat + distLng * distLng);
                    
                    // Si está muy cerca del destino, completar la entrega
                    if (distancia < 0.0001 && estadoActual != 5) {
                        Platform.runLater(() -> {
                            estadoActual = 5;
                            actualizarEstadosConHoras();
                        });
                    }
                    
                    // Mover hacia el destino
                    lat += distLat * velocidad;
                    lng += distLng * velocidad;
                    
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
        // Reiniciar el estado
        estadoActual = 0;
        actualizarEstadosConHoras();
        
        // Generar nuevo ID de pedido
        String newOrderId = String.format("PED%06d", (int)(Math.random() * 1000000));
        orderIdLabel.setText("Pedido #" + newOrderId);
    }

    @FXML
    private void handleActualizarEstado() {
        if (estadoActual < 5) {
            estadoActual++;
            actualizarEstadosConHoras();
        }
    }

    public void shutdown() {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
    }
}