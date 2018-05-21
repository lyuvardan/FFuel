package com.example.lyudvigv.ffuel.chat;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lyudvigv.ffuel.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.Random;



/**
 * Created by LyudvigV on 9/5/2017.
 */

public class ConnectTask extends AsyncTask<String, String, TcpClient> {

    TcpClient mTcpClient;
    private EditText msg_edittext;

    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    private ChatAdapter _chatAdapter;
    ListView msgListView;

    @Override
    protected TcpClient doInBackground(String... message) {

        //we create a TCPClient object
        mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                String arr[] = message.split(" ", 2);
                //filtering the stream;

//                if(!arr[0].equals(phoneHolderName)) {
                    final ChatMessage chatMessage2 = new ChatMessage(message,  false);
                    //chatMessage2.setMsgID();
                    chatMessage2.body = message;
                    _chatAdapter.add(chatMessage2);

                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _chatAdapter.notifyDataSetChanged();
                        }
                    });*/
                    publishProgress(message);
  //              }
            }
        });
        mTcpClient.run();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("test", "response " + values[0]);
        //process server response here....

    }
}
