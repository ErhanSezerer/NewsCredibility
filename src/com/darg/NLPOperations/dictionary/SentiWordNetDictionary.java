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
package com.darg.NLPOperations.dictionary;

import java.util.ArrayList;

import com.darg.NLPOperations.dictionary.model.SentiWordNet;
import com.darg.NLPOperations.dictionary.model.SentiWordNetWord;
import com.darg.NLPOperations.dictionary.model.SynsetTerm;
import com.darg.NLPOperations.pos.util.POSTagWordNet;
import com.darg.fileOperations.utils.FileUtils;


/**This singleton class handles the SentiWordNet dictionary by loading retrieving searching it. 
 * For heavy use: the use of indexing and pos tags are recommended.
 * 
 * @author erhan sezerer
 *
 */
public class SentiWordNetDictionary 
{
	private static boolean singletonUsed = false;

	private SentiWordNetParser parser;
	private String dictionaryPath;
	private boolean allowIndexing;
	
	private ArrayList<SentiWordNetWord> words;
	private int wordCount;
	private ArrayList<SentiWordNetWord> adjectives;
	private int adjectiveCount;
	private ArrayList<SentiWordNetWord> adverbs;
	private int adverbCount;
	private ArrayList<SentiWordNetWord> verbs;
	private int verbCount;
	private ArrayList<SentiWordNetWord> nouns;
	private int nounCount;
	

	ArrayList<Double> dataPos;
	ArrayList<Double> dataNeg;
	ArrayList<Double> dataObj;

	
	
	
	
	
	//constructors
	private SentiWordNetDictionary(final String dictionaryPath, final SentiWordNet version, final boolean allowIndexing) 
	{
		parser = new SentiWordNetParser(version);
		this.dictionaryPath = dictionaryPath;
		this.allowIndexing = allowIndexing;
		
		
		words = new ArrayList<SentiWordNetWord>();
		wordCount = 0;
		verbs = new ArrayList<SentiWordNetWord>();
		verbCount = 0;
		adjectives = new ArrayList<SentiWordNetWord>();
		adjectiveCount = 0;
		adverbs = new ArrayList<SentiWordNetWord>();
		adverbCount = 0;
		nouns = new ArrayList<SentiWordNetWord>();
		nounCount = 0;
		
		
		//holds the scores of every word. USed to build average scores.
		dataPos = new ArrayList<Double>();
		dataNeg = new ArrayList<Double>();
		dataObj = new ArrayList<Double>();

	}
	
	
	
	
	/**returns an instance to SentiWordNetDictionary, initialized with the given path and version.
	 * 
	 * @author erhan sezerer
	 *
	 * @param dictionaryPath - path to the sentiWordNet dictionary file.
	 * @param version - version of sentiWordNet to be used.
	 * @param allowIndexing - allows the words to be indexed into different types(verb, adverb, et.) which enables binary search. 
	 * 						  speeds up the searching operations but slows down the library loading time. 
	 * 						  recommended for heavy use.
	 * 
	 * @return SentiWordNetDictionary  - singleton object, if called once returns null.
	 */
	public static synchronized SentiWordNetDictionary getInstance(final String dictionaryPath, final SentiWordNet version, final boolean allowIndexing)
	{
		SentiWordNetDictionary retVal;
		
		if(singletonUsed)
		{
			retVal = null;
		}
		else
		{
			singletonUsed = true;
			retVal = new SentiWordNetDictionary(dictionaryPath, version, allowIndexing);
		}
			
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				LOADER FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**loads the sentiwordnet dictionary to the program.
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it loads the words correctly.
	 */
	public synchronized boolean loadDictionary()
	{
		boolean retVal = false;
		String text = null;
		
		
		
		try
		{
			text = FileUtils.readFile(dictionaryPath);
			
			
			//parameter check
			if(text != null && !text.isEmpty())
			{
				words = parser.parseSentiWordNet(text);
				
				//if parse correctly
				if(words != null)
				{
					retVal = true;
					wordCount = words.size();
			
				
					//if set start indexing for faster search
					if(allowIndexing)
					{
						SentiWordNetWord word;
						
						//for each word, place them where they belong
						for (int i=0; i<wordCount; i++)
						{
							word = words.get(i);
							
							switch(word.getPos())
							{
								case ADJECTIVE: adjectives.add(word);
												adjectiveCount++;
												break;
												
								case NOUN: nouns.add(word);
										   nounCount++;
										   break;
										   
								case VERB: verbs.add(word);
										   verbCount++;
										   break;
										   
								case ADVERB: adverbs.add(word);
											 adverbCount++;
											 break;
											 
								default: throw new IllegalArgumentException("Unknown type encountered at indexing\n");
		
							}
							
							
							dataPos.add((double) word.getPositiveScore());
							dataNeg.add((double) word.getNegativeScore());
							dataObj.add((double) word.getObjectivityScore());
							
						}//for each word
					}//if indexed
				}//if not null
			}
		
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = false;
		}
		catch(Exception e)
		{	
			System.err.print("Unknown exception: ");
			e.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				SEARCH FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**finds and returns the word whose id is given, from the sentiWordNet dictionary. 
	 * TIP: Use findWord(final int id, final int pos) if you can to further speed up the operation, 
	 * unless you don't use indexing.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word we are searching for.
	 * 
	 * @return SentiWordNetWord - instance corresponding to the id or null if it cannot be found.
	 */
	public SentiWordNetWord findWord(final int id)
	{
		SentiWordNetWord retVal = null;
		
		if(!allowIndexing)//brute force search
		{
			retVal = bruteSearch(id);
		}
		else //binary search
		{
			retVal = binarySearch(id);
		}
		
		
		return retVal;
	}
	
	
	
	
	
	
	/**finds and returns the word whose id is given, from the sentiWordNet dictionary. The use of the tag 
	 * speeds up the process even more if you use indexing. 
	 * 
	 * WARNING: it is not efficient to call this function if you don't use indexing.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word we are searching for.
	 * 
	 * @return SentiWordNetWord - instance corresponding to the id or null if it cannot be found.
	 */
	public SentiWordNetWord findWord(final int id, final POSTagWordNet pos)
	{
		SentiWordNetWord retVal = null;
		
		if(!allowIndexing)//brute force search
		{
			retVal = bruteSearch(id);
		}
		else //binary search
		{
			retVal = binarySearch(id, pos);
		}
		
		
		return retVal;
	}

	
	
	
	
	
	
	
	/**finds and returns the synset of the word whose id is given as a parameter. 
	 * if the word is not found returns null.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word to be searched
	 * 
	 * @return ArrayList<SynsetTerm> of synset terms or null if it cannot be found
	 */
	public ArrayList<SynsetTerm> findSynset(final int id)
	{
		ArrayList<SynsetTerm> retVal = null;
		SentiWordNetWord word;
		
		if(!allowIndexing)
		{
			word = bruteSearch(id);
		}
		else
		{
			word = binarySearch(id);
		}
		
		if(word != null)
		{
			retVal = word.getSynsetTerms();
		}
		
		
		return retVal;
	}

	
	
	
	
	
	
	
	/**finds and returns the synset of the word whose id is given as a parameter. 
	 * Faster version with the use of tag for indexing.
	 * if the word is not found returns null.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word to be searched
	 * 
	 * @return ArrayList<SynsetTerm> of synset terms or null if it cannot be found
	 */
	public ArrayList<SynsetTerm> findSynset(final int id, final POSTagWordNet tag)
	{
		ArrayList<SynsetTerm> retVal = null;
		SentiWordNetWord word;
		
		if(!allowIndexing)
		{
			word = bruteSearch(id);
		}
		else
		{
			word = binarySearch(id, tag);
		}
		
		if(word != null)
		{
			retVal = word.getSynsetTerms();
		}
		
		
		return retVal;
	}

	
	
	
	
	
	
	
	
	
	/**searches for the word in the dictionary with brute force.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word it is searching for
	 * 
	 * @return SentiWordNetWord - corresponding instance of the word or null if it cannot be found
	 */
	private SentiWordNetWord bruteSearch(final int id)
	{
		SentiWordNetWord tempWord;
		SentiWordNetWord retVal = null;
		
		for (int i=0; i<wordCount; i++)
		{
			tempWord = words.get(i);
			
			if(tempWord.getId() == id)
			{
				retVal = tempWord;
				break;
			}
		}
		
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	/**searches for the word in the dictionary with binary search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word it is searching for
	 * 
	 * @return SentiWordNetWord - corresponding instance of the word or null if it cannot be found
	 */
	private SentiWordNetWord binarySearch(final int id)
	{
		int left = 0;
		int right = 0;
		int current = 0;
		
		SentiWordNetWord tempWord;
		SentiWordNetWord retVal = null;
		boolean found = false;
		
		
		
		/*-------------------------------------------------------------------*/
		//search in the adjectives
		right = adjectiveCount -1;
		
		while(!found && left<=right)
		{
			current = (right-left)/2 + left;
			
			tempWord = adjectives.get(current);
			
			if(tempWord.getId() > id)//look for the smaller part
			{
				right = current -1;
			}
			else if(tempWord.getId() < id)//look for the bigger part
			{
				left = current+1;
			}
			else//it is found
			{
				retVal = tempWord;
				found = true;
			}
		}
		
		
		
		
		/*-------------------------------------------------------------------*/
		//search in the verbs
		left = 0;
		right = verbCount -1;
		
		while(!found && left<=right)
		{
			current = (right-left)/2 + left;
			
			tempWord = verbs.get(current);
			
			if(tempWord.getId() > id)//look for the smaller part
			{
				right = current-1;
			}
			else if(tempWord.getId() < id)//look for the bigger part
			{
				left = current+1;
			}
			else//it is found
			{
				retVal = tempWord;
				found = true;
			}
			
		}
		
		
		/*-------------------------------------------------------------------*/
		//search in the adverbs
		left = 0;
		right = adverbCount -1;
		
		while(!found && left<=right)
		{
			current = (right-left)/2 + left;
			
			tempWord = adverbs.get(current);
			
			if(tempWord.getId() > id)//look for the smaller part
			{
				right = current -1;
			}
			else if(tempWord.getId() < id)//look for the bigger part
			{
				left = current+1;
			}
			else//it is found
			{
				retVal = tempWord;
				found = true;
			}
		}

		
		/*-------------------------------------------------------------------*/
		//search in the nouns
		left = 0;
		right = nounCount -1;
		
		while(!found && left<=right)
		{
			current = (right-left)/2 + left;
			
			tempWord = nouns.get(current);
			
			if(tempWord.getId() > id)//look for the smaller part
			{
				right = current -1;
			}
			else if(tempWord.getId() < id)//look for the bigger part
			{
				left = current+1;
			}
			else//it is found
			{
				retVal = tempWord;
				found = true;
			}
		}

		
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	/**searches for the word in the dictionary with binary search. uses tag to speed up the search using indexes.
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - id of the word it is searching for.
	 * @param tag - corresponding pos tag of the word we are searching for
	 * 
	 * @return SentiWordNetWord - corresponding instance of the word or null if it cannot be found
	 */
	private SentiWordNetWord binarySearch(final int id, final POSTagWordNet tag)
	{
		int left = 0;
		int right = 0;
		int current = 0;
		
		SentiWordNetWord tempWord;
		SentiWordNetWord retVal = null;
		ArrayList<SentiWordNetWord> tempList = null;
		boolean found = false;
		
		
		switch(tag)
		{
			case ADJECTIVE: tempList = adjectives;
							right = tempList.size() -1;
							break;
							
			case ADVERB: tempList = adverbs;
						 right = tempList.size() -1;
						 break;
						 
			case NOUN: tempList = nouns;
					   right = tempList.size() -1;
					   break;
					   
			case VERB: tempList = verbs;
					   right = tempList.size() -1;
					   break;
					   
			default: found = true; //do not search there is a mistake
					 break;//return null
		}
		
		
		
		/*-------------------------------------------------------------------*/
		//search in the tempList
		
		while(!found && left<=right)
		{
			current = (right-left)/2 + left;
			
			tempWord = tempList.get(current);
			
			if(tempWord.getId() > id)//look for the smaller part
			{
				right = current -1;
			}
			else if(tempWord.getId() < id)//look for the bigger part
			{
				left = current+1;
			}
			else//it is found
			{
				retVal = tempWord;
				found = true;
			}
		}

	
	
		return retVal;
	}
	
	
	
	/**calculates and returns the average positive score of all words whose type is equal to the tag.
	 * if tag equals to the POSTagWordNet.OTHER returns the score of all words.
	 * 
	 * @param tag 
	 * 
	 * @author erhan sezerer
	 *
	 * @return double - average positive score of all words
	 */
	public double findAveragePositiveScore(final POSTagWordNet tag)
	{
		double average = 0;
		ArrayList<SentiWordNetWord> tempList = null;
		boolean flag = true;
		int size = 0;
		
		
		switch(tag)
		{
			case ADJECTIVE: tempList = adjectives;
							break;
							
			case ADVERB: tempList = adverbs;
						 break;
						 
			case NOUN: tempList = nouns;
					   break;
					   
			case VERB: tempList = verbs;
					   break;
					   
			case OTHER: tempList = words;
						break;
					   
			default: flag = false; //do not search there is a mistake
					 break;//return null
		}
		
		size = tempList.size();
		
		
		for(int i=0; i<size && flag; i++)
		{
			average += tempList.get(i).getPositiveScore();
		}
		
		average /= size;
		
		return average;
	}
	
	/**calculates and returns the average negative score of all words whose type is equal to the tag.
	 * if tag equals to the POSTagWordNet.OTHER returns the score of all words.
	 * 
	 * @param tag 
	 * 
	 * @author erhan sezerer
	 *
	 * @return double - average positive score of all words
	 * */
	public double findAverageNegativeScore(final POSTagWordNet tag)
	{
		double average = 0;
		ArrayList<SentiWordNetWord> tempList = null;
		boolean flag = true;
		int size = 0;
		
		
		switch(tag)
		{
			case ADJECTIVE: tempList = adjectives;
							break;
							
			case ADVERB: tempList = adverbs;
						 break;
						 
			case NOUN: tempList = nouns;
					   break;
					   
			case VERB: tempList = verbs;
					   break;
					   
			case OTHER: tempList = words;
						break;
					   
			default: flag = false; //do not search there is a mistake
					 break;//return null
		}
		
		size = tempList.size();
		
		
		for(int i=0; i<size && flag; i++)
		{
			average += tempList.get(i).getNegativeScore();
		}
		
		average /= size;
		
		return average;	
	}

	
	/**calculates and returns the average objectivity score of all words whose type is equal to the tag.
	 * if tag equals to the POSTagWordNet.OTHER returns the score of all words.
	 * 
	 * @param tag 
	 * 
	 * @author erhan sezerer
	 *
	 * @return double - average positive score of all words
	 * 
	 */
	public double findAverageObjectiveScore(final POSTagWordNet tag)
	{
		double average = 0;
		ArrayList<SentiWordNetWord> tempList = null;
		boolean flag = true;
		int size = 0;
		
		
		switch(tag)
		{
			case ADJECTIVE: tempList = adjectives;
							break;
							
			case ADVERB: tempList = adverbs;
						 break;
						 
			case NOUN: tempList = nouns;
					   break;
					   
			case VERB: tempList = verbs;
					   break;
					   
			case OTHER: tempList = words;
						break;
					   
			default: flag = false; //do not search there is a mistake
					 break;//return null
		}
		
		size = tempList.size();
		
		
		for(int i=0; i<size && flag; i++)
		{
			average += tempList.get(i).getObjectivityScore();
		}
		
		average /= size;
		
		return average;	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				OTHER FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**if the lower is set true, finds the words whose POS tag is given, with positivity score lower than limit. 
	 * if it is false, finds the words whose POS tag is given, with positivity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding positivity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getWordsWithPositiveScore(final double limit, final boolean lower, final POSTagWordNet tag)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList = new ArrayList<SentiWordNetWord>();
		SentiWordNetWord tempWord;
		
		try
		{
			switch(tag)
			{
				case ADJECTIVE: tempList = adjectives;
								break;
								
				case NOUN: tempList = nouns;
						   break;
						   
				case VERB: tempList = verbs;
						   break;
						   
				case ADVERB: tempList = adverbs;
							 break;
							 
				default: throw new IllegalArgumentException("Illegal tag\n");

			}
			
			int count = tempList.size();
			
			
			for (int i=0; i<count; i++)
			{
				tempWord = tempList.get(i);
				
				if(!lower && (tempWord.getPositiveScore() >= limit))
				{
					retVal.add(tempWord);
				}
				else if(lower && (tempWord.getPositiveScore() <= limit))
				{
					retVal.add(tempWord);
				}
			}
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
		
		
		
		return retVal;
	}
	
	
	
	
	
	
	
	/**if the lower is set true, finds the words with positivity score lower than limit. 
	 * if it is false, finds the words with positivity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding positivity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getWordsWithPositiveScore(final double limit, final boolean lower)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList;
		
		for(POSTagWordNet tag: POSTagWordNet.values())
		{
			if(tag != POSTagWordNet.OTHER)
			{
				tempList = getWordsWithPositiveScore(limit, lower, tag);
				
				if(tempList != null)
				{
					retVal.addAll(tempList);
				}
				else
				{
					retVal = null;
					break;
				}
				
			}
		}
		
		
		return retVal;
	}

	
	
	
	
	
	/**if the lower is set true, finds the words whose POS tag is given, with negativity score lower than limit. 
	 * if it is false, finds the words whose POS tag is given, with negativity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding negativity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getWordsWithNegativeScore(final double limit, final boolean lower, final POSTagWordNet tag)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList = new ArrayList<SentiWordNetWord>();
		SentiWordNetWord tempWord;
		
		try
		{
			switch(tag)
			{
				case ADJECTIVE: tempList = adjectives;
								break;
								
				case NOUN: tempList = nouns;
						   break;
						   
				case VERB: tempList = verbs;
						   break;
						   
				case ADVERB: tempList = adverbs;
							 break;
							 
				default: throw new IllegalArgumentException("Illegal tag\n");

			}
			
			int count = tempList.size();
			
			
			for (int i=0; i<count; i++)
			{
				tempWord = tempList.get(i);
				
				if(!lower && tempWord.getNegativeScore() >= limit)
				{
					retVal.add(tempWord);
				}
				else if(lower && tempWord.getNegativeScore() <= limit)
				{
					retVal.add(tempWord);
				}
			}
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
		
		
		
		return retVal;
	}

	
	
	
	
	/**if the lower is set true, finds the words with negativity score lower than limit. 
	 * if it is false, finds the words with negativity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding negativity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getWordsWithNegativeScore(final double limit, final boolean lower)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList;
		
		for(POSTagWordNet tag: POSTagWordNet.values())
		{
			if(tag != POSTagWordNet.OTHER)
			{
				tempList = getWordsWithNegativeScore(limit, lower, tag);
				
				if(tempList != null)
				{
					retVal.addAll(tempList);
				}
				else
				{
					retVal = null;
					break;
				}
			}
			
		}
		
		
		return retVal;
	}
	
	
	
	
	
	
	/**if the lower is set true, finds the words with objectivity score lower than limit. 
	 * if it is false, finds the words with objectivity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding objectivity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getObjectiveWords(final double limit, final boolean lower)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList;
		
		for(POSTagWordNet tag: POSTagWordNet.values())
		{
			if(tag != POSTagWordNet.OTHER)
			{

				tempList = getObjectiveWords(limit, lower, tag);
				
				if(tempList != null)
				{
					retVal.addAll(tempList);
				}
				else
				{
					retVal = null;
					break;
				}
			}
			
		}
		
		
		return retVal;
	}

	
	
	
	
	/**if the lower is set true, finds the words whose POS tag is given, with objectivity score lower than limit. 
	 * if it is false, finds the words whose POS tag is given, with objectivity score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding objectivity score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getObjectiveWords(final double limit, final boolean lower, final POSTagWordNet tag)
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList = new ArrayList<SentiWordNetWord>();
		SentiWordNetWord tempWord;
		
		try
		{
			switch(tag)
			{
				case ADJECTIVE: tempList = adjectives;
								break;
								
				case NOUN: tempList = nouns;
						   break;
						   
				case VERB: tempList = verbs;
						   break;
						   
				case ADVERB: tempList = adverbs;
							 break;
							 
				default: throw new IllegalArgumentException("Illegal tag\n");

			}
			
			int count = tempList.size();
			
			
			for (int i=0; i<count; i++)
			{
				tempWord = tempList.get(i);
				
				if(!lower && (tempWord.getObjectivityScore() >= limit))
				{
					retVal.add(tempWord);
				}
				else if(lower && (tempWord.getObjectivityScore() <= limit))
				{
					retVal.add(tempWord);
				}
			}
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
		
		
		
		return retVal;
	}

	
	
	
	
	
	
	/**if the lower is set true, finds the words whose POS tag is given, with negScores-posScores lower than limit. 
	 * if it is false, finds the words whose POS tag is given, with negScores-posScores score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding negScores-posScores score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * @param tag - 
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getOverallNegativeWords(final double limit, final boolean lower, final POSTagWordNet tag) 
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList = new ArrayList<SentiWordNetWord>();
		SentiWordNetWord tempWord;
		
		try
		{
			switch(tag)
			{
				case ADJECTIVE: tempList = adjectives;
								break;
								
				case NOUN: tempList = nouns;
						   break;
						   
				case VERB: tempList = verbs;
						   break;
						   
				case ADVERB: tempList = adverbs;
							 break;
							 
				default: throw new IllegalArgumentException("Illegal tag\n");

			}
			
			int count = tempList.size();
			
			
			for (int i=0; i<count; i++)
			{
				tempWord = tempList.get(i);
				
				if(!lower && (tempWord.getNegativeScore()-tempWord.getPositiveScore() >= limit))
				{
					retVal.add(tempWord);
				}
				else if(lower && (tempWord.getNegativeScore()-tempWord.getPositiveScore() <= limit))
				{
					retVal.add(tempWord);
				}
			}
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
		
		
		
		return retVal;

	}
	
	
	
	
	
	
	/**if the lower is set true, finds the words whose POS tag is given, with posScores-negScores lower than limit. 
	 * if it is false, finds the words whose POS tag is given, with posScores-negScores score greater than the limit.
	 * 
	 * @author erhan sezerer
	 *
	 * @param limit - a threshold for finding posScores-negScores score
	 * @param lower - if true looks for the smaller numbers than limit,
	 * 				  else looks for the greater ones
	 * @param tag - 
	 * 
	 * @return ArrayList<SentiWordNetWord> - list of words conforming the conditions.
	 */
	public ArrayList<SentiWordNetWord> getOverallPositiveWords(final double limit, final boolean lower, final POSTagWordNet tag) 
	{
		ArrayList<SentiWordNetWord> retVal = new ArrayList<SentiWordNetWord>();
		ArrayList<SentiWordNetWord> tempList = new ArrayList<SentiWordNetWord>();
		SentiWordNetWord tempWord;
			
		try
		{
			switch(tag)
			{
				case ADJECTIVE: tempList = adjectives;
								break;
									
				case NOUN: tempList = nouns;
						   break;
							   
				case VERB: tempList = verbs;
						   break;
							   
				case ADVERB: tempList = adverbs;
							 break;
								 
				default: throw new IllegalArgumentException("Illegal tag\n");

			}
				
			int count = tempList.size();
				
				
			for (int i=0; i<count; i++)
			{
				tempWord = tempList.get(i);
					
				if(!lower && (tempWord.getPositiveScore()-tempWord.getNegativeScore() >= limit))
				{
					retVal.add(tempWord);
				}
				else if(lower && (tempWord.getPositiveScore()-tempWord.getNegativeScore() <= limit))
				{
					retVal.add(tempWord);
				}
			}
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			retVal = null;
		}
			
			
			
		return retVal;
	}


	
	
	
	
	/**clears every wordnet data from the lists and frees them.
	 * makes this singleton class available again, but should not be used excessively 
	 * or the garbage collector should be run between calls.
	 * 
	 * @author erhan sezerer
	 *
	 */
	public void closeDictionary()
	{
		if(allowIndexing)
		{
			nouns.clear();
			nouns = null;
			adverbs.clear();
			adverbs = null;
			adjectives.clear();
			adjectives = null;
			verbs.clear();
			verbs = null;
		}
		
		words.clear();
		words = null;
		singletonUsed = false;
	}
	
	

	
	
	


	
	/*---------------------------------------------------------------------------------------------------------
	 * 		setters getters
	 *-------------------------------------------------------------------------------------------------------*/
	public String getDictionaryPath() 
	{
		return dictionaryPath;
	}

	public void setDictionaryPath(String dictionaryPath) 
	{
		this.dictionaryPath = dictionaryPath;
	}

	public ArrayList<SentiWordNetWord> getWords() 
	{
		return words;
	}

	public int getWordCount() 
	{
		return wordCount;
	}

	public ArrayList<SentiWordNetWord> getAdjectives() 
	{
		return adjectives;
	}

	public int getAdjectiveCount() 
	{
		return adjectiveCount;
	}

	public ArrayList<SentiWordNetWord> getAdverbs() 
	{
		return adverbs;
	}

	public int getAdverbCount() 
	{
		return adverbCount;
	}

	public ArrayList<SentiWordNetWord> getVerbs() 
	{
		return verbs;
	}

	public int getVerbCount() 
	{
		return verbCount;
	}

	public ArrayList<SentiWordNetWord> getNouns() 
	{
		return nouns;
	}

	public int getNounCount() 
	{
		return nounCount;
	}

	public ArrayList<Double> getDataPos() 
	{
		return dataPos;
	}

	public ArrayList<Double> getDataNeg() 
	{
		return dataNeg;
	}

	public ArrayList<Double> getDataObj() 
	{
		return dataObj;
	}

	
}
