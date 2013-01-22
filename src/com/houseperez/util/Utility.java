package com.houseperez.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import android.util.Log;

public class Utility {

	public static final String TAG = "Utility";
	
	// Constants
	private static final double KILOBYTES = 1024;
	private static final double MEGABYTES = KILOBYTES * KILOBYTES;
	private static final double GIGABYTES = MEGABYTES * KILOBYTES;

	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
		return Double.valueOf(twoDForm.format(d));
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

	public static boolean isInExcludedHogFiles(File file,
			ArrayList<File> excludedHogFiles) {
		for (File excludedFile : excludedHogFiles) {
			if (excludedFile.getAbsoluteFile().toString()
					.equals(file.getAbsoluteFile().toString())) {
				Log.i(TAG, "Inside isInExcludedHogFiles if");
				return true;
			}
		}
		return false;
	}
}
