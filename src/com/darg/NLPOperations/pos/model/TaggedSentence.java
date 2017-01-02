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
package com.darg.NLPOperations.pos.model;


import java.util.ArrayList;

import com.darg.NLPOperations.pos.util.POSTagEnglish;


public class TaggedSentence 
{
	private ArrayList<TaggedWord> words;

	
	
	//constructors
	public TaggedSentence()
	{
		words = new ArrayList<TaggedWord>();
	}
	
	public TaggedSentence(final ArrayList<TaggedWord> words) 
	{
		this.words = words;
	}
	
	
	
	
	/*-----------------------------------------------------------------------------------------------*/
	//									FUNCTIONS
	/*-----------------------------------------------------------------------------------------------*/
	/**construct a string that consists of the word-tag pairs in the sentences
	 * 
	 * @author erhan sezerer
	 *
	 * @return String - a tagged sentence or null if the sentence is empty
	 */
	public String toTaggedString()
	{
		String sentence = new String("");
		int size = words.size();
		
		
		if(size == 0)
		{
			sentence = null;
		}
		
		
		for (int i=0;  i<size; i++)
		{
			sentence = sentence.concat(words.get(i).toString());
		}
		
		
		return sentence;
	}
	
	
	
	/**adds the given word to the end of the sentence
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - an instance to the taggedWord object, represents word-tag pair
	 * 
	 * @return boolean true if the operation is successful
	 */
	public boolean appendToSentence(final TaggedWord word)
	{
		return words.add(word);
	}
	
	
	
	
	/**construct a list of strings from all of the tags in this sentence. 
	 * If the sentence is empty, the list that this function returns will be empty too
	 * 
	 * @author erhan sezerer
	 *
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getTagStrings()
	{
		ArrayList<String> tags = new ArrayList<String>();
		int count = words.size();
		
		for (int i=0; i<count; i++)
		{
			tags.add(words.get(i).getTagString());
		}
		
		
		return tags;
	}
	
	
	
	
	/**construct a list of POSTagEnglish tags from all of the tags in this sentence. 
	 * If the sentence is empty, the list that this function returns will be empty too

	 * @author erhan sezerer
	 *
	 * @return ArrayList<POSTagEnglish>
	 */
	public ArrayList<POSTagEnglish> getTags()
	{
		ArrayList<POSTagEnglish> tags = new ArrayList<POSTagEnglish>();
		int count = words.size();
		
		for (int i=0; i<count; i++)
		{
			tags.add(words.get(i).getTag());
		}
		
		
		return tags;
	}

	
	
	
	/*-----------------------------------------------------------------------------------------------*/
	//									SETTERS GETTERS AND ETC.
	/*-----------------------------------------------------------------------------------------------*/
	@Override
	public String toString()
	{
		String sentence = new String("");
		int size = words.size();
		
		for (int i=0;  i<size; i++)
		{
			sentence = sentence.concat(words.get(i).getWord() + " ");
		}
		
		return sentence;
	}

	
	public ArrayList<TaggedWord> getWords() 
	{
		return words;
	}

	public void setWords(final ArrayList<TaggedWord> words) 
	{
		this.words = words;
	}
	
	

}
