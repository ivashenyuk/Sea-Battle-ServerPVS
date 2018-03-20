package com.company.serverPVP;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Thread implements TCPConnectionListener {
    public static boolean stepIsTrue = true;
    public static Ship placeOfBattleUser[][];
    public static Ship placeOfBattleEnemy[][];
    public static Ship placeOfBattleUser1[][];
    public static Ship placeOfBattleEnemy1[][];
    private static final ArrayList<TCPConnection> connections = new ArrayList<TCPConnection>();
    public static boolean isReceiveShips = false;

    public Server() {
        System.out.println("Server is running...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SetShipNULL();
                try (ServerSocket serverSocket = new ServerSocket(8189)) {
                    System.out.println(serverSocket.getInetAddress());
                    while (true) {
                        if (connections.size() < 2) {
                            new TCPConnection(Server.this, serverSocket.accept());
                        }
                        if(connections.size() == 0) {
                            SetShipNULL();
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        thread.start();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Added connection.");
    }

    @Override
    public synchronized void onReceive(TCPConnection tcpConnection, String user, int enemyOrUser) {
        //System.out.println("Sending Data...");
        if (enemyOrUser == 1) {
            if (connections.get(0).in.hashCode() == TCPConnection.hash)
                placeOfBattleEnemy1 = new Gson().fromJson(user, Ship[][].class);
            else if (connections.size() == 2 && connections.get(1).in.hashCode() == TCPConnection.hash)
                placeOfBattleEnemy = new Gson().fromJson(user, Ship[][].class);

            if (connections.size() == 2)
                connections.get(1).SendData(new Gson().toJson(placeOfBattleEnemy1), 1);
            connections.get(0).SendData(new Gson().toJson(placeOfBattleEnemy), 1);

        } else if (enemyOrUser == 0) {
            if (connections.get(0).in.hashCode() == TCPConnection.hash)
                placeOfBattleUser = new Gson().fromJson("[" + user, Ship[][].class);
            else if (connections.size() == 2 && connections.get(1).in.hashCode() == TCPConnection.hash)
                placeOfBattleUser1 = new Gson().fromJson("[" + user, Ship[][].class);

            if (connections.size() == 2)
                connections.get(1).SendData(new Gson().toJson(placeOfBattleUser), 0);
            connections.get(0).SendData(new Gson().toJson(placeOfBattleUser1), 0);
        }
        SendMsgAllClientStep();
    }

    @Override
    public synchronized void onReceive1(TCPConnection tcpConnection, int step) {
        stepIsTrue = !stepIsTrue;
        SendMsgAllClientStep();
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onExeption(TCPConnection tcpConnection, Exception ex) {
        System.out.println("TCPConnection exeption: " + ex);
    }

    private static void SendMsgAllClient(String enemy, int enemyOrUser) {
        final int size = connections.size();
        for (int i = 0; i < size; i++) {
            if (i < 2) {
                if (connections.get(i).in.hashCode() != TCPConnection.hash) {
                    connections.get(i).SendData(enemy, enemyOrUser);
                }
            }

        }
    }

    private static void SendMsgAllClientStep() {
        if (connections.size() == 2) {
            if (stepIsTrue) {
                connections.get(0).YourStep(6);
                connections.get(1).YourStep(0);
            } else {
                connections.get(1).YourStep(6);
                connections.get(0).YourStep(0);
            }
        }
    }

    private static void SetShipNULL() {
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
        placeOfBattleUser1 = new Ship[][]
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

        placeOfBattleEnemy1 = new Ship[][]
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
    }
}
