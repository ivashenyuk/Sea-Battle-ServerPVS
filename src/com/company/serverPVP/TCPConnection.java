package com.company.serverPVP;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class TCPConnection {
    public final Socket socket;
    public final Thread rxThread;
    private final TCPConnectionListener eventListener;
    public BufferedReader in;
    private BufferedWriter out;
    public static long hash = 0;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPConnection.this.eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        String user = "";
                        int enemyOrUser = 100;
                        try {
                            user = in.readLine();
                            enemyOrUser = in.read();
                        } catch (SocketException ex) {
//                            Server.SetShipNULL();
                            //TCPConnection.this.eventListener.onDisconnect(TCPConnection.this);
                            rxThread.interrupt();
                        }
                        //System.out.println("Receive  data.");
                        hash = in.hashCode();
                        if (user.equals("easy") || user.equals("hard") || user.equals("impossible")) {
                            Bot.complexity = user.toString();
                            continue;
                        }
                        if (user.equals("yuorstep")) {
                            TCPConnection.this.eventListener.onReceive1(TCPConnection.this, enemyOrUser - 48);
                        } else if ((enemyOrUser - 48) != 7)
                            TCPConnection.this.eventListener.onReceive(TCPConnection.this, user, enemyOrUser - 48);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    rxThread.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                    rxThread.interrupt();
                } finally {
                    TCPConnection.this.eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void SendData(String enemy, int enemyOrUser) {
        try {
            out.write(enemy + "\r\n" + enemyOrUser + "\r\n");
            out.flush();
        } catch (IOException e) {
            this.eventListener.onExeption(TCPConnection.this, e);
            Disconnect();
        }
    }

    public synchronized void YourStep(int step) {
        try {
            out.write("_yuorstep" + "\r\n" + step + "\r\n");
            out.flush();
        } catch (IOException e) {
            this.eventListener.onExeption(TCPConnection.this, e);
            Disconnect();
        }
    }

    public synchronized void Disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            this.eventListener.onExeption(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnectin: " + socket.getInetAddress() + ":" + socket.getPort();
    }
}
