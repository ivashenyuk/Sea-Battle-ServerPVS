package com.company.serverPVP;

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection tcpConnection);
    void onReceive(TCPConnection tcpConnection, String user, int enemy);
    void onReceive1(TCPConnection tcpConnection, int step);
    void onDisconnect(TCPConnection tcpConnection);
    void onExeption(TCPConnection tcpConnection, Exception ex);
}
