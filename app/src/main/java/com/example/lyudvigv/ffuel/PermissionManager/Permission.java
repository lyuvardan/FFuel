package com.example.lyudvigv.ffuel.PermissionManager;

import java.io.Serializable;

public class Permission implements Serializable {

    private boolean PermCamera;
    private boolean PermFineLocation;
    private boolean PermGetAccounts;
    private boolean PermGetCall;
    private boolean PermExternalStorage;
    private boolean PermRecordAudio;

    public Permission() {

    }

    protected void setPermCamera(boolean permCamera) {
        PermCamera = permCamera;
    }

    public boolean getPermCamera() {
        return PermCamera;
    }

    protected void setPermCall(boolean permCall) {
        PermGetCall = permCall;
    }

    public boolean getPermCall() {
        return PermGetCall;
    }

    protected void setPermFineLocation(boolean permFineLocation) {
        PermFineLocation = permFineLocation;
    }

    public boolean getPermFineLocation() {
        return PermFineLocation;
    }

    protected void setPermGetAccounts(boolean permGetAccounts) {
        PermGetAccounts = permGetAccounts;
    }

    public boolean getPermGetAccounts() {
        return PermGetAccounts;
    }

    protected void setPermExternalStorage(boolean permExternalStorage) {
        PermExternalStorage = permExternalStorage;
    }

    public boolean getPermExternalStorage() {
        return PermExternalStorage;
    }

    protected void setPermRecordAudio (boolean permRecordAudio) {
        PermRecordAudio = permRecordAudio;
    }

    public boolean getPermRecordAudio() {
        return PermRecordAudio;
    }
}
