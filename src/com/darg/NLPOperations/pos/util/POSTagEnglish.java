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



/**uses penn treebank POS TAGS to make it compatible with stanford NLP 
 * 
 * @author erhan sezerer
 *
 */
public enum POSTagEnglish 
{
	CC(0), CD(1), DT(2), EX(3), FW(4), IN(5), JJ(6), JJR(7), JJS(8), LS(9),
	MD(10), NN(11), NNS(12), NNP(13), NNPS(14), PDT(15), POS(16), PRP(17), PRP$(18), RB(19),
	RBR(20), RBS(21), RP(22), SYM(23), TO(24), UH(25), VB(26), VBD(27), VBG(28), VBN(29),
	VBP(30), VBZ(31), WDT(32), WP(33), WP$(34), WRB(35), NUMSIGN(36), DOLSIGN(37), QUOTEOPEN(38), QUOTECLOSE(39), 
	COMMA(40), DOT(41), SEMICOLON(42), LCB(43), RCB(44), LRB(45), RRB(46), LSB(47), RSB(48), UNKNOWN(49);
	
	private transient static String quoteOpen = new String(new Character((char) 0x0060).toString() + new Character((char) 0x0060).toString());
	public transient final int value;
	private transient static String[] tagString = {"Coordinating Conjunction" , "Cardinal Number" , "Determiner" , "Existensial There" , "Foreign Word" ,
										 "Preposition or Subordinating Conjunction" , "Adjective" , "Adjective, Comparative" , "Adjective, Superlative" , "List Item Marker" ,
										 "Modal" , "Noun, Singular or Mass" , "Noun, Plural" , "Proper Noun, Singular" , "Proper Noun, Plural" ,
										 "Predeterminer" , "Possessive Ending" , "Personal Pronoun" , "Possessive Pronoun" , "Adverb" ,
										 "Adverb, Comparative" , "Adverb, Superlative" , "Particle" , "Symbol" , "To" , 
										 "Interjection" , "Verb, Base Form" , "Verb, Past Tense" , "Verb, Gerund or Present Participle" , "Verb, Past Participle" ,
										 "Verb, Non-3rd Person Singular Present" , "Verb, 3rd Peerson Singular Present" , "Wh-Determiner" , "Wh-Pronoun" , "Possessive Wh-Pronoun" ,
										 "Wh,Adverb" , "Number Sign" , "Dollar Sign" , "opening qotation mark" , "closing quotation mark" , 
										 "Comma" , "sentence-ending punctuation" , "colon, semicolon or ellipse" , "Left Curly Bracket" , "Right Curly Bracket" ,
										 "Left Round Bracket" , "Right Round Bracket" , "Left Square Bracket" , "Right Square Bracket" , "tag could not be found"};
	
	
	
	//constructor
	POSTagEnglish(final int value)
	{
		this.value = value;
	}

	
	
	
	
	/*--------------------------------------------------------------------------*/
	//                                 FUNCTIONS
	/*--------------------------------------------------------------------------*/
	/**determines whether the given tag is a noun or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish
	 * 
	 * @return boolean - true if the given tag is any kind of noun
	 */
	public static boolean isNoun(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag != null)
		{
			if(tag.equals(NN) || tag.equals(NNP) || tag.equals(NNPS) || tag.equals(NNS))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**determines whether the given tag is a noun or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of noun - 
	 */
	public static boolean isNoun(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(POSTagEnglish.NN.name()) || tag.equalsIgnoreCase(POSTagEnglish.NNP.name())
			   || tag.equalsIgnoreCase(POSTagEnglish.NNPS.name()) || tag.equalsIgnoreCase(POSTagEnglish.NNS.name()))
			{
				retVal = true;
			}
		}
		
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is a verb or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish
	 * 
	 * @return boolean - true if the given tag is any kind of verb
	 */
	public static boolean isVerb(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag != null)
		{
			if(tag.equals(VB) || tag.equals(VBD) || tag.equals(VBG) || tag.equals(VBN) || tag.equals(VBP) || tag.equals(VBZ))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is a verb or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of verb
	 */
	public static boolean isVerb(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(POSTagEnglish.VB.name()) || tag.equalsIgnoreCase(POSTagEnglish.VBD.name())
			  || tag.equalsIgnoreCase(POSTagEnglish.VBN.name()) || tag.equalsIgnoreCase(POSTagEnglish.VBG.name())
			  || tag.equalsIgnoreCase(POSTagEnglish.VBP.name()) || tag.equalsIgnoreCase(POSTagEnglish.VBZ.name()))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}

	
	
	
	/**determines whether the given tag is an adjective or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish
	 * 
	 * @return boolean - true if the given tag is any kind of adjective
	 */
	public static boolean isAdjective(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag != null)
		{
			if(tag.equals(JJ) || tag.equals(JJR) || tag.equals(JJS))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is an adjective or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of adjective
	 */
	public static boolean isAdjective(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(POSTagEnglish.JJ.name()) || tag.equalsIgnoreCase(POSTagEnglish.JJR.name())
			  || tag.equalsIgnoreCase(POSTagEnglish.JJS.name()))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}

	
	
	
	
	/**determines whether the given tag is an adverb or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish
	 * 
	 * @return boolean - true if the given tag is any kind of adverb
	 */
	public static boolean isAdverb(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag != null)
		{
			if(tag.equals(RB) || tag.equals(RBR) || tag.equals(RBS) || tag.equals(WRB))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is an adverb or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of adverb
	 */
	public static boolean isAdverb(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null &&!tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(POSTagEnglish.RB.name()) || tag.equalsIgnoreCase(POSTagEnglish.RBR.name())
			   || tag.equalsIgnoreCase(POSTagEnglish.RBS.name()) || tag.equalsIgnoreCase(POSTagEnglish.WRB.name()))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**determines whether the given tag is a pronoun or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of pronoun
	 */
	public static boolean isPronoun(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag != null)
		{
			if(tag.equals(PRP) || tag.equals(PRP$) || tag.equals(WP) || tag.equals(WP$))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**determines whether the given tag is a pronoun or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a penn treebank tag
	 * 
	 * @return boolean - true if the given tag is any kind of pronoun
	 */
	public static boolean isPronoun(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(POSTagEnglish.PRP.name()) || tag.equalsIgnoreCase(POSTagEnglish.PRP$.name()) ||
				tag.equalsIgnoreCase(POSTagEnglish.WP.name()) || tag.equalsIgnoreCase(POSTagEnglish.WP$.name()))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is a punctuation mark or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish that corresponds to a POSTagEnglish tag
	 * 
	 * @return boolean - true if the given tag is any kind of adverb
	 */
	public static boolean isPunctuation(final POSTagEnglish tag)
	{
		boolean retVal = false;
		
		if(tag.equals(DOT) || tag.equals(COMMA) || tag.equals(DOLSIGN) ||
		   tag.equals(NUMSIGN) || tag.equals(LCB) || tag.equals(LRB) ||
		   tag.equals(LSB) || tag.equals(RCB) || tag.equals(RRB) || tag.equals(RSB)||
		   tag.equals(QUOTECLOSE) || tag.equals(QUOTEOPEN) || tag.equals(SEMICOLON))
		{
			retVal = true;
		}
		
		return retVal;
	}
	
	
	
	
	/**determines whether the given tag is a punctuation mark or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a String that corresponds to a POSTagEnglish tag
	 * 
	 * @return boolean - true if the given tag is any kind of adverb
	 */
	public static boolean isPunctuation(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase(".") || tag.equalsIgnoreCase(",") || tag.equalsIgnoreCase("$") ||
			   tag.equalsIgnoreCase("#") || tag.equalsIgnoreCase("-LCB-") || tag.equalsIgnoreCase("-LSB-") ||
			   tag.equalsIgnoreCase("LRB") || tag.equalsIgnoreCase("-RCB-") || tag.equalsIgnoreCase("-RSB-") || tag.equalsIgnoreCase("-RRB-") ||
			   tag.equalsIgnoreCase("''") || tag.equalsIgnoreCase(":") || tag.equalsIgnoreCase(quoteOpen))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**determines whether the given tag is a punctuation that ends the sentence or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish that corresponds to a POSTagEnglish tag
	 * 
	 * @return boolean - true if the given tag is any kind of sentence terminator
	 */
	public static boolean isEndOfSentence(final POSTagEnglish tag)
	{
		return tag==DOT?true:false;
	}
	
	
	
	
	/**determines whether the given tag is a punctuation that ends the sentence or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - an instance to a POSTagEnglish that corresponds to a POSTagEnglish tag
	 * 
	 * @return boolean - true if the given tag is any kind of sentence terminator
	 */
	public static boolean isEndOfSentence(final String tag)
	{
		boolean retVal = false;
		
		if(tag != null && !tag.isEmpty())
		{
			if(tag.equalsIgnoreCase("."))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**finds the enum representation of a tag string generated from a penn treebank pos tagger.
	 * returns null if there is any error throughout the process.
	 * returns POSTagEnglish.UNKNOWN if the tag cannot be found
	 * 
	 * @author erhan sezerer
	 *
	 * @param tagString - a String instance representing a penn treebank tag
	 * 
	 * @return POSTagEnglish - a tag corresponding the string, null if there are any exception
	 */
	public static POSTagEnglish findTag(final String tagString)
	{
		POSTagEnglish tag = null;
		
		try
		{
			tag = POSTagEnglish.valueOf(tagString);
		}
		catch(IllegalArgumentException ia)
		{
			if(tagString.equalsIgnoreCase("."))
			{
				tag = POSTagEnglish.DOT;
			}
			else if(tagString.equalsIgnoreCase(","))
			{
				tag = POSTagEnglish.COMMA;
			}
			else if(tagString.equalsIgnoreCase("$"))
			{
				tag = POSTagEnglish.DOLSIGN;
			}
			else if(tagString.equalsIgnoreCase("#"))
			{
				tag = POSTagEnglish.NUMSIGN;
			}
			else if(tagString.equalsIgnoreCase("''"))
			{
				tag = POSTagEnglish.QUOTECLOSE;
			}
			else if(tagString.equalsIgnoreCase(quoteOpen))
			{
				tag = POSTagEnglish.QUOTEOPEN;
			}
			else if(tagString.equalsIgnoreCase(":"))
			{
				tag = POSTagEnglish.SEMICOLON;
			}
			else if(tagString.equalsIgnoreCase("-LCB-"))
			{
				tag = POSTagEnglish.LCB;
			}
			else if(tagString.equalsIgnoreCase("-LSB-"))
			{
				tag = POSTagEnglish.LSB;
			}
			else if(tagString.equalsIgnoreCase("-LRB-"))
			{
				tag = POSTagEnglish.LRB;
			}
			else if(tagString.equalsIgnoreCase("-RCB-"))
			{
				tag = POSTagEnglish.RCB;
			}
			else if(tagString.equalsIgnoreCase("-RSB-"))
			{
				tag = POSTagEnglish.RSB;
			}
			else if(tagString.equalsIgnoreCase("-RRB-"))
			{
				tag = POSTagEnglish.RRB;
			}
			else
			{
				tag = POSTagEnglish.UNKNOWN;
			}
			
		}
		catch(NullPointerException ne)
		{
			ne.printStackTrace();
			tag = null;
		}
		
		
		return tag;
	}
	
	
	
	
	
	/**returns the description of the tag.
	 * i.e: for JJ as tag returns adjective
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param tag - a POSTagEnglish instance to learn the description
	 * 
	 * @return String - penn treebank description of a tag, or null if there is some error
	 */
	public static String getDescription(final POSTagEnglish tag)
	{
		String retVal = null;
		
		if(tag != null)
		{
			retVal = new String(tagString[tag.value]);
		}
		
		return retVal;
	}
	
	
	
	
	
	/**returns the description of the tag.
	 * i.e: for JJ as tag returns adjective. 
	 * 
	 * if no such String exist, returns null
	 *
	 * @author erhan sezerer
	 *
	 * @param tag - a POSTagEnglish instance to learn the description
	 * 
	 * @return String - penn treebank description of a tag
	 */
	public static String getDescription(final String tag)
	{
		String retVal = new String("");
		
		try
		{
			retVal = tagString[POSTagEnglish.valueOf(tag).value];
		}
		catch(IllegalArgumentException ie)
		{
			retVal = null;
		}
		catch(NullPointerException ne)
		{
			retVal = null;
		}
		catch(ArrayIndexOutOfBoundsException ae)
		{
			ae.printStackTrace();
			retVal = null;
		}
		
		
		return retVal;
	}

	
}
