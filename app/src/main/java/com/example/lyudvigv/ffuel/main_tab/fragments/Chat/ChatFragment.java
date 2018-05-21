package com.example.lyudvigv.ffuel.main_tab.fragments.Chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lyudvigv.ffuel.MyApplication;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.adapters.ChatAdapter;
import com.example.lyudvigv.ffuel.chat.ChatMessage;
import com.example.lyudvigv.ffuel.chat.ChatMessageToSend;
import com.example.lyudvigv.ffuel.chat.TcpClient;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;
import com.google.gson.Gson;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by LyudvigV on 9/8/2017.
 */

public class ChatFragment extends Fragment {

    private UserInfo _userInfo;
    TcpClient mTcpClient;

    private EditText msg_edittext;

    private Random random;

    public static ArrayList<ChatMessage> chatlist;

    private ChatAdapter _chatAdapter;

    ListView msgListView;

    private int _type;
    private int _userID;
    private String _token;
    private String _userName;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _userInfo =((MyApplication)getActivity().getApplication()).getUserInfo();
        _type = 1;
        _userID = _userInfo.UserID;
        _userName = _userInfo.Name;
        _token = _userInfo.UserToken;

        new ConnectTask().execute("");
        random = new Random();
        //this.getActivity().getActionBar().setTitle(
          //      "AWB 0010002");
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendMessageButton);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTcpClient != null) {
                    connect(1, _userID,_userName,_token,null);
                }
            }
        },3000);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendMessageButton:
                    //sendTextMessage(v, true);
                    sendTextMessage();

            }
            }
        });

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        _chatAdapter = new ChatAdapter(this, chatlist);
        msgListView.setAdapter(_chatAdapter);
    }

    public  void sendMessageBytes(String message){
        List<Byte> messageBARRList = new ArrayList<>();
        if(message!=null) {

            byte[] messageBARR = message.getBytes();
            for (byte b : messageBARR) {
                messageBARRList.add(b);
            }

        }

        List<Byte> resultList = new ArrayList<>();
        resultList.addAll(messageBARRList);

        Byte[] resultarray = new Byte[resultList.size()];
        resultarray = resultList.toArray(resultarray);

        byte[] result= ArrayUtils.toPrimitive(resultarray);
        mTcpClient.sendMessage(result);
    }

    public void connect (int type, int userid, String username, String token, String message){

        byte[] typeBARR =  ByteBuffer.allocate(Long.BYTES).putLong(type).array();
        ArrayUtils.reverse(typeBARR);

        List<Byte> typeBARRList = new ArrayList<>();
        for (byte b:typeBARR) {
            typeBARRList.add(b);
        }

        byte[] userIdBARR =  ByteBuffer.allocate(Long.BYTES).putLong(userid).array();
        ArrayUtils.reverse(userIdBARR);

        List<Byte> userIdBARRList = new ArrayList<>();
        for (byte b:userIdBARR) {
            userIdBARRList.add(b);
        }

        byte[] userNameBARR = new byte[50];

        for (int i = 0; i < username.getBytes().length; i++) {
            userNameBARR[i] = username.getBytes()[i];
        }

        List<Byte> userNameBARRList = new ArrayList<>();
        for (byte b:userNameBARR) {
            userNameBARRList.add(b);
        }

        List<Byte> tokenBARRList = new ArrayList<>();

        byte[] tokenBARR = token.getBytes();

        for (byte b : tokenBARR) {
            tokenBARRList.add(b);
        }


        List<Byte> messageBARRList = new ArrayList<>();
        if(message!=null) {
            byte[] messageBARR = message.getBytes();
            for (byte b : messageBARR) {
                messageBARRList.add(b);
            }
        }

        List<Byte> resultList = new ArrayList<>();
        resultList.addAll(typeBARRList);
        resultList.addAll(userIdBARRList);
        resultList.addAll(userNameBARRList);
        resultList.addAll(tokenBARRList);
        resultList.addAll(messageBARRList);

        Byte[] resultarray = new Byte[resultList.size()];
        resultarray = resultList.toArray(resultarray);

        byte[] result= ArrayUtils.toPrimitive(resultarray);

        //String resultBase64 = new String(Base64.encodeToString(result,Base64.DEFAULT));
        mTcpClient.sendMessage(result);
    }

    public void sendTextMessage() {

        String message = msg_edittext.getText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage1 = new ChatMessage(message, true);
            if (mTcpClient != null) {
                ChatMessageToSend chatmsg = new ChatMessageToSend();
                chatmsg.CustomerWebUserID = _userID;
                chatmsg.FromWebUser = false;
                chatmsg.MainNotificationID = 24;
                chatmsg.NotificationType = "Chat";
                chatmsg.ReferenceType = "AWB";
                chatmsg.ReferenceID = 64444;
                chatmsg.Message = msg_edittext.getEditableText().toString();
                Gson gson= new Gson();
                String json = gson.toJson(chatmsg);
                sendMessageBytes(json);
            }
            msg_edittext.setText("");
            _chatAdapter.add(chatMessage1);
            _chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTcpClient.stopClient();
    }
    @Override
    public void onStop() {
        super.onStop();
        mTcpClient.stopClient();
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

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

                    if(!arr[0].equals(Integer.toString(_userID))) {
                        final ChatMessage chatMessage2 = new ChatMessage(message,  false);
                        chatMessage2.body = message;
                        _chatAdapter.add(chatMessage2);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            _chatAdapter.notifyDataSetChanged();
                            }
                        });
                        publishProgress(message);
                    }
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
}
