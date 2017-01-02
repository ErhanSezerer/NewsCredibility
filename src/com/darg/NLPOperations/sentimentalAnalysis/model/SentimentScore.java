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
package com.darg.NLPOperations.sentimentalAnalysis.model;

public class SentimentScore 
{
	private String article;
	private double negativeScore;
	private double negativeProb;
	private int negCount;
	private double positiveScore;
	private double positiveProb;
	private int posCount;
	private double objectiveScore;
	private double objectiveProb;
	private int objCount;
	private int wordCount;
	private int wordsFound;
	
	
	

	//constructors
	public SentimentScore()
	{
		article = "";
		
		negativeScore = 0;
		positiveScore = 0;
		objectiveScore = 0;
		
		negativeProb = 0;
		positiveProb = 0;
		objectiveProb = 0;
		
		wordCount = 0;
		wordsFound = 0;
		
		negCount = 0;
		posCount = 0;
		objCount = 0;
	}
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{
		String retVal;
		if(article == null || article.isEmpty())
		{
			retVal = new String("article variable not set. might be an empty article!");
		}
		else
		{
			retVal = new String("\nNegative score: " + negativeScore +
					   "\nPositive score: " + positiveScore + 
					   "\nObjectivity score: " + objectiveScore + 
					   "\n\nNegative probability: " + negativeProb + 
					   "\nPositive probability: " + positiveProb + 
					   "\nObjective Probability: " + objectiveProb + 
					   "\n\nWord Count: " + wordCount + 
					   "\nWords Found: " + wordsFound + 
					   "\nNegative Word Count: " + negCount + 
					   "\nPositive Word Count: " + posCount +
					   "\nObjective Word Count: " + objCount);
		}
		
		
		
		return retVal;
	}
	

	
	
	
	
	/**returns true if this score object belongs to an empty article/sentence/aspect, false otherwise
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		boolean retVal = false;
		
		if(article == null || article.isEmpty())
		{
			if(positiveProb == 0 && negativeProb== 0 && objectiveProb == 0)
			{
				retVal = true;
			}
		}
		
		
		return retVal;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//setters and getters
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getArticle() 
	{
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	
	
	
	
	public double getNegativeProb() 
	{
		return negativeProb;
	}
	
	
	public void setNegativeProb(final double negativeProb) 
	{
		this.negativeProb = negativeProb;
	}
	
	
	public double getPositiveProb() 
	{
		return positiveProb;
	}
	
	
	public void setPositiveProb(final double positiveProb) 
	{
		this.positiveProb = positiveProb;
	}
	
	
	public double getObjectiveProb() 
	{
		return objectiveProb;
	}
	
	
	public void setObjectiveProb(final double objectiveProb) 
	{
		this.objectiveProb = objectiveProb;
	}
	
	
	public int getWordCount() 
	{
		return wordCount;
	}
	
	
	public void setWordCount(final int wordCount) 
	{
		this.wordCount = wordCount;
	}
	
	
	public int getWordsFound() 
	{
		return wordsFound;
	}
	
	
	public void setWordsFound(final int wordsFound) 
	{
		this.wordsFound = wordsFound;
	}
	
	
	public int getNegCount() 
	{
		return negCount;
	}
	
	
	public void setNegCount(final int negCount) 
	{
		this.negCount = negCount;
	}
	
	
	public int getPosCount() 
	{
		return posCount;
	}
	
	
	public void setPosCount(final int posCount) 
	{
		this.posCount = posCount;
	}
	
	
	public int getObjCount() 
	{
		return objCount;
	}
	
	public void setObjCount(final int objCount) 
	{
		this.objCount = objCount;
	}

	public double getNegativeScore() 
	{
		return negativeScore;
	}

	public void setNegativeScore(final double negativeScore) 
	{
		this.negativeScore = negativeScore;
	}

	public double getPositiveScore() 
	{
		return positiveScore;
	}

	public void setPositiveScore(final double positiveScore) 
	{
		this.positiveScore = positiveScore;
	}

	public double getObjectiveScore() 
	{
		return objectiveScore;
	}

	public void setObjectiveScore(final double objectiveScore) 
	{
		this.objectiveScore = objectiveScore;
	}
	
}
