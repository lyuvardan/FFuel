package com.example.lyudvigv.ffuel;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lyudvigv.ffuel.PermissionManager.PermissionManager;

public class BaseActivity extends AppCompatActivity {

    protected BaseActivity _activity;

    private PermissionManager _permMngr;
    public PermissionManager getPermissionManager() {
        if(_permMngr == null)
            _permMngr = PermissionManager.createInstance(this);
        _permMngr.attachActivity(this);
        return _permMngr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);




    }
}
