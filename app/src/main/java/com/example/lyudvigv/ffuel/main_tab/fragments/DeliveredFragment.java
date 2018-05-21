package com.example.lyudvigv.ffuel.main_tab.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyudvigv.ffuel.R;

/**
 * Created by LyudvigV on 9/8/2017.
 */

public class DeliveredFragment extends Fragment {

    public static DeliveredFragment newInstance() {
        DeliveredFragment fragment = new DeliveredFragment();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_delivered,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
