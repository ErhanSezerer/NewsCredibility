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

public interface CompressedFileLoader extends FileLoader
{
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//functions
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**a function for loading documents from a path given as a file

	 * 
	 * @author erhan sezerer
	 *
	 */
	public boolean loadFiles(final File file, final String destination);
	
	public boolean extractFiles(final File file, final String destination, final boolean recursive);
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//setter getter and other minor methods
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	public void resetEntries();
	
	public ArrayList<EntryContainer> getEntries();
	
	public long getEntryCount();
	
	public ExtractedFileAttributes getAttribute();
	
	public void setAttribute(final ExtractedFileAttributes attribute);
	
}
