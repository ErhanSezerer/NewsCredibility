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
package com.darg.NLPOperations.pos;


import com.darg.fileOperations.utils.FileUtils;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;



public class POSTagger 
{
	private String modelPath;
	private MaxentTagger tagger;

	
	
	//constructors
	@SuppressWarnings("unused")
	private POSTagger()
	{
		
	}
	public POSTagger(final String modelPath) 
	{
		this.modelPath = modelPath;
		tagger = new MaxentTagger(modelPath);
	}

	
	
	
	
	/*-----------------------------------------------------------------------------------------------*/
	//						FUNCTIONS
	/*-----------------------------------------------------------------------------------------------*/
	/**tags a sentence with POS tags, uses Stanford NLP library
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a string to apply POS tagging
	 * 
	 * @return string- tagged text
	 */
	public String tagString(final String text)
	{
		return tagger.tagString(text);
	}
	
	
	
	
	
	/**writes the given tagged string to a file
	 * Deprecated: use FileUtils.writeFile() instead
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a tagged text as a string
	 * @param fileName - name to the file that it will create
	 * @param destination - a path to save the file
	 * 
	 * @return boolean - true if the operation is successful, false otherwise 
	 */
	@Deprecated
	public boolean exportTaggedText(final String text, final String fileName, final String destination)
	{
		return FileUtils.writeFile(destination, fileName, text);
	}
	
	
	
	
	
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------*/
	//setters and getters and other misc. functions
	/*-----------------------------------------------------------------------------------------------*/
	public String getModelPath() 
	{
		return modelPath;
	}

	public void setModelPath(final String modelPath) 
	{
		this.modelPath = modelPath;
	}
	
	
	
	
}
