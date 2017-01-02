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



public class SynsetTerm 
{
	private String term;
	private int senseNumber;
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//constructors
	//////////////////////////////////////////////////////////////////////////////////////////
	public SynsetTerm()
	{
		
	}
	public SynsetTerm(final String term, final int senseNumber) 
	{
		this.term = term;
		this.senseNumber = senseNumber;
	}

	
	
	
	
	
	/*--------------------------------------------------------------------------------------------
	 * 					FUNCTIONS
	 * -----------------------------------------------------------------------------------------*/
	/**constructs the attributes of SynsetTerm from the given string
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - an instance to a string that consists of word-sense number pair.
	 * @param divider - an instance to a string that divides the pair.
	 * 
	 * @return boolean - if any of the parameters are null or empty returns null.
	 * 					 if the text does not contain the divider or contains more than one returns null.
	 * 					 otherwise, if the operation is successful return true.
	 */
	public boolean setFromString(final String text, final String divider)
	{
		boolean retVal = true;
		
		try
		{
			if(text == null || text.isEmpty())
			{
				throw new IllegalArgumentException("illegal text input\n");
			}
			else if(divider == null || divider.isEmpty())
			{
				throw new IllegalArgumentException("Illegal divider\n");
			}
			
			
			String[] temp = text.split(divider);
			
			if(temp.length != 2)
			{
				throw new IllegalArgumentException("Incompatible text input");
			}
			
			term = temp[0].trim();
			senseNumber = Integer.parseInt(temp[1].trim());
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	/*--------------------------------------------------------------------------------------------
	 * 					setters getters and etc.
	 * -----------------------------------------------------------------------------------------*/
	@Override
	public String toString()
	{
		return new String(term + "#" +senseNumber);
	}
	
	
	
	public String getTerm() 
	{
		return term;
	}
	
	public void setTerm(final String term) 
	{
		this.term = term;
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
