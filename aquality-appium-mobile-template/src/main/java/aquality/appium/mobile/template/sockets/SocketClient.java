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

/**
 *
 * @author bjhisel
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Enviar un mensaje inicial al servidor
            out.println("Client123");

            // Enviar un mensaje al servidor
            out.println("Hola servidor, este es un mensaje de prueba.");

            // Leer y mostrar mensajes recibidos del servidor
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Respuesta del servidor: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}