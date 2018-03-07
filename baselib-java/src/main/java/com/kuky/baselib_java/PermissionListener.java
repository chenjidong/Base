package com.kuky.baselib_java;

import java.util.List;

/**
 * @author Kuky
 */
public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
