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
package aquality.appium.mobile.template.screens.pedidos;

/**
 *
 * @author bjhisel
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MapClient extends Application {
    private static final int PORT = 8080;
    private static final String SERVER_ADDRESS = "localhost";

    private Circle pedidoMarker;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        pedidoMarker = new Circle(10, Color.RED);
        pedidoMarker.setCenterX(300);
        pedidoMarker.setCenterY(200);
        statusLabel = new Label("Estado del pedido: Conectando...");
        statusLabel.setLayoutY(350);

        root.getChildren().addAll(pedidoMarker, statusLabel);

        primaryStage.setTitle("Mapa de Seguimiento de Pedido");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("MapClient");

            String response;
            while ((response = in.readLine()) != null) {
                String[] parts = response.split(":");
                if (parts[0].equals("UPDATE")) {
                    String[] coordinates = parts[1].split(",");
                    double x = Double.parseDouble(coordinates[0]);
                    double y = Double.parseDouble(coordinates[1]);

                    Platform.runLater(() -> {
                        pedidoMarker.setCenterX(x);
                        pedidoMarker.setCenterY(y);
                        statusLabel.setText("Pedido actualizado en: (" + x + ", " + y + ")");
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
