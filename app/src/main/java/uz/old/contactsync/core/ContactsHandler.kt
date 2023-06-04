package uz.old.contactsync.core

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import uz.old.contactsync.Column
import uz.old.contactsync.Contacts

class ContactsHandler(private val context: Context) {

    @SuppressLint("Range")
    fun getContactData(): ArrayList<Contacts> {

        val list = ArrayList<Contacts>()

        val cr = context.contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cursor?.count ?: 0) > 0) {
            while (cursor!!.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val timestamp =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    val numbers = ArrayList<String>()
                    val numbersType = ArrayList<String>()
                    val numbersTypeInts = ArrayList<Int>()
                    val contactHashMap = HashMap<String, Column>()
                    val paymeHashMap = HashMap<String, Column>()


                    while (pCur != null && pCur.moveToNext()) {

                        val normalNumber = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
                            )
                        )

                        val phoneNoType = pCur.getInt(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.TYPE
                            )
                        )

                        val customLabel = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.LABEL
                            )
                        )

                        val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                            context.resources, phoneNoType, customLabel
                        )

                        val accountType = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.RawContacts.ACCOUNT_TYPE
                            )
                        )

                        if (isNumberHasValidCountryCode(normalNumber)) {
                            if (accountType == Authority.ACCOUNT_TYPE) {
                                paymeHashMap[normalNumber] = Column(
                                    normalNumber, phoneNoType, customLabel, typeLabel, accountType
                                )
                            } else {
                                contactHashMap[normalNumber] = Column(
                                    normalNumber, phoneNoType, customLabel, typeLabel, accountType
                                )
                            }
                        }
                    }

                    pCur?.close()


                    paymeHashMap.forEach { (s, _) ->
                        if (contactHashMap.containsKey(s)) {
                            contactHashMap.remove(s)
                        }
                    }

                    contactHashMap.forEach { (_, column) ->
                        numbers.add(column.normalNumber)
                        numbersType.add(column.typeLabel.toString())
                        numbersTypeInts.add(column.phoneNoType)
                    }

                    if (numbers.isNotEmpty()) {

                        val contact =
                            Contacts.Builder().setId(id).setDisplayName(name).setNumbers(numbers)
                                .setNumbersType(numbersType).setNumbersTypeInts(numbersTypeInts)
                                .setLastUpdatedTimeStamp(timestamp.toLong()).build()

                        list.add(contact)
                    }
                }
            }
        }

        cursor?.close()
        return list
    }

    private fun isNumberHasValidCountryCode(normalNumber: String?): Boolean {
        return normalNumber != null && normalNumber.isNotEmpty() && normalNumber.isNotBlank()
    }
}