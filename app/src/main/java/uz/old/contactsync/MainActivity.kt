package uz.old.contactsync

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.old.contactsync.databinding.ActivityMainBinding

const val CONTACTS_PERMISSION_RESPONSE_CODE = 111
const val GOOGLE_CONTACTS_AUTHORITY = "com.android.contacts"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (hasContactsIntent()) {
            getContacts()
        }

        binding.btnStart.setOnClickListener {
            if (ContactsEntryPoint.isPermissionGranted(this)) {
                ContactsEntryPoint.subscribeOnSyncAdapter(this)
                showMessage("Starting sync adapter")
                binding.btnStart.isEnabled = false
            } else {
                requestContactsPermission()
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CONTACTS_PERMISSION_RESPONSE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContactsEntryPoint.subscribeOnSyncAdapter(this)
                showMessage("Starting sync adapter")
                binding.btnStart.isEnabled = false
            } else {
                showMessage("Permissions is not granted")
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("Range")
    private fun getContacts() {
        if (ContactsEntryPoint.isPermissionGranted(this)) {
            try {
                managedQuery(
                    intent.data, null, null, null, null
                ).use { cursor ->
                    if (cursor.moveToNext()) {
                        val name = cursor.getString(cursor.getColumnIndex("DATA2"))
                        val number = cursor.getString(cursor.getColumnIndex("DATA1"))
                        binding.contactsText.text = "${name}\n${number}"
                    }
                }
            } catch (exception: Exception) {
                showMessage("Unknown error: ${exception.message}")
            }
        } else {
            showMessage("Missing contacts permission")
            requestContactsPermission()
        }
    }

    private fun requestContactsPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
            ), CONTACTS_PERMISSION_RESPONSE_CODE
        )
    }

    private fun hasContactsIntent() =
        intent.data != null && intent.data?.authority == GOOGLE_CONTACTS_AUTHORITY
}