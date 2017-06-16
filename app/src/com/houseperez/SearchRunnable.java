package com.houseperez;

import com.houseperez.util.FileInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.houseperez.util.Utility.searchFiles;

/**
 * Created by jhouse on 6/16/17.
 */

public class SearchRunnable implements Runnable {

    private File file;
    private CountDownLatch latch;
    private List<FileInformation> hogFiles;

    public SearchRunnable(File file, CountDownLatch latch) {
        this.file = file;
        this.latch = latch;
        hogFiles = new ArrayList<>(2500);
    }

    @Override
    public void run() {
        searchFiles(file, hogFiles);
        latch.countDown();
    }

    public List<FileInformation> getHogFiles() {
        return hogFiles;
    }
}