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

import com.darg.NLPOperations.dictionary.model.WordNet;
import com.darg.NLPOperations.dictionary.model.WordNetSense;
import com.darg.NLPOperations.dictionary.model.WordNetWord;
import com.darg.NLPOperations.pos.util.POSTagWordNet;

public class WordNetParser 
{
	private WordNet version;
	
	
	public WordNetParser(final WordNet version) 
	{
		this.version = version;
	}
	
	
	
	
	
	/*---------------------------------------------------------------------------*/
	//				FUNCTIONS
	/*---------------------------------------------------------------------------*/
	/**parses the index files of the WordNet dictionary. and return all of the elements
	 * in an array. Compatible with WordNet 3.0
	 * 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance corresponding to a text that is read from an index file
	 *  			 (index.noun, index.adverb etc.) 
	 *  
	 * @return ArrayList<WordNetWord> - a list of words contained in that text. returns null if there is any error
	 * 									concerning the input such ass wrong file structure.
	 */

	public synchronized ArrayList<WordNetWord> parseWordNetIndex(final String text)
	{
		ArrayList<WordNetWord> tempList = null;
		
		switch(version)
		{
			case VERSION_30: tempList =  parseWordNetIndex30(text);
						 	 break;
						 
			default: tempList = null;			 
		}
		
		return tempList;
	}
	
	
	
	
	
	
	/**parses the sense file of the WordNet dictionary. and return all of the elements
	 * in an array. Compatible with WordNet 3.0
	 * 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance corresponding to a text that is read from a sense file (index.sense) 
	 *  
	 * @return ArrayList<WordNetWord> - a list of words contained in that text. returns null if there is any error
	 * 									concerning the input such ass wrong file structure.	 */
	public synchronized ArrayList<WordNetSense> parseWordNetSense(final String text)
	{
		ArrayList<WordNetSense> tempList = null;
		
		switch(version)
		{
			case VERSION_30: tempList =  parseWordNetSense30(text);
						 	 break;
						 
			default: tempList = null;			 
		}
		
		return tempList;
	}

	
	
	
	
	
	
	
	
	
	/**parses the index files of the WordNet dictionary. and return all of the elements
	 * in an array. 
	 * For version 3.0
	 * 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance corresponding to a text that is read from an index file
	 *  			 (index.noun, index.adverb etc.) 
	 *  
	 * @return ArrayList<WordNetWord> - a list of words contained in that text. returns null if there is any error
	 * 									concerning the input such ass wrong file structure.
	 */
	private synchronized ArrayList<WordNetWord> parseWordNetIndex30(final String text)
	{
		WordNetWord wordNetWord = null; 
		
		
		ArrayList<WordNetWord> words = new ArrayList<WordNetWord>();
		String word = null;
		POSTagWordNet pos;
		
		
		try
		{
			if(text != null && text.length() >= 2)//if the text is acceptable
			{
				String[] lines = text.split("\n");
				
				
				for(String line: lines)//for each line
				{
					String[] tempWords = line.split(" ");
					
					if(tempWords[0].length() >= 1)//if it is a comment line it will be empty string
					{
						word = tempWords[0];
						
						if(tempWords[1].length() == 1)//check for pos tag
						{
							pos = POSTagWordNet.getPosType(tempWords[1].charAt(0));
							
							if(pos == POSTagWordNet.OTHER)
							{
								throw new IllegalArgumentException("Illegal pos tag: " + tempWords[1]);
							}
						}
						else
						{
							throw new IllegalArgumentException("Unexpected argument: " + tempWords[1]);
						}

						int count = tempWords.length-1;
						String tempid = tempWords[count];
						wordNetWord = new WordNetWord(word, pos);
						
						while(tempid.length() == 8)//check for corresponding id's
						{
							wordNetWord.addId(Integer.parseInt(tempid));
							
							count--;
							tempid = tempWords[count];
						}
						
						words.add(wordNetWord);
					}
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			words = null;
		}
		
		
		
		return words;
	}
	
	
	
	
	
	/**parses the sense file of the WordNet dictionary. and return all of the elements
	 * in an array. 
	 * For version 3.0
	 * 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance corresponding to a text that is read from a sense file (index.sense) 
	 *  
	 * @return ArrayList<WordNetWord> - a list of words contained in that text. returns null if there is any error
	 * 									concerning the input such ass wrong file structure.	 */
	private synchronized ArrayList<WordNetSense> parseWordNetSense30(final String text)
	{
		ArrayList<WordNetSense> words = new ArrayList<WordNetSense>();
		String word = null;
		POSTagWordNet pos;
		int id = 0;
		int senseNumber = 0;
		
		
		try
		{
			if(text != null && text.length() >= 2)//if the text is acceptable
			{
				String[] lines = text.split("\n");
				
				
				for(String line: lines)//for each line
				{
					String[] tempWords = line.split(":");
					
					if(tempWords[0].length() >= 1)
					{
						String[] temp = tempWords[0].split("%");
						
						if(temp.length != 2)
						{
							throw new IllegalArgumentException("illegal arguments at: " + line);
						}
						else
						{
							word = temp[0];
							
							switch(Integer.parseInt(temp[1]))
							{
								case 1: pos = POSTagWordNet.NOUN;
										break;
										
								case 2: pos = POSTagWordNet.VERB;
										break;
								
								case 3: pos = POSTagWordNet.ADJECTIVE;
										break;
								
								case 4: pos = POSTagWordNet.ADVERB;
										break;
								
								case 5: pos = POSTagWordNet.ADJECTIVE;
										break;
								
								default: throw new IllegalArgumentException("illegal pos tag at: " + line);
							}
						}
					}
					else
					{
						throw new IllegalArgumentException("illegal argument at: " + line);
					}
					
					String tempString = tempWords[tempWords.length-1];
					
					if(tempString.length() >= 8)
					{
						tempWords = tempString.trim().split(" ");
						
						
						//get the id
						tempString = tempWords[tempWords.length-3];
						
						if(tempString == null || tempString.length() != 8)
						{
							throw new IllegalArgumentException("illegal argument at: " + line);
						}
						
						id = Integer.parseInt(tempString);
						
						
						
						//get the sense number
						tempString = tempWords[tempWords.length-2];
						
						if(tempString == null || tempString.length() >= 3)
						{
							throw new IllegalArgumentException("illegal sense number at: " + line);
						}
						
						senseNumber = Integer.parseInt(tempString);
						
						if(senseNumber >= 100 || senseNumber <=0)
						{
							throw new IllegalArgumentException("illegal sense number at: " + line);
						}
					}
					else
					{
						throw new IllegalArgumentException("illegal argument at: " + line);
					}
					
					
					words.add(new WordNetSense(word, id, pos, senseNumber));
					
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			words = null;
		}
		
		
		
		return words;
	}
	
	
}
