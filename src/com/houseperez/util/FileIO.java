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
 * 
 * Revision v 3.06
 * 
 * Edited read and write file to close the streams
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileIO {

	private static final String TAG = "FileIO";

	public static String getSearchFolder(int selectedSearchDirectory) {
		if (selectedSearchDirectory == Settings.ROOT_DIRECTORY)
			return Environment.getRootDirectory().getAbsolutePath();
		else
			return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static void writeObject(Object obj, String strFile, File path) {
		Log.i(TAG, "Writing new " + strFile);
		path.mkdirs(); // create folders where write files
		File filePath = new File(path, strFile);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(obj);
		} catch (Exception e) {
			Log.w(TAG, "Could not write file " + e.getMessage());
		} finally {
			try {
				if (fout != null)
					fout.close();
				if (oos != null)
					oos.close();
				fout = null;
				oos = null;
			} catch (IOException e) {
				Log.e(TAG, "Could not close output stream " + e.getMessage());
			}
		}
	}

	public static Object readObject(String strFile, File path) {
		Object obj = null;
		Log.i(TAG, "Reading " + strFile);
		File filePath = new File(path, strFile);
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {
			fin = new FileInputStream(filePath);
			ois = new ObjectInputStream(fin);
			obj = ois.readObject();
		} catch (FileNotFoundException e) {
			Log.w(TAG, "Could not find " + strFile);
		} catch (Exception e) {
			Log.e(TAG, "Could not read " + strFile);
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (ois != null)
					ois.close();
				fin = null;
				ois = null;
			} catch (IOException e) {
				Log.e(TAG, "Could not close input stream " + e.getMessage());
			}
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
