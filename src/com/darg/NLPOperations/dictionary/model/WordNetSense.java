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

import com.darg.NLPOperations.pos.util.POSTagWordNet;




/**stores the word and its corresponding id and pos.
 * can return id as 8 digit string. 
 * 
 * @author erhan sezerer
 *
 */
public class WordNetSense 
{
	private String word;
	private int id;
	private POSTagWordNet pos; // n=noun, v=verb, a=adjective, r=adverb
	private int senseNumber;
	
	
	
	//constructors
	@SuppressWarnings("unused")
	private WordNetSense()//prevent users from calling it
	{
		
	}
	public WordNetSense(final String word, final int id, final POSTagWordNet pos, final int senseNumber) 
	{
		this.word = word;
		this.id = id;
		this.pos = pos;
		this.senseNumber = senseNumber;
	}
	
	
	
	/*---------------------------------------------------------------------------------------*/
	//				FUNCTIONS
	/*---------------------------------------------------------------------------------------*/

	
	
	
	
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
	
	public int getId() 
	{
		return id;
	}
	
	public String getIdAsString()
	{
		return String.format("%08d", id);
	}
	
	public void setId(final int id) 
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
	
	public int getSenseNumber() 
	{
		return senseNumber;
	}
	
	public void setSenseNumber(final int senseNumber) 
	{
		this.senseNumber = senseNumber;
	}
	
}
