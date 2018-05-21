package com.example.lyudvigv.ffuel.PermissionManager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.example.lyudvigv.ffuel.PermissionManager.callbacks.OnCheckPermissionCompletedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionManager {


    private Activity _activity;

    //private String[] _permissions;
    //private int _requestCode;
    //private OnCheckPermissionCompletedListener _onCheckPermissionCompleted;

    private class PermInfo {
        public String[] _permissions;
        public int _requestCode;
        public ArrayList<OnCheckPermissionCompletedListener> _onCheckPermissionCompleted = new ArrayList<OnCheckPermissionCompletedListener>();

        public PermInfo(int requestCode, String[] permissions) {
            _requestCode = requestCode;
            _permissions = permissions;
        }
    }

    private HashMap<Integer, PermInfo> _permReq = new HashMap<Integer, PermInfo>();

    private PermissionManager() {

    }

    public static PermissionManager createInstance(Activity activity) {
        PermissionManager mngr = new PermissionManager();
        mngr.attachActivity(activity);
        return mngr;
    }

   /* public static boolean hasLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            return ActivityCompat.checkSelfPermission(_activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
*/
    public void attachActivity(Activity activity) {
        _activity = activity;
    }

    protected boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(_activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    protected boolean shouldShowPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, permission)) {
                return true;
            }
        }

        return false;
    }

    public void handlePermissionResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (verifyPermissionResults(grantResults)) {
                //onPermissionGranted();
            } else {
                //onPermissionDenied();
                if (!shouldShowPermissionRationale(permissions)) {
                    onPermissionSettings(requestCode, grantResults);
                    return;
                }
            }

            PermInfo requests = _permReq.get(requestCode);

            if(requests != null) {
                for (int i = 0; i < requests._onCheckPermissionCompleted.size(); i++) {
                    //if(_permReq.get(i)._permissions == permissions && requestCode == _permReq.get(i)._requestCode) {
                    requests._onCheckPermissionCompleted.get(i).onCheckPermissionCompleted(buildPermissions(requests._permissions, grantResults));
                    //}
                }
            }
            _permReq.remove(requestCode);
        }
    }

    public void checkPermissions(@NonNull String[] permissions, int requestCode, OnCheckPermissionCompletedListener onCheckPermissionCompleted) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions(permissions)) {
                //onPermissionGranted();

                onCheckPermissionCompleted.onCheckPermissionCompleted(buildPermissions(permissions, createDenyResults(permissions)));
                return;
            }

            PermInfo perm = _permReq.get(requestCode);
            if(perm == null) {
                perm = new PermInfo(requestCode, permissions);
                _permReq.put(requestCode, perm);
            }
            perm._onCheckPermissionCompleted.add(onCheckPermissionCompleted);

            //ToDo: Arthur - what is this?
            if (shouldShowPermissionRationale(permissions)) {
                requestPermission(permissions, requestCode);
            } else {
                requestPermission(permissions, requestCode);
            }
/*            if (shouldShowPermissionRationale()) {
                onPermissionSettings();
                onPermissionShowRationale();
            } else {
                requestPermission();
            }*/
        }
        else {
            onCheckPermissionCompleted.onCheckPermissionCompleted(buildPermissions(permissions, createDenyResults(permissions)));
        }
    }


    protected int[] createDenyResults(String[] permissions) {
        int[] grantResults = new int[permissions.length];
        for (int i = 0; i< permissions.length; i++)
            grantResults[i] = 0;

        return grantResults;
    }

    protected Permission buildPermissions(@NonNull String[] permissions, int[] grantResults) {
        final Permission perm = new Permission();

        for (int result = 0; result < grantResults.length; result++) {
            switch (permissions[result]) {
                case android.Manifest.permission.CAMERA: {
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermCamera(true);
                    break;
                }
                case android.Manifest.permission.ACCESS_FINE_LOCATION: {
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermFineLocation(true);
                    break;
                }
                case android.Manifest.permission.GET_ACCOUNTS: {
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermGetAccounts(true);
                    break;
                }
                case android.Manifest.permission.CALL_PHONE: {
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermCall(true);
                    break;
                }
                case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:
                case android.Manifest.permission.READ_EXTERNAL_STORAGE: {
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermExternalStorage(true);
                    break;
                }
                case Manifest.permission.RECORD_AUDIO:{
                    if (grantResults[result] == PackageManager.PERMISSION_GRANTED)
                        perm.setPermRecordAudio(true);
                    break;
                }
            }
        }

        return perm;
    }

    //ToDo: Armen, Arthur
    protected void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(_activity, permissions, requestCode);
    }

    protected boolean verifyPermissionResults(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    public void onPermissionSettings(int requestCode, int[] grantResults) {
        PermInfo perm = _permReq.get(requestCode);
        if(perm != null) {
            for(int i = 0; i < perm._onCheckPermissionCompleted.size(); i++) {
                perm._onCheckPermissionCompleted.get(i).onPermissionSettings(buildPermissions(perm._permissions, grantResults));
            }
        }
        _permReq.remove(requestCode);
    }
}
