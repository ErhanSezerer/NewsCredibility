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

/**
 * @author erhan sezerer
 *
 *this enum contains the attributes that will effect the 
 *course of the program. According to these attributes, 
 *our program will handle extracted files
 */

public enum ExtractedFileAttributes 
{
	
	KEEP_FILES_AFTER_EXTRACTION(1),
	AUTO_DELETE_FILES_AFTER_EXTRACTION(2),
	MANUAL_DELETE_FILES_AFTER_EXTRACTION(3),
	NONE(4);
	
	public transient final int value;

	
	ExtractedFileAttributes(final int value)
	{
		this.value = value;
	}
	
}
