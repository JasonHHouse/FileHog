package com.houseperez.filehog;

import java.io.File;
import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.houseperez.util.Constants;
import com.houseperez.util.FileIO;
import com.houseperez.util.HogFileList;
import com.houseperez.util.Pair;
import com.houseperez.util.Settings;
import com.houseperez.util.Utility;

public class FileListFragment extends ListFragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String TAG = "FileListFragment";

	// Globals
	private HogFileList hogFiles;
	private File clickedFile;
	private boolean isBiggestFiles;
	private Settings settings;

	public FileListFragment() {
		Log.i(TAG, "FileListFragment Constructor");
	}

	public HogFileList getHogFiles() {
		return hogFiles;
	}

	public void setHogFiles(HogFileList hogFiles) {
		this.hogFiles = hogFiles;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
		
		this.hogFiles = (HogFileList)getArguments().getSerializable(Constants.HOG_FILES);
		this.isBiggestFiles = getArguments().getBoolean(Constants.IS_BIGGEST_FILES);
		
		ArrayList<String> strValues = new ArrayList<String>();
		if (hogFiles != null)
			for (Pair pair : hogFiles.getHogFiles()) {
				if (!Utility.isInExcludedHogFiles(pair.getFile(),
						settings.getBiggestExternalExcludedHogFiles()))
					strValues.add("File: " + pair.getFile().getAbsoluteFile()
							+ "\nSize: "
							+ Utility.getCorrectByteSize(pair.getSize()));

				if (settings.getIntFileCount() <= strValues.size())
					break;
			}
		String[] values = strValues.toArray(new String[strValues.size()]);
		Log.i(TAG, "values.length: " + values.length);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "onAttach()");
		settings = Settings.getInstance(0);
	}

	@Override
	public void onResume(){
		super.onResume();
		Log.i(TAG, "onResume()");
		resetListView();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String strFile = ((String) l.getItemAtPosition((int) id));
		strFile = strFile.substring(strFile.indexOf("/"),
				strFile.lastIndexOf("Size: ") - 1);

		Log.i(TAG, "onListItemClick strFile: " + strFile);

		for (Pair pair : hogFiles.getHogFiles()) {
			if (pair.getFile().getAbsoluteFile().toString().equals(strFile)) {
				Log.i(TAG, "Found file in smallestHogFiles.");
				clickedFile = pair.getFile();
				break;
			}
		}

		// Check if they want to delete file or view it AlertDialog.Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Would you like to delete or view the file?")
				.setPositiveButton("Delete",
						dialogClickListener_DeleteCopyOrExclude)
				.setNegativeButton("Copy",
						dialogClickListener_DeleteCopyOrExclude)
				.setNeutralButton("Exclude",
						dialogClickListener_DeleteCopyOrExclude).show();

	}

	public void resetListView() {
		if (Constants.debugOn)
			Log.i(TAG, "Updating UI");

		ArrayList<String> strValues = new ArrayList<String>();

		if (Constants.debugOn)
			Log.i(TAG,
					"settings.getIntFileCount(): " + settings.getIntFileCount());

		Log.i(TAG,
				"(MainActivity)getActivity() is "
						+ (((MainActivity) getActivity()) == null ? "null"
								: "not null"));

		Log.i(TAG, "settings is " + (settings == null ? "null" : "not null"));
		
		Log.i(TAG,
				"settings.EXTERNAL_DIRECTORY: "
						+ (settings.getSelectedSearchDirectory() == Settings.EXTERNAL_DIRECTORY));

		switch (settings.getSelectedSearchDirectory()) {
		case (Settings.EXTERNAL_DIRECTORY):
			if (Constants.debugOn)
				Log.i(TAG,
						"settings.getIntFileCount() + settings.getBiggestExternalExcludedHogFiles().size(): "
								+ (settings.getIntFileCount() + settings
										.getBiggestExternalExcludedHogFiles()
										.size()));

			if (isBiggestFiles) {
				for (Pair pair : hogFiles.getHogFiles()) {
					if (!Utility.isInExcludedHogFiles(pair.getFile(),
							settings.getBiggestExternalExcludedHogFiles()))
						strValues.add("File: "
								+ pair.getFile().getAbsoluteFile() + "\nSize: "
								+ Utility.getCorrectByteSize(pair.getSize()));

					if (settings.getIntFileCount() <= strValues.size())
						break;
				}
			} else {
				for (Pair pair : hogFiles.getHogFiles()) {
					if (!Utility.isInExcludedHogFiles(pair.getFile(),
							settings.getSmallestExternalExcludedHogFiles()))
						strValues.add("File: "
								+ pair.getFile().getAbsoluteFile() + "\nSize: "
								+ Utility.getCorrectByteSize(pair.getSize()));

					if (settings.getIntFileCount() <= strValues.size())
						break;
				}
			}
			break;
		case Settings.ROOT_DIRECTORY:
			if (isBiggestFiles) {
				for (Pair pair : hogFiles.getHogFiles()) {
					if (!Utility.isInExcludedHogFiles(pair.getFile(),
							settings.getBiggestRootExcludedHogFiles()))
						strValues.add("File: "
								+ pair.getFile().getAbsoluteFile() + "\nSize: "
								+ Utility.getCorrectByteSize(pair.getSize()));

					if (settings.getIntFileCount() <= strValues.size())
						break;
				}
			} else {
				for (Pair pair : hogFiles.getHogFiles()) {
					if (!Utility.isInExcludedHogFiles(pair.getFile(),
							settings.getSmallestRootExcludedHogFiles()))
						strValues.add("File: "
								+ pair.getFile().getAbsoluteFile() + "\nSize: "
								+ Utility.getCorrectByteSize(pair.getSize()));

					if (settings.getIntFileCount() <= strValues.size())
						break;
				}
			}
			break;
		}

		String values[] = strValues.toArray(new String[strValues.size()]);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		setListAdapter(adapter);
		this.getListView().setEnabled(true);
	}

	DialogInterface.OnClickListener dialogClickListener_DeleteCopyOrExclude = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Delete
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Are you sure?")
						.setPositiveButton("Yes", dialogClickListener_YesOrNo)
						.setNegativeButton("No", dialogClickListener_YesOrNo)
						.show();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				// Copy
				builder = null;
				ClipboardManager clipboard = (ClipboardManager) getActivity()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData
						.newUri(getActivity().getContentResolver(), "URI",
								Uri.fromFile(clickedFile));
				clipboard.setPrimaryClip(clip);
				Toast.makeText(getActivity().getApplicationContext(),
						"File copied to clipboard", Toast.LENGTH_SHORT).show();
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				// Exclude File
				Log.i(TAG, clickedFile.getPath() + " added to excludedHogFiles");
				switch (settings.getSelectedSearchDirectory()) {
				case Settings.EXTERNAL_DIRECTORY:
					if (isBiggestFiles)
						settings.getBiggestExternalExcludedHogFiles().add(
								clickedFile);
					else
						settings.getSmallestExternalExcludedHogFiles().add(
								clickedFile);
					break;
				case Settings.ROOT_DIRECTORY:
					if (isBiggestFiles)
						settings.getBiggestRootExcludedHogFiles().add(
								clickedFile);
					else
						settings.getSmallestRootExcludedHogFiles().add(
								clickedFile);
					break;
				}

				// FileIO.writeObject(settings, getActivity()
				// .getApplicationContext(), Constants.SETTINGS_FILE);
				switch (settings.getSelectedSearchDirectory()) {
				case Settings.EXTERNAL_DIRECTORY:
					if (isBiggestFiles
							&& (hogFiles.getHogFiles().size()
									- settings
											.getBiggestExternalExcludedHogFiles()
											.size() < settings
										.getIntFileCount())) {
						Log.i(TAG, "Refresh on biggest files");

						((MainActivity) getActivity()).refresh();
					} else if (!isBiggestFiles
							&& (hogFiles.getHogFiles().size()
									- settings
											.getSmallestExternalExcludedHogFiles()
											.size() < settings
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
							&& (hogFiles.getHogFiles().size()
									- settings.getBiggestRootExcludedHogFiles()
											.size() < settings
										.getIntFileCount())) {
						Log.i(TAG, "Refresh on biggest files");

						((MainActivity) getActivity()).refresh();
					} else if (!isBiggestFiles
							&& (hogFiles.getHogFiles().size()
									- settings
											.getSmallestRootExcludedHogFiles()
											.size() < settings
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
				Pair[] aryHogFiles = hogFiles.getHogFiles().toArray(
						new Pair[hogFiles.getHogFiles().size()]);

				Log.i(TAG, "aryHogFiles.size(): " + aryHogFiles.length);

				for (Pair pair : aryHogFiles) {
					if (pair.getFile().getAbsoluteFile()
							.equals(clickedFile.getAbsoluteFile())) {
						Log.i(TAG, "Found file to delete");
						/*
						 * if (isBiggestFiles)
						 * biggestHogFiles.getHogFiles().remove(pair); else
						 * smallestHogFiles.getHogFiles().remove(pair);
						 */
						hogFiles.getHogFiles().remove(pair);
					}
				}

				boolean isDeleted = clickedFile.delete();
				clickedFile = null;
				String strOutput = "IsDeleted: " + isDeleted;

				if (Constants.debugOn)
					FileIO.writeFile(strOutput, "FileHog_Output.txt");

				switch (settings.getSelectedSearchDirectory()) {
				case Settings.EXTERNAL_DIRECTORY:
					if (isBiggestFiles
							&& hogFiles.getHogFiles().size()
									- settings
											.getBiggestExternalExcludedHogFiles()
											.size() < settings
										.getIntFileCount()) {

						((MainActivity) getActivity()).refresh();
					} else if (!isBiggestFiles
							&& hogFiles.getHogFiles().size()
									- settings
											.getSmallestExternalExcludedHogFiles()
											.size() < settings
										.getIntFileCount()) {

						((MainActivity) getActivity()).refresh();
					} else {
						resetListView();
					}
					break;
				case Settings.ROOT_DIRECTORY:
					if (isBiggestFiles
							&& hogFiles.getHogFiles().size()
									- settings.getBiggestRootExcludedHogFiles()
											.size() < settings
										.getIntFileCount()) {

						((MainActivity) getActivity()).refresh();
					} else if (!isBiggestFiles
							&& hogFiles.getHogFiles().size()
									- settings
											.getSmallestRootExcludedHogFiles()
											.size() < settings
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
