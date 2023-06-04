package uz.old.contactsync.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import uz.old.contactsync.core.Authenticator

class AccountAuthenticatorService : Service() {

    private var mAuthenticator: Authenticator? = null

    override fun onCreate() {
        super.onCreate()
        mAuthenticator = Authenticator(this)
    }

    override fun onBind(p0: Intent?): IBinder? = mAuthenticator?.iBinder

}