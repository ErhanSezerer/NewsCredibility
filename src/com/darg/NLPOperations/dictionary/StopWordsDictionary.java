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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.darg.utils.ArrayListUtils;
import com.darg.fileOperations.utils.FileUtils;;




/**This class is responsible for handling stop words
 * if you do not properly load the stop words,
 *  the functions won't work
 * 
 * 
 * 
 * @author erhan sezerer
 *
 */
public class StopWordsDictionary 
{
	private ArrayList<String> stopWords;
	private long stopWordsCount;
	
	
	 	
	//prevent users from calling empty constructor
	@SuppressWarnings("unused")
	private StopWordsDictionary()
	{
		
	}
	public StopWordsDictionary(final String path)
	{
		stopWords = new ArrayList<String>();
		stopWordsCount = 0;
		init(path);
	}
	public StopWordsDictionary(ArrayList<String> stopwords)
	{
		this.stopWords = stopwords;
		stopWordsCount = stopwords.size();
	}
	
	
	
	private boolean init(final String path)
	{
		boolean retVal = true;
		File file = new File(path);
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(file));) 
		{
			
			String line;
			
			while ((line = br.readLine()) != null) 
			{
				if(!line.isEmpty() && line.charAt(0) != '#')//do not count comment lines
				{
					retVal = addToList(line.trim());
					setStopWordsCount(getStopWordsCount()+1);
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			retVal = false;
		}
		
		
		return retVal;
	}

	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////FUNCTIONS ///////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////

	/**checks whether the given word is a stop word or not
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - a string representing a word to examine 
	 * 
	 * @return boolean - true if the word is a stop word false otherwise.
	 * 					  returns false if the string is null or empty.
	 */
	public boolean isStopWord(final String word)
	{
		boolean retVal = false;
		
		if(word != null && !word.isEmpty())
		{
			if(ArrayListUtils.containsIgnoresCase(stopWords, word.trim()))
			{
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	
	/**adds the given stop word to the current list of stop words
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - a string to add to the stop words
	 * 
	 * @return boolean - false if the string is null or empty
	 */
	public boolean addStopWord(final String word)
	{
		boolean retVal = false;
		
		if(word != null && !word.isEmpty())
		{
			retVal = addToList(word);
			setStopWordsCount(getStopWordsCount()+1);
		}
		
		return retVal;
	}
	
	
	/**adds the given list of stop words to the current list of stop words
	 * if the string is empty or null it will not try to add it to the list
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - a string to add to the stop words
	 * 
	 * @return boolean - true if all of the strings are added successfully
	 */
	public boolean addStopWords(final ArrayList<String> words)
	{
		boolean retVal = true;
		int length = words.size();
		
		for(int i=0; i<length; i++)
		{
			if(!addStopWord(words.get(i)))
			{
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
	
	
	
	/**retrieves the stop words from the file found from the path and deletes the current ones
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a string that is a path to a file containing stop words
	 * 
	 * @return boolean - false if there is any errors while loading the stop words from the file
	 * 					 otherwise true
	 */
	public boolean loadStopWords(final String path)
	{
		clearStopWords();
		return init(path);
	}
	
	

	
	/**creates a file from the current stop words in list
	 * 
	 * @author erhan sezerer
	 *
	 * @param destination - a path to export
	 * 
	 * @return boolean - to determine success, false if there is an error
	 * 											true if there is none
	 */
	public boolean exportStopWords(final String destination)
	{
		String content = new String("#STOP WORDS - output of darg.iyte.edu.tr data mining tool\n\n#total: "
				+ getStopWordsCount()+" stop words\n\n");
		
		for(String string : stopWords) 
		{
			content = content.concat(string);
			content = content.concat(new String("\n"));
		}
		
		return FileUtils.writeFile(destination, "stopwords.txt", content);
	}
	
	
	
	
	
	
	/**retrieves the stop words from the file found from the path and appends them to the current ones
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a string that is a path to a file containing stop words
	 *
	 * @return boolean - false if there is any errors while loading the stop words from the file
	 * 					 otherwise true
	 */
	public boolean appendStopWords(final String path)
	{
		return init(path);
	}
	
	
	
	
	/**removes the given word from stop word list
	 * if the word is null or empty, or it does not exist in the list
	 * it will return false
	 * 
	 * @author erhan sezerer
	 *
	 * @param word - word that will be removed from the list
	 * 
	 * @return boolean - true if the operation is successful
	 */
	public boolean removeStopWord(final String word)
	{
		boolean retVal = false;
		
		if(word != null && !word.isEmpty())
		{
				for(int i=0; i<stopWordsCount; i++)
				{
					if(stopWords.get(i).equalsIgnoreCase(word.trim()))
					{
						removeFromList(i);
						setStopWordsCount(getStopWordsCount()-1);;
						retVal = true;
					}
				}
		}
		
		return retVal;
	}
	
	
	
	
	/**removes the given words from stop word list
	 * if the words list is null or empty it will return false
	 * 
	 * @author erhan sezerer
	 *
	 * @param words - word that will be removed from the list
	 * @return boolean - true if the operation is successful
	 */
	public boolean removeStopWords(final ArrayList<String> words)
	{
		boolean retVal = false;
		int length = words.size();
		
		if(words != null && !words.isEmpty())
		{
			retVal = true;
			
			for(int i=0; i<length; i++)
			{
				removeStopWord(words.get(i));	
			}
		}
		
		return retVal;
	}
	
	
	
	
	
	
	
	
	
	/**removes the stop words from the given map whose keys are the words
	 * returns a map without the stop words
	 * 
	 * @author erhan sezerer
	 *
	 * @param words - a map to do the filtering operation
	 * 
	 * @return Map<String,E> - a reduced version of the map after the filtering operation
	 */
	public <E> Map<String, E> filterStopWords(final Map<String, E> words)
	{
		String keyTerm = null;
		HashMap<String, E> tempMap = (HashMap<String, E>) words;
		
		
		Iterator<String> tempWords = tempMap.keySet().iterator();
		
		
		while(tempWords.hasNext())
		{
			keyTerm = tempWords.next().trim();
			
			if(ArrayListUtils.containsIgnoresCase(stopWords, keyTerm))
			{
				tempWords.remove();
			}
		}
		
		return words;
	}
	
	
	
	
	
	/**removes the stop words from the given array list
	 * returns an array list without the stop words
	 * 
	 * @author erhan sezerer
	 *
	 * @param words - a map to do the filtering operation
	 * 
	 * @return ArrayList<String> - a reduced version of the map after the filtering operation
	 */
	public ArrayList<String> filterStopWords(final ArrayList<String> words)
	{
		String keyTerm = null;
		int length = words.size();
		
		
		for(int i=0; i<length; i++)
		{
			keyTerm = words.get(i);
			
			if(ArrayListUtils.containsIgnoresCase(stopWords, keyTerm))
			{
				words.remove(i);
			}
		}
		
		return words;
	}

	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	//setters and getters vs.
	////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{
		String retVal = new String("");
		
		for(int i=0; i<stopWordsCount; i++)
		{
			retVal += ("stopWord "+ i +": " + stopWords.get(i));
		}
		
		return retVal;
	}
	
	
	public synchronized void clearStopWords()
	{
		stopWords.clear();
		stopWordsCount = 0;
	}
	
	
	public synchronized boolean addToList(final String word)
	{
		return stopWords.add(word);
	}
	public synchronized String removeFromList(final int wordIndex)
	{
		return stopWords.remove(wordIndex);
	}
	
	
	
	public synchronized ArrayList<String> getStopWords() 
	{
		return stopWords;
	}

	public synchronized void setStopWords(ArrayList<String> stopWords) 
	{
		this.stopWords = stopWords;
	}
	
	public synchronized long getStopWordsCount() 
	{
		return stopWordsCount;
	}
	
	public synchronized void setStopWordsCount(long stopWordsCount) 
	{
		this.stopWordsCount = stopWordsCount;
	}
	
}
