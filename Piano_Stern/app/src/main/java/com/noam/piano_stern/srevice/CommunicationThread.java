package com.noam.piano_stern.srevice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationThread {

    private Socket clientSocket;
    public static PrintWriter outputStream;
    private boolean isConnected;
    private static final int SERVERPORT = 8899;
    private static final String SERVER_IP = "10.10.100.254";
    public CommunicationThread()
    {
        isConnected = false;
        new Thread(new ClientThread()).start();

    }


    class ClientThread implements Runnable
    {

        @Override
        public void run() {
            try {
               InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                clientSocket = new Socket(serverAddr, SERVERPORT);
            } catch (UnknownHostException e) {
                isConnected = false;
                 //e.printStackTrace();
            } catch (IOException e) {
                isConnected = false;
                //e.printStackTrace();
            }



            if(clientSocket!=null)
            {
                isConnected = true;


                try {

                    outputStream  = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(clientSocket.getOutputStream())),

                            true);

                } catch (IOException exp) {
                    isConnected = false;
                    exp.printStackTrace();
                }


            }
        }
    }

    /**
     * send data to the device
     * @param data
     */
    public void sendData(String data)
    {
        outputStream.println(data);

    }


    public Socket getClientSocket()
    {
        return clientSocket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void isConnected(boolean isConnected)
    {
        this.isConnected = isConnected;
    }


}

