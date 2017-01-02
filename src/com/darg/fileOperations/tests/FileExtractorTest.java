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
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.darg.fileOperations.model.EntryContainer;
import com.darg.fileOperations.utils.TarFileExtractor;
import com.darg.fileOperations.utils.ZipFileExtractor;
import com.darg.fileOperations.utils.FileSystemUtils;

public class FileExtractorTest 
{
	//needed parameters..set them correctly for your system
	private String destinationPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/");
	private String zipPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/Untitled.zip");
	private String extractionPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/Untitled"); //it should direct to the extracted files
																								//the last directory name should be equal to the zip file name
	private String tarPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/Untitled.tar.gz");
	
	private String invalidPathPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/datam/zart.zip"); //there sould be an invalid path here
	private String invalidFilePath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/Untitled 1.ppt"); //there should be a valid path but an invalid file here
	
	
	//////////////////////////////////////////////////////////////////////
	//.ZIP TESTS /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	/**checks whether we can get the files non-recursively from a tar file
	 * without retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unZipTest() 
	{
		
		File zipFile = new File(zipPath);
		
		
		if(new File(extractionPath).exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		ZipFileExtractor.unZip(zipFile, destinationPath, false);
		
		assertEquals(6, FileSystemUtils.getFileCount(extractionPath, true));
	}
	
	/**checks whether we can get the files recursively from a tar file
	 * without retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unZipRecursiveTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		
		
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		
		ZipFileExtractor.unZip(zipFile, destinationPath, true);
		
		assertEquals(10, FileSystemUtils.getFileCount(extractionPath, true));
	}
	
	
	
	/**checks whether we can get the files recursively from a zip file
	 * while retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unZipAndGetRecursiveTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		ArrayList<File> files = ZipFileExtractor.unZipAndGetFiles(zipFile, destinationPath, true);
		
		assertEquals(8, files.size());
	}
	
	
	
	/**checks whether we can retrieve the entries from a zip file correctly
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void getEntryTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		ArrayList<EntryContainer> files = ZipFileExtractor.getZipEntries(zipFile);
		
		assertEquals(5, files.size());
	}
	
	
	
	
	/**checks whether we can get the file we search for from a zip file
	 * while retrieving the file
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void searchFileZipTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		String fileName = new String("tez_proposal_model(3)");
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		
		File searchedFile = ZipFileExtractor.searchFileFromZip(zipFile, destinationPath, fileName, true);		
		assertEquals(FilenameUtils.removeExtension(searchedFile.getName()), fileName);
	}
	
	
	
	/**checks whether we can get the files in some particular directory recursively from a zip file
	 * while retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void searchAllFilesZipTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		String fileName = new String("asd/");
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		
		ArrayList<File> searchedFiles = ZipFileExtractor.searchAllFilesFromZip(zipFile, destinationPath, fileName, true);		
		assertEquals(2, searchedFiles.size());
	}
	
	
	
	
	/**checks whether we can get a particular file from a zip file
	 * while retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void getFileZipTest() 
	{
		File file = new File(extractionPath);
		
		File zipFile = new File(zipPath);
		
		String fileName = new String("asd/fdfdf");
		
		
		if(file.exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		
		File searchedFile = ZipFileExtractor.getFileFromZip(zipFile, destinationPath, fileName);		
		assertEquals(FilenameUtils.removeExtension(searchedFile.getName()), "fdfdf");
	}
	
	
	
	
	

	
	
	//////////////////////////////////////////////////////////////////////
	//.TAR.GZ TESTS //////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	/**checks whether we can get the files non-recursively from a tar file
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unTarTest() 
	{
		
		File zipFile = new File(tarPath);
		
		
		
		if(new File(extractionPath).exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		TarFileExtractor.unTar(zipFile, destinationPath, false);
		
		assertEquals(6, FileSystemUtils.getFileCount(extractionPath, true));
	}
	
	
	/**checks whether we can get the files recursively from a tar file
	 * without retrieving the files
	 * 
	 * @author erhan
	 *
	 */
	@Test
	public void unTarRecursiveTest() 
	{		
	
	File zipFile = new File(tarPath);
	
	
	if(new File(extractionPath).exists())
	{
		FileSystemUtils.deleteDirectory(extractionPath);
	}
	
	TarFileExtractor.unTar(zipFile, destinationPath, true);
	
	assertEquals(10, FileSystemUtils.getFileCount(extractionPath, true));
		
	}

	
	
	
	/**checks whether we can get the files recursively from a tar file
	 * while retrieving the files
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unTarAndGetRecursiveTest() 
	{
		
		File zipFile = new File(tarPath);
		
		
		if(new File(extractionPath).exists())
		{
			FileSystemUtils.deleteDirectory(extractionPath);
		}
		
		
		ArrayList<File> files = TarFileExtractor.unTarAndGetFiles(zipFile, destinationPath, true);
		
		assertEquals(8, files.size());
	}
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// FAIL SCENARIOS		/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	//Currently does not work because those exception are caught in the methods called!!!
	//either change the scenarios or make those functions throw the exceptions instead catching it
	/**test for invalid path
	 * for method unZipAndGetFiles()
	 * 
	 * @author erhan sezerer
	 */
	@Test(expected = InvalidPathException.class)
	public void zipInvalidPathTest()
	{
		File file2 = new File(invalidPathPath);
		
		ZipFileExtractor.unZipAndGetFiles(file2, destinationPath, true);
	}
	
	
	
	
	/**test for invalid file.
	 * should fail for non-zip files
	 * for method unZipAndGetFiles()
	 * 
	 * @author erhan sezerer
	 */
	@Test(expected = IOException.class)
	public void zipInvalidFileTest()
	{
		File file2 = new File(invalidFilePath);

		ZipFileExtractor.unZipAndGetFiles(file2, destinationPath, true);
	}
	
	
	
	/**should fail since the path does not exist
	 * * for method unTarAndGetFiles()
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test(expected = InvalidPathException.class)
	public void tarInvalidPathTest()
	{
		File file2 = new File(invalidPathPath);
		
		TarFileExtractor.unTarAndGetFiles(file2, destinationPath, true);
	}
	
	
	
	
	/**checks whether it can handle invalid files correctly
	 * for method unTarAndGetFiles()
	 * 
	 * @author erhan sezerer
	 */
	@Test(expected = IOException.class)
	public void tarInvalidFileTest()
	{
		File file2 = new File(invalidFilePath);

		TarFileExtractor.unTarAndGetFiles(file2, destinationPath, true);
	}
	
	
	
	/**should fail since the path does not exist
	 * * for method unZip()
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test(expected = InvalidPathException.class)
	public void zipInvalidPathTest2()
	{
		File file2 = new File(invalidPathPath);
		
		ZipFileExtractor.unZip(file2, destinationPath, true);
	}
	
	
	
	
	/**
	 * test for invalid file.
	 * should fail for non-zip files
	 * * for method unZip()
	 * 
	 * @author erhan sezerer
	 */
	@Test(expected = IOException.class)
	public void zipInvalidFileTest2()
	{
		File file2 = new File(invalidFilePath);
		
		ZipFileExtractor.unZip(file2, destinationPath, true);
	}
	
	
	
	
	/**should fail since the path does not exist
	 * * for method unTar()
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test(expected = InvalidPathException.class)
	public void tarInvalidPathTest2()
	{
		File file2 = new File(invalidPathPath);
		
		TarFileExtractor.unTar(file2, destinationPath, true);
	}
	
	
	
	
	/**
	 * test for invalid file.
	 * should fail for non-tar files
	 * * for method unTar()
	 * 
	 * 
	 * @author erhan sezerer
	 */
	@Test(expected = IOException.class)
	public void tarInvalidFileTest2()
	{
		File file2 = new File(invalidFilePath);

		TarFileExtractor.unTar(file2, destinationPath, true);
	}
}
