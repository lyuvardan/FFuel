package com.example.lyudvigv.ffuel.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.chat.ChatMessage;
import com.example.lyudvigv.ffuel.main_tab.fragments.Chat.ChatFragment;

import java.util.ArrayList;

/**
 * Created by LyudvigV on 9/1/2017.
 */

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private LinearLayout _layout;
    private LinearLayout _parent_layout;

    ArrayList<ChatMessage> chatMessageList;

    public ChatAdapter(ChatFragment fragment, ArrayList<ChatMessage> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = (ChatMessage) chatMessageList.get(position);
        if (message.isMine) {
            convertView = inflater.inflate(R.layout.chatbubble, null);
            _layout = (LinearLayout) convertView.findViewById(R.id.bubble_layout);
            _layout.setBackgroundResource(R.drawable.bluebub);

            TextView dt = (TextView)convertView.findViewById(R.id.dateTime);
            TextView msg = (TextView) convertView.findViewById(R.id.message_text);
            msg.setText(message.body);

            dt.setGravity(Gravity.RIGHT);
        }

        else {
            convertView = inflater.inflate(R.layout.chatwhitebubble,null);
            _layout = (LinearLayout) convertView.findViewById(R.id.white_bubble_layout);
            _layout.setBackgroundResource(R.drawable.whitebub);

            TextView dt = (TextView)convertView.findViewById(R.id.dateTime);
            TextView msg = (TextView) convertView.findViewById(R.id.message_text);
            msg.setText(message.body);

            dt.setGravity(Gravity.LEFT);
        }

        return convertView;
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }
}
