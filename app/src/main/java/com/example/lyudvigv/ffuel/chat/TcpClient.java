package com.example.lyudvigv.ffuel.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by LyudvigV on 9/5/2017.
 */
public class TcpClient {
    public static final String SERVER_IP = "10.0.13.73";
    public static final int SERVER_PORT = 8100;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    //private PrintWriter mBufferOut;
    private OutputStream mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    // Constructor of the class. OnMessagedReceived listens for the messages received from server

    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    // Sends the message entered by client to the server

    // @param message text entered by client

    public void sendMessage(byte[] message) {
        if (mBufferOut != null) {
            try {
                mBufferOut.write(message);
                mBufferOut.flush();
            }catch (Exception e){}
        }
    }
    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        mRun = false;
        if (mBufferOut != null) {
            try {
                mBufferOut.flush();
                mBufferOut.close();
            }catch (Exception e){}
        }
        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {
        mRun = true;
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            try {
                //sends the message to the server
                mBufferOut = socket.getOutputStream();
                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }

            } catch (Exception e) {}
            finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {}
    }
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
