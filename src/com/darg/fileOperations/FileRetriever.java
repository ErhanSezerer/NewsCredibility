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
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

import com.darg.fileOperations.model.FileType;
import com.darg.fileOperations.model.FilteringException;
import com.darg.fileOperations.utils.FileSystemUtils;
import com.darg.utils.ArrayListUtils;



/** This Class Controls the whole package which is responsible for parsing and reading
 * a data-set for reaching articles, stored in various formats(.zip, .tar.gz vs.). 
 * It does its duty with a singleton method and by searching folders one after one and deleting the existing one
 * ,
 * 
 * @author erhan sezerer
 *
 * 
 * NOTE::  this class is not thread safe. Use thread only when decompress==true
 *
 */
public class FileRetriever implements Iterator<File>
{
	
	private final String temporaryFilePath;
	boolean recursiveSearch;
	boolean decompressFiles;
	private ArrayList<String> extensions;
	
	
	private boolean retrieverUsed;//this attribute work as a flag to make retrieveFiles() method singleton
								  //once that method is called, it will be closed for all the outside calls and opened for
								  //internal calls
								  //retrieverUsed = false -> calls from the other classes are ok
								  //retrieverUsed = true -> calls from inner class are ok
	
	private boolean clearBuffer;//this parameter will be used to signal when we need to clear the temporarily extracted folders
	
	
	private ArrayList<File> files;
	private long fileCount;
	
	private ArrayList<File> directories;
	private long directoryCount;
	
	private ArrayList<File> unknownFiles;
	private long unknownCount;
	

	
	//constructors
	public FileRetriever() 
	{
		files = new ArrayList<File>();
		directories = new ArrayList<File>();
		unknownFiles = new ArrayList<File>();
		extensions = new ArrayList<String>();
		
		fileCount = 0;
		directoryCount = 0;
		unknownCount = 0;
		
		temporaryFilePath = new String(new File(FileSystemUtils.getDefaultSystemPath(), "/temp_files").getAbsolutePath());
		retrieverUsed = false;
		recursiveSearch = false;
		decompressFiles = false;
		clearBuffer = false;
	}
	
	public FileRetriever(final String temporaryFilePath) 
	{
		files = new ArrayList<File>();
		directories = new ArrayList<File>();
		unknownFiles = new ArrayList<File>();
		extensions = new ArrayList<String>();
		
		fileCount = 0;
		directoryCount = 0;
		unknownCount = 0;
		
		this.temporaryFilePath = temporaryFilePath;
		retrieverUsed = false;
		recursiveSearch = false;
		decompressFiles = false;
		clearBuffer = false;
	}
	
	
	

	/**This functions extract the given directory and constructs the lists of files and directories
	 * for later use. 
	 * WARNING!!!: this method is a singleton method. if you want to call it multiple times you have to create another
	 * FileRetriever object.
	 * Or you can call it after all of the files&directories that are from the first call have processed 
	 * 
	 * 
	 * @param directory - obvious
	 * @param fileExtensions - a list of strings that contains the extensions we search for.
	 * 						   Extensions should be given without the dots
	 * 							If null,every file will be returned.
	 * 					   NOTE: if decompress is true, you don't need to give .zip and .tar.gz as extension,
	 * 					   since they are automatically decompressed.
	 * 					   ->if you want to find .zip file themselves, set decompress=false
	 * 
	 * @param recursive - this parameter will be used for parsing the directory.
	 * 					Sub-directories will be searched if this is true
	 * 
	 * @param decompress -  this parameter will be used for parsing the directory.
	 * 					Compressed files will be extracted and searched if this is true.
	 * 					!--Currently supports .zip and .tar.gz(.tgz) extensions as compressed files--!
	 * 					NOTE: if the recursive is set to false, this parameter will have no effect
	 * 
	 * @return a boolean for test issues, true if there is no error
	 * 
	 * 
	 * @exception IllegalAccessError - if this singleton method is called again it will throw an illegal access error
	 */
	
	//this function only works as a gate for calls from other classes to make retrieveFilesImpl() singleton
	public boolean retrieveFiles(final String directory, final ArrayList<String> fileExtensions, 
            					final boolean recursive, final boolean decompress) throws IllegalAccessError
    {
				boolean retVal = false;
				
		
				if(retrieverUsed)
				{
					throw new IllegalAccessError("Error: Singleton method is already in use!");
				}
				else
				{
					retrieverUsed = true;
					recursiveSearch = recursive;
					decompressFiles = decompress;
					extensions = fileExtensions;
					
					retVal = retrieveFilesImpl(directory);
				}
				
				
				return retVal;
    }
	
	
	
	//actual implementation of previous method
	private boolean retrieveFilesImpl(final String directory)
	{
		boolean retVal = true;
		
		
		try
		{
			File rootFile = new File(directory);
			String extension = FilenameUtils.getExtension(directory);
			
			
			
			if(!rootFile.exists())
			{
				throw new IOException("Directory Does Not Exist: " + directory);
			}
			else if(rootFile.isDirectory())
			{
				DirectoryFileLoader loader = new DirectoryFileLoader();
				loader.loadFiles(rootFile);
				
				//find their extension and types find and classify the correct ones
				if(!filterFiles(loader.getFiles()))
				{
					throw new FilteringException("Error when filtering the files");
				}
	
			}
			else if(extension.equalsIgnoreCase(FileType.GZ.toString().toLowerCase(Locale.ENGLISH)) || extension.equalsIgnoreCase(FileType.TGZ.toString().toLowerCase(Locale.ENGLISH)))
			{
				TARFileLoader loader = new TARFileLoader();
				loader.loadFiles(rootFile, temporaryFilePath);

				//find their extension and types find and classify the correct ones
				if(!filterFiles(loader.getFiles()))
				{
					throw new FilteringException("Error when filtering the files");
				}
				
				clearBuffer = true;
			}
			else if(extension.equalsIgnoreCase(FileType.ZIP.toString().toLowerCase(Locale.ENGLISH)))
			{
				ZIPFileLoader loader = new ZIPFileLoader();
				loader.loadFiles(rootFile, temporaryFilePath);

				//find their extension and types find and classify the correct ones
				if(!filterFiles(loader.getFiles()))
				{
					throw new FilteringException("Error when filtering the files");
				}
				
				clearBuffer = true;
			}
			else
			{
				throw new IOException("Unknown Format In Path: " + directory);
			}
				
				
				
		}
		catch(IOException io)
		{
			io.printStackTrace();
			retVal = false;
		}
		catch(FilteringException fe)
		{
			fe.printStackTrace();
			retVal = false;
		}
		
		
		
		return retVal;	
	}

	
	
	
	
	/**filters the files according to the extension and unknown formats and 
	 * returns Files that are expected by the user.
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param extensions - a list of strings that contains the extensions we search for.
	 * 					   If null,every file will be returned 
	 * 
	 * @param tempFiles - unfiltered files that are found in the zip file,
	 * 					  will be added to file if they are wanted, or to unwantedFiles if they are not
	 * 
	 * @return a boolean for test runs
	 */
	private boolean filterFiles(final ArrayList<File> tempFiles)
	{
		boolean retVal = true;
		File tempFile = null;
		String extensionOfFile = null;
		long tempFilesCount = 0;
		
		
		try
		{
				tempFilesCount = tempFiles.size();
			
			
				for (int i=0; i<tempFilesCount; i++)
				{
					tempFile = tempFiles.get(i);
					
					if(!tempFile.exists())
					{
						throw new FilteringException("Error when filtering the files:"
														+ "Non-existing file found: " + tempFile.getName());
					}
					
					extensionOfFile = FilenameUtils.getExtension(tempFile.getName()).toLowerCase(Locale.ENGLISH);
						
					
					if(tempFile.isDirectory())
					{
						directories.add(tempFile);	
						directoryCount++;
					}
					else if(FileType.isCompressed(tempFile))
					{
						if(decompressFiles && recursiveSearch)
						{
							directories.add(tempFile);
							directoryCount++;
						}
						else
						{
							files.add(tempFile);
							fileCount++;
						}
					}
					else if(extensions == null)
					{
						files.add(tempFile);
						fileCount++;
					}
					else if(extensions != null)
					{
						ArrayList<String> tempFileExtentions = extensions;
						int count = tempFileExtentions.size();
						
						for (int j=0; j<count; j++)
						{
							tempFileExtentions.set(j, tempFileExtentions.get(j).toLowerCase(Locale.ENGLISH));
						}
						
						
						if(ArrayListUtils.containsIgnoresCase(tempFileExtentions, extensionOfFile))
						{
							files.add(tempFile);
							fileCount++;
						}
						else
						{
							unknownFiles.add(tempFile);
							unknownCount++;
						}
					}
				
					
	
				}
				
		}
		catch(FilteringException fe)
		{
			fe.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}




	
	
	
	/**checks whether there are more files to iterate or not.
	 * Will return false when an exception occurs or there are no files remaining
	 * 
	 * If the extensions are set, then unwanted files does not count towards having another item
	 * 
	 * 
	 *WARNING: do not call it until the file is read, or you may lose it because this function
	 *may delete the decompressed files after called.
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if there are no other files remaining
	 */
	@Override
	public synchronized boolean hasNext()
	{
		boolean retVal = false;
		
		
		try
		{
				if(fileCount != 0)
				{
					retVal = true;
				}
				else if(directoryCount != 0 && recursiveSearch)
				{
					File tempFile = directories.remove(0);
					directoryCount--;
					
					//if we have remaining files from the previous extraction that
					//needs deleting delete them first before creating others
					if(clearBuffer)
					{
						FileSystemUtils.deleteDirectory(temporaryFilePath);
						clearBuffer = false;
					}
					
					//parse the document
					retrieveFilesImpl(tempFile.getCanonicalPath());
					
					
					//now check again if we have a file to return
					//we might have to parse a couple of times until it finds a file
					//there can be directories inside other directories
					//if there is an empty directory it will return a false in the end
					retVal = hasNext();
	
				}
				
				
				
				//there are no files left
				if(retVal == false)
				{
					
					//since there are no files left make retrieveFiles() usable again
					resetUsable();
					
					//reset all the remaining data in case this class is used again.
					//if extensions are set there might be some data left in the unknown files list
					resetData();
					
					//if we have remaining files from the previous extraction that
					//needs deleting, delete them first before creating others
					if(clearBuffer)
					{
						FileSystemUtils.deleteDirectory(temporaryFilePath);
						clearBuffer = false;
					}
				}

		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}
	
	
	
	
	
	
	/**returns the next file in the retriever.
	 * If there are no files remaining returns null.
	 * In case an IOException occurs also returns null.
	 * 
	 * DO NOT CALL IT WITHOUT USING HASNEXT()!!!
	 * It will return null and result in an exception
	 * 
	 * @author erhan sezerer
	 *
	 * @return file - next file instance
	 */
	@Override
	public synchronized File next()
	{
		File retFile = null;
		
		
		//if we still have files get one
		if(fileCount != 0)
		{
			retFile = files.remove(0);
			fileCount--;
		}
		//if we don't have anything then return null
		else
		{
			retFile = null;
		}
		
		
		//no need to check for directories here 
		//since the hasNext() method already checked and parsed if there are any
		
		
		return retFile;
	}
	
	
	
	
	
	/**Extracts the given compresed file.
	 * Currently supports .zip and .tar.gz
	 * 
	 * @author erhan sezerer
	 *
	 * @param compressedFile - an instance to the compressed file
	 * @param destination - destination path to extract, 
	 * 						if destination is set to null, extracts it to a default path
	 * 
	 * 
	 * @return boolean - true if the extraction is successfull
	 */
	public boolean extract(final File compressedFile, final String destination)
	{
		boolean retVal = true;
		String path = destination;
		
		
		try
		{
				if(!FileType.isCompressed(compressedFile))
				{
					retVal = false;
				}
				else
				{
					//if it is a tar.gz file
					if(FilenameUtils.getExtension(compressedFile.getName()).equalsIgnoreCase(FileType.GZ.name().toLowerCase(Locale.ENGLISH))
						|| FilenameUtils.getExtension(compressedFile.getName()).equalsIgnoreCase(FileType.TGZ.name().toLowerCase(Locale.ENGLISH)))
					{
						TARFileLoader loader = new TARFileLoader();
						
						if(path == null)
						{
							path = FileSystemUtils.getDefaultSystemPath();
						}
						
						retVal = loader.loadFiles(compressedFile, path);
					}
					else if(FilenameUtils.getExtension(compressedFile.getName()).equalsIgnoreCase(FileType.ZIP.name().toLowerCase(Locale.ENGLISH)))
					{
						ZIPFileLoader loader = new ZIPFileLoader();
						
						if(path == null)
						{
							path = FileSystemUtils.getDefaultSystemPath();
						}
						
						retVal = loader.loadFiles(compressedFile, path);
					}
				}
		}
		catch(InvalidPathException ipe)
		{
			ipe.printStackTrace();
			retVal = false;
		}
				
		
		return retVal;
	}
	
	
	
	
	
	


	
	///////////////////////////////////////////////////////////////////////////////////////////
	//setters and getters
	///////////////////////////////////////////////////////////////////////////////////////////
	/**Clears all the data this class currently holds
	 * 
	 * @author erhan sezerer
	 *
	 * @return
	 */
	public boolean resetData()
	{
		boolean retVal = true;
		
		files.clear();
		directories.clear();
		unknownFiles.clear();
		
		fileCount = 0;
		directoryCount = 0;
		unknownCount = 0;
		
		return retVal;
	}
	
	
	
	
	/**changes the availability of the singleton method retrieveFiles().
	 * It is private to make sure that it cannot be tempered with from outside the class
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if the reset is successful
	 */
	private boolean resetUsable()
	{
		boolean retVal = false;
		
		if(fileCount == 0 && directoryCount == 0)
		{
			retrieverUsed = false;
		}
		
		return retVal;
	}
	
	
	
	
	
	@Override
	public String toString()
	{
		String retVal = new String("");
		
		for (int i=0; i<fileCount; i++)
		{
			retVal += new String("\tFile :" + i + "\n");
		}
		
		for (int i=0; i<directoryCount; i++)
		{
			retVal += new String("\tDirectory :" + i + "\n");
		}
		
		for (int i=0; i<unknownCount; i++)
		{
			retVal += new String("\tUnknown File :" + i + "\n");
		}
		
		return retVal;
	}
	
	
	
	
	
	/**Not recommended to be used without calling hasNext()
	 * 
	 * @author erhan sezerer
	 *
	 * @return
	 */
	public synchronized ArrayList<File> getFiles() 
	{
		ArrayList<File> tempFiles = new ArrayList<File>(files);
		files.clear();
		fileCount = 0;
		
		return tempFiles;
	}
	
	/**Not recommended to be used without calling hasNext()
	 * 
	 * @author erhan sezerer
	 *
	 * @return
	 */
	public synchronized ArrayList<File> getDirectories()
	{
		ArrayList<File> tempDirectories = new ArrayList<File>(directories);
		directories.clear();
		directoryCount = 0;

		return tempDirectories;
	}
	
	public boolean isRetrieverUsable()
	{
		return !retrieverUsed;
	}
	
	public synchronized long getFileCount() 
	{
		return fileCount;
	}

	public synchronized long getDirectoryCount() 
	{
		return directoryCount;
	}

	public synchronized long getUnknownCount() 
	{
		return unknownCount;
	}

	public ArrayList<File> getUnknownFiles() 
	{
		return unknownFiles;
	}

	public String getTemporaryFile_Path() 
	{
		return temporaryFilePath;
	}

	public boolean isRecursiveSearch() 
	{
		return recursiveSearch;
	}

	public boolean isDecompressFiles() 
	{
		return decompressFiles;
	}
	
	
	
}
