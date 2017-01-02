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
package com.darg.NLPOperations.tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WhitespaceTokenizer;



/**NOTE: needs stanford-postagger-3.3.1.jar as a library.
 * 
 * Other versions might work but not guaranteed.
 * 
 * @author erhan sezerer
 *
 */
public class Tokenizer 
{
	
	
	
	/**splits the text from file whose location is given by the path into tokens
	 * . Can be used with whitespace tokenizer or PTB tokenizer
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - String that represents the path of a text file
	 * 
	 * @param method - boolean for describing which tokenization method will be used. 
	 * 					If true PTB tokenizer will be used
	 * 					else WhiteSpace Tokenizer will be used
	 * 
	 * @return ArrayList<Word> - tokenized words. returns null if there are any errors
	 */
	public static ArrayList<Word> tokenizeText(final String path, final boolean method)
	{
		ArrayList<Word> tempList = null;
		
		if(method)
		{
			tempList = tokenizeTextPTB(path);
		}
		else
		{
			tempList = tokenizeTextWhitespace(path);
		}
		
		return tempList;
	}
	
	
	
	
	
	
	/**splits the text from file whose location is given by the path into tokens.
	 *  Uses PTBTokenizer of Stanford NLP
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - String that represents the path of a text file
	 * 
	 * @return ArrayList<Word> - tokenized words. returns null if there are any errors
	 */
	private static ArrayList<Word> tokenizeTextPTB(final String path)
	{
		ArrayList<Word> tempWords = new ArrayList<Word>();
		File file = new File(path);
		
		try(FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr))
		{
			
			PTBTokenizer<Word> words = PTBTokenizer.newPTBTokenizer(br);
			
			while(words.hasNext())
			{
				tempWords.add(words.next());
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			tempWords = null;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			tempWords = null;
		}
		
		
		
		return tempWords;
	}

	
	
	
	
	
	
	/**splits the text from file whose location is given by the path into tokens
	 * . Uses WhiteSpaceTokenizer of Stanford NLP
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - String that represents the path of a text file
	 * 
	 * @return ArrayList<Word> - tokenized words. returns null if there are any errors
	 */
	private static ArrayList<Word> tokenizeTextWhitespace(final String path)
	{
		ArrayList<Word> tempWords = new ArrayList<Word>();
		File file = new File(path);
		
		try(FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr))
		{
			
			WhitespaceTokenizer<Word> words = WhitespaceTokenizer.newWordWhitespaceTokenizer(br);
			
			while(words.hasNext())
			{
				tempWords.add(words.next());
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			tempWords = null;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			tempWords = null;
		}
		
		
		
		return tempWords;
	}
	
	
	
	
	
	
	/**tokenizes the text into sentences. Uses every sentence ending punctuation (.!?).
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance to tokenize
	 * 
	 * @return ArrayList<String> containing sentences. null if there is any error or the text parameter is empty or null.
	 */
	public static ArrayList<String> tokenizeIntoSentences(final String text)
	{
		ArrayList<String> sentences = null;
		String tempsentence = null;
		
		if(text != null && !text.isEmpty())
		{
			sentences = new ArrayList<String>();
			
			String[] temp = text.split("(?<=[\\.|!|\\?])");
			int count = temp.length;
			
			for (int i=0; i<count; i++)
			{
				tempsentence = temp[i];

				if(tempsentence != null && tempsentence.length() >= 2)
				{
					sentences.add(tempsentence.trim());
				}
			}
			

		}
		
		
		return sentences;
	}
	
	
	
	
	
	/**tokenizes the text into sentences. Uses every sentence ending punctuation (.!?). 
	 * Also separates parenthesis as a different sentence
	 * 
	 * @author erhan sezerer
	 *
	 * @param text - a String instance to tokenize
	 * 
	 * @return ArrayList<String> containing sentences. null if there is any error or the text parameter is empty or null.
	 */
	public static ArrayList<String> tokenizeIntoSentencesWithPar(final String text)
	{
		ArrayList<String> sentences = null;
		String tempsentence = null;
		
		
		if(text != null && !text.isEmpty())
		{
			sentences = new ArrayList<String>();
			
			String[] temp = text.split("(?<=[\\.|!|\\?|\\(|\\{|\\[|\\]|\\}|\\)])");
			int count = temp.length;
			
			for (int i=0; i<count; i++)
			{
				tempsentence = temp[i];
				tempsentence = tempsentence.replaceAll("\\[|\\]|\\{|\\}|\\(|\\)", "").trim();

				if(tempsentence != null && tempsentence.length() >= 2)
				{
					sentences.add(tempsentence);
				}
			}
			
		}
		
		
		
		return sentences;
	}

	
}
