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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public final class FileUtils 
{

	
	/**reads a string from a file (text file)
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - an absolute path to the file
	 * 
	 * @return String - content of the file , or null if there is any error.
	 */
	public static String readFile(final String path)
	{
		String line = null;
		File exportFile = new File(path);
		StringBuilder retVal = new StringBuilder();
		
		
		try(FileReader fr = new FileReader(exportFile);
			BufferedReader br = new BufferedReader(fr);)
		{
			
			
			while((line = br.readLine()) != null)
			{
				retVal.append(line);
				retVal.append(System.lineSeparator());
			}
			
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			retVal = null;
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			retVal = null;
		}
		
		
		return retVal.toString();
	}
	
	
	
	
	/**writes the given text to a file which's path is given by the string destination
	 *  and name is given by the string fileName
	 *  
	 * @author erhan sezerer
	 *
	 * @param destination - path of file
	 * @param fileName - name of the file
	 * @param text - content of the file
	 * 
	 * @return boolean - true if the operation is successful. it can be false if there is any errors
	 *  such as administrative right to the path or the content equal to null or so.
	 */
	public static boolean writeFile(final String destination, final String fileName, final String text)
	{
		boolean retVal = true;
		
		File exportFile = new File(destination, fileName);
	
		retVal = FileSystemUtils.mkdirs(destination);
		
		if(retVal != false)
		{
			try(FileOutputStream fos = new FileOutputStream(exportFile);
					BufferedOutputStream bos = new BufferedOutputStream(fos);)
				{
					bos.write(text.getBytes());
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
					retVal = false;
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
					retVal = false;
				}
				catch(NullPointerException ne)
				{
					ne.printStackTrace();
					retVal = false;
				}
		}
		
		
		return retVal;
	}

	
	
	
	
	
	/**writes the given text to a file which's object is given
	 *  
	 * @author erhan sezerer
	 *
	 * @param file - destination file
	 * @param text - content of the file
	 * 
	 * @return boolean - true if the operation is successful. it can be false if there is any errors
	 *  such as administrative right to the path or the content equal to null or so.
	 */
	public static boolean writeFile(final File file, final String text)
	{
		boolean retVal = true;
		
		try
		{
			retVal = FileSystemUtils.mkdirs(file.getAbsolutePath());
		}
		catch(Exception e)
		{
			retVal = false;
			e.printStackTrace();
		}
		
		
		
		if(retVal != false)
		{
			try(FileOutputStream fos = new FileOutputStream(file);
					BufferedOutputStream bos = new BufferedOutputStream(fos);)
				{
					bos.write(text.getBytes());
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
					retVal = false;
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
					retVal = false;
				}
				catch(NullPointerException ne)
				{
					ne.printStackTrace();
					retVal = false;
				}
		}
		
		
		return retVal;
	}
	
}
