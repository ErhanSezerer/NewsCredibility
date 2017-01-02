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

package com.darg.NLPOperations.sentimentalAnalysis;

import java.util.ArrayList;


import com.darg.NLPOperations.dictionary.SentiWordNetDictionary;
import com.darg.NLPOperations.dictionary.WordNetDictionary;
import com.darg.NLPOperations.dictionary.model.SentiWordNetWord;
import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.pos.util.POSTagConverter;
import com.darg.NLPOperations.pos.util.POSTagWordNet;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScoreType;

public class NaiveBayesSentimentAnalyzer implements SentimentAnalyzer
{
	
	private WordNetDictionary wordNet;
	private SentiWordNetDictionary sentiWordNet;
	
	//for m-estimate of conditional probability
	private double objWordCount;
	private double negWordCount;
	private double posWordCount;
	private double wordcount;
	
	private double averageObjScore;
	private double averageNegScore;
	private double averagePosScore;
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////			CONSTRUCTORS    			/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	private NaiveBayesSentimentAnalyzer()
	{
		
	}
	public NaiveBayesSentimentAnalyzer(final WordNetDictionary wordNet, final SentiWordNetDictionary sentiWordNet) 
	{
		this.wordNet = wordNet;
		this.sentiWordNet = sentiWordNet;
		objWordCount = sentiWordNet.getObjectiveWords(0.5, false).size();
		negWordCount = sentiWordNet.getWordsWithNegativeScore(0.5, false).size();
		posWordCount = sentiWordNet.getWordsWithPositiveScore(0.5, false).size();
		wordcount = sentiWordNet.getWordCount();
	
		averageObjScore = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.OTHER);
		averageNegScore = sentiWordNet.findAverageNegativeScore(POSTagWordNet.OTHER);
		averagePosScore = sentiWordNet.findAveragePositiveScore(POSTagWordNet.OTHER);
	}






	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////			IMPLEMENTED METHODS			/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@Deprecated
	public SentimentScore documentBasedAnalysis(ArrayList<TaggedWord> wordList) 
	{
		return null;
	}


	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<SentimentScore> sentenceBasedAnalysis(ArrayList<TaggedSentence> wordList) 
	{
		//variables
		ArrayList<SentimentScore> scores = new ArrayList<SentimentScore>();
		
		String article = "";//article content with scores
		double negScore = 1;//will hold probabilities. initialize to 1 because we will multiply them using naive bayes
		double posScore = 1;
		double objScore = 1;
		double totalNegScore = 0;//total scores
		double totalPosScore = 0;
		double totalObjScore = 0;
		int foundWords = 0;
		int totalWords = 0;
		int negCount = 0;
		int posCount = 0;
		int objCount = 0;
		int size = wordList.size();
		
		
		//temporary variables for inner loops
		boolean emptySentence = true;
		double subNegScore = 0;
		double subPosScore = 0;
		double subObjScore = 0;
		SentimentScore sentiScore;
		int idSize = 0;
		int sentenceSize = 0;
		TaggedSentence tempSentence;
		TaggedWord tempWord;
		SentiWordNetWord tempSentiWord;
		POSTagWordNet tempTag;
		ArrayList<Integer> tempIds;
		
		
		try
		{
			
			if(wordList != null && !wordList.isEmpty())
			{
				//for every sentence
				for (int i=0; i<size; i++)
				{
					tempSentence = wordList.get(i);
					
					
					//if the sentence is not empty
					if(tempSentence != null && !tempSentence.getWords().isEmpty())
					{
						emptySentence = false;
						sentenceSize = tempSentence.getWords().size();
						totalWords += sentenceSize;
						
						
						//for every word
						for (int j=0; j<sentenceSize ; j++)
						{
							//get the words properties
							tempWord = tempSentence.getWords().get(j);
							tempTag = POSTagConverter.getPOSType(tempWord.getTag());
							
							//find the word from wordnet
							tempIds = wordNet.findIDsFromSense(tempWord.getWord(), tempTag);
							
							//if the word is found
							if(tempIds != null && !tempIds.isEmpty())
							{
								idSize = tempIds.size();
								foundWords++;
								
								
								//get the score of every id found
								for (int k=0; k<idSize; k++)
								{							
									tempSentiWord = sentiWordNet.findWord(tempIds.get(k), tempTag);
								
									subNegScore += tempSentiWord.getNegativeScore();
									subPosScore += tempSentiWord.getPositiveScore();
									subObjScore += tempSentiWord.getObjectivityScore();
								}
								
								subPosScore /= idSize;
								subNegScore /= idSize;
								subObjScore /= idSize;
								
								
								
								//add to the total score
								totalNegScore += subNegScore;
								totalPosScore += subPosScore;
								totalObjScore += subObjScore;
								
								
								//count how many negative or positive words in the article
								if(subNegScore > 0)
								{
									negCount++;
								}
								if(subPosScore > 0)
								{
									posCount++;
								}
								if(subObjScore > 0)
								{
									objCount++;
								}
							}//if the  word is found
							
							
							//update the article with scores
							article = article.concat(tempWord.getWord() + "_" + Math.round( subObjScore * 100.0 ) / 100.0 + ":" + Math.round( subPosScore * 100.0 ) / 100.0 + ":" + Math.round( subNegScore * 100.0 ) / 100.0 + " ");

							
							//calculate the probabilities.
							posScore = posScore * calculateBayesianProbability(subPosScore, SentimentScoreType.POS);
							negScore = negScore * calculateBayesianProbability(subNegScore, SentimentScoreType.NEG);
							objScore = objScore * calculateBayesianProbability(subObjScore, SentimentScoreType.OBJ);

							subNegScore = 0;
							subPosScore = 0;
							subObjScore = 0;
							
							
							
						}//for every word
						
					}//if the sentence is not empty
					else
					{
						emptySentence = true;
					}
					
					
					
					
					//add the score of the sentence to the list
					sentiScore = new SentimentScore();
					
					if(emptySentence)//if the sentence is empty
					{
						sentiScore.setNegativeProb(0);
						sentiScore.setNegativeScore(0);
						sentiScore.setNegCount(0);
						sentiScore.setPositiveProb(0);
						sentiScore.setPositiveScore(0);
						sentiScore.setPosCount(0);
						
						sentiScore.setObjectiveProb(0);
						sentiScore.setObjectiveScore(0);
						sentiScore.setObjCount(0);
						
						sentiScore.setWordCount(0);
						sentiScore.setWordsFound(0);
						sentiScore.setArticle(null);
						
						scores.add(sentiScore);
					}
					else
					{
						sentiScore.setNegativeProb(negScore);
						sentiScore.setNegativeScore(totalNegScore);
						sentiScore.setNegCount(negCount);
						
						sentiScore.setPositiveProb(posScore);
						sentiScore.setPositiveScore(totalPosScore);
						sentiScore.setPosCount(posCount);
						
						sentiScore.setObjectiveProb(objScore);
						sentiScore.setObjectiveScore(totalObjScore);
						sentiScore.setObjCount(objCount);
						
						sentiScore.setWordCount(totalWords);
						sentiScore.setWordsFound(foundWords);
						sentiScore.setArticle(article);
						scores.add(sentiScore);
					}
					
					negScore = 1;
					posScore = 1;
					objScore = 1;
					article = "";
					
				}//for every sentences
			}//if the list is not empty
			else
			{
				scores = null;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			scores = null;
		}
		
		
		
		return scores;
	}




	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@Deprecated
	public ArrayList<SentimentScore> aspectBasedAnalysis(ArrayList<TaggedWord> wordList) 
	{
		return null;
	}
	
	

	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////			OTHER METHODS			/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private double calculateBayesianProbability(final double score, final SentimentScoreType type) throws Exception
	{
		double probability = score;

		switch(type)
		{
			case POS: probability = score + (wordcount*averagePosScore) / (posWordCount + wordcount);
					  break;
					
			case NEG: probability = score + (wordcount*averageNegScore) / (negWordCount + wordcount);
					  break;
				
			case OBJ: probability = score + (wordcount*averageObjScore) / (objWordCount + wordcount);
			  		  break;
				
			default: throw new Exception("Error: Unknown score type\n");
		}
		
		
		return probability;
	}
	
	
}
