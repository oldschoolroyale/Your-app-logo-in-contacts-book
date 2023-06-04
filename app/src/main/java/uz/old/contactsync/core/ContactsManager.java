package uz.old.contactsync.core;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import uz.old.contactsync.R;

public class ContactsManager {
    private static final String MIMETYPE_P2P = "vnd.android.cursor.item/uz.old.contactsync";

    public static void registerNumber(Context context, String contact_id, String number, Integer type, String contactName) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();

        // insert account name and type
        operations.add(ContentProviderOperation
                .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Authority.ACCOUNT_NAME)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Authority.ACCOUNT_TYPE)
                .withValue(ContactsContract.RawContacts.AGGREGATION_MODE,
                        ContactsContract.RawContacts.AGGREGATION_MODE_DEFAULT)
                .build()
        );

        // insert by phone number (because its unique)
        operations.add(ContentProviderOperation
                .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number) // Supply the number to be synced
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, type)
                .build()
        );

        // insert display name
        operations.add(ContentProviderOperation
                .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName) // Supply the display name to be synced
                .build()
        );

        /* (This will be the data retrieved when you click you app from contacts) */
        // insert your app data for message
        operations.add(ContentProviderOperation
                .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_P2P)
                .withValue(ContactsContract.Data.DATA1, number)
                .withValue(ContactsContract.Data.DATA2, contactName)
                .withValue(ContactsContract.Data.DATA3, String.format("%s to %s", context.getString(R.string.send_money), number))
                .build()
        );


        ContentProviderResult[] contentProviderResult = new ContentProviderResult[0];
        try {
            contentProviderResult = resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
        }

        /*
        Manually aggregate the new rawcontactid with a rawcontactid of number to be registered
        (If automatic aggregation does not work)
         */
        long newRawContactId = ContentUris.parseId(contentProviderResult[0].uri);

        try{
            joinIntoExistingContact(resolver, Long.parseLong(contact_id), newRawContactId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void joinIntoExistingContact(ContentResolver resolver, long existingContactId, long newRawContactId) throws RemoteException, OperationApplicationException {

        // get all existing raw-contact-ids that belong to the contact-id
        List<Long> existingRawIds = new ArrayList<>();
        Cursor cur = resolver.query(ContactsContract.RawContacts.CONTENT_URI, new String[] { ContactsContract.RawContacts._ID }, ContactsContract.RawContacts.CONTACT_ID + "=" + existingContactId, null, null);
        while (cur.moveToNext()) {
            existingRawIds.add(cur.getLong(0));
        }
        cur.close();

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // go over all existing raw ids, and join with our new one
        for (Long existingRawId : existingRawIds) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI);
            builder.withValue(ContactsContract.AggregationExceptions.TYPE, ContactsContract.AggregationExceptions.TYPE_KEEP_TOGETHER);
            builder.withValue(ContactsContract.AggregationExceptions.RAW_CONTACT_ID1, newRawContactId);
            builder.withValue(ContactsContract.AggregationExceptions.RAW_CONTACT_ID2, existingRawId);
            ops.add(builder.build());
        }

        resolver.applyBatch(ContactsContract.AUTHORITY, ops);
    }

    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
        }
        return uri;
    }
}
