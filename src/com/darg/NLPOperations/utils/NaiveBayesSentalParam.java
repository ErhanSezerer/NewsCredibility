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

import com.darg.NLPOperations.sentimentalAnalysis.NaiveBayesSentimentAnalyzer;




public class NaiveBayesSentalParam 
{
	private int docID;
	private NaiveBayesSentimentAnalyzer analyzer;
	private File file;
	private boolean difTag;
	private String outputFolder;
	private String articleOutputFolder;

	
	
	
	//constructors
	@SuppressWarnings("unused")
	private NaiveBayesSentalParam()
	{
		
	}
	public NaiveBayesSentalParam(final NaiveBayesSentimentAnalyzer analyzer,
							  final File file, 
							  final boolean difTag,
							  final String outputFolder,
							  final String articleOutputFolder,
							  final int docID) 
	{
		this.analyzer = analyzer;
		this.file = file;
		this.difTag = difTag;
		this.outputFolder = outputFolder;
		this.articleOutputFolder = articleOutputFolder;
		this.docID = docID;
	}

	
	
	//setters and getters
	public NaiveBayesSentimentAnalyzer getAnalyzer() 
	{
		return analyzer;
	}
	
	public File getFile() 
	{
		return file;
	}
	
	public boolean isDifTag() 
	{
		return difTag;
	}
	
	public String getOutputFolder()
	{
		return outputFolder;
	}
	
	public int getDocID() 
	{
		return docID;
	}
	
	public String getArticleOutputFolder() 
	{
		return articleOutputFolder;
	}
	
}
