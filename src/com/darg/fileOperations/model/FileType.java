/*
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
package com.darg.fileOperations.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


import org.apache.commons.io.FilenameUtils;

public enum FileType 
{
	XML(1),TXT(2),GZ(3), TGZ(4), ZIP(5),OTHER(98),DIRECTORY(99);
		
	public transient final int value;

	
	
	//constructor
	FileType(final int value)
	{
		this.value = value;
	}
	
	
	
	
	/**tests whether the file is a compressed file or not. 
	 * Currently supports .zip and .tar.gz formats
	 * 
	 * 
	 * @author erhan
	 *
	 * @param file - an instance of a file to test
	 * @return boolean - true if it's a compress file false if it's not
	 */
	public static boolean isCompressed(final File file)
	{
		boolean retVal = false;
		
		
		if(file != null && file.exists())
		{
			String fileExtension = FilenameUtils.getExtension(file.getName()).toUpperCase(Locale.ENGLISH);
			
			if(FileType.ZIP.name().equalsIgnoreCase(fileExtension))
			{
				retVal = true;
			}
			else if(FileType.GZ.name().equalsIgnoreCase(fileExtension))
			{
				retVal = true;
			}
			else if(FileType.TGZ.name().equalsIgnoreCase(fileExtension))
			{
				retVal = true;
			}
			else
			{
				retVal = false;
			}
		}
		
		
		
		return retVal;
	}
	
	
	
	
	
	/**returns the extensions of supported compressed file types
	 * 
	 * 
	 * @author erhan
	 *
	 * @return ArrayList<String> - extensions
	 */
	public static ArrayList<String> getSupportedCompressedTypes()
	{
		ArrayList<String> compressedTypes = new ArrayList<String>();
		
		compressedTypes.add(FileType.GZ.name().toLowerCase(Locale.ENGLISH));
		compressedTypes.add(FileType.ZIP.name().toLowerCase(Locale.ENGLISH));
		compressedTypes.add(FileType.TGZ.name().toLowerCase(Locale.ENGLISH));
		
		return compressedTypes;
	}
	
}
