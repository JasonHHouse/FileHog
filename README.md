# FILEHOG

This code is copyright (c) 2017 Jason H House

## Requirements
Android Minimum Version 21
Gradle 3.3
Java 1.8

## Building
1. Pull down the source code
2. Run *gradle clean build*
3. Install apk on Android device

## History
##### v5.0
* Coming soon!

##### v4.11
* Fixed the about hyperlink
* Updated russian
* Updated thai
* Updated indonesian

##### v4.10
* Fixed the xxhdpi icon issue
* Updated spanish
* Updated german

##### v4.09
* Fixed copy to clipboard but showing view on dialog issue
* Fixed english to use strings.xml
* Updated french to use strings.xml

##### v4.06
* Fixed random crashing when loading files/swiping between
* Fixed refreshing of the listview causing crashes
* Removed listview size setting, always 50 items on each

##### v4.05
* Added releaseOfLiabilityDialog back in
* Removed size label, moves size value to bottom right
* Updated releaseOfLiabilityDialog layout
* Made releaseOfLiabilityDialog not cancelable so users must click it to use the app
* Updated title and buttons of releaseOfLiabilityDialog
* Fixed dialog not showing when home button is pressed then reopening the app
* Refresh now shows a circular loading bar when pressed

##### v4.04
* Speed up the file searching more
* Cut down on file searching memory usage
* Changed action bar text color to apps darkBlue
* Added more padding to file information labels and file information
* Right justified the file information labels

##### v4.03
* Speed up the file searching
* Fixed files not showing up

##### v4.02
* Changed from using runnable to use async task for searching for files
* Removed counting dialog
* Removed searching dialog

##### v4.01
* Updated listview items look and feel

##### v4.0
* Added FileInformationAdapter
* Added last modified information
* Split the folder and file name up in the list view
* Switched excluded files and refresh to icon
* Created listview item layout for better information presentation

##### v3.07
* Added Russian back in

##### v3.06
* Edited read and write file to close the streams
* Changed the number formatting for file size to fit international standards

##### v3.05
* Fixed the settings not updating issue

##### v3.04
* Made Settings into a singleton 
* Changed saving to not require context
* Changed FileListFragment to default constructor 
* and pass a bundle of information
* Fixed the on app killed, blank screen issue

##### v3.03
* Overhauled the rotation code to better check for and handle refreshes 

##### v3.02
* Fixed the refresh rotation issue

##### v3.01
* Fixed the rotation issue

##### v3.00
* Changed from single list view activity to pager with fragments 
* showing smallest and largest files.  This removes the need to use settings for smallest or largest files to be shown.
* Settings largest/smallest option has been removed

##### v2.07
* Language pack updated with Hindi, Korean, Polish, French, Russian

##### v2.06
* Fixed the seekbar not updating bug
* Made the app Holo after requests
* Fixed the updating issues

##### v2.05
* Fixed viewing issues of settings page
* Added Holo.Light
* Changed settings to Holo.Light styling
* Reduced refreshes

##### v2.04
* Added language pack supporting German, Spanish, Arabic, Portuguese, and English 
* Created Settings Activity to move many dialogs into one location

##### v2.03
* Added checks to properly close dialogs
* Added more threading checks
* Now keeping screen on during searching to allow for long searches to complete
* Refresh Frequency now shows selected refresh frequency in dialog
* Refresh Frequency dialog now visible onload to set the value easier
* Version check to make sure stored files are not out of date

##### v2.00
* shorten menu item names for more visibility
    * change file count -> file count
    * change directory -> directory
    * change file sorting -> file sort
* Count files now adjusted for excluded folders and files
* Created new dialog to show counting files
* Updated searching files dialog with file count
* Fixed the dialog disappearing on display darkening issue
* Fixed excluded files and file count issue
* Created excluded lists for each directory and sort
* Added automatic saving of file count, directory, sort and excluded files

##### v2.02
* Switched many if/else to switch case for speed boost
* Created new menu item Research Frequency
    * This allows users to set how often FileHog should refresh
    * This will limit unwanted refreshes
    * Default is set to daily

##### v2.01
* Read and write internally
* Objectized reading and writing for more generic usage
* Added Release Of Liability requirement

##### v1.09
* Fixed the sorting size dialog box showing up after refresh
* Hyper link added to about menu item
* Let menu items be visible in the action bar instead of always hidden
* Increased size of hidden stored files
* Removed refreshing to cached file list
    * Change file count
    * Remove file
    * Exclude file
* Fixed excluding duplicate files issue

##### v1.08
* Fixed the java.lang.IllegalArgumentException: View not attached to window manager
* Fixed the orientation progress bar issue
* Removed viewing file in folder, changed to copying location to clipboard
* Added exclude files feature
    * To exclude click on a file listed
    * To add the file back in the search click the excluded files menu item

##### v1.07
* Updated the percentage calculation
* Updated the ProgressDialog

##### v1.06
* Updated the progress bar to show more detailed search

##### v1.05
* Changed threading to help with crash issues
* Added ProgressDialog
* Changed icons and images

##### v1.04
* Revised and cleaned code and added generics
* Removed donate by making paid and free version
* Added dialog to change between root and external directory
* Added searching for the smallest files 

##### v1.03
* Fixed the java.lang.NumberFormatException
* Fixed selected file not usable after clicked view
* Updated settings options
* Updated settings - ChangeFileCount 
    * Make the number of files shown adjustable
* Update settings - About
    * Added about menu for more information
* Update settings - Refresh
    * Added refresh button
    
##### v1.02
* Fixed deleting files issue
* Fixed duplicate row issue

##### v1.01
* Running on separate thread from main to fix workload issues
* Delete files from the program as been added

##### v1.0
* Find the 10 biggest files in the external directory
* On click, open folder system intent or copy the file location to the clipboard