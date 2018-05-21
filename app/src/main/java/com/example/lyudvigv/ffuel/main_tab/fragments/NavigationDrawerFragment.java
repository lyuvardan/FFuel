package com.example.lyudvigv.ffuel.main_tab.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lyudvigv.ffuel.MyApplication;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;

public class NavigationDrawerFragment extends Fragment {
    private static final int PROFILE = 0;
    private static final int SHIPMENTS = 1;
    private static final int INVENTORY = 2;
    private static final int CHAT = 3;
    private static final int NOTIFICATIONS = 4;
    private static final int SETTINGS = 5;
    private static final int SIGN_OUT = 6;

    private ActionBarDrawerToggle _drawerToggle;
    private DrawerLayout _drawerLayout;
    private NavigationView _navigationView;
    private UserInfo _userInfo;

    public static NavigationDrawerFragment newInstance() {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _userInfo =((MyApplication)getActivity().getApplication()).getUserInfo();
        //organizeNavMenuClicks(view);
    }

    public void setUp(final DrawerLayout drawerLayout, Toolbar toolbar, ImageView leftMenu) {

        _drawerLayout=drawerLayout;
        _drawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };

        _drawerLayout.addDrawerListener(_drawerToggle);
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void organizeNavMenuClicks(View view)
    {
        _navigationView = (NavigationView)view.findViewById(R.id.navigation_drawer);

        Menu menu = _navigationView.getMenu();

        menu.getItem(PROFILE).setTitle(Html.fromHtml("<b>"+_userInfo.Name+"</b>"));
        menu.getItem(PROFILE).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(SHIPMENTS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(INVENTORY).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(CHAT).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(NOTIFICATIONS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(SETTINGS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
        menu.getItem(SIGN_OUT).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }
}
