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

import java.util.ArrayList;

import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.tokenizer.Tokenizer;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.WordTag;

public final class POSTagTokenizer 
{
	
	
	
	/**tokenizes the tagged text into words and splits the word tag pair
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a string to file location
	 * @param divider - a string to determine which character(s) are used for splitting the tagged word
	 * 
	 * @return ArrayList<TaggedWord> - word-tag pairs in this text. null if there is any error.
	 */
	public static ArrayList<TaggedWord> tokenizeTaggedText(final String path, final String divider)
	{
		ArrayList<TaggedWord> tempList = null;
		boolean flag = false;
		
		//tokenize the text into words
		ArrayList<Word> taggedWords = Tokenizer.tokenizeText(path, false);
		
		if(taggedWords != null)
		{
			tempList = new ArrayList<TaggedWord>();
			
			int count = taggedWords.size();
			
			
			//parse each word for tags and add to the list
			for (int i=0; i<count; i++) 
			{
				TaggedWord tw = new TaggedWord();
				flag = tw.setPair(taggedWords.get(i).word(), divider);
				
				if(flag)
				{
					tempList.add(tw);
				}
			}
			
		}
		
		
		return tempList;
	}
	
	
	
	
	
	/**tokenizes the tagged text into words and splits the word tag pair.
	 * Uses WordTag object from Stanford
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a string to file location
	 * @param divider - a string to determine which character(s) are used for splitting the tagged word
	 * 
	 * @return ArrayList<WordTag> - word-tag pairs in this text. null if there is any error.
	 */
	public static ArrayList<WordTag> tokenizeTaggedTextWordTag(final String path, final String divider)
	{
		ArrayList<WordTag> tempList = null;
		
		//tokenize the text into words
		ArrayList<Word> taggedWords = Tokenizer.tokenizeText(path, false);
		
		
		if(taggedWords != null)
		{
			tempList = new ArrayList<WordTag>();
			int count = taggedWords.size();
			
			//parse each word and add them to the list
			for (int i=0; i<count; i++) 
			{
				tempList.add(WordTag.valueOf(taggedWords.get(i).word(), divider));
			}
			
		}
		
		return tempList;
	}
	
	
	
	
	
	/**tokenizes and splits the tagged text into sentences. 
	 * Uses White space tokenizer. 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param path - a path to the file that contains tagged text
	 * @param divider - a string that divides the word tag pair in the text
	 * 
	 * 
	 * @return ArrayList<TaggedSentence> - or null if there are any error (i.e. invalid path ot empty file)
	 */
	public static ArrayList<TaggedSentence> tokenizeTaggedTextIntoSentences(final String path, final String divider)
	{
		ArrayList<TaggedSentence> sentences = new ArrayList<TaggedSentence>();
		
		Word word;
		TaggedWord taggedWord;
		TaggedSentence s = new TaggedSentence();
		
		//tokenize the text into words
		ArrayList<Word> tempList = Tokenizer.tokenizeText(path, false);
		
		//error check
		if(tempList == null || tempList.isEmpty())
		{
			sentences = null;
		}
		else
		{
			int count = tempList.size();
			
			//parse each word and decide whether it is the end of a sentence or not
			for(int i=0; i<count; i++)
			{
				word = tempList.get(i);
				taggedWord = new TaggedWord();
				taggedWord.setPair(word.word(), divider);
				
				
				//if nothing is wrong.
				//in some cases exceptional words will occur which will lead to mistakes and exceptions
				//(such as the words "10" in article 4039 of reuters)
				if(taggedWord.getWord() != null || taggedWord.getTag() != null)
				{
					s.appendToSentence(taggedWord);
				}
				
				
				if(POSTagEnglish.isEndOfSentence(taggedWord.getTag()))
				{
					sentences.add(s);
					s = new TaggedSentence();
				}
				
			}

		}
		
		return sentences;
	}
	
	
	
	
	
	/**splits the given tagged words into sentences. 
	 *  
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param taggedWords - a list of tokenized word-tag pairs
	 * 
	 * @return ArrayList<TaggedSentence> - or null if there are any error (i.e. null or empty list)
	 */
	public static ArrayList<TaggedSentence> tokenizeTaggedTextIntoSentences(final ArrayList<TaggedWord> taggedWords)
	{
		ArrayList<TaggedSentence> sentences = new ArrayList<TaggedSentence>();
		
		TaggedWord taggedWord = new TaggedWord();
		TaggedSentence s = new TaggedSentence();
		
		
		if(taggedWords == null || taggedWords.isEmpty())
		{
			sentences = null;
		}
		else
		{
			int count = taggedWords.size();
			
			//parse each word and decide whether it is the end of a sentence or not
			for(int i=0; i<count; i++)
			{
				taggedWord = taggedWords.get(i);
				s.appendToSentence(taggedWord);
				
				
				if(POSTagEnglish.isEndOfSentence(taggedWord.getTag()))
				{
					sentences.add(s);
					s = new TaggedSentence();
				}
				
			}

		}
		
		
		return sentences;
	}

	
}
