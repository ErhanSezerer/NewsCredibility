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
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.pos.util.POSTagConverter;
import com.darg.NLPOperations.pos.util.POSTagWordNet;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;
import com.darg.utils.Normalizer;

/**this class uses average scores as a basis for analysis. for example:
 * assume that;
 * 
 * average positive score for a word = 0.2
 * average negative score for a word = 0.15
 * average objectivity score for a word = 0.65
 * (average of entire dictionary or by type)
 * 
 * average positive score for a word in a specific article = 0.4
 * average negative score for a word in a specific article = 0.1
 * average objectivity score for a word in a specific article = 0.5
 * 
 * increase in positive score = 0.2
 * decrease in negative score = 0.05
 * decrease in objectivity score = 0.15
 * normalize to hundred and use it as a percentage to increase or decrease the 0.333 possibility.
 * (i.e. for positivity-> 33.3 + 33.3%50)
 * 
 * the result is: %49.95 positive
 * 				  %29.14 negative
 * 				  %20.81 objective
 * 
 * 
 * 
 * @author erhan sezerer
 *
 */
public class AverageScoreSentimentAnalyzer 
{
	private WordNetDictionary wordNet;
	private SentiWordNetDictionary sentiWordNet;
	
	
	private boolean control = true;
	private double nounScore[] = {0,0,0};
	private double adjScore[] = {0,0,0};
	private double advScore[] = {0,0,0};
	private double verbScore[] = {0,0,0};
	private double allwordsScore[] = {0,0,0};
	
	ArrayList<Double> dataPos;
	ArrayList<Double> dataNeg;
	ArrayList<Double> dataObj;
	
	
	
	//constructors
	@SuppressWarnings("unused")
	private AverageScoreSentimentAnalyzer()//prevent users from calling empty constructor
	{
		
	}
	
	public AverageScoreSentimentAnalyzer(final WordNetDictionary wordNet, final SentiWordNetDictionary sentiWordNet) 
	{
		this.wordNet = wordNet;
		this.sentiWordNet = sentiWordNet;
		
		dataPos = new ArrayList<Double>();
		dataNeg = new ArrayList<Double>();
		dataObj = new ArrayList<Double>();
		
		
		//load average score variables
		if(!loadAverageScores())
		{
			control = false;
		}
	}

	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			ANALYZER FUNCTIONS 
	//------------------------------------------------------------------------------------------------------------------------------
	/**performs sentimental analysis over a list of words extracted from a document.
	 * if diftag is true, uses tags to calculate average scores. if it is false uses
	 * overall averages.
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param wordList - list of words
	 * @param difTag - a flag for deciding whether to use different average scores for each tag, or
	 * 					to use overall averages.
	 * 
	 * @return SentimentScore - contains all the info about sentiment of the document.
	 */
	public SentimentScore documentBasedAnalysis(final ArrayList<TaggedWord> wordList, final boolean difTag)
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
				for(int i=0; i<size && control; i++)
				{
					//get the id of the word from the WordNet dictionary
					tempWord = wordList.get(i);
					tempTag = POSTagConverter.getPOSType(tempWord.getTag());
					
					
					tempIds = wordNet.findIDsFromSense(tempWord.getWord(), tempTag);
					
					//if the is word found
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
						//general_score = word's_score - expected score of that word 
						if(!difTag)
						{
							posScore += (subPosScore - allwordsScore[0]);
			   			 	negScore += (subNegScore - allwordsScore[1]);
			   			 	objScore += (subObjScore - allwordsScore[2]);
						}
						else
						{
							switch(tempTag)
							{
								case NOUN: posScore += (subPosScore - nounScore[0]);
										   negScore += (subNegScore - nounScore[1]);
										   objScore += (subObjScore - nounScore[2]);
										   break;
										   
								case ADVERB: posScore += (subPosScore - advScore[0]);
								   			 negScore += (subNegScore - advScore[1]);
								   			 objScore += (subObjScore - advScore[2]);
								   			 break;
								   			 
								case ADJECTIVE: posScore += (subPosScore - adjScore[0]);
								   		   		negScore += (subNegScore - adjScore[1]);
								   		   		objScore += (subObjScore - adjScore[2]);
								   		   		break;
								   
								case VERB: posScore += (subPosScore - verbScore[0]);
						   			 	   negScore += (subNegScore - verbScore[1]);
						   			 	   objScore += (subObjScore - verbScore[2]);
						   			 	   break;
						   			 		 
						   		default: //do nothing since possible errors of postags are already 
						   				 //handled while calling wordnet dictionary just after
						   				 //the for loop above
						   				 break; 
							}
						}//if we don't differentiate tags
					}//if the word is found
				}//for each word in the article
				
				
				
				if(!control)
				{
					throw new Exception("cannot load average values from sentiwordnet");
				}
				else
				{
					
					score = new SentimentScore();
					posScore /= size;
					negScore /= size;
					objScore /= size;
					
					
					
					double[] tempList = Normalizer.normalizeSumAbsolute(new double[]{negScore, posScore, objScore}, 1);
					
					score.setNegativeProb(33.33 + 33.33*tempList[0]);
					score.setNegativeScore(totalNegScore);
					score.setNegCount(negCount);

					score.setPositiveProb(33.33 + 33.33*tempList[1]);
					score.setPositiveScore(totalPosScore);
					score.setPosCount(posCount);

					score.setObjectiveProb(33.33 + 33.33*tempList[2]);
					score.setObjectiveScore(totalObjScore);
					score.setObjCount(objCount);
					
					score.setWordCount(size);
					score.setWordsFound(foundWords);
					score.setArticle(article);
				}
				

		}
		catch(Exception e)
		{
			e.printStackTrace();
			score = null;
		}
		
		return score;
	}
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			OTHER FUNCTIONS 
	//------------------------------------------------------------------------------------------------------------------------------
	/**loads the average score values from senti word net dictionary.
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - false if there is a problem loadin any of the scores.
	 */
	private boolean loadAverageScores()
	{
		boolean control = true;
		
		nounScore[0] = sentiWordNet.findAveragePositiveScore(POSTagWordNet.NOUN);
		nounScore[1] = sentiWordNet.findAverageNegativeScore(POSTagWordNet.NOUN);
		nounScore[2] = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.NOUN);
		verbScore[0] = sentiWordNet.findAveragePositiveScore(POSTagWordNet.VERB);
		verbScore[1] = sentiWordNet.findAverageNegativeScore(POSTagWordNet.VERB);
		verbScore[2] = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.VERB);
		adjScore[0] = sentiWordNet.findAveragePositiveScore(POSTagWordNet.ADJECTIVE);
		adjScore[1] = sentiWordNet.findAverageNegativeScore(POSTagWordNet.ADJECTIVE);
		adjScore[2] = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.ADJECTIVE);
		advScore[0] = sentiWordNet.findAveragePositiveScore(POSTagWordNet.ADVERB);
		advScore[1] = sentiWordNet.findAverageNegativeScore(POSTagWordNet.ADVERB);
		advScore[2] = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.ADVERB);
		allwordsScore[0] = sentiWordNet.findAveragePositiveScore(POSTagWordNet.OTHER);
		allwordsScore[1] = sentiWordNet.findAverageNegativeScore(POSTagWordNet.OTHER);
		allwordsScore[2] = sentiWordNet.findAverageObjectiveScore(POSTagWordNet.OTHER);
		
		
		if(nounScore[0] == 0 || nounScore[0] == 0 || nounScore[0] == 0 ||
		   verbScore[0] == 0 || verbScore[0] == 0 || verbScore[0] == 0 ||
		   adjScore[0] == 0 || adjScore[0] == 0 || adjScore[0] == 0 ||
		   allwordsScore[0] == 0 || allwordsScore[0] == 0 || allwordsScore[0] == 0)
		{
			control = false;
		}

		dataPos = sentiWordNet.getDataPos();
		dataNeg = sentiWordNet.getDataNeg();
		dataObj = sentiWordNet.getDataObj();
		
		
		if(control && (dataPos == null || dataPos.isEmpty() || 
		   dataNeg == null || dataNeg.isEmpty() || 
		   dataObj == null || dataObj.isEmpty()))
		{
			control = false;
		}
			

		return control;
	}
	
}






