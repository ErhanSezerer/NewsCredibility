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

import java.io.File;
import java.util.ArrayList;

import com.darg.NLPOperations.dictionary.model.WordNet;
import com.darg.NLPOperations.dictionary.model.WordNetSense;
import com.darg.NLPOperations.dictionary.model.WordNetWord;
import com.darg.NLPOperations.pos.util.POSTagWordNet;
import com.darg.fileOperations.utils.FileUtils;



/**this singleton class handles the interactions with WordNet dictionaries.
 * It finds, loads or etc. words from the dictionary.
 * compatible with wordNet 3.0
 * 
 * 
 * @author erhan sezerer
 *
 */
public class WordNetDictionary 
{
	private static boolean singletonUsed = false;
	private WordNetParser wordNetParser;
	
	private String dictionaryPath;
	private final String nounPath = "index.noun";
	private final String verbPath = "index.verb";
	private final String adjectivePath = "index.adj";
	private final String adverbPath = "index.adv";
	private final String sensePath = "index.sense";
	
	private transient ArrayList<WordNetWord> nouns;
	private transient ArrayList<WordNetWord> adverbs;
	private transient ArrayList<WordNetWord> adjectives;
	private transient ArrayList<WordNetWord> verbs;
	private transient ArrayList<WordNetSense> senses;
	private int nounCount;
	private int adverbCount;
	private int adjectiveCount;
	private int verbCount;
	private int senseCount;
	
	
	

	
	//constructors
	private WordNetDictionary(final String dictionaryPath, final WordNet version) 
	{
		this.dictionaryPath = dictionaryPath;
		
		nouns = null;
		adverbs = null;
		adjectives = null;
		verbs = null;
		senses = null;
		
		nounCount = 0;
		adverbCount = 0;
		adjectiveCount = 0;
		verbCount = 0;
		senseCount = 0;
		
		wordNetParser = new WordNetParser(version);
	}
	
	
	
	
	/**Returns an instance of WordNet, initializes with the given path and version
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a path to the WordNet dictionary.
	 * 				a path to WordNet/dict must be given
	 * @param version - WordNet version to use.
	 * 
	 * @return WordNet - singleton object, if called once returns null
	 */
	public static synchronized WordNetDictionary getInstance(final String path, final WordNet version)
	{
		WordNetDictionary retVal;
		
		if(singletonUsed)
		{
			retVal = null;
		}
		else
		{
			singletonUsed = true;
			retVal = new WordNetDictionary(path, version);
		}
			
		
		return retVal;
	}
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				LOADER FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**parses and loads the wordnet dictionary. 
	 * Loads everything including: nouns, adjectives, adverbs, verbs and sense words
	 * 						(index.noun, index.adjective, index.adverb, index.verb, index.sense)
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the wordnet dictionary
	 */
	public synchronized boolean loadDictionary()
	{
		return loadNouns() && loadAdjectives() && loadAdverbs() && loadVerbs() && loadSenseWords();
	}
	
	
	
	/**parses and loads the index files from wordnet dictionary. 
	 * Includes: index.noun, index.verb, index.adverb, index.adjective
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the indexes wordnet dictionary
	 */
	public synchronized boolean loadIndexes()
	{
		return loadNouns() && loadAdjectives() && loadAdverbs() && loadVerbs();
	}
	
	
	
	/**parses and loads the nouns from the wordnet dictinary. 
	 * Includes: index.noun
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the nouns from wordnet dictionary
	 */
	public synchronized boolean loadNouns()
	{
		boolean retVal = false;
		String content = FileUtils.readFile(new File(dictionaryPath, nounPath).getAbsolutePath());
		
		if(content != null)
		{
			nouns = wordNetParser.parseWordNetIndex(content);
			
			if(nouns != null || nouns.size() != 0)
			{
				retVal = true;
				nounCount = nouns.size();
			}
		}
		
		return retVal;
	}

	
	
	/**parses and loads the verbs from the wordnet dictinary. 
	 * Includes: index.verb
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the verbs from wordnet dictionary
	 */
	public synchronized boolean loadVerbs()
	{
		boolean retVal = true;
		String content = FileUtils.readFile(new File(dictionaryPath, verbPath).getAbsolutePath());
		
		if(content != null)
		{
			verbs = wordNetParser.parseWordNetIndex(content);
			
			if(verbs != null || verbs.size() != 0)
			{
				retVal = true;
				verbCount = verbs.size();
			}
		}
		
		
		return retVal;
	}

	
	
	/**parses and loads the adverbs from the wordnet dictinary. 
	 * Includes: index.adverb
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the adverbs from wordnet dictionary
	 */
	public synchronized boolean loadAdverbs()
	{
		boolean retVal = true;
		String content = FileUtils.readFile(new File(dictionaryPath, adverbPath).getAbsolutePath());
		
		if(content != null)
		{
			adverbs = wordNetParser.parseWordNetIndex(content);
			
			if(adverbs != null || adverbs.size() != 0)
			{
				retVal = true;
				adverbCount = adverbs.size();
			}
		}
		
		
		return retVal;
	}

	
	
	/**parses and loads the adjectives from the wordnet dictinary. 
	 * Includes: index.adjective
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the adjectives from wordnet dictionary
	 */
	public synchronized boolean loadAdjectives()
	{
		boolean retVal = true;
		String content = FileUtils.readFile(new File(dictionaryPath, adjectivePath).getAbsolutePath());
		
		if(content != null)
		{
			adjectives = wordNetParser.parseWordNetIndex(content);
			
			if(adjectives != null || adjectives.size() != 0)
			{
				retVal = true;
				adjectiveCount = adjectives.size();
			}
		}
		
		
		return retVal;
	}

	
	
	/**parses and loads the sense words from the wordnet dictinary. 
	 * Includes: index.sense
	 * 
	 * @author erhan sezerer
	 *
	 * @return boolean - true if it successfully loads the words from wordnet dictionary
	 */
	public synchronized boolean loadSenseWords()
	{
		boolean retVal = true;
		String content = FileUtils.readFile(new File(dictionaryPath, sensePath).getAbsolutePath());
		
		if(content != null)
		{
			senses = wordNetParser.parseWordNetSense(content);
			
			if(senses != null || senses.size() != 0)
			{
				retVal = true;
				senseCount = senses.size();
			}
		}
		
		
		return retVal;
	}

	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				SEARCH FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**finds and retrieves the word from the given id(from wordnet index files) and part-of-speech(penn treebank) tag. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - corresponding id of the word from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the word from penn treebank pos tags
	 * 
	 * @return String - word that is found from the id and pos. If it cannot be found returns null
	 */
	public String findWord(final int id, final POSTagWordNet pos)
	{
		String word = null;
		int size = 0;
		ArrayList<WordNetWord> words;
		WordNetWord tempWord = null;
		
		
		try
		{
			switch(pos)
			{
				case NOUN: words = nouns;
					  	  break;
					  
				case VERB: words = verbs;
			  	  		  break;

				case ADJECTIVE: words = adjectives;
						  break;

				case ADVERB: words = adverbs;
			  	  		  break;

				default: throw new IllegalArgumentException("illegal Pos Value: " + pos);
			}
			
			size = words.size();
			
			for (int i=0; i<size; i++) 
			{
				tempWord = words.get(i);
				
				if(tempWord.getId().contains(id) && pos == tempWord.getPos())
				{
					word = tempWord.getWord();
					break;
				}
			}
			
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			word = null;
		}
		
		
		return word;
	}
	
	
	
	
	
	
	
	
	/**finds and retrieves the id's of the word from the given word(from wordnet index files) and part-of-speech(penn treebank) tag. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word.
	 * 								returns null if no id that corresponds to the word, or if there is some error.
	 */
	public ArrayList<Integer> findIDs(final String word, final POSTagWordNet pos)
	{
		ArrayList<Integer> id = null;
		
		int size = 0;
		ArrayList<WordNetWord> words;
		WordNetWord tempWord = null;
		
		try
		{
			if(word == null || word.isEmpty())
			{
				throw new IllegalArgumentException("invalid parameter in word");
				
			}
			else
			{
				switch(pos)
				{
					case NOUN: words = nouns;
						  	  break;
						  
					case VERB: words = verbs;
				  	  		  break;

					case ADJECTIVE: words = adjectives;
							  break;

					case ADVERB: words = adverbs;
				  	  		  break;

					default: throw new IllegalArgumentException("illegal Pos Value: " + pos);
				}
				
				size = words.size();
				
				for (int i=0; i<size; i++) 
				{
					tempWord = words.get(i);
					
					if(tempWord.getWord().equalsIgnoreCase(word) && pos == tempWord.getPos())
					{
						id = tempWord.getId();
						break;
					}
				}
			}
			
			
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			id = null;
		}
		
		
		return id;
	}
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the id of the word from the given word(from wordnet index files), part-of-speech(penn treebank)
	 *  tag and sense number. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * @param senseNumber - corresponding sense number of the id from wordnet dictionary
	 * 
	 * @return ArrayList<Integer> - an 8 digit integer that corresponds to the given word. 
									Returns -1 if there is an error or if the word cannot be found	 
	 */
	public int findID(final String word, final POSTagWordNet pos, final int senseNumber)
	{
		int id = -1;
		
		int size = 0;
		ArrayList<WordNetWord> words;
		WordNetWord tempWord = null;
		
		try
		{
			if(word == null || word.isEmpty())
			{
				throw new IllegalArgumentException("invalid parameter");
				
			}
			else
			{
				switch(pos)
				{
					case NOUN: words = nouns;
						  	  break;
						  
					case VERB: words = verbs;
				  	  		  break;

					case ADJECTIVE: words = adjectives;
							  break;

					case ADVERB: words = adverbs;
				  	  		  break;

					default: throw new IllegalArgumentException("illegal Pos Value: " + pos);
				}
				
				size = words.size();
				
				for (int i=0; i<size; i++) 
				{
					tempWord = words.get(i);
					
					if(tempWord.getWord().equalsIgnoreCase(word) && pos == tempWord.getPos())
					{
						if(senseNumber > tempWord.getId().size())
						{
							throw new IllegalArgumentException("illegal sense number: " + senseNumber);
						}
						
						id = tempWord.getId().get(senseNumber-1);
						break;
					}
				}
			}
			
			
		}
		catch(IllegalArgumentException ie)
		{
			ie.printStackTrace();
			id = -1;
		}
		
		
		return id;

	}
	
	
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the word from the given id(from wordnet index.sense file) and part-of-speech(penn treebank) tag. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb
	 * 
	 * @author erhan sezerer
	 *
	 * @param id - corresponding id of the word from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the word from penn treebank pos tags
	 * 
	 * @return String - word that is found from the id and pos. 
	 * 					If it cannot be found returns null
	 */
	public String findWordFromSense(final int id, final POSTagWordNet pos)
	{
		String word = null;
		WordNetSense tempWord = null;
		
		
		try
		{

			for (int i=0; i<senseCount; i++) 
			{
				tempWord = senses.get(i);
				
				if(tempWord.getId() == id && pos == tempWord.getPos())
				{
					word = tempWord.getWord();
					break;
				}
			}
			
		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			word = null;
		}
		
		
		return word;
	}

	
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the id's of the word from the given id(from wordnet index.sense file) and part-of-speech(penn treebank) tag. 
	 * Faster version of id search -> uses binary search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word and pos tag
	 * 							    if nothing has been found returns null
	 */
	public ArrayList<Integer> findIDsFromSense(final String word, final POSTagWordNet pos)
	{
		ArrayList<Integer> id = new ArrayList<Integer>();
		WordNetSense tempWord = null;
		
		boolean found = false;
		
		int left = 0;
		int right = senseCount-1;
		int current = 0;
		int comparison = 0;
		
		try
		{
			while(left<right && !found)//start binary search (since the file is alphabetically designed)
			{
				current = (right-left)/2 + left;
				tempWord = senses.get(current);
				
				
				comparison = tempWord.getWord().compareToIgnoreCase(word);
				
				
				if(comparison < 0)//go to right half
				{
					left = current+1;
				}
				else if(comparison > 0)//go to left half
				{
					right = current-1;
				}
				else//if the word we are searching is found
				{
					found = true;

					//now check whether the pos tag is the same too
					if(tempWord.getPos() == pos)
					{
						id.add(tempWord.getId());
					}
					
					
					//SINCE THERE CAN BE MORE THAN ONE OCCURENCE OF THE SAME WORD
					//look for the same word occurrences in upper lines
					int upper = current-1;
					tempWord = senses.get(upper);
					while(tempWord.getWord().compareToIgnoreCase(word) == 0)
					{
						if(tempWord.getPos() == pos)
						{
							id.add(tempWord.getId());
						}
						
						upper--;
						tempWord = senses.get(upper);
					}
					//look for the same word occurrences in lower lines
					int lower = current+1;
					tempWord = senses.get(lower);
					while(tempWord.getWord().compareToIgnoreCase(word) == 0)
					{
						if(tempWord.getPos() == pos)
						{
							id.add(tempWord.getId());
						}
						
						lower++;
						tempWord = senses.get(lower);
					}
				}
			}

		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			id = null;
		}
	
		//if no occurence can be found, return null
		if(id !=null && id.isEmpty())
		{
			id = null;
		}

		
		
		return id;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the id of the word from the given id(from wordnet index.sense file) , part-of-speech(penn treebank) tag,
	 * and sense number. 
	 * Faster version of id search -> uses binary search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * @param senseNumber - corresponding sense number of the id as a digit from wordnet dictionary
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word and pos tag
	 * 							    if nothing has been found returns null
	 */
	public int findIDFromSense(final String word, final POSTagWordNet pos, final int senseNumber)
	{
		int id = -1;
		WordNetSense tempWord = null;
		
		boolean found = false;
		
		int left = 0;
		int right = senseCount-1;
		int current = 0;
		int comparison = 0;
		
		try
		{
			while(left<right && !found)//start binary search (since the file is alphabetically designed)
			{
				current = (right-left)/2 + left;
				tempWord = senses.get(current);
				
				comparison = tempWord.getWord().compareToIgnoreCase(word);
				
				if(comparison < 0)//go to right half
				{
					left = current+1;
				}
				else if(comparison > 0)//go to left half
				{
					right = current-1;
				}
				else//if the word we are searching is found
				{
					found = true;

					//now check whether the pos tag is the same too
					if(tempWord.getPos() == pos && tempWord.getSenseNumber() == senseNumber)
					{
						id = tempWord.getId();
					}
					else
					{
							//SINCE THERE CAN BE MORE THAN ONE OCCURENCE OF THE SAME WORD
							//look for the same word occurrences in upper lines
							int upper = current-1;
							tempWord = senses.get(upper);
							while(tempWord.getWord().compareToIgnoreCase(word) == 0)
							{
								if(tempWord.getPos() == pos && tempWord.getSenseNumber() == senseNumber)
								{
									id = tempWord.getId();
								}
								
								upper--;
								tempWord = senses.get(upper);
							}
							//look for the same word occurrences in lower lines
							int lower = current+1;
							tempWord = senses.get(lower);
							while(tempWord.getWord().compareToIgnoreCase(word) == 0)
							{
								if(tempWord.getPos() == pos && tempWord.getSenseNumber() == senseNumber)
								{
									id = tempWord.getId();
								}
								
								lower++;
								tempWord = senses.get(lower);
							}
					
					}//else
				}//else
			}//while
			
		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			id = -1;
		}
	
		
		
		return id;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the id of the word from the given id(from wordnet index.sense file) and sense number. 
	 * Faster version of id search -> uses binary search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param senseNumber - corresponding sense number of the id as a digit from wordnet dictionary
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word and pos tag
	 * 							    if nothing has been found returns null
	 */
	public int findIDFromSense(final String word, final int senseNumber)
	{
		int id = -1;
		WordNetSense tempWord = null;
		
		boolean found = false;
		
		int left = 0;
		int right = senseCount-1;
		int current = 0;
		int comparison = 0;
		
		try
		{
			while(left<right && !found)//start binary search (since the file is alphabetically designed)
			{
				current = (right-left)/2 + left;
				tempWord = senses.get(current);
				
				comparison = tempWord.getWord().compareToIgnoreCase(word);
				
				if(comparison < 0)//go to right half
				{
					left = current+1;
				}
				else if(comparison > 0)//go to left half
				{
					right = current-1;
				}
				else//if the word we are searching is found
				{
					found = true;

					//now check whether the pos tag is the same too
					if(tempWord.getSenseNumber() == senseNumber)
					{
						id = tempWord.getId();
					}
					else
					{
							//SINCE THERE CAN BE MORE THAN ONE OCCURENCE OF THE SAME WORD
							//look for the same word occurrences in upper lines
							int upper = current-1;
							tempWord = senses.get(upper);
							while(tempWord.getWord().compareToIgnoreCase(word) == 0)
							{
								if(tempWord.getSenseNumber() == senseNumber)
								{
									id = tempWord.getId();
								}
								
								upper--;
								tempWord = senses.get(upper);
							}
							//look for the same word occurrences in lower lines
							int lower = current+1;
							tempWord = senses.get(lower);
							while(tempWord.getWord().compareToIgnoreCase(word) == 0)
							{
								if(tempWord.getSenseNumber() == senseNumber)
								{
									id = tempWord.getId();
								}
								
								lower++;
								tempWord = senses.get(lower);
							}
					
					}//else
				}//else
			}//while
			
		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			id = -1;
		}
	
		
		
		return id;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**finds and retrieves the id's of the word from the given id(from wordnet index.sense file) and part-of-speech(penn treebank) tag. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb.  
	 * Slower version of id search -> uses brute force search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word and pos tag
	 * 							    if nothing has been found returns null
	 */
	public ArrayList<Integer> findIDsFromSenseSlow(final String word, final POSTagWordNet pos)
	{
		ArrayList<Integer> id = new ArrayList<Integer>();
		WordNetSense tempWord = null;
		
		
		try
		{
			//search all records for occurrence
			for (int i=0; i<senseCount; i++) 
			{
				tempWord = senses.get(i);
				
				if(tempWord.getWord().equalsIgnoreCase(word) && pos == tempWord.getPos())
				{
					id.add(tempWord.getId());
				}
			}
			
		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			id = null;
		}
		
		
		//if no occurence can be found, return null
		if(id.isEmpty())
		{
			id = null;
		}
		
		return id;
	}
	
	
	
	
	
	
	
	
	/**finds and retrieves the id of the word from the given id(from wordnet index.sense file) , part-of-speech(penn treebank) tag 
	 * and sense number. 
	 * Possible tags are r=adverb , a=adjective , n=noun , v=verb.  
	 * Slower version of id search -> uses brute force search.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - corresponding word of the id from wordnet dictionary
	 * @param pos - corresponding part-of-speech tag of the id from penn treebank pos tags
	 * @param senseNumber - corresponding sense number of the id as a digit.
	 * 
	 * @return ArrayList<Integer> - a list of 8 digit integers that corresponds to the given word and pos tag
	 * 							    if nothing has been found returns -1
	 */
	public int findIDFromSenseSlow(final String word, final POSTagWordNet pos, final int senseNumber)
	{
		int id = -1;
		WordNetSense tempWord = null;
		
		
		try
		{
			//search all records for occurrence
			for (int i=0; i<senseCount; i++) 
			{
				tempWord = senses.get(i);
				
				if(tempWord.getWord().equalsIgnoreCase(word) && pos == tempWord.getPos() && tempWord.getSenseNumber() == senseNumber)
				{
					id = tempWord.getId();
				}
			}
			
		}
		catch(Exception ie)
		{
			ie.printStackTrace();
			id = -1;
		}
		
		
		
		return id;
	}
	
	
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//				OTHER FUNCTIONS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**clears every wordnet data from the lists and frees them.
	 * makes this singleton class available again, but should not be used excessively 
	 * or the garbage collector should be run between calls.
	 * 
	 * @author erhan sezerer
	 *
	 */
	public synchronized void closeDictionary()
	{
		nouns.clear();
		nouns = null;
		adverbs.clear();
		adverbs = null;
		adjectives.clear();
		adjectives = null;
		verbs.clear();
		verbs = null;
		senses.clear();
		senses = null;
		singletonUsed = false;
	}

	
	
	
	
	/**adds a list of words to the dictionary.
	 *  
	 * @author erhan sezerer
	 *
	 * @param words - a list of WordNetWord.
	 * 
	 * @return boolean - if the operation is successful returns true.
	 */
	public boolean addIndexWords(final ArrayList<WordNetWord> words)
	{
		boolean retVal = true;
		int count = words.size();
		
		for (int i=0; i<count; i++)
		{
			if(!addIndexWord(words.get(i)))
			{
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
	
	
	
	
	
	/**adds a single word to the dictionary.
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - an instance to a WordNetWord object to be added.
	 * 
	 * @return - boolean - true if the word successfully added.
	 */
	public synchronized boolean addIndexWord(final WordNetWord word)
	{
		boolean retVal = false;
		
		if(word != null)
		{
			switch(word.getPos())
			{
				case NOUN: retVal = nouns.add(word);
					  	  nounCount++;
						  break;	  	  
					  
				case VERB: retVal = verbs.add(word);
						  verbCount++;
			  	  		  break;

				case ADJECTIVE: retVal = adjectives.add(word);
						  adjectiveCount++;
						  break;

				case ADVERB: retVal = adverbs.add(word);
						  adverbCount++;
			  	  		  break;

				default: break;
			}
		}
		
		return retVal;
	}
	
	
	
	/**adds a single sense word to the dictionary.
	 * @author erhan sezerer
	 *
	 * @param word - an instance to a WordNetSense object to be added.
	 * 
	 * @return - boolean - true if the sense word successfully added.
	 */
	public synchronized boolean addSense(final WordNetSense word)
	{
		boolean retVal = false;
		
		if(word != null)
		{
			retVal = senses.add(word);
			senseCount++;
		}
		
		return retVal;
	}
	
	
	
	/**adds a list of sense words to the dictionary.
	 *  
	 * @author erhan sezerer
	 *
	 * @param words - a list of WordNetSense.
	 * 
	 * @return boolean - if the operation is successful returns true.
	 */
	public boolean addSenses(final ArrayList<WordNetSense> words)
	{
		boolean retVal = true;
		int count = words.size();
		
		for (int i=0; i<count; i++)
		{
			if(!addSense(words.get(i)))
			{
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
 
	
	
	
	
	
	
	
	
	/*-------------------------------------------------------------------------------------------------------*/
	//				setters and getters
	/*-------------------------------------------------------------------------------------------------------*/
	public String getDictionaryPath() 
	{
		return dictionaryPath;
	}
	
	public synchronized void setDictionaryPath(final String dictionaryPath) 
	{
		this.dictionaryPath = dictionaryPath;
	}
	
	public ArrayList<WordNetWord> getNouns() 
	{
		return nouns;
	}
	
	public ArrayList<WordNetWord> getAdverbs() 
	{
		return adverbs;
	}
	
	public ArrayList<WordNetWord> getAdjectives() 
	{
		return adjectives;
	}
	
	public ArrayList<WordNetWord> getVerbs() 
	{
		return verbs;
	}
		
	public ArrayList<WordNetSense> getSenses() 
	{
		return senses;
	}
		
	public int getNounCount()
	{
		return nounCount;
	}
	
	public int getAdverbCount() 
	{
		return adverbCount;
	}
	
	public int getAdjectiveCount() 
	{
		return adjectiveCount;
	}

	public int getVerbCount()
	{
		return verbCount;
	}

	public int getSenseCount() 
	{
		return senseCount;
	}
	
	
}
