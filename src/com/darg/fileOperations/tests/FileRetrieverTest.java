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
import java.util.ArrayList;

import org.junit.Test;

import com.darg.fileOperations.FileRetriever;
import com.darg.fileOperations.utils.FileSystemUtils;

public class FileRetrieverTest 
{
	//needed parameters..set them correctly for your system
	private String path = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/ret");
	private String emptyPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/empty"); //there should be some empty directories here
	private String zipPath = new String("/home/erhan/Desktop/TEST-FOR-THESIS/data/Untitled.zip");
	

	
	/** these 3 functions check whether the retrieveFiles works or not
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void retrieveTest() 
	{
		File file = null;
		FileRetriever fr = new FileRetriever();
		int count = 0;
		
		fr.retrieveFiles(path, null, true, true);
		
		while(fr.hasNext())
		{
			file = fr.next();
			
			System.out.println(file.getName());
			count++;
			
		}
		
		assertEquals(22, count);
		
		fail("temporary path should also be emptied. But it is not possible to control this automatically do it manually\n"
				+ "path" + FileSystemUtils.getDefaultSystemPath());
	}
	

	@Test
	public void retrieveWithoutDecompressTest()
	{
		File file = null;
		FileRetriever fr = new FileRetriever();
		int count = 0;
		
		fr.retrieveFiles(path, null, true, false);
		
		while(fr.hasNext())
		{
			file = fr.next();
			
			System.out.println(file.getName());
			count++;
			
		}
		
		assertEquals(8, count);
	}
	
	@Test
	public void retrieveWithoutRecursionTest()
	{
		File file = null;
		FileRetriever fr = new FileRetriever();
		int count = 0;
		
		fr.retrieveFiles(path, null, false, true);
		
		while(fr.hasNext())
		{
			file = fr.next();
			
			System.out.println(file.getName());
			count++;
			
		}
		
		assertEquals(6, count);
	}
	
	
	
	
	
	

	/**check whether the retrieveFiles works or not when it is given empty directory inside another empty directory
	 * should not give any exception
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void retrieveEmptyDirectoryTest() 
	{
		File file = null;
		FileRetriever fr = new FileRetriever();
		int count = 0;
		
		fr.retrieveFiles(emptyPath, null, true, true);
		
		while(fr.hasNext())
		{
			file = fr.next();
			
			System.out.println(file.getName());
			count++;
			
		}
		
		assertEquals(0, count);
	}
	
	
	
	
	/**check whether the filtering works or not
	 * 
	 * 
	 * @author erhan sezerer
	 * 
	 */
	@Test
	public void filterTest()
	{
		File file = null;
		int count = 0;
		
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("png");
		extensions.add("pdf");
		
		
		FileRetriever fr = new FileRetriever();
		fr.retrieveFiles(path, extensions, true, true);
		
		
		while(fr.hasNext())
		{
			file = fr.next();
			
			System.out.println(file.getName());
			count++;
		}
		
		assertEquals(8, count);
	}
	
	
	
	
	/**check whether extract method works correctly or not
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void extractTest()
	{
		FileRetriever fr = new FileRetriever();
		String path = new File(FileSystemUtils.getDefaultSystemPath() , "Untitled").getAbsolutePath();
		int count = 0;
		
		fr.extract(new File(zipPath), null);
		count = FileSystemUtils.getFileCount(path, true);
		
		assertEquals(10, count);	
	}

	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//FAIL SCENARIOS //////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**check if we can violate the singleton pattern
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test(expected = IllegalAccessError.class)
	public void singletonViolationTest() 
	{
		FileRetriever fr = new FileRetriever();
		
		fr.retrieveFiles(path, null, true, true);
		fr.retrieveFiles(path, null, true, true);
	}
	
	
	
}
