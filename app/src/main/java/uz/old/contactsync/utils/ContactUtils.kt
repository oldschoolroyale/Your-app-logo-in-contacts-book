package uz.old.contactsync.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object ContactUtils {

    fun isPermissionGranted(context: Context, vararg permissionList: String) : Boolean {
        var result = true
        // For Android < Android M, self permissions are always granted.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            loop@ for (permission in permissionList){
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    result = false
                    break@loop
                }
            }
        }
        return result
    }
}