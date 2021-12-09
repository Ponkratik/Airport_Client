package com.ponkratov.airport.client.tcpconnection;

import com.ponkratov.airport.client.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ClientSocket {
    private static Socket s;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static ClientSocket instance;
    private static User currnetUser;

    private static final String CLIENT_PROPERTIES = "src/main/java/com/ponkratov/airport/client/tcpconnection/client.properties";
    private static final String IP;
    private static final int PORT;

    static {
        Properties clientProperties = new Properties();
        try (InputStream propertiesFile = Files.newInputStream(Paths.get(CLIENT_PROPERTIES))) {
            clientProperties.load(propertiesFile);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load client properties.");
        }

        IP = clientProperties.getProperty("ip");
        PORT = Integer.parseInt(clientProperties.getProperty("port"));
    }

    private ClientSocket() throws IOException {
        s = new Socket(IP, PORT);
        oos = new ObjectOutputStream(s.getOutputStream());
        ois = new ObjectInputStream(s.getInputStream());
    }

    public static ClientSocket getInstance() throws IOException {
        if (instance == null) {
            instance = new ClientSocket();
        }

        return instance;
    }

    public static ObjectOutputStream getOos() {
        return oos;
    }

    public static ObjectInputStream getOis() {
        return ois;
    }

    public static User getCurrnetUser() {
        return currnetUser;
    }

    public static void setCurrnetUser(User currnetUser) {
        ClientSocket.currnetUser = currnetUser;
    }
}
