package com.houseperez.filehog.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.houseperez.filehog.MainActivity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    private Set<FileInformation> biggestExternalExcludedHogFiles;
    private Set<FileInformation> smallestExternalExcludedHogFiles;
    private Set<FileInformation> biggestRootExcludedHogFiles;
    private Set<FileInformation> smallestRootExcludedHogFiles;
    private int intFileCount;
    private int selectedSearchDirectory;
    private long timeToDelayRefresh;
    private boolean onOpenRefresh;
    private int researchFrequency;

    private Settings(Set<FileInformation> biggestExternalExcludedHogFiles,
                     Set<FileInformation> smallestExternalExcludedHogFiles,
                     Set<FileInformation> biggestRootExcludedHogFiles,
                     Set<FileInformation> smallestRootExcludedHogFiles, int intFileCount,
                     int selectedSearchDirectory, long timeToDelayRefresh,
                     boolean onOpenRefresh, int researchFrequency) {
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
        FileIO.readObject(Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public static Settings getInstance() {
        if (instance == null) {

            if ((instance = (Settings) FileIO.readObject(
                    Constants.SETTINGS_FILE, MainActivity.getFilePath())) == null) {
                Log.i(TAG, "FileIO.readSettings(): null");
                instance = new Settings(new HashSet<FileInformation>(),
                        new HashSet<FileInformation>(), new HashSet<FileInformation>(),
                        new HashSet<FileInformation>(), Constants.STARTING_FILE_COUNT,
                        Settings.EXTERNAL_DIRECTORY, Settings.DAY_IN_MILLI,
                        false, Settings.DAILY);
                FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                        MainActivity.getFilePath());
            } else {
                Log.i(TAG, "FileIO.readSettings(): object");
            }
            Log.i(TAG, "instance null");

        } else {
            Log.i(TAG, "instance not null");
        }
        return instance;
    }

    public Set<FileInformation> getBiggestExternalExcludedHogFiles() {
        return biggestExternalExcludedHogFiles;
    }

    public void setBiggestExternalExcludedHogFiles(
            Set<FileInformation> biggestExternalExcludedHogFiles) {
        this.biggestExternalExcludedHogFiles = biggestExternalExcludedHogFiles;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public Set<FileInformation> getSmallestExternalExcludedHogFiles() {
        return smallestExternalExcludedHogFiles;
    }

    public void setSmallestExternalExcludedHogFiles(
            Set<FileInformation> smallestExternalExcludedHogFiles) {
        this.smallestExternalExcludedHogFiles = smallestExternalExcludedHogFiles;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public Set<FileInformation> getBiggestRootExcludedHogFiles() {
        return biggestRootExcludedHogFiles;
    }

    public void setBiggestRootExcludedHogFiles(
            Set<FileInformation> biggestRootExcludedHogFiles) {
        this.biggestRootExcludedHogFiles = biggestRootExcludedHogFiles;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public Set<FileInformation> getSmallestRootExcludedHogFiles() {
        return smallestRootExcludedHogFiles;
    }

    public void setSmallestRootExcludedHogFiles(
            Set<FileInformation> smallestRootExcludedHogFiles) {
        this.smallestRootExcludedHogFiles = smallestRootExcludedHogFiles;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public int getIntFileCount() {
        return intFileCount;
    }

    public void setIntFileCount(int intFileCount) {
        this.intFileCount = intFileCount;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public int getSelectedSearchDirectory() {
        return selectedSearchDirectory;
    }

    public void setSelectedSearchDirectory(int selectedSearchDirectory) {
        this.selectedSearchDirectory = selectedSearchDirectory;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public long getTimeToDelayRefresh() {
        return timeToDelayRefresh;
    }

    public void setTimeToDelayRefresh(long timeToDelayRefresh) {
        this.timeToDelayRefresh = timeToDelayRefresh;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public boolean isOnOpenRefresh() {
        return onOpenRefresh;
    }

    public void setOnOpenRefresh(boolean onOpenRefresh) {
        this.onOpenRefresh = onOpenRefresh;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }

    public int getResearchFrequency() {
        return researchFrequency;
    }

    public void setResearchFrequency(int researchFrequency) {
        this.researchFrequency = researchFrequency;
        FileIO.writeObject(instance, Constants.SETTINGS_FILE,
                MainActivity.getFilePath());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable((Serializable) biggestExternalExcludedHogFiles);
        dest.writeSerializable((Serializable) smallestExternalExcludedHogFiles);
        dest.writeSerializable((Serializable) biggestRootExcludedHogFiles);
        dest.writeSerializable((Serializable) smallestRootExcludedHogFiles);
        dest.writeInt(intFileCount);
        dest.writeInt(selectedSearchDirectory);
        dest.writeLong(timeToDelayRefresh);
        dest.writeByte((byte) (onOpenRefresh ? 1 : 0));
        dest.writeInt(researchFrequency);
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
        biggestExternalExcludedHogFiles = (Set<FileInformation>) in
                .readSerializable();
        smallestExternalExcludedHogFiles = (Set<FileInformation>) in
                .readSerializable();
        biggestRootExcludedHogFiles = (Set<FileInformation>) in.readSerializable();
        smallestRootExcludedHogFiles = (Set<FileInformation>) in.readSerializable();
        intFileCount = in.readInt();
        selectedSearchDirectory = in.readInt();
        timeToDelayRefresh = in.readLong();
        onOpenRefresh = in.readByte() == 1;
        researchFrequency = in.readInt();
    }
}
