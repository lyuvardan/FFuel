package com.example.lyudvigv.ffuel.main_tab.fragments.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lyudvigv.ffuel.DataModel.RoomData;
import com.example.lyudvigv.ffuel.MyApplication;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.RequestManager;
import com.example.lyudvigv.ffuel.adapters.RoomAdapter;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LyudvigV on 12/13/2017.
 */

public class RoomFragment extends Fragment {

    private ListView _roomsList;
    private String _baseUrl;
    private String _getRoomsUrl;
    private RoomAdapter _adapter;
    private UserInfo _userInfo;
    private RoomData[] _rooms;

    public static RoomFragment newInstance() {
        RoomFragment fragment = new RoomFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _roomsList = (ListView)getActivity().findViewById(R.id.roomList);
        _adapter = new RoomAdapter(getContext(),R.layout.room_list_item);
        _baseUrl = getResources().getString(R.string.service_url);
        _getRoomsUrl = _baseUrl + "api/json/data/list";
        _userInfo = ((MyApplication)getActivity().getApplication()).getUserInfo();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    getRooms(_getRoomsUrl);
                } catch (Exception e) {}
            }
        });
        thread.start();
    }

    private void getRooms(String url) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-type","application/json");
        headers.put("UserToken",_userInfo.UserToken);

        JSONObject bodyParams = new JSONObject();
        try {
            bodyParams.accumulate("ServiceAction", "Notification");

            String response = RequestManager.post(url,headers,bodyParams);
            if(response.length()!=0)
            {
                _rooms = new Gson().fromJson(response, RoomData[].class);
                _adapter.addAll(_rooms);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _roomsList.setAdapter(_adapter);
                    }
                });
            }else {
            }
        } catch (Exception e) {
        }
    }
}
