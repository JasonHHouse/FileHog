package com.houseperez.filehog.activity;

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
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListFragment extends ListFragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = FileListFragment.class.getName();

    // Globals
    private FileInformationAdapter fileInformationAdapter;
    private List<FileInformation> hogFiles;
    private FileInformation clickedFile;
    private boolean isBiggestFiles;
    private Settings settings;
    private Context context;
    private int position;

    public FileListFragment(Context context, List<FileInformation> hogFiles) {
        this.context = context;
        this.hogFiles = hogFiles;
        settings = Settings.getInstance();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");

        settings = Settings.getInstance();
        isBiggestFiles = getArguments().getBoolean(Constants.IS_BIGGEST_FILES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (hogFiles != null) {
            fileInformationAdapter = new FileInformationAdapter(context, android.R.layout.simple_list_item_1, hogFiles);
            setListAdapter(fileInformationAdapter);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        FileInformation fileInformation = ((FileInformation) listView.getItemAtPosition((int) id));

        this.position = position;

        clickedFile = fileInformation;

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

    //public void startFileSearch() {
    //    settings = Settings.getInstance();


    //MainActivity mainActivity = (MainActivity) getActivity();
    //mainActivity.refresh();
    //Toast.makeText(context.getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
    //  }

    private List<FileInformation> removeExcludedFromHogFiles(List<FileInformation> excludedHogFiles) {
        List<FileInformation> updatedHogFiles = new ArrayList<FileInformation>();
        for (FileInformation fileInformation : hogFiles) {
            updatedHogFiles.add(fileInformation);
        }

        for (FileInformation fileInformation : excludedHogFiles) {
            updatedHogFiles.remove(fileInformation);
        }
        return updatedHogFiles;
    }


    DialogInterface.OnClickListener dialogClickListener_DeleteCopyOrExclude = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Delete
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    File file = new File(clickedFile.getFolder() + File.separator + clickedFile.getName());
                    Uri uri = Uri.fromFile(file);
                    ClipData clip = ClipData.newUri(context.getContentResolver(), "URI", uri);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context.getApplicationContext(), "File copied to clipboard", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    // Exclude File
                    Log.i(TAG, clickedFile.getFolder() + " added to excludedHogFiles");
                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if (isBiggestFiles) {
                                settings.getBiggestExternalExcludedHogFiles().add(clickedFile);
                                List<FileInformation> updatedHogFiles = removeExcludedFromHogFiles(settings.getBiggestExternalExcludedHogFiles());
                                fileInformationAdapter.setFileInformations(updatedHogFiles);
                            } else {
                                settings.getSmallestExternalExcludedHogFiles().add(clickedFile);
                                List<FileInformation> updatedHogFiles = removeExcludedFromHogFiles(settings.getSmallestExternalExcludedHogFiles());
                                fileInformationAdapter.setFileInformations(updatedHogFiles);
                            }
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles) {
                                settings.getBiggestRootExcludedHogFiles().add(clickedFile);
                                List<FileInformation> updatedHogFiles = removeExcludedFromHogFiles(settings.getBiggestRootExcludedHogFiles());
                                fileInformationAdapter.setFileInformations(updatedHogFiles);
                            } else {
                                settings.getSmallestRootExcludedHogFiles().add(clickedFile);
                                List<FileInformation> updatedHogFiles = removeExcludedFromHogFiles(settings.getSmallestRootExcludedHogFiles());
                                fileInformationAdapter.setFileInformations(updatedHogFiles);
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
                    File removingFile = new File(fileInformations[position].getFolder() + File.separator + fileInformations[position].getName());

                    Log.d(TAG, "removingFile: " + removingFile);

                    boolean isRemoved = removingFile.delete();
                    //boolean isRemoved = hogFiles.remove(removingFile);
                    String result;
                    if (isRemoved) {
                        result = "File removed";
                        fileInformationAdapter.remove(position);
                    } else {
                        result = "File not removed";
                    }
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                    // boolean isDeleted = clickedFile.delete();
                    clickedFile = null;
                    // String strOutput = "IsDeleted: " + isDeleted;

                    // if (Constants.debugOn)
                    // FileIO.writeFile(strOutput, "FileHog_Output.txt");

                  /*  switch (settings.getSelectedSearchDirectory()) {
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
                    }*/
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
