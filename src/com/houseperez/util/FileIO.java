package com.houseperez.util;

/*
 * File:        FileIO.java
 * Created:     12/7/2012
 * Author:      Jason House
 * Description: Find the 10 biggest files in the external directory
 * 				On click, open folder system intent or copy the file location to the clipboard
 * 
 * This code is copyright (c) 2012 HousePerez
 * 
 * History:
 * Revision v 2.0
 * 
 * Read and write the file settings
 * 
 * Revision v 2.01
 * 
 * Read and write internally
 * Objectized reading and writing for more generic usage
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileIO {

	private static final String TAG = "FileIO";

	public static void writeFile(String strOutput, String strFile) {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, strFile);
		if (strOutput.length() > 0) {
			try {
				if (root.canWrite()) {
					FileWriter filewriter = new FileWriter(file);
					BufferedWriter out = new BufferedWriter(filewriter);
					out.write(strOutput);
					out.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "Could not write file " + e.getMessage());
			}

		}
	}
	
	public static String getSearchFolder(int selectedSearchDirectory) {
		if (selectedSearchDirectory == Settings.ROOT_DIRECTORY)
			return Environment.getRootDirectory().getAbsolutePath();
		else
			return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static void writeObject(Object obj, Context ctx, String strFile) {
		try {
			Log.i(TAG, "Writing new " + strFile);
			FileOutputStream fout = ctx.openFileOutput(strFile,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(obj);
			oos.close();
		} catch (Exception e) {
			Log.w(TAG, "Could not write file " + e.getMessage());
		}
	}

	public static Object readObject(Context ctx, String strFile) {
		Object obj = null;
		try {
			Log.i(TAG, "Reading " + strFile);
			FileInputStream fin = ctx.openFileInput(strFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			obj = ois.readObject();
			ois.close();

		} catch (FileNotFoundException e) {
			Log.w(TAG, "Could not find " + strFile);
		} catch (Exception e) {
			Log.e(TAG, "Could not read " + strFile);
		}
		return obj;
	}
	
	public static boolean deleteObject(Context ctx, String strFile) {
		try {
			Log.i(TAG, "Deleting " + strFile);
			return ctx.deleteFile(strFile);

		} catch (Exception e) {
			Log.e(TAG, "Could not delete " + strFile);
		}
		return false;
	}
}
