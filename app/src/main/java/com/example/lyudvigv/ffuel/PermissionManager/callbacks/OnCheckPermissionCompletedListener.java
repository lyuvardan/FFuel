package com.example.lyudvigv.ffuel.PermissionManager.callbacks;

import com.example.lyudvigv.ffuel.PermissionManager.Permission;

public interface OnCheckPermissionCompletedListener {
    void onCheckPermissionCompleted(Permission permissions);
    void onPermissionSettings(Permission permissions);
}
