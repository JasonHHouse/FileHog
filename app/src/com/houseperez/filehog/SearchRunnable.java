package com.houseperez.filehog;

import android.util.Log;

import com.houseperez.filehog.util.FileInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.houseperez.filehog.util.Utility.searchFiles;

/**
 * Created by jhouse on 6/16/17.
 */

public class SearchRunnable implements Runnable {

    private static final String TAG = SearchRunnable.class.getName();

    private File file;
    private CountDownLatch latch;
    private List<FileInformation> hogFiles;

    public SearchRunnable(File file, CountDownLatch latch) {
        Log.d(TAG, "SearchRunnable(" + file + ")");
        this.file = file;
        this.latch = latch;
        hogFiles = new ArrayList<>(2500);
    }

    @Override
    public void run() {
        Log.d(TAG, "run()");
        searchFiles(file, hogFiles);
        Collections.sort(hogFiles);
        latch.countDown();
    }

    public List<FileInformation> getHogFiles() {
        return hogFiles;
    }
}