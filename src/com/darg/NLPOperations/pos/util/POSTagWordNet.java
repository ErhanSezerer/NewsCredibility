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
package com.darg.NLPOperations.pos.util;

/**pos tags used by wordnet dictionary. Consists of (a=adjective , r=adverb , n=noun, v=verb)
 * 
 * @author erhan sezerer
 *
 */
public enum POSTagWordNet 
{
	ADJECTIVE(0), ADVERB(1), VERB(2), NOUN(3), OTHER(5);
	
	public transient final int value;
	
	//constructor
	POSTagWordNet(final int value)
	{
		this.value = value;
	}

	
	
	

	/*-----------------------------------------------------------------------------------
	 * 					FUNCTIONS
	 -----------------------------------------------------------------------------------*/
	/**converts the given character into the POSTagWordNet type.
	 * character must be one of the supported types for wordnet dictionary otherwise POSTagWordNet.Other 
	 * will be returned.
	 * 
	 * @author erhan sezerer
	 *
	 * @param pos - character denoting the pos tag.
	 * 
	 * @return POSTagWordNet - corresponding tag.
	 */
	public static POSTagWordNet getPosType(final char pos)
	{
		POSTagWordNet tag;
		
		switch(pos)
		{
			case 'a': tag = ADJECTIVE;
					  break;
					  
			case 'r': tag = ADVERB;
					  break;
					  
			case 'v': tag = VERB;
					  break;
					  
			case 'n': tag = NOUN;
					  break;
					  
			default:  tag = OTHER;
		}
		
		return tag;
	}
	
	
	/**converts the pos tag into character representation of the wordnet dictionary.
	 * if not found returns question mark '?'
	 * 
	 * @author erhan sezerer
	 *
	 * @param postag - a POSTagWordNet tag
	 * 
	 * @return char - corresponding pos tag as a char
	 */
	public static char getChar(POSTagWordNet postag)
	{
		char tag = '?';
		
		switch(postag)
		{
			case ADJECTIVE: tag = 'a';
					  break;
					  
			case ADVERB: tag = 'r';
					  break;
					  
			case VERB: tag = 'v';
					  break;
					  
			case NOUN: tag = 'n';
					  break;
					  
			default:  tag = '?';
		}

		
		return tag;
	}
	
	
	
}
