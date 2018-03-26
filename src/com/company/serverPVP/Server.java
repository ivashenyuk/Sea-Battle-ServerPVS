package com.company.serverPVP;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Thread implements TCPConnectionListener {
    public static boolean stepIsTrue = false;
    public static Ship placeOfBattleUser[][];
    public static Ship placeOfBattleEnemy[][];
    public static Bot bot;
    public static Player playerUser = null;
    public static Player playerEnemy = null;

    private static final ArrayList<TCPConnection> connections = new ArrayList<TCPConnection>();
    private static boolean isReceiveShips = false;

    public Server() {
        System.out.println("Server is running...");
        Thread thread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8191)) {
                System.out.println(serverSocket.getInetAddress());
                while (true) {
                    if (connections.size() == 0)
                        SetShipNULL();
                    if (connections.size() < 1) {
                        new TCPConnection(Server.this, serverSocket.accept());
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        thread.start();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Added connection.");
        connections.get(0).SendData(new Gson().toJson(placeOfBattleUser), 1);
    }

    @Override
    public synchronized void onReceive(TCPConnection tcpConnection, String user, int enemyOrUser) {
        //System.out.println("Sending Data...");

        if (enemyOrUser == 1) {
            if (connections.get(0).in.hashCode() == TCPConnection.hash) {
                if (user.charAt(1) != '[')
                    placeOfBattleEnemy = new Gson().fromJson("[" + user, Ship[][].class);
                else
                    placeOfBattleEnemy = new Gson().fromJson(user, Ship[][].class);
                if (!isReceiveShips) {
                    Bot.Learning();
                }
            }
            if (!isReceiveShips) {
                isReceiveShips = true;
                if (playerEnemy == null) {
                    playerEnemy = new Player();
                    playerEnemy.ipAddress = connections.get(0).socket.getInetAddress().toString();
                    System.out.println(playerEnemy.ipAddress);
                    connections.get(0).SendData(new Gson().toJson(placeOfBattleUser), 1);
                }
            }
        } else if (enemyOrUser == 0) {
            if (connections.get(0).in.hashCode() == TCPConnection.hash)
                placeOfBattleUser = new Gson().fromJson("[" + user, Ship[][].class);
            connections.get(0).SendData(new Gson().toJson(placeOfBattleUser), 1);
        }
    }

    @Override
    public synchronized void onReceive1(TCPConnection tcpConnection, int step) {
        if (step != 0)
            stepIsTrue = true;
        else
            stepIsTrue = false;
        if (stepIsTrue) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bot.Step();
            connections.get(0).SendData(new Gson().toJson(placeOfBattleEnemy), 0);
            connections.get(0).YourStep(4);
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onExeption(TCPConnection tcpConnection, Exception ex) {
        System.out.println("TCPConnection exeption: " + ex);
    }

    public static void SetShipNULL() {
        placeOfBattleUser = new Ship[][]
                {
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                };

        placeOfBattleEnemy = new Ship[][]
                {
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                        {new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship(), new Ship()},
                };
        playerUser = new Player();
        if(bot != null)
            bot = null;
        bot = new Bot();
        isReceiveShips = false;
    }
}
