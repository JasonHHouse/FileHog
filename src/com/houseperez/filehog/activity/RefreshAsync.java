package com.houseperez.filehog.activity;

import android.os.AsyncTask;
import android.util.Log;

import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;
import com.houseperez.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jhouse on 4/18/2014.
 */
public class RefreshAsync extends AsyncTask<File, Integer, Void> {

    private static final String TAG = RefreshAsync.class.getName();

    private boolean isRoot;
    private long timer;
    private Settings settings;
    private FileListFragment.TaskCallbacks taskCallbacks;
    private List<FileInformation> hogFiles;

    public RefreshAsync(FileListFragment.TaskCallbacks taskCallbacks, boolean isRoot) {
        this.taskCallbacks = taskCallbacks;
        this.settings = Settings.getInstance();
        hogFiles = new ArrayList<FileInformation>(2500);
        this.isRoot = isRoot;
    }

    @Override
    protected Void doInBackground(File... params) {
        searchFiles(params[0]);
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (taskCallbacks != null) {
            taskCallbacks.onPreExecute();
        }
        timer = System.currentTimeMillis();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        if (taskCallbacks != null) {
            taskCallbacks.onProgressUpdate(percent[0]);
        }
    }

    @Override
    protected void onCancelled() {
        if (taskCallbacks != null) {
            taskCallbacks.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(Void ignore) {
        List<FileInformation> biggestHogFiles = new ArrayList<FileInformation>();
        List<FileInformation> smallestHogFiles = new ArrayList<FileInformation>();

        if (hogFiles != null) {
            Collections.sort(hogFiles);

            if (hogFiles.size() > 50) {
                biggestHogFiles = hogFiles.subList(0, 50);
                smallestHogFiles = hogFiles.subList(hogFiles.size() - 50, hogFiles.size());
            }
        }

        if (taskCallbacks != null) {
            taskCallbacks.onPostExecute(biggestHogFiles, smallestHogFiles, isRoot);
        }
        timer = System.currentTimeMillis() - timer;
        Log.d(TAG, "timer: " + timer);
    }

    public void searchFiles(File folder) {
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (file.isFile()
                        && ((settings.getSelectedSearchDirectory() == Settings.EXTERNAL_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, settings.getBiggestExternalExcludedHogFiles()))
                        || (settings.getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, settings.getSmallestExternalExcludedHogFiles()))
                        || (settings.getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, settings.getBiggestRootExcludedHogFiles())) || (settings
                        .getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, settings.getSmallestRootExcludedHogFiles())))) {

                    FileInformation fileInformation = new FileInformation();
                    fileInformation.setName(file.getName());
                    fileInformation.setSize(file.length());
                    fileInformation.setLastModified(file.lastModified());
                    fileInformation.setFolder(file.getParentFile().getAbsolutePath());

                    hogFiles.add(fileInformation);

                } else {
                    // Recurse directories
                    if (file != null && file.exists() && file.canRead() && !file.isHidden())
                        searchFiles(file);
                }
            }
        }

    }


}
