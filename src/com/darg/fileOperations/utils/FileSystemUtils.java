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
package com.darg.fileOperations.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Locale;

import org.apache.commons.io.FileUtils;




/**
 * @author erhan sezerer
 * 
 * 
 * a static-like class for handling file system properties
 *
 */
public final class FileSystemUtils 
{
	public enum OSType{WINDOWS,LINUX};
	
	
	
	
	///////////////////////////////////////////////////////////////
	//prevent users from calling the constructor
	private FileSystemUtils()
	{
		
	}
	
	
	
	
	
	
	
	/**
	 * this function finds and enumerates the type of operating system that runs this JVM
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 *
	 * @return type of Operating system that this JVM runs on. 
	 * 		   Null if type of Operating System is unknown
	 */
	public static final OSType getOperatingSystemType()
	{
		OSType os;
		String type = new String(System.getProperty("os.name")).toLowerCase(Locale.ENGLISH);
		
		
		
		if(type.startsWith("windows"))
		{
			os = OSType.WINDOWS;
		}
		else if(type.equalsIgnoreCase("linux"))
		{
			os = OSType.LINUX;
		}
		else
		{
			os = null;
		}
		
		
		return os;
	}
	
	
	
	/**returns the user name that currently logged in to the operating system
	 * 
	 * @author erhan sezerer
	 *
	 * @return
	 */
	public static final String getUserName()
	{
		return System.getProperty("user.name");
	}
	
	
	
	
	
	/**this functions returns the absolute path of the project that uses this code
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return String - an absolute path to the project
	 */
	public static final String getProjectPath()
	{
		return new String(System.getProperty("user.dir"));
	}
	
	
	
	
	/**finds the absolute path of the workspace which contains this project
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return String - an absolute path of workspace. 
	 * 			Null if there is an exception when gathering the path
	 * 
	 * @exception may not work in all operating systems(Linux and Windows excluded)
	 */
	public static final String getWorkspacePath()
	{
		String retVal = null;
		File file;
		
		try
		{

			retVal = System.getProperty("user.dir");
			retVal = (String)retVal.subSequence(0, retVal.lastIndexOf("/"));
			
			file = new File(retVal);
			
			if(!file.exists())
			{
				throw new IOException("Error: Cannot find workspace path correctly");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = null;
		}
		
		
		return retVal;
	}
	
	
	
	
	/**returns a path to the system to use as a temporary file location.
	 * Only supports ubuntu and windows
	 * 
	 * @author erhan sezerer
	 *
	 * @return String - a path to a location in that file system that can be used for temporary files
	 * 
	 * @exception InvalidPathException - if the system path cannot be found throws an exception
	 */
	public static final String getDefaultSystemPath() throws InvalidPathException
	{
		String path = null;
		
		
		switch(getOperatingSystemType())
		{
			case LINUX:	path = new String("/home");
				
							if(!new File(path, FileSystemUtils.getUserName()).exists())
							{
								throw new InvalidPathException(path, "Cannot create default path");
							}
							path = new File(path, FileSystemUtils.getUserName()).getAbsolutePath();
				
							break;
							
			case WINDOWS: path = new String("C:\\");
		
							if(!new File(path).exists())
							{
								throw new InvalidPathException(path, "Cannot create default path");
							}
		
						break;
						
			default: path = null;
				 	 break;
		}
		
		
		
		return path;
	}
	
	
	
	
	
	/**creates all the necessary parents of a file in case they don't exist. 
	 * If all the parents exists, this function does nothing
	 * 
	 * @author erhan sezerer
	 *
	 * @param dir - a file to create a path for
	 * @return boolean - true if operation is successful
	 */
	public static boolean mkdirs(File dir)
	{
		boolean retVal = true;
		
		try
		{

			if(!dir.getParentFile().exists())
			{
				retVal = dir.getParentFile().mkdirs();
			}
		}
		catch(SecurityException se)
		{
			se.printStackTrace();
			retVal = false;
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}
	
	
	
	/**creates all the necessary parents of a path in case they don't exist. 
	 * If all the parents exists, this function does nothing
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - obvious
	 * @return boolean - true for success operation
	 */
	public static boolean mkdirs(String path)
	{
		boolean retVal = true;
		File file;
		
		try
		{
			file = new File(path);
			
			if(!file.getParentFile().exists())
			{
				retVal = file.getParentFile().mkdirs();
			}
		}
		catch(SecurityException se)
		{
			se.printStackTrace();
			retVal = false;
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}
	
	
	
	/**deletes the entire directory including all the files inside. 
	 * Be careful when using it!
	 * 
	 * @author erhan sezerer
	 * 
	 * @param directoryPath - a path to the directory which it deletes
	 *
	 * @return boolean for success
	 */
	public static boolean deleteDirectory(String directoryPath)
	{
		boolean retVal = true;
		File directory;
		
		try
		{
			directory = new File(directoryPath);
			
			
			if(!directory.exists())
			{
				throw new InvalidPathException(directory.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			else if(!directory.isDirectory())
			{
				throw new InvalidPathException(directory.getCanonicalPath(), "Error: Path is not a directory ->"); 
			}
			else
			{
				FileUtils.deleteDirectory(directory);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		return retVal;
	}
	
	
	
	
	
	/**returns the number of files in the given directory including its subdirectories
	 * 
	 * @author erhan sezerer
	 *
	 * @param dirPath - a path to search for
	 * @return int - count of files
	 */
	public static int getFileCount(final String dirPath, final boolean recursive)
	{
			int count = 0;
		    File file = new File(dirPath);
		    
		    File[] files  = file.listFiles();

		    if(files != null)
		    {
			    for(int i=0; i < files.length; i++)
			    {
				        
			    	if(!files[i].isDirectory())
				    {
				        count++;
				    }
			    	else if(recursive)
				    {   
				        count += getFileCount(files[i].getAbsolutePath(), recursive); 
				    }
			    		
				}
		    }
		    
		    
		    return count;
	}
	
	
	
	
	
	/**returns the number of files in the given directory including its subdirectories. 
	 * The directory given is excluded
	 * 
	 * @author erhan sezerer
	 *
	 * @param dirPath - a path to search for
	 * @return int - count of folder
	 */
	public static int getFolderCount(final String dirPath, final boolean recursive)
	{
			int count = 0;
		    File file = new File(dirPath);
		    
		    File[] files  = file.listFiles();

		    if(files != null)
		    {
				    for(int i=0; i < files.length; i++)
				    {
				       
				        if(files[i].isDirectory())
				        {    
				        	 count++;
				        	 
				        	 if(recursive)
				        	 {
					             count += getFolderCount(files[i].getAbsolutePath(), recursive); 
				        	 }
				        }
				     }
		    }
		    
		    return count;
	}

	
}
