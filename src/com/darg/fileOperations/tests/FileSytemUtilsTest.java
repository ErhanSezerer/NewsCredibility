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

import com.darg.fileOperations.utils.FileSystemUtils;
import com.darg.fileOperations.utils.FileSystemUtils.OSType;

public class FileSytemUtilsTest {
	
	
	//needed parameters..set them correctly for your system
	private String workspacePath = new String("/home/erhan/HERŞEY/eclipse projects");
	private String projectPath = new String("/home/erhan/HERŞEY/eclipse projects/NewsStory");
	
	private String directory = new String("/home/erhan/Desktop/mkdir/");
	private String directoryFile = new String("/home/erhan/Desktop/mkdir/temp.txt");
	private String defaultPath = new String("/home/erhan");
	private String forCount = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/");
	
	

	@Test
	public void OperatingSytemTest() 
	{
		assertEquals(FileSystemUtils.getOperatingSystemType(), OSType.LINUX);
	}
	
	@Test
	public void getUserTest()
	{
		assertEquals("erhan", FileSystemUtils.getUserName());
	}
	
	
	@Test
	public void workspacePathTest() 
	{
		assertEquals(workspacePath, FileSystemUtils.getWorkspacePath());
	}

	
	
	@Test
	public void projectPathTest() 
	{
		assertEquals(projectPath, FileSystemUtils.getProjectPath());
	}

	
	
	@Test
	public void mkdirsTest() 
	{
		FileSystemUtils.mkdirs(new File(directoryFile));
		assertEquals(true, new File(directory).exists());
		
		
		FileSystemUtils.mkdirs(directoryFile);	
		assertEquals(true, new File(directory).exists());
	}

	
	
	@Test
	public void deleteDirectoryTest() 
	{
		FileSystemUtils.mkdirs(directoryFile);
		assertEquals(true, FileSystemUtils.deleteDirectory(directory));
		assertEquals(false, new File(directory).exists());
	}

	
	
	@Test
	public void getFileCountTest() 
	{
		assertEquals(20, FileSystemUtils.getFileCount(forCount, true));
		assertEquals(10, FileSystemUtils.getFileCount(forCount, false));
	}


	@Test
	public void getFolderCountTest() 
	{
		assertEquals(8, FileSystemUtils.getFolderCount(forCount, true));
		assertEquals(3, FileSystemUtils.getFolderCount(forCount, false));
	}
	

	
	@Test
	public void getSystemPathTest()
	{
		assertEquals(defaultPath, FileSystemUtils.getDefaultSystemPath());
	}
}
