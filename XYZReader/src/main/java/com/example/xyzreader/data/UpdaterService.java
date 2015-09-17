package com.example.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import com.example.xyzreader.XYZReaderApplication;
import com.example.xyzreader.remote.Article;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
// ------------------------------ FIELDS ------------------------------

    public static final String BROADCAST_ACTION_STATE_CHANGE = "com.example.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING = "com.example.xyzreader.intent.extra.REFRESHING";
    private static final String TAG = "UpdaterService";

// --------------------------- CONSTRUCTORS ---------------------------

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NetworkInfo ni = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            return;
        }

        sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        Uri dirUri = ItemsContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        for (Article article : XYZReaderApplication.getXYZReaderApi(getApplicationContext()).getRecipes()) {
            ContentValues values = new ContentValues();
            values.put(ItemsContract.Items.SERVER_ID, article.id);
            values.put(ItemsContract.Items.AUTHOR, article.author);
            values.put(ItemsContract.Items.TITLE, article.title);
            values.put(ItemsContract.Items.BODY, article.body);
            values.put(ItemsContract.Items.PHOTO_URL, article.photo);
            values.put(ItemsContract.Items.PUBLISHED_DATE, article.publishedDate.getTime());
            cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
        }

        try {
            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
        }

        sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
