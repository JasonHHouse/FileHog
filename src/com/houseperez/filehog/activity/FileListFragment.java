package com.houseperez.filehog.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.houseperez.filehog.adapter.FileInformationAdapter;
import com.houseperez.util.Constants;
import com.houseperez.util.FileIO;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;

import java.io.File;
import java.util.List;

public class FileListFragment extends ListFragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = FileListFragment.class.getName();

    // Globals
    private FileInformationAdapter fileInformationAdapter;
    private List<FileInformation> hogFiles;
    private File clickedFile;
    private boolean isBiggestFiles;
    private Settings settings;
    private Context context;

    public FileListFragment(Context context, List<FileInformation> hogFiles) {
        this.context = context;
        this.hogFiles = hogFiles;
        settings = Settings.getInstance();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");

        isBiggestFiles = getArguments().getBoolean(Constants.IS_BIGGEST_FILES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(hogFiles != null) {
            fileInformationAdapter = new FileInformationAdapter(context, android.R.layout.simple_list_item_1, hogFiles);
            setListAdapter(fileInformationAdapter);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        FileInformation fileInformation = ((FileInformation) listView.getItemAtPosition((int) id));

        if (hogFiles.contains(fileInformation)) {
            clickedFile = new File(fileInformation.getName());
        }

        // Check if they want to delete file or view it AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("File Options");
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setMessage("Would you like to delete the file, view in folder, or add to the Excluded File list?");
        builder.setPositiveButton("Delete", dialogClickListener_DeleteCopyOrExclude);
        builder.setNegativeButton("View", dialogClickListener_DeleteCopyOrExclude);
        builder.setNeutralButton("Exclude", dialogClickListener_DeleteCopyOrExclude).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setHogFiles(List<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
    }

    public void updateAdapter(List<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
        if (fileInformationAdapter == null) {
            fileInformationAdapter = new FileInformationAdapter(context, android.R.layout.simple_list_item_1, hogFiles);
            setListAdapter(fileInformationAdapter);
        } else {
            fileInformationAdapter.setFileInformations(hogFiles);
        }
    }

    public void startFileSearch() {
        settings = Settings.getInstance();
    }


    DialogInterface.OnClickListener dialogClickListener_DeleteCopyOrExclude = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Delete
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setTitle("Delete File");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes", dialogClickListener_YesOrNo);
                    builder.setNegativeButton("No", dialogClickListener_YesOrNo).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // Copy
                    builder = null;
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(
                            Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newUri(context.getContentResolver(), "URI", Uri.fromFile(clickedFile));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context.getApplicationContext(), "File copied to clipboard", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    // Exclude File
                    Log.i(TAG, clickedFile.getPath() + " added to excludedHogFiles");
                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if (isBiggestFiles)
                                settings.getBiggestExternalExcludedHogFiles().add(clickedFile);
                            else
                                settings.getSmallestExternalExcludedHogFiles().add(clickedFile);
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles)
                                settings.getBiggestRootExcludedHogFiles().add(clickedFile);
                            else
                                settings.getSmallestRootExcludedHogFiles().add(clickedFile);
                            break;
                    }

                    // FileIO.writeObject(settings, context
                    // .getApplicationContext(), Constants.SETTINGS_FILE);
                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if (isBiggestFiles
                                    && (hogFiles.size() - settings.getBiggestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                startFileSearch();
                            } else if (!isBiggestFiles
                                    && (hogFiles.size() - settings.getSmallestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                startFileSearch();
                            } else {
                                Log.i(TAG, "startFileSearch()");
                                startFileSearch();
                            }
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles
                                    && (hogFiles.size() - settings.getBiggestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                startFileSearch();
                            } else if (!isBiggestFiles
                                    && (hogFiles.size() - settings.getSmallestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                startFileSearch();
                            } else {
                                Log.i(TAG, "startFileSearch()");
                                startFileSearch();
                            }
                            break;
                    }
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogClickListener_YesOrNo = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

				/*
                 * Pair[] aryHogFiles = (isBiggestFiles ? biggestHogFiles
				 * .getHogFiles().toArray( new
				 * Pair[biggestHogFiles.getHogFiles().size()]) :
				 * smallestHogFiles.getHogFiles() .toArray( new
				 * Pair[smallestHogFiles.getHogFiles() .size()]));
				 */
                    FileInformation[] fileInformations = hogFiles.toArray(new FileInformation[hogFiles.size()]);

                    Log.i(TAG, "aryHogFiles.size(): " + fileInformations.length);

                    for (FileInformation fileInformation : fileInformations) {
                        if (fileInformation.getName().equals(clickedFile.getAbsoluteFile())) {
                            Log.i(TAG, "Found file to delete");
                        /*
                         * if (isBiggestFiles)
						 * biggestHogFiles.getHogFiles().remove(pair); else
						 * smallestHogFiles.getHogFiles().remove(pair);
						 */
                            hogFiles.remove(fileInformation);
                        }
                    }

                    // boolean isDeleted = clickedFile.delete();
                    clickedFile = null;
                    // String strOutput = "IsDeleted: " + isDeleted;

                    // if (Constants.debugOn)
                    // FileIO.writeFile(strOutput, "FileHog_Output.txt");

                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if (isBiggestFiles
                                    && hogFiles.size() - settings.getBiggestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                startFileSearch();
                            } else if (!isBiggestFiles
                                    && hogFiles.size() - settings.getSmallestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                startFileSearch();
                            } else {
                                startFileSearch();
                            }
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles
                                    && hogFiles.size() - settings.getBiggestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                startFileSearch();
                            } else if (!isBiggestFiles
                                    && hogFiles.size() - settings.getSmallestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                startFileSearch();

                            } else {
                                startFileSearch();
                            }
                            break;
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
            dialog.dismiss();
        }
    };

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    static interface TaskCallbacks {
        void onPreExecute();

        void onProgressUpdate(int percent);

        void onCancelled();

        void onPostExecute(List<FileInformation> biggestHogFiles, List<FileInformation> smallestHogFiles, boolean isRoot);
    }

}
