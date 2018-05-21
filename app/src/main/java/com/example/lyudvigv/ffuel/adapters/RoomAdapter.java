package com.example.lyudvigv.ffuel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.DataModel.RoomData;
import com.example.lyudvigv.ffuel.R;

/**
 * Created by LyudvigV on 9/1/2017.
 */

public class RoomAdapter extends ArrayAdapter<RoomData> {

    private int _layoutResourceId;
    private TextView _tvAwbNumber;
    private RoomData _roomData;

    public RoomAdapter(Context context,int layoutResourceId) {
        super(context, layoutResourceId);
        _layoutResourceId=layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_list_item, null);
        _roomData = getItem(position);

        _tvAwbNumber = (TextView) convertView.findViewById(R.id.awbNumber);
        _tvAwbNumber.setText(_roomData.ReferenceID+"");

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
