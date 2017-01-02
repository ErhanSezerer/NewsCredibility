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
package com.darg.NLPOperations.utils;

import com.darg.NLPOperations.pos.POSTagger;

public class PosParam 
{
	private int docID;
	private String text;
	private String outputFolder;
	private String fileName;
	private POSTagger tagger;
	
	
	
	//constructors
	@SuppressWarnings("unused")
	private PosParam()
	{
		
	}
	public PosParam(final int docID, final String text, final String fileName, 
					final String outputFolder, final POSTagger tagger) 
	{
		this.docID = docID;
		this.tagger = tagger;
		this.text = text;
		this.fileName = fileName;
		this.outputFolder = outputFolder;
	}
	
	
	
	
	//setters and getters
	public String getText() 
	{
		return text;
	}
	
	public String getOutputFolder() 
	{
		return outputFolder;
	}
	
	public String getFileName() 
	{
		return fileName;
	}
	
	public POSTagger getTagger()
	{
		return tagger;
	}
	
	public int getDocID() 
	{
		return docID;
	}

	
}
