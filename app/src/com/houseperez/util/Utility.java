package com.houseperez.util;

import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

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

    private static boolean isInExcludedHogFiles(File file, Set<FileInformation> excludedHogFiles) {
        for (FileInformation fileInformation : excludedHogFiles) {
            String excludedFile = fileInformation.getFolder() + File.separator + fileInformation.getName();
            if (excludedFile.equals(file.getAbsoluteFile().toString())) {
                Log.i(TAG, "Inside isInExcludedHogFiles if");
                return true;
            }
        }
        return false;
    }

    public static void searchFiles(File folder, List<FileInformation> hogFiles) {
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (file.isFile()
                        && ((Settings.getInstance().getSelectedSearchDirectory() == Settings.EXTERNAL_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, Settings.getInstance().getBiggestExternalExcludedHogFiles()))
                        || (Settings.getInstance().getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, Settings.getInstance().getSmallestExternalExcludedHogFiles()))
                        || (Settings.getInstance().getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, Settings.getInstance().getBiggestRootExcludedHogFiles())) || (Settings.getInstance()
                        .getSelectedSearchDirectory() == Settings.ROOT_DIRECTORY && !Utility
                        .isInExcludedHogFiles(file, Settings.getInstance().getSmallestRootExcludedHogFiles())))) {

                    FileInformation fileInformation = new FileInformation();
                    fileInformation.setName(file.getName());
                    fileInformation.setSize(file.length());
                    fileInformation.setLastModified(file.lastModified());
                    fileInformation.setFolder(file.getParentFile().getAbsolutePath());

                    hogFiles.add(fileInformation);

                } else {
                    // Recurse directories
                    if (file != null && file.exists() && file.canRead() && !file.isHidden())
                        searchFiles(file, hogFiles);
                }
            }
        }
    }

}
