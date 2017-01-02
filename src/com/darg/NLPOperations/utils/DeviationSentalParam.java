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

import java.io.File;

import com.darg.NLPOperations.sentimentalAnalysis.DeviationSentimentAnalyzer;
import com.darg.utils.TempStorage;

public class DeviationSentalParam 
{
	private int docID;
	private DeviationSentimentAnalyzer analyzer;
	private File file;
	private String outputFolder;
	private String articleOutputFolder;
	private TempStorage storage;
	
	
	
	//constructors
	@SuppressWarnings("unused")
	private DeviationSentalParam()
	{
		
	}
	public DeviationSentalParam(final int docID, final DeviationSentimentAnalyzer analyzer,
								final File file, final String outputFolder, final String articleOutputFolder, final TempStorage storage) 
	{
		this.docID = docID;
		this.analyzer = analyzer;
		this.file = file;
		this.outputFolder = outputFolder;
		this.articleOutputFolder = articleOutputFolder;
		this.storage = storage;
	}
	
	
	
	
	
	
	
	////////////////////////////////////////////
	//getters
	public int getDocID() 
	{
		return docID;
	}
	
	public DeviationSentimentAnalyzer getAnalyzer() 
	{
		return analyzer;
	}
	
	public File getFile() 
	{
		return file;
	}
	
	public String getOutputFolder() 
	{
		return outputFolder;
	}
	
	public String getArticleOutputFolder() 
	{
		return articleOutputFolder;
	}
	
	public TempStorage getStorage() 
	{
		return storage;
	}
	
	
	
	
	
	

}











