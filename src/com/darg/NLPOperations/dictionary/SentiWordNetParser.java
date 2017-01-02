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
package com.darg.NLPOperations.dictionary;

import java.util.ArrayList;

import com.darg.NLPOperations.dictionary.model.SentiWordNet;
import com.darg.NLPOperations.dictionary.model.SentiWordNetWord;
import com.darg.NLPOperations.dictionary.model.SynsetTerm;
import com.darg.NLPOperations.pos.util.POSTagWordNet;



/**This class parses the SentiWordNEt file. 
 * Compatible with the version 3.0
 * 
 * @author erhan sezerer
 *
 */
public class SentiWordNetParser 
{
	public SentiWordNet version;
	
	
	
	
	
	//constructor
	public SentiWordNetParser(final SentiWordNet version) 
	{
		this.version = version;
	}
	
	
	
	
	/*-----------------------------------------------------------------------------------*/
	//						FUNCTIONS
	/*-----------------------------------------------------------------------------------*/
	/**parses the sentiwordnet file and returns the entries as a SentiWordNetWord list.
	 * uses the version attribute to decide which kind of parsing operations to use
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - raw text of a sentiwordnet file.
	 * 
	 * @return ArrayList<SentiWordNetWord> - a list that contains every word in the string.
	 * 										 if the string is empty or null or if there is an error returns null.
	 */
	public synchronized ArrayList<SentiWordNetWord> parseSentiWordNet(final String text)
	{
		ArrayList<SentiWordNetWord> tempList = null;
		
		switch(version)
		{
			case VERSION_30: tempList =  parseSentiWordNet30(text);
							 break;
							 
			default: tempList = null;
		}
		
		return tempList;
	}
	
	
	
	
	/**parses the sentiwordnet file and returns the entries as a SentiWordNetWord list.
	 * For version 3.0
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - raw text of a sentiwordnet file.
	 * 
	 * @return ArrayList<SentiWordNetWord> - a list that contains every word in the string.
	 * 										 if the string is empty or null or if there is an error returns null.
	 */
	private synchronized ArrayList<SentiWordNetWord> parseSentiWordNet30(final String text)
	{
		SentiWordNetWord word;
		ArrayList<SentiWordNetWord> wordList;
		
		
		try
		{
			//check parameters for error
			if(text == null || text.isEmpty())
			{
				wordList = null;
			}
			else
			{
				//prepare variables
				wordList = new ArrayList<SentiWordNetWord>();
				word = new SentiWordNetWord();
				
				String line;
				String[] lineEntries;
				String[] lines = text.split("\n");
				
				
				//control the text
				if(lines.length <= 0)
				{
					throw new IllegalArgumentException("Error: Either the file is either empty or there is no entry\n");
				}
				
				
				
				//for each line
				for (int i=0; i<lines.length; i++)
				{
					line = lines[i];
					POSTagWordNet pos;
					word = new SentiWordNetWord();
					
					if(!line.trim().startsWith("#"))//if it is not a comment line
					{
						//get the columns from line
						lineEntries = line.trim().split("\t");
						
						//validate the columns
						if(lineEntries.length != 6)
						{
							throw new IllegalArgumentException("Illegal argument(s) at(either too few or too much): " + line);
						}
						
						//find the pos tag and assign
						pos = POSTagWordNet.getPosType(lineEntries[0].trim().charAt(0));
						
						if(pos == POSTagWordNet.OTHER)
						{
							throw new IllegalArgumentException("Illegal pos tag at: " + line);
						}
						
						//assign the extracted attributes to the word
						word.setPos(pos);
						word.setId(Integer.parseInt(lineEntries[1].trim()));
						word.setPositiveScore(Float.parseFloat(lineEntries[2].trim()));
						word.setNegativeScore(Float.parseFloat(lineEntries[3].trim()));
						word.setGlossary(lineEntries[5].trim());
						
						
						//parse synset terms
						String[] synWords = lineEntries[4].trim().split(" ");
						SynsetTerm synTerm;
						
						for (int j=0; j<synWords.length; j++)
						{
							synTerm = new SynsetTerm();
							
							if(synTerm.setFromString(synWords[j], "#"))
							{
								word.addSynTerm(synTerm);
							}
							else
							{
								throw new IllegalArgumentException("invalid synset term: " + synTerm);
							}
						
						}
						
						wordList.add(word);
						
					}//if not comment
				}//for each line
			}//else (text not empty)
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			wordList = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			wordList = null;
		}
		
		
		
		return wordList;
	}
	
}





