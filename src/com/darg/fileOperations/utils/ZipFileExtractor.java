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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import org.apache.commons.io.FilenameUtils;

import com.darg.fileOperations.model.EntryContainer;
import com.darg.fileOperations.model.FileType;
import com.darg.utils.ArrayListUtils;



public final class ZipFileExtractor 
{
	
	//constructor - made it private to prevent users from calling
	private ZipFileExtractor()
	{
		
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//ZIP OPERATIONS ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**decompresses the given zip file into the given folder.   
	 * If the file does not exist or if it is not a zip file returns null. otherwise,
	 * if the operation is successful returns the files.
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance of a zip file
	 * @param destination - destination for extraction
	 * @param recursive - a boolean for recursive search. 
	 * 					   If it is true compressed files within this file are also extracted
	 * 
	 * @return - ArrayList<File> all the files inside zip
	 */
	public static ArrayList<File> unZipAndGetFiles(final File file, final String destination, final boolean recursive) 
	{
		ArrayList<File> retVal = new ArrayList<File>();
		String zipPath = new String("");
		
		ZipEntry zipEntry = null;
		
		BufferedInputStream zipInputStream = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream dest = null;
		
		File tempFile = null;
		String extension = new String("");
		
		
		try
		{
			//creating a directory for extracted files (with same name as the zip file)
			zipPath = new File(destination, FilenameUtils.removeExtension(file.getName())).getCanonicalPath();
			
			
			
			//first control whether it is okay to extract 
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for extraction
			else if(!FileSystemUtils.mkdirs(zipPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
			}
			else
			{
				//open zip file
				try(ZipFile zipFile = new ZipFile(file))
				{
					Enumeration<? extends ZipEntry> entry = zipFile.entries();
			
					
					
					//iterate over the elements in the zip file folder
					while(entry.hasMoreElements())
					{
						
						
						//get the first entry and create the path and file for it
						zipEntry = entry.nextElement();
						extension = FilenameUtils.getExtension(zipEntry.getName());
						tempFile = new File(zipPath, zipEntry.getName());

						
						if(!FileSystemUtils.mkdirs(tempFile))
						{
							throw new IOException("Error: Cannot create parent folders for path: " + tempFile.getCanonicalPath());
						}
						
												

						
						
						//if it is not a directory create it, if it is a directory skip it we already
						//create it when we create sub files, with mkdirs()
						if(!zipEntry.isDirectory())
						{
							zipInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
							outputStream = new FileOutputStream(tempFile);
							dest = new BufferedOutputStream(outputStream);
				
				
							unZipFile(zipInputStream, dest);
				
							
				

							dest.flush();
							dest.close();
							outputStream.close();
							zipInputStream.close();
							
							
							
							//if the first entry is another compressed file itself. also extract it recursively
							if(recursive && ArrayListUtils.containsIgnoresCase(FileType.getSupportedCompressedTypes(), extension))
							{
								if(extension.equalsIgnoreCase(FileType.ZIP.name()))
								{
									ArrayList<File> tempList = unZipAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
								
									if(tempList != null && !tempList.isEmpty())
									{
										retVal.addAll(tempList);
									}
								}
								else if(extension.equalsIgnoreCase(FileType.GZ.name()) || extension.equalsIgnoreCase(FileType.TGZ.name()))
								{
									ArrayList<File> tempList = TarFileExtractor.unTarAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
									
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
						

					}//while
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Throwable[] exceptions = ex.getSuppressed();
			
					for (Throwable e : exceptions) 
					{
						e.printStackTrace();
					}
					
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
		
	
	
	
	/**returns the entries contained in the given zip file
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance to zip file
	 * 
	 * @return ArrayLis<String> - an array list containing the entries if there is no entry,  
	 * 							  or if there is an exception it is null
	 */
	public static ArrayList<EntryContainer> getZipEntries(final File file)
	{
		ArrayList<EntryContainer> entries = new ArrayList<EntryContainer>();
		ZipEntry zipEntry = null;
		
		
		try
		{
			//first control whether it is okay to extract 
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			else
			{
				//open zip file
				try(ZipFile zipFile = new ZipFile(file))
				{
					Enumeration<? extends ZipEntry> entry = zipFile.entries();
			
					
					
					//iterate over the elements in the zip file folder
					while(entry.hasMoreElements())
					{
						
						//get the first entry
						zipEntry = entry.nextElement();
						
						//learn whether it is contained in sub folder or not
						//countSlash == 0 if it is not
						if(!zipEntry.getName().contains("/") || zipEntry.getName().endsWith("/"))
						{
								if(!zipEntry.isDirectory())
								{
									entries.add(new EntryContainer(file, FileType.DIRECTORY, zipEntry.getName()));
								}
								else if(FileType.isCompressed(file))
								{
									if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(FileType.GZ.name()))
									{
										entries.add(new EntryContainer(file, FileType.GZ, zipEntry.getName()));
									}
									else if(FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(FileType.TGZ.name()))
									{
										entries.add(new EntryContainer(file, FileType.TGZ, zipEntry.getName()));
									}
									
									else
									{
										entries.add(new EntryContainer(file, FileType.ZIP, zipEntry.getName()));
									}
								}
								else
								{
									entries.add(new EntryContainer(file, FileType.OTHER, zipEntry.getName()));
								}
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Throwable[] exceptions = ex.getSuppressed();
			
					for (Throwable e : exceptions) 
					{
						e.printStackTrace();
					}
					
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
	
	
	
	
	/**
	 *decompresses the given zip file into the given folder. 
	 * If the file does not exist or if it is not a zip file returns false. Otherwise,
	 * if the operation is successful returns true.
	 * 
	 * @author erhan sezerer
	 *
	 * @param file - an instance of a zip file
	 * @param destination - destination for extraction
	 * @param recursive - a boolean for recursive search.
	 * 					  If it is true compressed files within this file are also extracted
	 * 
	 * 
	 * @return boolean - true if it is successful
	 */
	public static boolean unZip(final File file, final String destination, final boolean recursive) 
	{
		boolean retVal = true;
		String zipPath;
		
		ZipEntry zipEntry = null;
		
		BufferedInputStream zipInputStream = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream dest = null;
		
		File tempFile = null;
		String extension = new String("");
		
		
		
		try
		{
			//creating a directory for extracted files
			zipPath = new File(destination, FilenameUtils.removeExtension(file.getName())).getCanonicalPath();
			
			
			//first control whether it is okay to extract 
			if(!file.exists())
			{
				throw new InvalidPathException(file.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for extraction
			else if(!FileSystemUtils.mkdirs(zipPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
			}
			else
			{
				//open zip file
				try(ZipFile zipFile = new ZipFile(file))
				{
					Enumeration<? extends ZipEntry> entry = zipFile.entries();
			
					
					
					//iterate over the elements in the zip file folder
					while(entry.hasMoreElements())
					{
						
						
						//get the first entry and create the path and file for it
						zipEntry = entry.nextElement();
						extension = FilenameUtils.getExtension(zipEntry.getName());
						tempFile = new File(zipPath, zipEntry.getName());
						

						
						if(!FileSystemUtils.mkdirs(tempFile))
						{
							throw new IOException("Error: Cannot create parent folders for path: " + tempFile.getCanonicalPath());
						}
					
						
						
						
						//if it is not a directory create it, if it is a directory skip it we already
						//create it when we create parent files, with mkdirs()
						if(!zipEntry.isDirectory())
						{
							zipInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
							outputStream = new FileOutputStream(tempFile);
							dest = new BufferedOutputStream(outputStream);
				
				
							unZipFile(zipInputStream, dest);
				

							dest.flush();
							dest.close();
							outputStream.close();
							zipInputStream.close();
							
							
							//if the first entry is another compressed file itself. also extract it recursively
							if(recursive && ArrayListUtils.containsIgnoresCase(FileType.getSupportedCompressedTypes(), extension))
							{
								if(extension.equalsIgnoreCase(FileType.ZIP.name()))
								{
									unZip(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
								}
								else if(extension.equalsIgnoreCase(FileType.GZ.name()) || extension.equalsIgnoreCase(FileType.TGZ.name()))
								{
									TarFileExtractor.unTar(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
								}

							}
						}
						

					}//while
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					Throwable[] exceptions = ex.getSuppressed();
			
					for (Throwable e : exceptions) 
					{
						e.printStackTrace();
					}
					
			
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

	
	
	
	
	/**reads a single file from zip into destination from the given input and output streams.  
	 * Throws an IOException if there is an error while reading the file
	 * 
	 * @author erhan sezerer
	 *
	 * @param zipInputStream
	 * @param outputStream
	 * @throws IOException
	 */
	private static void unZipFile(final BufferedInputStream inputStream, final BufferedOutputStream outputStream) throws IOException
	{
		int buffer = 2048;
		int read = 0;
		byte[] bytes = new byte[buffer];

		//read file into the temporary file and folder
		while ((read = inputStream.read(bytes , 0 , buffer)) != -1) 
		{
			outputStream.write(bytes, 0, read);
		}
	
	}

	
	
	
	
	/**searches for a single file in zip by the given entry path
	 * 
	 * @author erhan sezerer
	 *
	 * @param zipFile - an instance to the zip file
	 * @param destination - a path for extraction
	 * @param zipEntryPath - a path to the entry inside the zip (example: asd.zip -> "/files/file.txt")
	 * @return File - file that we are looking for, null if there is any errors while trying to read it
	 */
	public static File getFileFromZip(final File zipFile, final String destination, final String zipEntryPath)
	{
		File file = null;
		String zipPath = new String("");
		
		ZipEntry zipEntry = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream dest = null;


		
		try(ZipFile zFile = new ZipFile(zipFile))
		{
			//creating a directory for extracted files
			zipPath = new File(destination, FilenameUtils.removeExtension(zipFile.getName())).getCanonicalPath();
			
			
			
			//first control whether the zip file exists or not
			if(!zipFile.exists())
			{
				throw new InvalidPathException(zipFile.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for extraction
			else if(!FileSystemUtils.mkdirs(zipPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
			}
			else
			{
				//get the entry we are searching for
				zipEntry = zFile.getEntry(zipEntryPath);
				
				
				//if it does not exist
				if(zipEntry == null)
				{
					throw new InvalidPathException(zipPath + " - " + zipEntryPath, "Error: zip entry cannot be found");
				}
				else
				{
					
					//create the file and the path for it
					file = new File(zipPath, zipEntry.getName());
					
					if(!FileSystemUtils.mkdirs(file))
					{
						throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
					}
						
					BufferedInputStream zipInputStream = new BufferedInputStream(zFile.getInputStream(zipEntry));
					outputStream = new FileOutputStream(file);
					dest = new BufferedOutputStream(outputStream);
					
					
					
					
					//read it if it is not a directory
					if(!zipEntry.isDirectory())
					{
						unZipFile(zipInputStream, dest);
					}
					
					
					dest.flush();
					dest.close();
					outputStream.close();
					zipInputStream.close();
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Throwable[] exceptions = e.getSuppressed();
			
			for (Throwable exc : exceptions) 
			{
				exc.printStackTrace();
			}

			file = null;
		}
		
		
		
		return file;
	}
	
	
	
	
	
	/**searches for a single file in zip by the name of the file
	 * 
	 * @author erhan sezerer
	 *
	 * @param zipFile - an instance to the zip file
	 * @param destination - a path for extraction
	 * @param fileName - the name of the file we are searching for
	 * @param recursive - if true, searches through the sub-compressed files
	 * 
	 * @return File - file that we are looking for, null if there is any errors while trying to read it
	 */
	public static File searchFileFromZip(final File zipFile, final String destination, final String fileName, final boolean recursive)
	{
		File file = null;
		File tempFile = null;
		String zipPath = new String("");
		String extension = new String("");
		
		ZipEntry zipEntry = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream dest = null;


		
		try(ZipFile zFile = new ZipFile(zipFile))
		{
			//creating a directory for extracted files
			zipPath = new File(destination, FilenameUtils.removeExtension(zipFile.getName())).getCanonicalPath();
			
			
			
			
			//first control whether the zip file exists or not
			if(!zipFile.exists())
			{
				throw new InvalidPathException(zipFile.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for extraction
			else if(!FileSystemUtils.mkdirs(zipPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
			}
			else
			{
					Enumeration<? extends ZipEntry> entry = zFile.entries();
					
					
					//iterate over the elements in the zip file folder
					while (entry.hasMoreElements()) 
					{
						
						//get the first entry in the zip file
						zipEntry = entry.nextElement();
						extension = FilenameUtils.getExtension(zipEntry.getName());
						tempFile = new File(zipPath, zipEntry.getName()); 
						


						if(!tempFile.isDirectory())
						{

							
								//if it is the file we are searching for
								if(FilenameUtils.removeExtension(FilenameUtils.getName(zipEntry.getName())).equalsIgnoreCase(fileName))
								{
	
									//create the file and stream to read it
									file = new File(zipPath, zipEntry.getName());
									
									if(!FileSystemUtils.mkdirs(file))
									{
										throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
									}
									
									BufferedInputStream zipInputStream = new BufferedInputStream(zFile.getInputStream(zipEntry));
									outputStream = new FileOutputStream(file);
									dest = new BufferedOutputStream(outputStream);
										
									
									
									
									unZipFile(zipInputStream, dest);
										
									dest.flush();
									dest.close();
									outputStream.close();
									zipInputStream.close();
									
									break;
									
								}
								//if it is another compressed file, also extract it
								else if(recursive && extension.equalsIgnoreCase(FileType.ZIP.name()))
								{
									//create the file and stream to read it
									BufferedInputStream zipInputStream = new BufferedInputStream(zFile.getInputStream(zipEntry));
									
									if(!FileSystemUtils.mkdirs(tempFile))
									{
										throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
									}

									outputStream = new FileOutputStream(tempFile);
									dest = new BufferedOutputStream(outputStream);
									
									
										
									unZipFile(zipInputStream, dest);
										
									dest.flush();
									dest.close();
									outputStream.close();
									zipInputStream.close();
									
									
									file = searchFileFromZip(tempFile, tempFile.getParentFile().getCanonicalPath(), fileName, recursive);
									
								}
								
							}
						
					}
					
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Throwable[] exceptions = e.getSuppressed();
			
			for (Throwable exc : exceptions) 
			{
				exc.printStackTrace();
			}

			file = null;
		}
		
		
		
		return file;
	}
	
	
	
	
	
	/**searches for all entries starts with the filename in zip by the name of the file
	 * 
	 * @author erhan sezerer
	 *
	 * @param zipFile - an instance to the zip file
	 * @param destination - a path for extraction
	 * @param fileName - the name of the file we are searching for
	 * @param recursive - if true, searches through the sub-compressed files
	 * 
	 * @return ArrayList<File> - files that we are looking for, null if there is any errors while trying to read it
	 */
	public static ArrayList<File> searchAllFilesFromZip(final File zipFile, final String destination, final String fileName, final boolean recursive)
	{
		ArrayList<File> file = new ArrayList<File>();
		File tempFile = null;
		String zipPath = new String("");
		String extension = new String("");
		
		ZipEntry zipEntry = null;
		FileOutputStream outputStream = null;
		BufferedOutputStream dest = null;


		
		try(ZipFile zFile = new ZipFile(zipFile))
		{
			//creating a directory for extracted files
			zipPath = new File(destination, FilenameUtils.removeExtension(zipFile.getName())).getCanonicalPath();
			
			
			
			
			//first control whether the zip file exists or not
			if(!zipFile.exists())
			{
				throw new InvalidPathException(zipFile.getCanonicalPath(), "Error: Path name cannot be resolved ->");
			}
			//check and create the path for extraction
			else if(!FileSystemUtils.mkdirs(zipPath))
			{
				throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
			}
			else
			{
					Enumeration<? extends ZipEntry> entry = zFile.entries();
					
					
					//iterate over the elements in the zip file folder
					while (entry.hasMoreElements()) 
					{
						
						//get the first entry in the zip file
						zipEntry = entry.nextElement();
						extension = FilenameUtils.getExtension(zipEntry.getName());
						tempFile = new File(zipPath, zipEntry.getName()); 
						


						if(!zipEntry.isDirectory())
						{
							
								//if it is the file we are searching for
								if(FilenameUtils.removeExtension(zipEntry.getName()).contains(fileName))
								{
	
									//create the file and stream to read it
									
									if(!FileSystemUtils.mkdirs(tempFile))
									{
										throw new IOException("Error: Cannot create parent folders for path: " + zipPath);
									}
									
									BufferedInputStream zipInputStream = new BufferedInputStream(zFile.getInputStream(zipEntry));
									outputStream = new FileOutputStream(tempFile);
									dest = new BufferedOutputStream(outputStream);
									
									
									
									unZipFile(zipInputStream, dest);
										
									dest.flush();
									dest.close();
									outputStream.close();
									zipInputStream.close();
									
									

									//if it is another compressed file, also extract it
									if(recursive && ArrayListUtils.containsIgnoresCase(FileType.getSupportedCompressedTypes(), extension))
									{
										if(extension.equalsIgnoreCase(FileType.ZIP.name()))
										{
											ArrayList<File> tempList = unZipAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
										
											if(tempList != null && !tempList.isEmpty())
											{
												file.addAll(tempList);
											}
										}
										else if(extension.equalsIgnoreCase(FileType.GZ.name()) || extension.equalsIgnoreCase(FileType.TGZ.name()))
										{
											ArrayList<File> tempList = TarFileExtractor.unTarAndGetFiles(tempFile, tempFile.getParentFile().getCanonicalPath(), recursive);
											
											if(tempList != null && !tempList.isEmpty())
											{
												file.addAll(tempList);
											}
										}

									}
									else
									{
										file.add(tempFile);
									}
									
									
								}
						}//if
						
					}//while
					
			}//else
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Throwable[] exceptions = e.getSuppressed();
			
			for (Throwable exc : exceptions) 
			{
				exc.printStackTrace();
			}

			file = null;
		}
		
		
		
		return file;
	}

	
	
	
	
	

}
