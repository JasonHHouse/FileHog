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
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.houseperez.filehog.adapter.FileInformationAdapter;
import com.houseperez.util.Constants;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;

import java.io.File;
import java.util.ArrayList;

public class FileListFragment extends ListFragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = "FileListFragment";

    // Globals
    private FileInformationAdapter fileInformationAdapter;
    private ArrayList<FileInformation> hogFiles;
    private File clickedFile;
    private boolean isBiggestFiles;
    private Settings settings;

    public FileListFragment() {
        Log.i(TAG, "FileListFragment Constructor");
    }

    public ArrayList<FileInformation> getHogFiles() {
        return hogFiles;
    }

    public void setHogFiles(ArrayList<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");

        hogFiles = (ArrayList<FileInformation>) getArguments().getSerializable(Constants.HOG_FILES);
        isBiggestFiles = getArguments().getBoolean(Constants.IS_BIGGEST_FILES);

        //resetListView();

        fileInformationAdapter = new FileInformationAdapter(getActivity(), android.R.layout.simple_list_item_1, hogFiles);
        setListAdapter(fileInformationAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach()");
        settings = Settings.getInstance(0);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        FileInformation fileInformation = ((FileInformation) listView.getItemAtPosition((int) id));

        if(hogFiles.contains(fileInformation)) {
            clickedFile = new File(fileInformation.getName());
        }

        // Check if they want to delete file or view it AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like to delete or view the file?")
                .setPositiveButton("Delete", dialogClickListener_DeleteCopyOrExclude)
                .setNegativeButton("Copy", dialogClickListener_DeleteCopyOrExclude)
                .setNeutralButton("Exclude", dialogClickListener_DeleteCopyOrExclude).show();

    }

    public void resetListView() {
	/*	if (Constants.debugOn)
			Log.i(TAG, "Updating UI");

		ArrayList<String> strValues = new ArrayList<String>();

		Log.i(TAG, "settings is " + (settings == null ? "null" : "not null"));
		Log.i(TAG, "(MainActivity)getActivity() is " + (((MainActivity) getActivity()) == null ? "null" : "not null"));

		if (settings != null) {
			switch (settings.getSelectedSearchDirectory()) {
			case (Settings.EXTERNAL_DIRECTORY):
				if (isBiggestFiles) {
					for ( : hogFiles) {
						if (!Utility
								.isInExcludedHogFiles(pair.getFile(), settings.getBiggestExternalExcludedHogFiles()))
							strValues.add("File: " + pair.getFile().getAbsoluteFile() + "\nSize: "
									+ Utility.getCorrectByteSize(pair.getSize()));

						if (settings.getIntFileCount() <= strValues.size())
							break;
					}
				} else {
					for (Pair pair : hogFiles.getHogFiles()) {
						if (!Utility.isInExcludedHogFiles(pair.getFile(),
								settings.getSmallestExternalExcludedHogFiles()))
							strValues.add("File: " + pair.getFile().getAbsoluteFile() + "\nSize: "
									+ Utility.getCorrectByteSize(pair.getSize()));

						if (settings.getIntFileCount() <= strValues.size())
							break;
					}
				}
				break;
			case Settings.ROOT_DIRECTORY:
				if (isBiggestFiles) {
					for (Pair pair : hogFiles.getHogFiles()) {
						if (!Utility.isInExcludedHogFiles(pair.getFile(), settings.getBiggestRootExcludedHogFiles()))
							strValues.add("File: " + pair.getFile().getAbsoluteFile() + "\nSize: "
									+ Utility.getCorrectByteSize(pair.getSize()));

						if (settings.getIntFileCount() <= strValues.size())
							break;
					}
				} else {
					for (Pair pair : hogFiles.getHogFiles()) {
						if (!Utility.isInExcludedHogFiles(pair.getFile(), settings.getSmallestRootExcludedHogFiles()))
							strValues.add("File: " + pair.getFile().getAbsoluteFile() + "\nSize: "
									+ Utility.getCorrectByteSize(pair.getSize()));

						if (settings.getIntFileCount() <= strValues.size())
							break;
					}
				}
				break;
			}

			String[] values = strValues.toArray(new String[strValues.size()]);
			Log.i(TAG, "values.length: " + values.length);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
					android.R.id.text1, values);
			setListAdapter(adapter);
			// adapter.notifyDataSetChanged();
			// this.setListAdapter(adapter);
			this.getListView().setEnabled(true);
		}*/

    }

    DialogInterface.OnClickListener dialogClickListener_DeleteCopyOrExclude = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Delete
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener_YesOrNo)
                            .setNegativeButton("No", dialogClickListener_YesOrNo).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // Copy
                    builder = null;
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(
                            Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newUri(getActivity().getContentResolver(), "URI", Uri.fromFile(clickedFile));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity().getApplicationContext(), "File copied to clipboard", Toast.LENGTH_SHORT)
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

                    // FileIO.writeObject(settings, getActivity()
                    // .getApplicationContext(), Constants.SETTINGS_FILE);
                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if (isBiggestFiles
                                    && (hogFiles.size() - settings.getBiggestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                ((MainActivity) getActivity()).refresh();
                            } else if (!isBiggestFiles
                                    && (hogFiles.size() - settings.getSmallestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                ((MainActivity) getActivity()).refresh();
                            } else {
                                Log.i(TAG, "resetListView()");
                                resetListView();
                            }
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles
                                    && (hogFiles.size() - settings.getBiggestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                ((MainActivity) getActivity()).refresh();
                            } else if (!isBiggestFiles
                                    && (hogFiles.size() - settings.getSmallestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                ((MainActivity) getActivity()).refresh();
                            } else {
                                Log.i(TAG, "resetListView()");
                                resetListView();
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

                                ((MainActivity) getActivity()).refresh();
                            } else if (!isBiggestFiles
                                    && hogFiles.size() - settings.getSmallestExternalExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                ((MainActivity) getActivity()).refresh();
                            } else {
                                resetListView();
                            }
                            break;
                        case Settings.ROOT_DIRECTORY:
                            if (isBiggestFiles
                                    && hogFiles.size() - settings.getBiggestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                ((MainActivity) getActivity()).refresh();
                            } else if (!isBiggestFiles
                                    && hogFiles.size() - settings.getSmallestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount()) {

                                ((MainActivity) getActivity()).refresh();

                            } else {
                                resetListView();
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

}
