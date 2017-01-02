/*
 * Written by Erhan Sezerer
 * Contact <erhansezerer@iyte.edu.tr> for comments and bug reports
 *
 * Copyright (C) 2014 Erhan Sezerer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package com.darg.fileOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class DirectoryFileLoader implements FileLoader
{
	private ArrayList<File> files;
	private long fileCount;
	
	
	//constructor(s)
	public DirectoryFileLoader()
	{	
		files = new ArrayList<File>();
		fileCount = 0;
	}
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * finds and loads all the documents/files in the given directory
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - contains the path to search for
	 * @return boolean for test issues
	 * 
	 * 	 */
	@Override
	public boolean loadFiles(final File file)
	{
		boolean retVal = true;
		int count = 0;
		File[] fileArray;
		
		try
		{
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			
			fileArray = file.listFiles();
			count = fileArray.length;
			
			for(int i=0; i<count; i++)
			{
				files.add(fileArray[i]);
				fileCount++;
			}
			
			
		}
		catch(InvalidPathException ipe)
		{
			System.out.println(ipe.getReason() + "\n" + ipe.getInput());
			ipe.printStackTrace();
			fileCount = 0;
			retVal = false;
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			fileCount = 0;
			retVal = false;
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			fileCount = 0;
			retVal = false;
		}

		
		
		return retVal;
	}
		
		
		
		
		
		
		
		
	/**
	 * finds and loads all the documents/files in the given directory.
	 *  This time searches recursively
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - contains the path to search for
	 * @param extension - desired file types to filter the search. 
	 * 						If null all files will be returned
	 * 
	 * @return boolean for test issues
	 * 
	 */
	public boolean loadFilesRecursive(final File file, final String[] extensions)
	{
		boolean retVal = false;
		Iterator<File> fileIterator;
		
		try
		{
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			
			fileIterator = FileUtils.iterateFiles(file, extensions, true);
			
			while(fileIterator.hasNext())
			{
				files.add(fileIterator.next());
				fileCount++;
			}
			
			
		}
		catch(InvalidPathException ipe)
		{
			System.out.println(ipe.getReason() + "\n" + ipe.getInput());
			ipe.printStackTrace();
			fileCount = 0;
			retVal = false;
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			fileCount = 0;
			retVal = false;
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
			fileCount = 0;
			retVal = false;
		}
		
		
		
		return retVal;
	}

	
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	//setters getters and other overridden methods
	@Override
	public void resetFiles()
	{
		files.clear();
		fileCount = 0;
	}
	
	
	@Override
	public String toString()
	{
		String retVal;
		
		if(fileCount == 0)
		{
			retVal = new String("No Files Found In Directory");
		}
		else
		{
			retVal = new String("Files:\n");
			
			for(int i=0; i<fileCount; i++)
			{
				retVal += new String("\t" + files.get(i).getName() + "\n");
			}
		}
		
		
		return retVal;
	}
	


	@Override
	public ArrayList<File> getFiles()
	{
		return files;
	}

	@Override
	public long getFileCount() 
	{
		return fileCount;
	}

	
}
























