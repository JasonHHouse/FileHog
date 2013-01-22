package com.houseperez.filehog;

public class Resources {

	private String sorting[];
	private String directoryOptions[];
	private String alertDialogItems[];
	private String researchFrequencyItems[];
	private String strAbout;
	private String strFileCount;
	private String strExcludedFiles;
	private String strDirectory;
	private String strFileSort;
	private String strCancel;
	private String strRefresh;
	private String strOkay;
	private String strChangeDirectoryDialog;
	private String strChangeFileSortingDialog;
	private String strSearching;
	private String strReleaseOfLiability;
	private String strResearchFrequency;

	public Resources(String[] sorting, String[] directoryOptions,
			String[] alertDialogItems, String[] researchFrequencyItems,
			String strAbout, String strFileCount, String strExcludedFiles,
			String strDirectory, String strFileSort, String strCancel,
			String strRefresh, String strOkay, String strChangeDirectoryDialog,
			String strChangeFileSortingDialog, String strSearching,
			String strReleaseOfLiability, String strResearchFrequency) {
		super();
		this.sorting = sorting;
		this.directoryOptions = directoryOptions;
		this.alertDialogItems = alertDialogItems;
		this.researchFrequencyItems = researchFrequencyItems;
		this.strAbout = strAbout;
		this.strFileCount = strFileCount;
		this.strExcludedFiles = strExcludedFiles;
		this.strDirectory = strDirectory;
		this.strFileSort = strFileSort;
		this.strCancel = strCancel;
		this.strRefresh = strRefresh;
		this.strOkay = strOkay;
		this.strChangeDirectoryDialog = strChangeDirectoryDialog;
		this.strChangeFileSortingDialog = strChangeFileSortingDialog;
		this.strSearching = strSearching;
		this.strReleaseOfLiability = strReleaseOfLiability;
		this.strResearchFrequency = strResearchFrequency;
	}

	public String[] getSorting() {
		return sorting;
	}

	public void setSorting(String[] sorting) {
		this.sorting = sorting;
	}

	public String[] getDirectoryOptions() {
		return directoryOptions;
	}

	public void setDirectoryOptions(String[] directoryOptions) {
		this.directoryOptions = directoryOptions;
	}

	public String[] getAlertDialogItems() {
		return alertDialogItems;
	}

	public void setAlertDialogItems(String[] alertDialogItems) {
		this.alertDialogItems = alertDialogItems;
	}

	public String[] getResearchFrequencyItems() {
		return researchFrequencyItems;
	}

	public void setResearchFrequencyItems(String[] researchFrequencyItems) {
		this.researchFrequencyItems = researchFrequencyItems;
	}

	public String getStrAbout() {
		return strAbout;
	}

	public void setStrAbout(String strAbout) {
		this.strAbout = strAbout;
	}

	public String getStrFileCount() {
		return strFileCount;
	}

	public void setStrFileCount(String strFileCount) {
		this.strFileCount = strFileCount;
	}

	public String getStrExcludedFiles() {
		return strExcludedFiles;
	}

	public void setStrExcludedFiles(String strExcludedFiles) {
		this.strExcludedFiles = strExcludedFiles;
	}

	public String getStrDirectory() {
		return strDirectory;
	}

	public void setStrDirectory(String strDirectory) {
		this.strDirectory = strDirectory;
	}

	public String getStrFileSort() {
		return strFileSort;
	}

	public void setStrFileSort(String strFileSort) {
		this.strFileSort = strFileSort;
	}

	public String getStrCancel() {
		return strCancel;
	}

	public void setStrCancel(String strCancel) {
		this.strCancel = strCancel;
	}

	public String getStrRefresh() {
		return strRefresh;
	}

	public void setStrRefresh(String strRefresh) {
		this.strRefresh = strRefresh;
	}

	public String getStrOkay() {
		return strOkay;
	}

	public void setStrOkay(String strOkay) {
		this.strOkay = strOkay;
	}

	public String getStrChangeDirectoryDialog() {
		return strChangeDirectoryDialog;
	}

	public void setStrChangeDirectoryDialog(String strChangeDirectoryDialog) {
		this.strChangeDirectoryDialog = strChangeDirectoryDialog;
	}

	public String getStrChangeFileSortingDialog() {
		return strChangeFileSortingDialog;
	}

	public void setStrChangeFileSortingDialog(String strChangeFileSortingDialog) {
		this.strChangeFileSortingDialog = strChangeFileSortingDialog;
	}

	public String getStrSearching() {
		return strSearching;
	}

	public void setStrSearching(String strSearching) {
		this.strSearching = strSearching;
	}

	public String getStrReleaseOfLiability() {
		return strReleaseOfLiability;
	}

	public void setStrReleaseOfLiability(String strReleaseOfLiability) {
		this.strReleaseOfLiability = strReleaseOfLiability;
	}

	public String getStrResearchFrequency() {
		return strResearchFrequency;
	}

	public void setStrResearchFrequency(String strResearchFrequency) {
		this.strResearchFrequency = strResearchFrequency;
	}

}
