/*
 * Written by Erhan Sezerer
 * Contact <erhansezerer@iyte.edu.tr> for comments and bug reports
 *
 * Copyright (C) 2014 Erhan Sezerer
 *
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.darg.fileOperations.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.darg.fileOperations.ZIPFileLoader;
import com.darg.fileOperations.model.ExtractedFileAttributes;

/**
 *Test cases for the FileLoaderFromZipFile class
 *
 * @author erhan sezerer
 *
 *
 */
public class ZIPFileLoaderTest 
{
	//needed parameters..set them correctly for your system
	private String destinationPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/");
	private String zipPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/Untitled.zip");
	
	
	
	
	
	
	/**
	 * test whether we can get accurate number of files from the zip file or not
	 * 
	 * @author erhan sezerer
	 * 
	 */
	@Test
	public void ziploaderTest()
	{
		ZIPFileLoader loader = new ZIPFileLoader(ExtractedFileAttributes.NONE);
		
		File file = new File(zipPath);
		loader.loadFiles(file, destinationPath);
		
		assertEquals(8, loader.getFileCount());
	}
	
	
	
	/**check whether we can read the correct number of entries from a zip file
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void zipEntryLoaderTest()
	{
		ZIPFileLoader loader = new ZIPFileLoader(ExtractedFileAttributes.NONE);
		
		File file2 = new File(zipPath);
		loader.loadFiles(file2);
		assertEquals(5, loader.getEntryCount());
	}
	

	
	/**check whether auto delete works or not
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void autoDeleteTest()
	{
		ZIPFileLoader loader = new ZIPFileLoader(ExtractedFileAttributes.AUTO_DELETE_FILES_AFTER_EXTRACTION);
		File file = new File(zipPath);
		loader.loadFiles(file, destinationPath);
		
		
		fail("Cannot be done with a jUnit test check whether this works or not manually"
				+ "\n P.S: deletes the files but cannot delete the directories");
	}
	
}
