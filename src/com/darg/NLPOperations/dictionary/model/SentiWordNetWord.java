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

public class SentiWordNetWord 
{
	private POSTagWordNet pos;
	private Integer id;
	private float positiveScore;
	private float negativeScore;
	private ArrayList<SynsetTerm> synsetTerms;
	private String glossary;
	
	
	
	//constructors
	public SentiWordNetWord() 
	{
		id = 0;
		positiveScore = 0;
		negativeScore = 0;
		synsetTerms = new ArrayList<SynsetTerm>();
		glossary = null;
		pos = POSTagWordNet.OTHER;
		
	}

	public SentiWordNetWord(final POSTagWordNet pos, final Integer id, final float posScore,
			final float negScore, final ArrayList<SynsetTerm> synsetTerms, final String glossary) 
	{
		this.pos = pos;
		this.id = id;
		this.positiveScore = posScore;
		this.negativeScore = negScore;
		this.synsetTerms = synsetTerms;
		this.glossary = glossary;
	}


	
	
	/*----------------------------------------------------------------------------------------------------------*/
	//						FUNCTIONS
	/*----------------------------------------------------------------------------------------------------------*/
	/**returns the id as a 8digit string
	 * 
	 * @author erhan sezerer
	 *
	 * @return String - id
	 */
	public String getIdAsString()
	{
		return String.format("%08d", id.toString());
	}
	
	
	
	/**calculates the objectivity of the word by using its positive and negative scores.
	 * 
	 * @author erhan sezerer
	 *
	 * @return float - objectivity score
	 */
	public float getObjectivityScore()
	{
		return 1-(positiveScore + negativeScore);
	}
	
	
	
	
	
	/**adds a synset term to the list of synsets.
	 * 
	 * @author erhan sezerer
	 *
	 * @param term - a synset term to be added
	 * 
	 * @return boolean - true if the operation is successful
	 */
	public boolean addSynTerm(final SynsetTerm term)
	{
		return synsetTerms.add(term);
	}
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------------------------------*/
	//						SETTERS GETTERS AND ETC.
	/*----------------------------------------------------------------------------------------------------------*/
	@Override
	public String toString()
	{
		String retVal = new String("---------------------------------------\n");
		
		retVal = retVal.concat("pos: " +pos+ "\n");
		retVal = retVal.concat("id: " +id+ "\n");
		retVal = retVal.concat("positive score: " +positiveScore+ "\n");
		retVal = retVal.concat("negative score: " +negativeScore+ "\n");
		retVal = retVal.concat("synset terms: " +synsetTerms.toString()+ "\n");
		retVal = retVal.concat("glossary: " +glossary+ "\n");
		retVal = retVal.concat("---------------------------------------\n");
		
		return retVal;
	}
	
	
	
	
	
	
	public POSTagWordNet getPos() 
	{
		return pos;
	}

	public void setPos(final POSTagWordNet pos) 
	{
		this.pos = pos;
	}

	public Integer getId() 
	{
		return id;
	}

	public void setId(final Integer id) 
	{
		this.id = id;
	}

	public float getPositiveScore() 
	{
		return positiveScore;
	}

	public void setPositiveScore(final float posScore) 
	{
		this.positiveScore = posScore;
	}

	public float getNegativeScore() 
	{
		return negativeScore;
	}

	public void setNegativeScore(final float negScore) 
	{
		this.negativeScore = negScore;
	}

	public ArrayList<SynsetTerm> getSynsetTerms() 
	{
		return synsetTerms;
	}

	public void setSynsetTerms(final ArrayList<SynsetTerm> synsetTerms) 
	{
		this.synsetTerms = synsetTerms;
	}

	public String getGlossary() 
	{
		return glossary;
	}

	public void setGlossary(final String glossary) 
	{
		this.glossary = glossary;
	}
	
	
	

}
