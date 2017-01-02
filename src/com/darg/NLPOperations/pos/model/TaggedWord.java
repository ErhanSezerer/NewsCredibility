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

import com.darg.NLPOperations.pos.util.POSTagEnglish;





public class TaggedWord 
{
	private String word;
	private String tagString;
	private POSTagEnglish tag;
	
	
	//constructors
	public TaggedWord()
	{
		
	}
	
	public TaggedWord(final String word, final POSTagEnglish tag, final String tagString) 
	{
		this.word = word;
		this.tag = tag;
		this.tagString = tagString;
	}

	
	
	
	/*---------------------------------------------------------------------------------------*/
	//							FUNCTIONS
	/*---------------------------------------------------------------------------------------*/
	/**constructs the word tag pair from the given string.
	 * Uses underscore to split the pair. compatible with stanford postagger library
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a string representation of a word from tokenized tagged text.
	 * 
	 * @return boolean - if the operation is successful returns true.
	 */
	public boolean setPair(final String text)
	{
		boolean retVal = false;
		
		if(text != null && text.contains("_"))
		{
			String[] pair = text.trim().split("_");
			
			if(pair.length == 2)
			{
				word = pair[0];
				tag = POSTagEnglish.findTag(pair[1]);
				tagString = pair[1];
				
				if(tag != null && word != null && word.length() != 0)
				{
					retVal = true;
				}
			}
		}
		
		return retVal;
	}
	
	
	
	/**constructs the word tag pair from the given string.
	 * Uses the delimeter parameter to split the pair.
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a string representation of a word from tokenized tagged text.
	 * @param delimeter - a string used to split the word-tag pair
	 * 
	 * @return boolean - if the operation is successful returns true.
	 */
	public boolean setPair(final String text, final String divider)
	{
		boolean retVal = false;
		
		if(text != null && text.contains(divider))
		{
			String[] pair = text.trim().split(divider);
			
			if(pair.length == 2)
			{
				word = pair[0];
				tag = POSTagEnglish.findTag(pair[1]);
				tagString = pair[1];
				
				if(tag != null && word != null && word.length() != 0)
				{
					retVal = true;
				}
			}
		}
		
		return retVal;
	}

	
	
	
	
	/*---------------------------------------------------------------------------------------*/
	//							SETTERS GETTERS ETC.
	/*---------------------------------------------------------------------------------------*/
	/**returns the word-tag pair as a string
	 * 
	 * @author erhan sezerer
	 * 
	 * @return String - string representation of a word tag pair
	 * 
	 */
	@Override
	public String toString()
	{
		return new String(word + "_" + tagString);
	}
	
	
	
	public String getTagString() 
	{
		return tagString;
	}

	public void setTagString(final String tagString) 
	{
		this.tagString = tagString;
	}

	public String getWord() 
	{
		return word;
	}

	public void setWord(final String word) 
	{
		this.word = word;
	}

	public POSTagEnglish getTag() 
	{
		return tag;
	}

	public void setTag(final POSTagEnglish tag) 
	{
		this.tag = tag;
	}
}
