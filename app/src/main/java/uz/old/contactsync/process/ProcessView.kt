package uz.old.contactsync.process

import androidx.lifecycle.Observer
import uz.old.contactsync.process.ProcessState

interface ProcessView {

    fun onAllContactsSync(stateObserver: Observer<ProcessState>)

    fun onLimitContactsSync(limit: Int, stateObserver: Observer<ProcessState>)
}