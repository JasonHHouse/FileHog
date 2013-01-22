package com.houseperez.util;

import java.io.Serializable;
import java.util.ArrayList;

public class HogFileList implements Serializable {
	private static final long serialVersionUID = 4475963439268553031L;
	private ArrayList<Pair> hogFiles;
	private long lngCreatedDate;
	
	public HogFileList(ArrayList<Pair> hogFiles, long lngCreatedDate) {
		super();
		this.hogFiles = hogFiles;
		this.lngCreatedDate = lngCreatedDate;
	}

	public ArrayList<Pair> getHogFiles() {
		return hogFiles;
	}

	public void setHogFiles(ArrayList<Pair> hogFiles) {
		this.hogFiles = hogFiles;
	}

	public long getLngCreatedDate() {
		return lngCreatedDate;
	}

	public void setLngCreatedDate(long lngCreatedDate) {
		this.lngCreatedDate = lngCreatedDate;
	}

}
