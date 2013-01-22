/*
 * File:		Pair.java
 * Created:     12/7/2012
 * Author:      Jason House
 * Description: Creates an object for comparable on the size of the file but also contains the file itself
 * 
 * This code is copyright (c) 2012 HousePerez
 *  
 * History:
 * Revision v 1.0
 * 
 * Created class
 * 
 * Revision v 1.01
 * 
 * Added generics to the Comparable
 * 
 */

package com.houseperez.util;

import java.io.File;
import java.io.Serializable;

public class Pair implements Comparable<Pair>, Serializable  {

	private static final long serialVersionUID = 5455292743124294559L;
	private double size;
	private File file;
	
	public Pair(double size, File file) {
		super();
		this.size = size;
		this.file = file;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public int compareTo(Pair pair) {
		Double d = pair.getSize();

		if (size < d)
			return 1;
		else if (size == d)
			return 0;
		else
			return -1;

	}
}
