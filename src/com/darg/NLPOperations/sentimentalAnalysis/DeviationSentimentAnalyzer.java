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
import com.darg.RConnector.REngineConnector;
import com.darg.RConnector.model.RException;



public class DeviationSentimentAnalyzer implements SentimentAnalyzer
{
	private WordNetDictionary wordNet;
	private SentiWordNetDictionary sentiWordNet;
	private REngineConnector rConnector;
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////			CONSTRUCTORS    			/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private DeviationSentimentAnalyzer()
	{
		
	}
	public DeviationSentimentAnalyzer(final WordNetDictionary wordNet,final SentiWordNetDictionary sentiWordNet,
									  final REngineConnector rConnector) 
	{
		this.rConnector = rConnector;
		this.wordNet = wordNet;
		this.sentiWordNet = sentiWordNet;
	}




	
	
	


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////			IMPLEMENTED METHODS			/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SentimentScore documentBasedAnalysis(ArrayList<TaggedWord> wordList) 
	{
		//variables
		SentimentScore score = null;
		double negScore = 0;
		double posScore = 0;
		double objScore = 0;
		double totalNegScore = 0;
		double totalPosScore = 0;
		double totalObjScore = 0;
		int foundWords = 0;
		int size = wordList.size();
		int negCount = 0;
		int posCount = 0;
		int objCount = 0;
		String article = "";
		
		
		
		//temporary variables needed for inner loops
		double subNegScore = 0;
		double subPosScore = 0;
		double subObjScore = 0;
		int idSize = 0;
		TaggedWord tempWord;
		SentiWordNetWord tempSentiWord;
		POSTagWordNet tempTag;
		ArrayList<Integer> tempIds;
		

		
		try
		{
				//for every word in the article
				for(int i=0; i<size; i++)
				{
					//get the id of the word from the WordNet dictionary
					tempWord = wordList.get(i);
					tempTag = POSTagConverter.getPOSType(tempWord.getTag());
					
					
					tempIds = wordNet.findIDsFromSense(tempWord.getWord(), tempTag);
					
					
					//if the word is found
					if(tempIds != null && tempIds.size() != 0)
					{
						idSize = tempIds.size();
						foundWords++;
						subNegScore = 0;
						subPosScore = 0;
						subObjScore = 0;
						
						//for every id get the score from SentiWordNet and take the average 
						for(int j=0; j<idSize; j++)
						{
							tempSentiWord = sentiWordNet.findWord(tempIds.get(j), tempTag);
							
							subNegScore += tempSentiWord.getNegativeScore();
							subPosScore += tempSentiWord.getPositiveScore();
							subObjScore += tempSentiWord.getObjectivityScore();
						}
						
						subPosScore /= idSize;
						subNegScore /= idSize;
						subObjScore /= idSize;
						
						
						//update the article with scores
						article = article.concat(tempWord.getWord() + "_" + Math.round( subObjScore * 100.0 ) / 100.0 + ":" + Math.round( subPosScore * 100.0 ) / 100.0 + ":" + Math.round( subNegScore * 100.0 ) / 100.0 + " ");
						
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
						
							
						
						//calculate the difference from expected values.
						double tempPosScore = 0;
						double tempNegScore = 0;
						double tempObjScore = 0;
						
						if(     (tempPosScore = rConnector.evaluateScore(subPosScore, SentimentScoreType.POS)) == -1
							 || (tempNegScore = rConnector.evaluateScore(subNegScore, SentimentScoreType.NEG)) == -1
							 || (tempObjScore = rConnector.evaluateScore(subObjScore, SentimentScoreType.OBJ)) == -1 )
						{
							throw new RException("Cannot evaluate the score of: " + tempWord.getWord());
						}
						else
						{
							posScore += tempPosScore;
							negScore += tempNegScore;
							objScore += tempObjScore;
							article = article.concat(tempWord.getWord() + "_" + Math.round( tempObjScore * 100.0 ) / 100.0 + ":" + Math.round( tempPosScore * 100.0 ) / 100.0 + ":" + Math.round( tempNegScore * 100.0 ) / 100.0 + " ");
							
						}
						
			   			
					}//if the word is found
				}//for each word in the article
				
				
				
				score = new SentimentScore();
				posScore /= foundWords;
				negScore /= foundWords;
				objScore /= foundWords;
					
					
				score.setNegativeProb(negScore);
				score.setNegativeScore(totalNegScore);
				score.setNegCount(negCount);
					
				score.setPositiveProb(posScore);
				score.setPositiveScore(totalPosScore);
				score.setPosCount(posCount);
					
				score.setObjectiveProb(objScore);
				score.setObjectiveScore(totalObjScore);
				score.setObjCount(objCount);
					
				score.setWordCount(size);
				score.setWordsFound(foundWords);
				score.setArticle(article);
				

		}
		catch(Exception e)
		{
			e.printStackTrace();
			score = null;
		}
		
		return score;
	}

	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ArrayList<SentimentScore> sentenceBasedAnalysis(ArrayList<TaggedSentence> wordList) 
	{
		//variables
		ArrayList<SentimentScore> scores = new ArrayList<SentimentScore>();
		
		String article = "";//article content with scores
		double negScore = 0;
		double posScore = 0;
		double objScore = 0;
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
					foundWords = 0;
					
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
							
							subNegScore = 0;
							subPosScore = 0;
							subObjScore = 0;
							
							
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
								
								
								//calculate the probabilities.
								double tempPosScore = 0;
								double tempNegScore = 0;
								double tempObjScore = 0;
								
								if(     (tempPosScore = rConnector.evaluateScore(subPosScore, SentimentScoreType.POS)) == -1
									 || (tempNegScore = rConnector.evaluateScore(subNegScore, SentimentScoreType.NEG)) == -1
									 || (tempObjScore = rConnector.evaluateScore(subObjScore, SentimentScoreType.OBJ)) == -1 )
								{
									throw new RException("Cannot evaluate the score of: " + tempWord.getWord());
								}
								else
								{
									posScore += tempPosScore;
									negScore += tempNegScore;
									objScore += tempObjScore;
									article = article.concat(tempWord.getWord() + "_" + Math.round( tempObjScore * 100.0 ) / 100.0 + ":" + Math.round( tempPosScore * 100.0 ) / 100.0 + ":" + Math.round( tempNegScore * 100.0 ) / 100.0 + " ");
									
								}
								
							}//if the  word is found
							
							
							//update the article with scores
							article = article.concat(tempWord.getWord() + "_" + Math.round( subObjScore * 100.0 ) / 100.0 + ":" + Math.round( subPosScore * 100.0 ) / 100.0 + ":" + Math.round( subNegScore * 100.0 ) / 100.0 + " ");

							
						}//for every word
						
						posScore /= foundWords;
						negScore /= foundWords;
						objScore /= foundWords;
						
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
					
					negScore = 0;
					posScore = 0;
					objScore = 0;
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
	@Deprecated
	@Override
	public ArrayList<SentimentScore> aspectBasedAnalysis(ArrayList<TaggedWord> wordList) 
	{
		return null;
	}
	
}
