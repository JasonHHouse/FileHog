/*
 * File:        MainActivity.java
 * Created:     12/7/2012
 * Author:      Jason House
 * Description: Find the 10 biggest files in the external directory
 * 				On click, open folder system intent or copy the file location to the clipboard
 * 
 * This code is copyright (c) 2012 HousePerez
 *  
 * History:
 * Revision v 1.0
 * 
 * Find the 10 biggest files in the external directory
 * On click, open folder system intent or copy the file location to the clipboard
 * 
 * Revision v 1.01
 * 
 * Running on separate thread from main to fix workload issues
 * Delete files from the program as been added
 * 
 * Revision v 1.02
 * 
 * Fixed deleting files issue
 * Fixed duplicate row issue
 * 
 * Revision v 1.03
 * 
 * Fixed the java.lang.NumberFormatException
 * Fixed selected file not usable after clicked view
 * Updated settings options
 * Updated settings - ChangeFileCount 
 * 		Make the number of files shown adjustable
 * Update settings - About
 * 		Added about menu for more information
 * Update settings - Refresh
 * 		Added refresh button
 * 
 * Revision v 1.04
 * 
 * Revised and cleaned code and added generics
 * Removed donate by making paid and free version
 * Added dialog to change between root and external directory
 * Added searching for the smallest files 
 * 
 * Revision v 1.05
 * 
 * Changed threading to help with crash issues
 * Added ProgressDialog
 * Changed icons and images
 * 
 * Revision v 1.06
 * 
 * Updated the progress bar to show more detailed search
 * 
 * Revision v 1.07
 * 
 * Updated the percentage calculation
 * Updated the ProgressDialog
 * 
 * Revision v 1.08
 * 
 * Fixed the java.lang.IllegalArgumentException: View not attached to window manager
 * Fixed the orientation progress bar issue
 * Removed viewing file in folder, changed to copying location to clipboard
 * Added exclude files feature
 * 		To exclude click on a file listed
 * 		To add the file back in the search click the excluded files menu item
 * 
 * Revision v 1.09
 * 
 * Fixed the sorting size dialog box showing up after refresh
 * Hyper link added to about menu item
 * Let menu items be visible in the action bar instead of always hidden
 * Increased size of hidden stored files
 * Removed refreshing to cached file list
 * 		Change file count
 * 		Remove file
 * 		Exclude file
 * Fixed excluding duplicate files issue
 * 
 * Revision v 2.00
 * 
 * shorten menu item names for more visibility
 * 		change file count -> file count
 * 		change directory -> directory
 * 		change file sorting -> file sort
 * Count files now adjusted for excluded folders and files
 * Created new dialog to show counting files
 * Updated searching files dialog with file count
 * Fixed the dialog disappearing on display darkening issue
 * Fixed excluded files and file count issue
 * Created excluded lists for each directory and sort
 * Added automatic saving of file count, directory, sort and excluded files
 * 
 * Revision v 2.01
 * 
 * Read and write internally
 * Objectized reading and writing for more generic usage
 * Added Release Of Liability requirement
 * 
 * Revision v 2.02
 * 
 * Switched many if/else to switch case for speed boost
 * Created new menu item Research Frequency
 * 		This allows users to set how often FileHog should refresh
 * 		This will limit unwanted refreshes
 * 		Default is set to daily
 * 
 * Revision v 2.03
 * 
 * Added checks to properly close dialogs
 * Added more threading checks
 * Now keeping screen on during searching to allow for long searches to complete
 * Refresh Frequency now shows selected refresh frequency in dialog
 * Refresh Frequency dialog now visible onload to set the value easier
 * Version check to make sure stored files are not out of date
 * 
 * Revision v 2.04
 * 
 * Added language pack supporting German, Spanish, Arabic, Portuguese, and English 
 * Created Settings Activity to move many dialogs into one location
 * 
 * Revision v 2.05
 * 
 * Fixed viewing issues of settings page
 * Added Holo.Light
 * Changed settings to Holo.Light styling
 * Reduced refreshes
 * 
 * Revision v 2.06
 * 
 * Fixed the seekbar not updating bug
 * Made the app Holo after requests
 * Fixed the updating issues
 * 
 * Revision v 2.07
 * 
 * Language pack updated with Hindi, Korean, Polish, French, Russian
 * 
 * Revision v 3.00
 * 
 * Changed from single list view activity to pager with fragments 
 * showing smallest and largest files.  This removes the need to use settings 
 * for smallest or largest files to be shown.
 * Settings largest/smallest option has been removed
 * 
 * Revision v 3.01
 * 
 * Fixed the rotation issue
 * 
 * Revision v 3.02
 * 
 * Fixed the refresh rotation issue
 * 
 * Revision v 3.03
 * 
 * Overhauled the rotation code to better check for and handle refreshes 
 * 
 * Revision v 3.04
 * 
 * Made Settings into a singleton 
 * Changed saving to not require context
 * Changed FileListFragment to default constructor 
 * and pass a bundle of information
 * Fixed the on app killed, blank screen issue
 * 
 * Revision v 3.05
 * 
 * Fixed the settings not updating issue
 * 
 * Revision v 3.06
 * 
 * Edited read and write file to close the streams
 * Changed the number formatting for file size to fit international standards
 * 
 * Revision v 3.07
 * 
 * Added Russian back in
 *
 * Revision 4.0
 *
 * Added FileInformationAdapter
 * Added last modified information
 * Split the folder and file name up in the list view
 * Switched excluded files and refresh to icon
 * Created listview item layout for better information presentation
 *
 * Revision 4.01
 *
 * Updated listview items look and feel
 *
 * Revision 4.02
 *
 * Changed from using runnable to use async task for searching for files
 * Removed counting dialog
 * Removed searching dialog
 *
 * Revision 4.03
 *
 * Speed up the file searching
 * Fixed files not showing up
 *
 * Revision 4.04
 *
 * Speed up the file searching more
 * Cut down on file searching memory usage
 * Changed action bar text color to apps darkBlue
 * Added more padding to file information labels and file information
 * Right justified the file information labels
 *
 *
 *
 */

package com.houseperez.filehog.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.houseperez.filehog.R;
import com.houseperez.util.Constants;
import com.houseperez.util.FileIO;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements FileListFragment.TaskCallbacks {

    public static final String TAG = MainActivity.class.getName();

    // Globals
    private List<FileInformation> biggestHogFiles;
    private List<FileInformation> smallestHogFiles;
    private AlertDialog releaseOfLiabilityDialog;
    private Settings settings;
    private boolean needToRefreshList;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FileListFragment[] fileListFragments;
    private static File path;

    public static File getFilePath() {
        return path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate()");

        path = getDir("obj", Context.MODE_PRIVATE);

        checkVersion();

        fileListFragments = new FileListFragment[2];
        settings = Settings.getInstance();

        needToRefreshList = false;

        /*if (FileIO.readObject(Constants.RELEASE_OF_LIABILITY_FILE, path) == null) {
            releaseOfLiabilityDialog = buildReleaseOfLiabilityDialog().create();
            releaseOfLiabilityDialog.setCanceledOnTouchOutside(false);
            releaseOfLiabilityDialog.show();
        } else {*/

        needToRefreshList = settings.isOnOpenRefresh();
        settings.setOnOpenRefresh(false);
        // FileIO.writeObject(settings, Constants.SETTINGS_FILE, path);

        initalizeAndRefresh();
        //}
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

        if (releaseOfLiabilityDialog != null)
            releaseOfLiabilityDialog.dismiss();

        // FileIO.writeObject(settings, Constants.SETTINGS_FILE, path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState()");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.About:
                onClick_About();
                return true;
            case R.id.Settings:
                onClick_Settings();
                return true;
            case R.id.ExcludedFiles:
                onClick_ExcludeFiles();
                return true;
            case R.id.Refresh:
                needToRefreshList = true;
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void onClick_ExcludeFiles() {
        ArrayList<File> mergedExcludedFiles = new ArrayList<File>();
        String strTitle = "";

        switch (settings.getSelectedSearchDirectory()) {
            case Settings.EXTERNAL_DIRECTORY:

                if (mViewPager.getCurrentItem() == 0) {
                    mergedExcludedFiles.addAll(settings.getBiggestExternalExcludedHogFiles());
                    strTitle = "Excluded Biggest External Directory Files";
                } else {
                    mergedExcludedFiles.addAll(settings.getSmallestExternalExcludedHogFiles());
                    strTitle = "Excluded Smallest External Directory Files";
                }
                break;
            case Settings.ROOT_DIRECTORY:
                if (mViewPager.getCurrentItem() == 0) {
                    mergedExcludedFiles.addAll(settings.getBiggestRootExcludedHogFiles());
                    strTitle = "Excluded Biggest Root Directory Files";
                } else {
                    mergedExcludedFiles.addAll(settings.getSmallestRootExcludedHogFiles());
                    strTitle = "Excluded Smallest Root Directory Files";
                }
                break;
        }

        String[] excludedFiles = new String[mergedExcludedFiles.size()];

        final boolean[] mSelectedItems = new boolean[mergedExcludedFiles.size()];

        for (int i = 0; i < mergedExcludedFiles.size(); i++) {
            excludedFiles[i] = mergedExcludedFiles.get(i).getAbsoluteFile().toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(strTitle)

                .setMultiChoiceItems(excludedFiles, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems[which] = true;
                        } else if (mSelectedItems[which] == true) {
                            mSelectedItems[which] = false;
                        }
                    }
                }).setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                boolean shouldRemove = false;
                for (boolean selectedItem : mSelectedItems) {
                    if (selectedItem) {
                        shouldRemove = true;
                        break;
                    }
                }
                if (shouldRemove) {
                    for (int i = mSelectedItems.length - 1; i >= 0; i--)
                        if (mSelectedItems[i] == true)
                            switch (settings.getSelectedSearchDirectory()) {
                                case Settings.EXTERNAL_DIRECTORY:
                                    if (mViewPager.getCurrentItem() == 0)
                                        settings.getBiggestExternalExcludedHogFiles().remove(i);
                                    else
                                        settings.getSmallestExternalExcludedHogFiles().remove(i);

                                    break;
                                case Settings.ROOT_DIRECTORY:
                                    if (mViewPager.getCurrentItem() == 0)
                                        settings.getBiggestRootExcludedHogFiles().remove(i);
                                    else
                                        settings.getSmallestRootExcludedHogFiles().remove(i);

                                    break;
                            }

                    // FileIO.writeObject(settings,
                    // Constants.SETTINGS_FILE, path);

                    switch (settings.getSelectedSearchDirectory()) {
                        case Settings.EXTERNAL_DIRECTORY:
                            if ((biggestHogFiles.size()
                                    - settings.getBiggestExternalExcludedHogFiles().size() < settings.getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                refresh();
                            } else if ((smallestHogFiles.size()
                                    - settings.getSmallestExternalExcludedHogFiles().size() < settings.getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                refresh();
                            } else {
                                Log.i(TAG, "startFileSearch()");

                                for (FileListFragment fileListFragment : fileListFragments)
                                    if (fileListFragment != null)
                                        fileListFragment.startFileSearch();
                            }

                            break;
                        case Settings.ROOT_DIRECTORY:
                            if ((biggestHogFiles.size() - settings.getBiggestRootExcludedHogFiles().size() < settings
                                    .getIntFileCount())) {
                                Log.i(TAG, "Refresh on biggest files");

                                refresh();
                            } else if ((smallestHogFiles.size()
                                    - settings.getSmallestRootExcludedHogFiles().size() < settings.getIntFileCount())) {
                                Log.i(TAG, "Refresh on smallest files");

                                refresh();
                            } else {
                                Log.i(TAG, "startFileSearch()");
                                for (FileListFragment fileListFragment : fileListFragments)
                                    if (fileListFragment != null)
                                        fileListFragment.startFileSearch();
                            }
                            break;
                    }
                }
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onProgressUpdate(int percent) {
    }

    @Override
    public void onCancelled() {
    }

    @Override
    public void onPostExecute(List<FileInformation> biggestHogFiles, List<FileInformation> smallestHogFiles) {
        this.biggestHogFiles = biggestHogFiles;
        this.smallestHogFiles = smallestHogFiles;
        fileListFragments[0].updateAdapter(biggestHogFiles);
        fileListFragments[1].updateAdapter(smallestHogFiles);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.i(TAG, "SectionsPagerAdapter()");

            for (int i = 0; i < 2; i++) {
                fileListFragments[i] = new FileListFragment();
                Bundle args = new Bundle();
                args.putInt(FileListFragment.ARG_SECTION_NUMBER, i + 1);
                args.putBoolean(Constants.IS_BIGGEST_FILES, (i == 1 ? false : true));
                fileListFragments[i].setArguments(args);
                fileListFragments[i].setRetainInstance(true);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fileListFragments[position];
        }

        public FileListFragment getFragment(int key) {
            return (key == 1 ? fileListFragments[1] : fileListFragments[0]);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.Largest);
                case 1:
                    return getString(R.string.Smallest);
            }
            return null;
        }

    }

    private void checkVersion() {
        Log.i(TAG, "checkVersion() starting");
        Double tempVersion = (Double) FileIO.readObject(Constants.VERSION_FILE, path);
        if (tempVersion == null) {
            Log.i(TAG, "tempVersion == null");
            clearApplicationData();
            FileIO.writeObject(Constants.VERSION, Constants.VERSION_FILE, path);
        } else if (tempVersion != Constants.VERSION) {
            clearApplicationData();
            FileIO.writeObject(Constants.VERSION, Constants.VERSION_FILE, path);
        } else {
            Log.i(TAG, "tempVersion == VERSION");
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private void readHogFiles() {
        switch (settings.getSelectedSearchDirectory()) {
            case Settings.EXTERNAL_DIRECTORY:
                biggestHogFiles = (ArrayList<FileInformation>) FileIO.readObject(Constants.BIGGEST_EXTERNAL_HOGFILES, path);
                smallestHogFiles = (ArrayList<FileInformation>) FileIO.readObject(Constants.SMALLEST_EXTERNAL_HOGFILES, path);
                break;
            case Settings.ROOT_DIRECTORY:
                biggestHogFiles = (ArrayList<FileInformation>) FileIO.readObject(Constants.BIGGEST_ROOT_HOGFILES, path);
                smallestHogFiles = (ArrayList<FileInformation>) FileIO.readObject(Constants.SMALLEST_ROOT_HOGFILES, path);
                break;
            default:
                biggestHogFiles = null;
                smallestHogFiles = null;
        }
    }

    private AlertDialog.Builder buildReleaseOfLiabilityDialog() {
        AlertDialog.Builder liabilityBuilder = new AlertDialog.Builder(this);
        liabilityBuilder.setMessage(R.string.ReleaseOfLiability).setTitle("FileHog Release of Liability")
                .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String strOutput = "User agreed on " + DateFormat.getDateTimeInstance().format(new Date());
                        Log.i(TAG, "User agreed on " + strOutput);

                        FileIO.writeObject(strOutput, Constants.RELEASE_OF_LIABILITY_FILE, path);
                        needToRefreshList = settings.isOnOpenRefresh();
                        initalizeAndRefresh();

                    }
                }).setNegativeButton("I do not agree", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                releaseOfLiabilityDialog.dismiss();
                releaseOfLiabilityDialog = null;
                finish();
            }
        });
        return liabilityBuilder;
    }

    //ToDo
    //Fix
    private boolean hogFilesOutOfDate() {
        return true;//System.currentTimeMillis()// - settings.getTimeToDelayRefresh() > smallestHogFiles.getLngCreatedDate();
    }

    private void initalizeAndRefresh() {
        for (int i = 0; i < 2; i++) {
            fileListFragments[i] = new FileListFragment();
            Bundle args = new Bundle();
            args.putInt(FileListFragment.ARG_SECTION_NUMBER, i + 1);
            args.putBoolean(Constants.IS_BIGGEST_FILES, (i == 1 ? false : true));
            fileListFragments[i].setArguments(args);
            fileListFragments[i].setRetainInstance(true);
        }

        if (mSectionsPagerAdapter == null) {
            Log.i(TAG, "mSectionsPagerAdapter == null");
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        }

        // Set up the ViewPager with the sections adapter.
        if (mViewPager == null) {
            Log.i(TAG, "mViewPager == null");
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        refresh();
    }

    private void refresh() {
        fileListFragments[0].startFileSearch(this);
    }

    private void onClick_Settings() {
        Log.i(TAG, "settings.getResearchFrequency(): " + settings.getResearchFrequency());
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void onClick_About() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final TextView message = new TextView(MainActivity.this);

        final SpannableString s = new SpannableString("www.houseperez.com");
        Linkify.addLinks(s, Linkify.WEB_URLS);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setTextSize(20);

        builder.setTitle("About FileHog v " + Constants.VERSION);
        builder.setMessage("A product of HousePerez. " + "For more information, please visit us at: ");
        builder.setNeutralButton(R.string.Okay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close Dialog
                dialog.cancel();
            }
        });
        builder.setView(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
