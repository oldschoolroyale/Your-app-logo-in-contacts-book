package uz.old.contactsync.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {

    companion object {

        private val sSyncAdapterLock = Object()
        private var sSyncAdapter: SyncAdapter? = null
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = SyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = sSyncAdapter?.syncAdapterBinder
}