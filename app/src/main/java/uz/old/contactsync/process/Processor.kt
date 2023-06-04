package uz.old.contactsync.process

import android.content.Context
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import uz.old.contactsync.Contacts
import uz.old.contactsync.core.ContactsHandler
import uz.old.contactsync.core.ContactsManager
import kotlin.coroutines.CoroutineContext

class Processor constructor(private val context: Context) : ProcessView, CoroutineScope {

    private var stateObserver: Observer<ProcessState>? = null

    private var job: Job = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        stateObserver?.onChanged(ProcessState.ERROR)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + exceptionHandler

    private var limit: Int? = null

    override fun onAllContactsSync(stateObserver: Observer<ProcessState>) {
        this.stateObserver = stateObserver
        stateObserver.onChanged(ProcessState.IN_PROGRESS)

        launch {
            val mContactsList: ArrayList<Contacts> = async {
                val cHandler = ContactsHandler(context = context)
                cHandler.getContactData()
            }.await()


            for (contacts in mContactsList) {
                contacts.getNumbers()?.let { numbers ->
                    for (j in numbers.indices) {
                        val number = numbers[j]
                        ContactsManager.registerNumber(context,
                        contacts.getId(),
                        number,
                        contacts.getNumbersTypeInts()?.get(j),
                        contacts.getDisplayName())
                    }
                }

            }
        }

        stateObserver.onChanged(ProcessState.COMPLETE)
    }

    override fun onLimitContactsSync(limit: Int, stateObserver: Observer<ProcessState>) {
        this.limit = limit
        onAllContactsSync(stateObserver = stateObserver)
    }


}