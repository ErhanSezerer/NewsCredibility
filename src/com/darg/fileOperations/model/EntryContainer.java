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
import java.io.IOException;
import java.util.Locale;




/**contains the file and its type
 * 
 * 
 * @author erhan sezerer
 *
 */
public class EntryContainer 
{
	
	private File parentFile;
	private FileType type;
	private String entryString;
	
	
	
	//constructors
	public EntryContainer(final File file, final FileType type, final String entryString)
	{
		this.parentFile = file;
		this.type = type;
		this.entryString = entryString;
	}
	
	public EntryContainer()
	{
		parentFile = null;
		type = null;
		entryString = null;
	}
	
	
	
	
	
	//setter getters and overridden methods
	@Override
	public String toString()
	{
		String retVal = new String("");
		
		try 
		{
			if(parentFile != null)
			{
				retVal += new String("File: " + parentFile.getCanonicalPath() + "\nType: " + type.name().toLowerCase(Locale.ENGLISH) + "\n");
			}
			else
			{
				retVal += new String("There is no file in the container!\n");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			retVal = null;
		}
		
		
		return retVal;
	}

	
	
	
	public File getFile() 
	{
		return parentFile;
	}

	public void setFile(final File file) 
	{
		this.parentFile = file;
	}

	public FileType getType() 
	{
		return type;
	}

	public void setType(final FileType type) 
	{
		this.type = type;
	}

	public String getEntryString() 
	{
		return entryString;
	}

	public void setEntryString(final String entryString) 
	{
		this.entryString = entryString;
	}
	

}
