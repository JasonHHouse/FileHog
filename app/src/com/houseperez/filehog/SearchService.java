package com.houseperez.filehog;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.houseperez.SearchRunnable;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jhouse on 6/16/17.
 */

public final class SearchService extends IntentService {

    private static final String TAG = SearchService.class.getName();

    private CountDownLatch latch;
    private ExecutorService executor;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SearchService() {
        super("SearchService");
        executor = Executors.newFixedThreadPool(1);
        latch = new CountDownLatch(1);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String strFile = intent.getStringExtra("FILE");
            Log.d(TAG, "strFile:" + strFile);

            File file = new File(strFile);
            SearchRunnable runnable = new SearchRunnable(file, latch);
            executor.execute(runnable);

            try {
                latch.await();
                Log.d(TAG, "Files found: " + runnable.getHogFiles());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(TAG, "Handle Intent null");
        }

    }
}
