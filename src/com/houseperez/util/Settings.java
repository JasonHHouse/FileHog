package com.houseperez.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.houseperez.filehog.MainActivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Settings implements Serializable, Parcelable {

	public static final String TAG = "Settings";

	// Constants
	public static final int EXTERNAL_DIRECTORY = 0;
	public static final int ROOT_DIRECTORY = 1;

	public static final int ALWAYS = 0;
	public static final int HOURLY = 1;
	public static final int DAILY = 2;
	public static final int WEEKLY = 3;
	public static final int BI_WEEKLY = 4;

	public static final int ENGLISH = 0;
	public static final int ARABIC = 1;
	public static final int GERMAN = 2;
	public static final int SPANISH = 3;
	public static final int INDONESIAN = 4;
	public static final int PORTUGUESE = 5;

	public static final int HOUR_IN_MILLI = 3600000;
	public static final int DAY_IN_MILLI = 86400000;
	public static final int WEEK_IN_MILLI = 604800000;
	public static final int BI_WEEK_IN_MILLI = 1209600000;

	public static final boolean BIGGEST_FILES = true;
	public static final boolean SMALLEST_FILES = false;

	private static final long serialVersionUID = -1334344095817501479L;

	// Global
	private static Settings instance = null;
	private ArrayList<File> biggestExternalExcludedHogFiles;
	private ArrayList<File> smallestExternalExcludedHogFiles;
	private ArrayList<File> biggestRootExcludedHogFiles;
	private ArrayList<File> smallestRootExcludedHogFiles;
	private int intFileCount;
	private int selectedSearchDirectory;
	private long timeToDelayRefresh;
	private boolean onOpenRefresh;
	private int researchFrequency;
	private int intRotation;

	private Settings(ArrayList<File> biggestExternalExcludedHogFiles,
			ArrayList<File> smallestExternalExcludedHogFiles,
			ArrayList<File> biggestRootExcludedHogFiles,
			ArrayList<File> smallestRootExcludedHogFiles, int intFileCount,
			int selectedSearchDirectory, long timeToDelayRefresh,
			boolean onOpenRefresh, int researchFrequency, int intRotation) {
		super();
		this.biggestExternalExcludedHogFiles = biggestExternalExcludedHogFiles;
		this.smallestExternalExcludedHogFiles = smallestExternalExcludedHogFiles;
		this.biggestRootExcludedHogFiles = biggestRootExcludedHogFiles;
		this.smallestRootExcludedHogFiles = smallestRootExcludedHogFiles;
		this.intFileCount = intFileCount;
		this.selectedSearchDirectory = selectedSearchDirectory;
		this.timeToDelayRefresh = timeToDelayRefresh;
		this.onOpenRefresh = onOpenRefresh;
		this.researchFrequency = researchFrequency;
		this.intRotation = intRotation;
	}

	public static Settings getInstance(int intRotation) {
		if (instance == null) {

			if ((instance = (Settings) FileIO.readObject(
					Constants.SETTINGS_FILE, MainActivity.getFilePath())) == null) {
				Log.i(TAG, "FileIO.readSettings(): null");
				instance = new Settings(new ArrayList<File>(0),
						new ArrayList<File>(0), new ArrayList<File>(0),
						new ArrayList<File>(0), Constants.STARTING_FILE_COUNT,
						Settings.EXTERNAL_DIRECTORY, Settings.DAY_IN_MILLI,
						false, Settings.DAILY, intRotation);
				FileIO.writeObject(instance, Constants.SETTINGS_FILE,
						MainActivity.getFilePath());
			} else {
				Log.i(TAG, "FileIO.readSettings(): object");
			}
			Log.i(TAG, "instance null");

		} else
			Log.i(TAG, "instance not null");
		return instance;
	}

	public ArrayList<File> getBiggestExternalExcludedHogFiles() {
		return biggestExternalExcludedHogFiles;
	}

	public void setBiggestExternalExcludedHogFiles(
			ArrayList<File> biggestExternalExcludedHogFiles) {
		this.biggestExternalExcludedHogFiles = biggestExternalExcludedHogFiles;
	}

	public ArrayList<File> getSmallestExternalExcludedHogFiles() {
		return smallestExternalExcludedHogFiles;
	}

	public void setSmallestExternalExcludedHogFiles(
			ArrayList<File> smallestExternalExcludedHogFiles) {
		this.smallestExternalExcludedHogFiles = smallestExternalExcludedHogFiles;
	}

	public ArrayList<File> getBiggestRootExcludedHogFiles() {
		return biggestRootExcludedHogFiles;
	}

	public void setBiggestRootExcludedHogFiles(
			ArrayList<File> biggestRootExcludedHogFiles) {
		this.biggestRootExcludedHogFiles = biggestRootExcludedHogFiles;
	}

	public ArrayList<File> getSmallestRootExcludedHogFiles() {
		return smallestRootExcludedHogFiles;
	}

	public void setSmallestRootExcludedHogFiles(
			ArrayList<File> smallestRootExcludedHogFiles) {
		this.smallestRootExcludedHogFiles = smallestRootExcludedHogFiles;
	}

	public int getIntFileCount() {
		return intFileCount;
	}

	public void setIntFileCount(int intFileCount) {
		this.intFileCount = intFileCount;
	}

	public int getSelectedSearchDirectory() {
		return selectedSearchDirectory;
	}

	public void setSelectedSearchDirectory(int selectedSearchDirectory) {
		this.selectedSearchDirectory = selectedSearchDirectory;
	}

	public long getTimeToDelayRefresh() {
		return timeToDelayRefresh;
	}

	public void setTimeToDelayRefresh(long timeToDelayRefresh) {
		this.timeToDelayRefresh = timeToDelayRefresh;
	}

	public boolean isOnOpenRefresh() {
		return onOpenRefresh;
	}

	public void setOnOpenRefresh(boolean onOpenRefresh) {
		this.onOpenRefresh = onOpenRefresh;
	}

	public int getResearchFrequency() {
		return researchFrequency;
	}

	public void setResearchFrequency(int researchFrequency) {
		this.researchFrequency = researchFrequency;
	}

	public int getIntRotation() {
		return intRotation;
	}

	public void setIntRotation(int intRotation) {
		this.intRotation = intRotation;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(biggestExternalExcludedHogFiles);
		dest.writeSerializable(smallestExternalExcludedHogFiles);
		dest.writeSerializable(biggestRootExcludedHogFiles);
		dest.writeSerializable(smallestRootExcludedHogFiles);
		dest.writeInt(intFileCount);
		dest.writeInt(selectedSearchDirectory);
		dest.writeLong(timeToDelayRefresh);
		dest.writeByte((byte) (onOpenRefresh ? 1 : 0));
		dest.writeInt(researchFrequency);
		dest.writeInt(intRotation);
	}

	public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>() {
		public Settings createFromParcel(Parcel in) {
			return new Settings(in);
		}

		public Settings[] newArray(int size) {
			return new Settings[size];
		}
	};

	@SuppressWarnings("unchecked")
	private Settings(Parcel in) {
		biggestExternalExcludedHogFiles = (ArrayList<File>) in
				.readSerializable();
		smallestExternalExcludedHogFiles = (ArrayList<File>) in
				.readSerializable();
		biggestRootExcludedHogFiles = (ArrayList<File>) in.readSerializable();
		smallestRootExcludedHogFiles = (ArrayList<File>) in.readSerializable();
		intFileCount = in.readInt();
		selectedSearchDirectory = in.readInt();
		timeToDelayRefresh = in.readLong();
		onOpenRefresh = in.readByte() == 1;
		researchFrequency = in.readInt();
		intRotation = in.readInt();
	}
}
