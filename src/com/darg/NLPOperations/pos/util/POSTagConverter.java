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



/**This class works as a medium for POSTagEnglish and POSTagWordNet classes, making them compatible.
 *  
 * @author erhan sezerer
 *
 */
public final class POSTagConverter 
{
	
	
	
	/** takes a penn treebank tag as a string and returns the type of the tag as a tag recognizable by
	 * wordnet dictionary. If not found returns POSTagWordNet.OTHER
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - a string instance to a penn treebank tag. 
	 * 
	 * @return POSTagWordNet - a tag recognizable by wordnet.
	 */
	public static POSTagWordNet getPOSType(final String tag)
	{
		POSTagWordNet simplifiedTag;
		
		if(POSTagEnglish.isAdjective(tag))
		{
			simplifiedTag = POSTagWordNet.ADJECTIVE;
		}
		else if(POSTagEnglish.isAdverb(tag))
		{
			simplifiedTag = POSTagWordNet.ADVERB;
		}
		else if(POSTagEnglish.isNoun(tag))
		{
			simplifiedTag = POSTagWordNet.NOUN;
		}
		else if(POSTagEnglish.isVerb(tag))
		{
			simplifiedTag = POSTagWordNet.VERB;
		}
		else
		{
			simplifiedTag = POSTagWordNet.OTHER;
		}
		
		
		return simplifiedTag;
	}
	
	
	
	
	/**takes a penn treebank tag as a POSTagEnglish and returns the type of the tag as a tag recognizable by
	 * wordnet dictionary. If not found returns POSTagWordNet.OTHER
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - a POSTagEnglish instance to a penn treebank tag. 
	 * 
	 * @return POSTagWordNet - a tag recognizable by wordnet.
	  */
	public static POSTagWordNet getPOSType(POSTagEnglish tag)
	{
		POSTagWordNet simplifiedTag;
		
		if(POSTagEnglish.isAdjective(tag))
		{
			simplifiedTag = POSTagWordNet.ADJECTIVE;
		}
		else if(POSTagEnglish.isAdverb(tag))
		{
			simplifiedTag = POSTagWordNet.ADVERB;
		}
		else if(POSTagEnglish.isNoun(tag))
		{
			simplifiedTag = POSTagWordNet.NOUN;
		}
		else if(POSTagEnglish.isVerb(tag))
		{
			simplifiedTag = POSTagWordNet.VERB;
		}
		else
		{
			simplifiedTag = POSTagWordNet.OTHER;
		}
		
		return simplifiedTag;
	}
	
	
	
	

}
