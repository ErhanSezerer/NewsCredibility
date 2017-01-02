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
package com.darg.NLPOperations.dictionary.model;

import java.util.ArrayList;

import com.darg.NLPOperations.pos.util.POSTagWordNet;



/**stores the word and its corresponding id and pos.
 * can return id as 8 digit string. 
 * 
 * @author erhan sezerer
 *
 */
public class WordNetWord 
{
	private String word;
	private ArrayList<Integer> id; //index of the id indicates the sense number (exp: id.get(1) will get the word with the sense number 2)
	private POSTagWordNet pos; // n=noun, v=verb, a=adjective, r=adverb
	
	
	
	//constructors
	public WordNetWord(final String word, final POSTagWordNet pos) 
	{
		this.word = word;
		this.id = new ArrayList<Integer>();
		this.pos = pos;
	}
	@SuppressWarnings("unused")
	private WordNetWord()//prevent users from calling it
	{
		
	}
	public WordNetWord(final String word, final ArrayList<Integer> id, final POSTagWordNet pos) 
	{
		this.word = word;
		this.id = id;
		this.pos = pos;
	}
	
	
	
	/*---------------------------------------------------------------------------------------*/
	//				FUNCTIONS
	/*---------------------------------------------------------------------------------------*/
	/**deletes all of the id's corresponding to the word
	 * 
	 * @author erhan sezerer
	 *
	 */
	public void clearId()
	{
		id.clear();
	}
	
	
	
	/**adds the given id to the list of id's that correspond to the word
	 * WARNING: If you want to use sense numbers do not use this function since it assign the ids automatically.
	 * Prone to errors, use with caution.
	 * 
	 * @author erhan sezerer
	 *
	 * @param key - id
	 * 
	 * @return boolean - true if the id is successfully added
	 */
	public boolean addId(final int key)
	{
		return id.add(key);
	}
	
	
	
	
	
	
	/*---------------------------------------------------------------------------------------*/
	//				setters and getters
	/*---------------------------------------------------------------------------------------*/

	@Override
	public String toString()
	{
		return new String(getWord() + " - " + getIdAsString());
	}
	
	public String getWord() 
	{
		return word;
	}
	
	public void setWord(final String word) 
	{
		this.word = word;
	}
	
	public ArrayList<Integer> getId() 
	{
		return id;
	}
	
	public ArrayList<String> getIdAsString()
	{
		ArrayList<String> idString = new ArrayList<String>();
		
		for (int i=0; i<id.size(); i++)
		{
			idString.add(String.format("%08d", id.get(i).toString()));
		}
		
		return idString;
	}
	
	public void setId(final ArrayList<Integer> id) 
	{
		this.id = id;
	}
	
	public POSTagWordNet getPos() 
	{
		return pos;
	}
	
	public void setPos(final POSTagWordNet pos) 
	{
		this.pos = pos;
	}
	
}
