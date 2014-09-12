package com.pract.plugin.util;

import java.io.File;
import java.io.FileFilter;

public class FileUtility {

	public static File[] getfilesInDirectory(File directory){
		return directory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
	}

	public static int countNumberOfFilesInDirectory(File directory){
		return getfilesInDirectory(directory).length;
	}

	public static File[] getSubDirectories(File directory){
		return directory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	public static int countNumberOfDirectoriesInDirectory(File directory){
		return getSubDirectories(directory).length;
	}

	public Boolean hasSubdirectories(final File directory) {
        return getSubDirectories(directory).length > 0;
    }

}
