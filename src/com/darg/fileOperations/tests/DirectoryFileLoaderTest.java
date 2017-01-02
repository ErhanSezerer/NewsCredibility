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

import com.darg.fileOperations.DirectoryFileLoader;

public class DirectoryFileLoaderTest 
{
	
	//needed parameters..set them correctly for your system
	private String path = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data");
	
	
	
	
	/**checks whether it can retrieve the correct number of documents
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void loadFromDirectoryTest() 
	{
		DirectoryFileLoader loader = new DirectoryFileLoader();
		
		File file = new File(path);
		loader.loadFiles(file);
		
		assertEquals(13, loader.getFileCount());
	}
	
	
	
	/**checks whether it can retrieve the correct number of documents
	 * this time recursively
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void loadFromDirectoryRecursiveTest() 
	{
		DirectoryFileLoader loader = new DirectoryFileLoader();
		
		File file = new File(path);
		loader.loadFilesRecursive(file, null);
		
		assertEquals(20, loader.getFileCount());
	}

}
