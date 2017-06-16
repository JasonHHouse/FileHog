package com.houseperez.filehog.util;

import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;

public class Utility {

    private static final String TAG = "Utility";

    // Constants
    private static final double KILOBYTES = 1024;
    private static final double MEGABYTES = KILOBYTES * KILOBYTES;
    private static final double GIGABYTES = MEGABYTES * KILOBYTES;

    private static String roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        try {
            return twoDForm.parse(twoDForm.format(d)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCorrectByteSize(double size) {
        if (size / KILOBYTES < 1)
            return roundTwoDecimals(size) + " B";
        else if (size / MEGABYTES < 1)
            return roundTwoDecimals(size / KILOBYTES) + " KB";
        else if (size / GIGABYTES < 1)
            return roundTwoDecimals(size / MEGABYTES) + " MB";
        else
            return roundTwoDecimals(size / GIGABYTES) + " GB";
    }

    public static void searchFiles(File folder, List<FileInformation> hogFiles) {
        Log.d(TAG, "searchFiles(" + folder + ")");
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (file != null) {
                    if (file.isFile()) {

                        FileInformation fileInformation = new FileInformation();
                        fileInformation.setName(file.getName());
                        fileInformation.setSize(file.length());
                        fileInformation.setLastModified(file.lastModified());
                        fileInformation.setFolder(file.getParentFile().getAbsolutePath());

                        Log.d(TAG, fileInformation.getName());

                        hogFiles.add(fileInformation);
                    } else {
                        // Recurse directories
                        if (file.exists() && file.canRead() && !file.isHidden())
                            searchFiles(file, hogFiles);
                    }
                }
            }
        }
    }

}
