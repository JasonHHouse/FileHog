package com.houseperez.util;

import java.io.Serializable;

/**
 * Created by jhouse on 4/18/2014.
 */
public class FileInformation implements Serializable, Comparable<FileInformation> {
    private String name;
    private String folder;
    private double size;
    private long lastModified;

    public FileInformation() {
    }

    public FileInformation(String name, String folder, double size, long lastModified) {
        this.name = name;
        this.folder = folder;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public int compareTo(FileInformation another) {
        return (int) ( another.size - this.size);
    }


}
