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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.houseperez.filehog.FileInfoAdapter;
import com.houseperez.filehog.R;
import com.houseperez.filehog.SearchService;
import com.houseperez.util.Constants;
import com.houseperez.util.FileIO;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Settings;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    private static File path;
    private final int FRAGMENT_LIST_SIZE = 4;
    // Globals
    private AlertDialog releaseOfLiabilityDialog;
    private Settings settings;
    private RecyclerView recyclerView;

    public static File getFilePath() {
        return path;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate()");

        path = getDir("obj", Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        checkVersion();

        settings = Settings.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollbarFadingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        if (FileIO.readObject(Constants.RELEASE_OF_LIABILITY_FILE, path) == null) {
            releaseOfLiabilityDialog = buildReleaseOfLiabilityDialog().create();
            releaseOfLiabilityDialog.setCanceledOnTouchOutside(false);
            releaseOfLiabilityDialog.show();
        } else {
            settings.setOnOpenRefresh(false);
            initializeAndRefresh();
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
            default:
                return super.onOptionsItemSelected(item);
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
                initializeAndRefresh();

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

    private void initializeAndRefresh() {
        FileInfoAdapter fileInfoAdapter = new FileInfoAdapter(new ArrayList<FileInformation>());
        recyclerView.setAdapter(fileInfoAdapter);

        refresh();
    }

    public void refresh() {
        Log.d(TAG, "refresh()");
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SearchService.class);
        intent.putExtra("FILE", Environment.getExternalStorageDirectory().toString());
        startService(intent);
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
