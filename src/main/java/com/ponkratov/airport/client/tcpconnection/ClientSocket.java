package com.ponkratov.airport.client.tcpconnection;

import com.ponkratov.airport.client.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
    private static Socket s;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static ClientSocket instance;
    private static User currnetUser;

    private ClientSocket() throws IOException {
        s = new Socket("127.0.0.1", 11111);
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
