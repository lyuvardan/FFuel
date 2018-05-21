package com.example.lyudvigv.ffuel.main_tab;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.BaseActivity;
import com.example.lyudvigv.ffuel.DataModel.FlightAwareData;
import com.example.lyudvigv.ffuel.DataModel.FlightAwareRequest;
import com.example.lyudvigv.ffuel.DataModel.FreightShipmentData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.adapters.ViewPagerAdapter;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;
import com.example.lyudvigv.ffuel.main_tab.fragments.Chat.RoomFragment;
import com.example.lyudvigv.ffuel.main_tab.fragments.Map.MapFragment;
import com.example.lyudvigv.ffuel.main_tab.fragments.NavigationDrawerFragment;
import com.example.lyudvigv.ffuel.main_tab.fragments.Shipment.ShipmentBottomFragment;
import com.example.lyudvigv.ffuel.main_tab.fragments.Shipment.ShipmentFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * Created by LyudvigV on 9/8/2017.
 */

public class MainTabActivity extends BaseActivity{


    private Bundle _savedInstanceState;
    private Toolbar _toolbar;
    private ImageView _leftMenuIcon;
    private SlidingUpPanelLayout _slidingLayout;
    private NavigationDrawerFragment _drawerFragment;
    private TextView _toolbarText;
    private ImageView _upDownArrow;

    private TabLayout _tabLayoutTop;
    private ViewPager _viewPagerTop;
    private ViewPagerAdapter _adapterTop;

    private MapFragment _mapFragment;
    private ShipmentFragment _shipmentFragment;
    private RoomFragment _roomFragment;
    //private ChatFragment _chatFragment;

    private FrameLayout _shipmentBottomFragmentContainer;
    private ShipmentBottomFragment _shipmentBottomFragment;


    private UserInfo _userInfo;
    private ShipmentData[] _shipments;
    private FreightShipmentData[] _freightShipments;
    private FlightAwareData[] _flightAwareShipments;

    private String _awbs;

    private List<FlightAwareRequest> _flightAwareRequestData;
    private String _baseUrl;
    private String _dataHubUrl;
    private String _freightUrl;
    private String _getShipmentsUrl;
    private String _getFlightAwareDataUrl;
    private Integer _slidingLayoutPanelHeight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        _savedInstanceState = savedInstanceState;
        initializeNavigationComponents();
        _tabLayoutTop = (TabLayout) findViewById(R.id.tab_layout);
        _viewPagerTop = (ViewPager) findViewById(R.id.pager);
        _slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        _upDownArrow = (ImageView)findViewById(R.id.upDown);
        _slidingLayout.setDragView(R.id.tab_layout_bottom);
        _slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if(slideOffset == 1){
                    _upDownArrow.setImageResource(R.drawable.ic_arrow_down);
                }
                else {
                    _upDownArrow.setImageResource(R.drawable.ic_arrow_up);
                }
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        _slidingLayoutPanelHeight = _slidingLayout.getPanelHeight();

        _mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 0);
        _shipmentFragment = (ShipmentFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
        _roomFragment = (RoomFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
        //_chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
        if(_mapFragment == null)
            _mapFragment = MapFragment.newInstance();
        if(_shipmentFragment == null)
            _shipmentFragment = ShipmentFragment.newInstance();
        if(_roomFragment==null)
            _roomFragment = RoomFragment.newInstance();
        /*if(_chatFragment==null)
            _chatFragment = ChatFragment.newInstance();*/

        _adapterTop = new ViewPagerAdapter(getSupportFragmentManager());
        _adapterTop.addFragment(_mapFragment,"");
        _adapterTop.addFragment(_shipmentFragment, "");
        _adapterTop.addFragment(_roomFragment,"");
        //_adapterTop.addFragment(_chatFragment,"");

        _viewPagerTop.setOffscreenPageLimit(3);
        _viewPagerTop.setAdapter(_adapterTop);
        _tabLayoutTop.setupWithViewPager(_viewPagerTop);
        _tabLayoutTop.getTabAt(0).setIcon(R.drawable.ic_map);
        _tabLayoutTop.getTabAt(1).setIcon(R.drawable.ic_gas_station);
        _tabLayoutTop.getTabAt(2).setIcon(R.drawable.ic_support);
        _tabLayoutTop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:{
                        _toolbarText.setText("Карта");
                        _slidingLayout.setPanelHeight(_slidingLayoutPanelHeight);
                    }break;
                    case 1:{
                        _toolbarText.setText("Бензоколонки");
                        //_slidingLayout.setPanelHeight(0);
                        _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        _slidingLayout.setPanelHeight(0);

                    }break;
                    case 2:{
                        _toolbarText.setText("Помощ");
                        //_slidingLayout.setPanelHeight(0);
                        _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        _slidingLayout.setPanelHeight(0);
                    }break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        _shipmentBottomFragmentContainer = (FrameLayout) findViewById(R.id.shipmentBottomFragmentContainer);
        _shipmentBottomFragment = ShipmentBottomFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(_shipmentBottomFragmentContainer.getId(), _shipmentBottomFragment, "btmShipments").commit();
    }

    private void initializeNavigationComponents()
    {
        _toolbar = (Toolbar) findViewById(R.id.app_bar);
        _leftMenuIcon = (ImageView)findViewById(R.id.leftMenuIcon);
        _toolbarText = (TextView)findViewById(R.id.toolbarText);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        _drawerFragment.setUp((DrawerLayout)findViewById(R.id.drawer_layout),_toolbar, _leftMenuIcon);
    }






}
