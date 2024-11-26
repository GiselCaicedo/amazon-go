package aquality.appium.mobile.template.amazongo;

import aquality.appium.mobile.template.sockets.SocketServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainServer extends Application {
    private SocketServer server;

    @Override
    public void start(Stage stage) {
        try {
            // Iniciar el servidor en un hilo separado
            startServer();
            
            // Cargar la interfaz principal
            FXMLLoader fxmlLoader = new FXMLLoader(MainServer.class.getResource("/views/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
            
            // Cargar los estilos CSS
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            
            stage.setTitle("Sistema de Delivery - Panel de Control");
            stage.setScene(scene);
            stage.show();

            // Configurar el cierre de la aplicación
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        new Thread(() -> {
            server = new SocketServer();
            server.start();
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}