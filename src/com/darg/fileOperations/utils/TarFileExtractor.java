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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;

import com.darg.fileOperations.model.EntryContainer;
import com.darg.fileOperations.model.FileType;
import com.darg.utils.ArrayListUtils;

public final class TarFileExtractor 
{
	
	
	//constructor - made it private to prevent users from calling
	private TarFileExtractor()
	{
		
	}

	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//TAR.GZ OPERATIONS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**decompresses the given tar.gz file into the given folder.  
	 * If the file does not exist or if it is not a tar file returns null. Otherwise,
	 * if the operation is successful returns an array containing the files.
	 * 
	 * also works on .tgz
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance to the tar file
	 * @param destination - a path to extract files
	 * @param recursive - a boolean for recursive search.
	 * 					  If it is true compressed files within this file are also extracted
	 * 
	 * @return ArrayList<File> files in the tar folder
	 */
	public static ArrayList<File> unTarAndGetFiles(final File file, final String destination, final boolean recursive)
	{
		ArrayList<File> retVal = new ArrayList<File>();
		String tarPath = new String("");
		
		TarArchiveEntry entry = null;
		File tempFile = null;
		String extension = new String("");
		FileOutputStream fout = null;
		BufferedOutputStream outputStream = null;		
		
		
		
		try
		{
			//creating a directory for extracted files
			tarPath = new File(destination, FilenameUtils.removeExtension(FilenameUtils.removeExtension(file.getName()))).getCanonicalPath();
			
			
			//first check if the file exist
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for the extraction
			else if(!FileSystemUtils.mkdirs(tarPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + tarPath);
			}
			else
			{
		
					//then open stream with try-with-resources so that it will close automatically
					try(FileInputStream fin = new FileInputStream(file.getCanonicalPath());
						BufferedInputStream bif = new BufferedInputStream(fin);
						GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bif);
						TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);)
					{
						
						//read all the files one by one
						while ((entry = tarIn.getNextTarEntry()) != null) 
						{
							
							if(tarIn.canReadEntryData(entry))
							{
								
								//get the first entry and create the path and file for it
								extension = FilenameUtils.getExtension(entry.getName());
								tempFile = new File(tarPath, entry.getName());
								
								
								//create all necessary parents
								if(!FileSystemUtils.mkdirs(tempFile))
								{
									throw new IOException("Error: Cannot create parent folders for path: " + tarPath);
								}
								
								
								
								//if it is not a directory create it, if it is a directory skip it we already
								//create it when we create sub files, with mkdirs()
								if(!entry.isDirectory())
								{
						            fout = new FileOutputStream(tempFile);
						            outputStream = new BufferedOutputStream(fout);
						            
						            unTarFile(tarIn, outputStream);
						            
						          
						            
						            outputStream.flush();
						            outputStream.close();
						            fout.close();
						            
						            
						            
						            
									//if the first entry is another compressed file itself. also extract it recursively
									if(recursive && ArrayListUtils.containsIgnoresCase(FileType.getSupportedCompressedTypes(), extension))
									{
										if(extension.equalsIgnoreCase(FileType.ZIP.name()))
										{
											ArrayList<File> tempList = ZipFileExtractor.unZipAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
										
											if(tempList != null && !tempList.isEmpty())
											{
												retVal.addAll(tempList);
											}
										}
										else if(extension.equalsIgnoreCase(FileType.GZ.name()) || extension.equalsIgnoreCase(FileType.TGZ.name()))
										{
											ArrayList<File> tempList = unTarAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
											
											if(tempList != null && !tempList.isEmpty())
											{
												retVal.addAll(tempList);
											}
										}

									}
									else
									{
										  retVal.add(tempFile);
									}
									
								}
								
							}
							else
							{
								throw new Exception("Error: cannot read file from tar: " + entry.getName());
							}
							
						}
		
					}
					catch (FileNotFoundException fnfe) 
					{
						fnfe.printStackTrace();
						retVal = null;
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
						retVal = null;
					}
					catch(Exception e)
					{
						retVal = null;
					}
			}
		}
		catch(InvalidPathException ipe)
		{
			System.out.println(ipe.getReason() + "\n" + ipe.getInput());
			ipe.printStackTrace();
			retVal = null;
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
		
		
	
		
		return retVal;
	}
	
	
	
	
	
	/**decompresses the given tar.gz file into the given folder.   
	 * If the file does not exist or if it is not a tar file returns false. Otherwise,
	 * if the operation is successful returns true.
	 * 
	 * also works on .tgz
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance to the tar file
	 * @param destination - a path to extract files
	 * @param recursive - a boolean for recursive search.
	 * 					  if it is true compressed files within this file are also extracted
	 * 
	 * @return boolean - true if operation is successful
	 */
	public static boolean unTar(final File file, final String destination, final boolean recursive)
	{
		boolean retVal = true;
		String tarPath = new String("");
		
		TarArchiveEntry entry = null;
		File tempFile = null;
		String extension = new String("");
		FileOutputStream fout = null;
		BufferedOutputStream outputStream = null;
		
		
		try
		{
			//creating a directory for extracted files
			tarPath = new File(destination, FilenameUtils.removeExtension(FilenameUtils.removeExtension(file.getName()))).getCanonicalPath();
			
			
			
			
			//first check if the file exist
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the folders necessary for extraction
			else if(!FileSystemUtils.mkdirs(tarPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + tarPath);
			}
			else
			{
		
					//then open stream with try-with-resources so that it will close automatically
					try(FileInputStream fin = new FileInputStream(file.getCanonicalPath());
						BufferedInputStream bif = new BufferedInputStream(fin);
						GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bif);
						TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);)
					{

						//read all the files one by one
						while ((entry = tarIn.getNextTarEntry()) != null) 
						{
								
							if(tarIn.canReadEntryData(entry))
							{
								
								//get the first entry and create the path and file for it
								extension = FilenameUtils.getExtension(entry.getName());
								tempFile = new File(tarPath, entry.getName());
								

								//create all necessary parents
								if(!FileSystemUtils.mkdirs(tempFile))
								{
									throw new IOException("Error: Cannot create parent folders for path: " + tarPath);
								}
								
								
								//if it is not a directory create it, if it is a directory skip it we already
								//create it when we create sub files, with mkdirs()
								if(!entry.isDirectory())
								{
						            fout = new FileOutputStream(tempFile);
						            outputStream = new BufferedOutputStream(fout);
						            
						            unTarFile(tarIn, outputStream);
						            
						            outputStream.flush();
						            outputStream.close();
						            fout.close();
						            
						            

									//if it is another compressed file recursively open it
									if(recursive && ArrayListUtils.containsIgnoresCase(FileType.getSupportedCompressedTypes(), extension))
									{

										if(extension.equalsIgnoreCase(FileType.ZIP.name()))
										{
											ZipFileExtractor.unZip(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
										}
										else if(extension.equalsIgnoreCase(FileType.GZ.name()) || extension.equalsIgnoreCase(FileType.TGZ.name()))
										{
											unTar(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
										}
									}
									
									
								}
								
							}
							else
							{
								throw new Exception("Error: cannot read file from tar: " + entry.getName());
							}
							
						}
		
					}
					catch (FileNotFoundException fnfe) 
					{
						fnfe.printStackTrace();
						retVal = false;
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
						retVal = false;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						retVal = false;
					}
			}
		}
		catch(InvalidPathException ipe)
		{
			System.out.println(ipe.getReason() + "\n" + ipe.getInput());
			ipe.printStackTrace();
			retVal = false;
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			retVal = false;
		}
		
		
	
		
		return retVal;
	}

	
	
	
	
	
	/**reads a single file from tar.gz into destination from the given input and output streams.  
	 * Throws an IOException if there is an error while reading the file
	 * 
	 * @author erhan sezerer
	 *
	 * @param zipInputStream
	 * @param outputStream
	 * @throws IOException
	 */
	private static void unTarFile(final TarArchiveInputStream inputStream, final BufferedOutputStream outputStream) throws IOException
	{
		int BUFFER = 2048;
		int read = 0;
		byte[] bytes = new byte[BUFFER];

		
		//read file into the temporary file and folder
		while ((read = inputStream.read(bytes , 0 , BUFFER)) != -1) 
		{
			outputStream.write(bytes, 0, read);
		}
	
	}

	
	
	
	/**returns the entries contained in the given tar file
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance to tar file
	 * 
	 * @return ArrayLis<String> - an array list containing the entries if there is no entry,  
	 * 							  or if there is an exception it is null
	 */
	public static ArrayList<EntryContainer> getTarEntries(final File file)
	{
		ArrayList<EntryContainer> entries = new ArrayList<EntryContainer>();
		TarArchiveEntry entry = null;	
		
		
		try
		{

			//first check if the file exist
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			else
			{
		
					//then open stream with try-with-resources so that it will close automatically
					try(FileInputStream fin = new FileInputStream(file.getCanonicalPath());
						BufferedInputStream bif = new BufferedInputStream(fin);
						GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bif);
						TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);)
					{
						
						//read all the files one by one
						while ((entry = tarIn.getNextTarEntry()) != null) 
						{
							
								//learn whether it is contained in sub folder or not
								//countSlash == 0 if it is not
								if(!entry.getName().contains("/") || entry.getName().endsWith("/"))
								{
										if(!entry.isDirectory())
										{
											entries.add(new EntryContainer(file, FileType.DIRECTORY, entry.getName()));
										}
										else if(FileType.isCompressed(file))
										{
											if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(FileType.GZ.name()))
											{
												entries.add(new EntryContainer(file, FileType.GZ, entry.getName()));
											}
											else if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(FileType.TGZ.name()))
											{
												entries.add(new EntryContainer(file, FileType.TGZ, entry.getName()));
											}
											else
											{
												entries.add(new EntryContainer(file, FileType.ZIP, entry.getName()));
											}
										}
										else
										{
											entries.add(new EntryContainer(file, FileType.OTHER, entry.getName()));
										}
								}
							
						}
		
					}
					catch (FileNotFoundException fnfe) 
					{
						fnfe.printStackTrace();
						entries = null;
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
						entries = null;
					}
					catch(Exception e)
					{
						entries = null;
					}
			}
		}
		catch(InvalidPathException ipe)
		{
			System.out.println(ipe.getReason() + "\n" + ipe.getInput());
			ipe.printStackTrace();
			entries = null;
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			entries = null;
		}
		
		
	
		
		return entries;

	}
	

	


}
