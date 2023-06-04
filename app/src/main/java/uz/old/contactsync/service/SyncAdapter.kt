package uz.old.contactsync.service

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import uz.old.contactsync.process.ProcessView
import uz.old.contactsync.process.Processor

class SyncAdapter constructor(context: Context, autoInitialize: Boolean) :
    AbstractThreadedSyncAdapter(context, autoInitialize) {

    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
        val processView : ProcessView = Processor(context)
        processView.onAllContactsSync{ run {} }
    }
}