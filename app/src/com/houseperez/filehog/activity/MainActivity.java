/*
 * File:        MainActivity.java
 * Created:     12/7/2012
 * Author:      Jason House
 * Description: Find the 10 biggest files in the external directory
 * 				On click, open folder system intent or copy the file location to the clipboard
 * 
 * This code is copyright (c) 2012 HousePerez
 */

package com.houseperez.filehog.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private final int FRAGMENT_LIST_SIZE = 4;

    // Globals
    private List<FileInformation> biggestExternalHogFiles;
    private List<FileInformation> smallestExternalHogFiles;
    private List<FileInformation> biggestRootHogFiles;
    private List<FileInformation> smallestRootHogFiles;
    private AlertDialog releaseOfLiabilityDialog;
    private Settings settings;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FileListFragment[] fileListFragments;
    private static File path;
    private RefreshAsync refreshAsync;
    //private int currentPage;

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

        fileListFragments = new FileListFragment[FRAGMENT_LIST_SIZE];
        settings = Settings.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (FileIO.readObject(Constants.RELEASE_OF_LIABILITY_FILE, path) == null) {
            releaseOfLiabilityDialog = buildReleaseOfLiabilityDialog().create();
            releaseOfLiabilityDialog.setCanceledOnTouchOutside(false);
            releaseOfLiabilityDialog.show();
        } else {
            settings.setOnOpenRefresh(false);
            initalizeAndRefresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

        if (releaseOfLiabilityDialog != null) {
            releaseOfLiabilityDialog.dismiss();
        }
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
            case R.id.ExcludedFiles:
                onClick_ExcludeFiles();

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
        List<FileInformation> mergedExcludedFiles = new ArrayList<FileInformation>();

        switch (settings.getSelectedSearchDirectory()) {
            case Settings.EXTERNAL_DIRECTORY:

                if (mViewPager.getCurrentItem() == 0) {
                    mergedExcludedFiles.addAll(settings.getBiggestExternalExcludedHogFiles());
                } else {
                    mergedExcludedFiles.addAll(settings.getSmallestExternalExcludedHogFiles());
                }
                break;
            case Settings.ROOT_DIRECTORY:
                if (mViewPager.getCurrentItem() == 0) {
                    mergedExcludedFiles.addAll(settings.getBiggestRootExcludedHogFiles());
                } else {
                    mergedExcludedFiles.addAll(settings.getSmallestRootExcludedHogFiles());
                }
                break;
        }

        String[] excludedFiles = new String[mergedExcludedFiles.size()];

        final boolean[] mSelectedItems = new boolean[mergedExcludedFiles.size()];

        for (int i = 0; i < mergedExcludedFiles.size(); i++) {
            excludedFiles[i] = mergedExcludedFiles.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ExcludedFiles)

                .setMultiChoiceItems(excludedFiles, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems[which] = true;
                        } else if (mSelectedItems[which] == true) {
                            mSelectedItems[which] = false;
                        }
                    }
                }).setPositiveButton(R.string.Remove, new DialogInterface.OnClickListener() {
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


                    Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                    refresh();
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
    public void onPostExecute(List<FileInformation> biggestHogFiles, List<FileInformation> smallestHogFiles, boolean isRoot) {
        if (!isRoot) {
            this.biggestExternalHogFiles = biggestHogFiles;
            this.smallestExternalHogFiles = smallestHogFiles;
            fileListFragments[0].updateAdapter(biggestHogFiles);
            fileListFragments[1].updateAdapter(smallestHogFiles);
        } else {
            this.biggestRootHogFiles = biggestHogFiles;
            this.smallestRootHogFiles = smallestHogFiles;
            fileListFragments[2].updateAdapter(biggestHogFiles);
            fileListFragments[3].updateAdapter(smallestHogFiles);
        }
        //fileListFragments[currentPage].setListShown(true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.i(TAG, "SectionsPagerAdapter()");

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fileListFragments[position].setHogFiles(biggestExternalHogFiles);
                    break;
                case 1:
                    fileListFragments[position].setHogFiles(smallestExternalHogFiles);
                    break;
                case 2:
                    fileListFragments[position].setHogFiles(biggestRootHogFiles);
                    break;
                case 3:
                    fileListFragments[position].setHogFiles(smallestRootHogFiles);
                    break;
            }
            return fileListFragments[position];
        }

        @Override
        public int getCount() {
            return FRAGMENT_LIST_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.LargestExternal);
                case 1:
                    return getString(R.string.SmallestExternal);
                case 2:
                    return getString(R.string.LargestInternal);
                case 3:
                    return getString(R.string.SmallestInternal);
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

    private AlertDialog.Builder buildReleaseOfLiabilityDialog() {
        View view = getLayoutInflater().inflate(R.layout.release_of_liability_dialog, null);

        AlertDialog.Builder liabilityBuilder = new AlertDialog.Builder(this);
        liabilityBuilder.setTitle(R.string.ReleaseofLiability);
        liabilityBuilder.setView(view);
        liabilityBuilder.setCancelable(false);
        liabilityBuilder.setPositiveButton(R.string.IAgree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String strOutput = "User agreed on " + DateFormat.getDateTimeInstance().format(new Date());
                Log.i(TAG, "User agreed on " + strOutput);

                FileIO.writeObject(strOutput, Constants.RELEASE_OF_LIABILITY_FILE, path);
                //needToRefreshList = settings.isOnOpenRefresh();
                initalizeAndRefresh();

            }
        });
        liabilityBuilder.setNegativeButton(R.string.IDontAgree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                releaseOfLiabilityDialog.dismiss();
                releaseOfLiabilityDialog = null;
                finish();
            }
        });
        return liabilityBuilder;
    }

    private void initalizeAndRefresh() {
        for (int i = 0; i < fileListFragments.length; i++) {
            fileListFragments[i] = new FileListFragment(getApplicationContext(), new ArrayList<FileInformation>());
            Bundle args = new Bundle();
            args.putInt(FileListFragment.ARG_SECTION_NUMBER, i + 1);
            args.putBoolean(Constants.IS_BIGGEST_FILES, (i % 2 == 0 ? true : false));
            args.putBoolean(Constants.IS_ROOT, (i > 1 ? true : false));
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
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //currentPage = position;
                    //fileListFragments[position].setListShown(true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        refresh();
    }

    public void refresh() {
        refreshAsync = new RefreshAsync(this, true);
        refreshAsync.execute(Environment.getRootDirectory());

        refreshAsync = new RefreshAsync(this, false);
        refreshAsync.execute(Environment.getExternalStorageDirectory());
    }

    private void onClick_About() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.about_layout, null);
        TextView txtAbout = (TextView) view.findViewById(R.id.txtAbout);
        TextView txtLink = (TextView) view.findViewById(R.id.txtLink);

        SpannableString s = new SpannableString("http://jasonhhouse.weebly.com/");
        Linkify.addLinks(s, Linkify.WEB_URLS);
        txtLink.setText(s);
        txtLink.setMovementMethod(LinkMovementMethod.getInstance());

        txtAbout.setText(R.string.AboutMessage);

        builder.setTitle(getString(R.string.About) + " v" + Constants.VERSION);
        builder.setView(view);
        builder.setNeutralButton(R.string.Okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close Dialog
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
