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
import java.util.ArrayList;

import com.darg.fileOperations.model.EntryContainer;
import com.darg.fileOperations.model.ExtractedFileAttributes;
import com.darg.fileOperations.utils.TarFileExtractor;



/**this class handles the jobs concerning opening, extracting and retrieving files from the tar files
 * 
 * @author erhan sezerer
 *
 */
public class TARFileLoader implements CompressedFileLoader
{
	private ArrayList<File> files;
	private ArrayList<EntryContainer> entries;
	private ExtractedFileAttributes attribute;
	
	private long entryCount;
	private long fileCount;
	
	
	
	
	//constructors/////////////////////////////////////////////////
	public TARFileLoader(final ExtractedFileAttributes attribute) 
	{
		files = new ArrayList<File>();
		entries = new ArrayList<EntryContainer>();
		this.attribute = attribute; 
		fileCount = 0;
		entryCount = 0;
	}

	public TARFileLoader()
	{
		files = new ArrayList<File>();
		entries = new ArrayList<EntryContainer>();
		attribute = ExtractedFileAttributes.NONE;
		fileCount = 0;
		entryCount = 0;
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/**finds and retrieves all the documents/files in the given .tar file
	* 
	* 
	* @author erhan sezerer
	* 
	* @param file - file to extract and parse
	* @param destination - a path for temporarily extract the files in tar
	* 
	* @return a boolean for test runs.
	* 			True if there is no error
	* 
	*/
	@Override
	public boolean loadFiles(final File file, final String destination)
	{
		boolean retVal = false;
		File tempF = null;
		ArrayList<File> tempFiles = TarFileExtractor.unTarAndGetFiles(file, destination, true);
		
		
		if(tempFiles != null)
		{
			for (int i=0; i<tempFiles.size(); i++) 
			{
				tempF = tempFiles.get(i);
				files.add(tempF);
				
				if(attribute == ExtractedFileAttributes.AUTO_DELETE_FILES_AFTER_EXTRACTION)
				{
					tempF.deleteOnExit();
				}
				
				fileCount++;
			}
			
			retVal = true;
		}
		
		
		return retVal;
	}
	
	
	
	
	/**finds and retrieves all entries in the given .tar file. 
	 * Does not work recursively
	 * 
	 * 
	 * @author erhan sezerer
	 * 
	 * @param file - file to extract and parse
	 * @param destination - a path for temporarily extract the files in tar
	 * 
	 * @return a boolean for test runs.
	 * 			true if there is no error
	 *
	 */
	@Override
	public boolean loadFiles(final File file)
	{
		boolean retVal = false;
		ArrayList<EntryContainer> tempFiles = TarFileExtractor.getTarEntries(file);
		
		if(tempFiles != null)
		{
			for (int i=0; i<tempFiles.size(); i++) 
			{
				entries.add(tempFiles.get(i));
				entryCount++;
			}
			
			retVal = true;
		}
		
	
		return retVal;
	}
	
	
	
	
	
	
	/**finds and extracts all files in the given .tar.gz file
	 * 
	 * 
	 * @author erhan sezerer
	 * 
	 * @param file - file to extract and parse
	 * @param destination - a path for temporarily extract the files in tar
	 * @param recursive - if true untars compressed files inside this tar
	 * 						(see FileType for supported compressed types)
	 * 
	 * @return a boolean for test runs.
	 * 			true if there is no error
	 * 
	 */
	@Override
	public boolean extractFiles(final File file, final String destination, final boolean recursive)
	{
		boolean retVal = false;
		
		if(TarFileExtractor.unTar(file, destination, recursive))
		{
			retVal = true;
		}
		
		return retVal;
	}

	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//setters and getters and overridden methods
	////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**clears all of the loaded files from list.
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Override
	public void resetFiles()
	{
		files.clear();
		fileCount = 0;
	}
	@Override
	public void resetEntries()
	{
		entries.clear();
		entryCount = 0;
	}
	
	
	
	@Override
	public String toString()
	{
		String retVal;
		
		if(fileCount == 0)
		{
			retVal = new String("No Files Found In ZIP File");
		}
		else
		{
			retVal = new String("Files in ZIP:\n");
			
			for(int i=0; i<fileCount; i++)
			{
				retVal += new String("\t" + files.get(i).getName() + "\n");
			}
		}
		
		
		return retVal;
	}
	
	
	
	
	@Override
	public ExtractedFileAttributes getAttribute() 
	{
		return attribute;
	}
	
	@Override
	public void setAttribute(final ExtractedFileAttributes attribute) 
	{
		this.attribute = attribute;
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

	@Override
	public ArrayList<EntryContainer> getEntries() 
	{
		return entries;
	}

	@Override
	public long getEntryCount() 
	{

		return entryCount;
	}

	
}
