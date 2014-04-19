package com.houseperez.filehog.activity;

import android.os.AsyncTask;
import android.util.Log;

import com.houseperez.util.Constants;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;
import com.houseperez.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jhouse on 4/18/2014.
 */
public class RefreshAsync extends AsyncTask<File, Integer, Void> {

    private static final String TAG = RefreshAsync.class.getName();

    private Settings settings;
    private FileListFragment.TaskCallbacks taskCallbacks;
    private ArrayList<FileInformation> biggestHogFiles;
    private ArrayList<FileInformation> smallestHogFiles;

    public RefreshAsync(FileListFragment.TaskCallbacks taskCallbacks) {
        this.taskCallbacks = taskCallbacks;
        this.settings = Settings.getInstance();
        biggestHogFiles = new ArrayList<FileInformation>();
        smallestHogFiles = new ArrayList<FileInformation>();
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
        if (taskCallbacks != null) {
            taskCallbacks.onPostExecute(biggestHogFiles, smallestHogFiles);
        }
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

                    if (biggestHogFiles.size() < Constants.MAX_FILE_COUNT) {
                        FileInformation newFileInformation = new FileInformation();
                        newFileInformation.setName(file.getName());
                        newFileInformation.setSize(file.length());
                        newFileInformation.setLastModified(file.lastModified());
                        newFileInformation.setFolder(file.getParentFile().getAbsolutePath());

                        biggestHogFiles.add(newFileInformation);
                    } else {
                        for (FileInformation fileInformation : biggestHogFiles) {
                            if (file.length() > fileInformation.getSize()) {
                                FileInformation newFileInformation = new FileInformation();
                                newFileInformation.setName(file.getName());
                                newFileInformation.setSize(file.length());
                                newFileInformation.setLastModified(file.lastModified());
                                newFileInformation.setFolder(file.getParentFile().getAbsolutePath());
                                biggestHogFiles.add(newFileInformation);

                                Collections.sort(biggestHogFiles);
                                biggestHogFiles.remove(biggestHogFiles.size() - 1);
                                break;
                            }
                        }
                    }

                    if (smallestHogFiles.size() < Constants.MAX_FILE_COUNT) {
                        FileInformation newFileInformation = new FileInformation();
                        newFileInformation.setName(file.getName());
                        newFileInformation.setSize(file.length());
                        newFileInformation.setLastModified(file.lastModified());
                        newFileInformation.setFolder(file.getParentFile().getAbsolutePath());

                        smallestHogFiles.add(newFileInformation);
                    } else {
                        for (FileInformation fileInformation : smallestHogFiles) {
                            if (file.length() < fileInformation.getSize()) {
                                FileInformation newFileInformation = new FileInformation();
                                newFileInformation.setName(file.getName());
                                newFileInformation.setSize(file.length());
                                newFileInformation.setLastModified(file.lastModified());
                                newFileInformation.setFolder(file.getParentFile().getAbsolutePath());
                                smallestHogFiles.add(newFileInformation);

                                Collections.sort(smallestHogFiles);
                                smallestHogFiles.remove(0);
                                break;
                            }
                        }
                    }

                } else {
                    // Recurse directories
                    if (file != null && file.exists() && file.canRead() && !file.isHidden())
                        searchFiles(file);
                }
            }
        }

    }


}
