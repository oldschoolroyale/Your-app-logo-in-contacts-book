### Sync adapter or how I call it "Contacts synchorinzation"

This repository contains an example of using [Sync Adapter](https://developer.android.com/training/sync-adapters/creating-sync-adapter).<br />
The application, using Sync Adapter service, allows you to add icon of your app in contacts database and apear in contact details.<br /><br />

The application is a Wallet emulation, when the app is open in first time (sync adapter) starts after pressing button and repeats every 14 days (you can costumize your update time) <br />

When you click on one of the contacts, you will see your application icon in contacts detail. When you press it you can extract contacts detail from "DATA1" and "DATA2". <br />

When one of the user press the back button the connection stops and the apps will return to the search screen.
<br /><br />

<p align="middle">
  <img src="https://github.com/oldschoolroyale/contacts-sync/blob/master/Main%20page.png" width="260" />
  <img src="https://github.com/oldschoolroyale/contacts-sync/blob/master/Contacts.png" width="267" /> 
  <img src="https://github.com/oldschoolroyale/contacts-sync/blob/master/Result.png" width="260" />
</p>

#### Tutorial
To use this project add these permissions to your manifest:
```
    <uses-permission android:name="android.permission.READ_SYNC_STATS"/>
    
    <!-- Required to enable our SyncAdapter after it's created. -->
    <uses-permission android:name="android.permission.WRITE_SYNC_SETTINGS"/>
    
    <!-- Required because we're manually creating a new account. -->
    <uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS"/>
    
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
```
Setup you Authority
```
const val CONTENT_AUTHORITY = "com.android.contacts"

    const val ACCOUNT_TYPE = "uz.old.contactsync"

    const val ACCOUNT_NAME = "Smart wallet"

    private var account: Account? = null

    fun getAccount() : Account {
        return account ?: Account(ACCOUNT_NAME, ACCOUNT_TYPE)
    }

    fun setAccount(account: Account) {
        Authority.account = account
    }
```

Dont forget also xml, which is used in Manifest
1. Authenticator
```
<account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
                       android:accountType="uz.old.contactsync"
                       android:icon="@mipmap/ic_launcher"
                       android:smallIcon="@mipmap/ic_launcher"
                       android:label="@string/app_name"
        />
```
2. Contacts
```
<ContactsSource
    xmlns:android="http://schemas.android.com/apk/res/android">
    <ContactsDataKind
        android:mimeType="vnd.android.cursor.item/uz.old.contactsync"
        android:icon="@mipmap/ic_launcher"
        android:summaryColumn="data2"
        android:detailColumn="data3"
        android:detailSocialSummary="true" />
</ContactsSource>

```
3. Syncadapter
```
<sync-adapter xmlns:android="http://schemas.android.com/apk/res/android"
    android:accountType="uz.old.contactsync"
    android:allowParallelSyncs="false"
    android:contentAuthority="com.android.contacts"
    android:isAlwaysSyncable="true"
    android:supportsUploading="false"
    android:userVisible="true" />

```
### Video preview
<video src='https://github.com/oldschoolroyale/contacts-sync/assets/38427828/7f7a730f-93f3-436d-8960-aa1c20a06b93' width=180/>
