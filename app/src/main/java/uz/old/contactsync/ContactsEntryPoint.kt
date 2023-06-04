package uz.old.contactsync

import android.Manifest
import android.content.Context
import uz.old.contactsync.utils.ContactUtils
import uz.old.contactsync.utils.ContactsSyncUtils

object ContactsEntryPoint : ContactsSyncUtils() {

    @Throws(UnsupportedOperationException::class)
    fun subscribeOnSyncAdapter(context: Context){
        if (isPermissionGranted(context)){
            createSyncAccount(context)
        }
        else{
            throw UnsupportedOperationException("Permission is not granted")
        }
    }

    @Throws(UnsupportedOperationException::class)
    fun triggerSyncAdapterNow(context: Context){
        if (isPermissionGranted(context)){
            triggerRefresh()
        }
        else {
            throw UnsupportedOperationException("Permission is not granted")
        }
    }

    fun isPermissionGranted(context: Context): Boolean {
        return ContactUtils.isPermissionGranted(
            context, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
        )
    }
}