package uz.old.contactsync.core

import android.accounts.Account
import androidx.annotation.VisibleForTesting

object Authority {

    const val CONTENT_AUTHORITY = "com.android.contacts"

    const val ACCOUNT_TYPE = "uz.old.contactsync"

    const val ACCOUNT_NAME = "Smart wallet"

    private var account: Account? = null

    fun getAccount() : Account {
        return account ?: Account(ACCOUNT_NAME, ACCOUNT_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setAccount(account: Account) {
        Authority.account = account
    }
}