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
package com.darg.NLPOperations.dictionary.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.darg.NLPOperations.dictionary.SentiWordNetDictionary;
import com.darg.NLPOperations.dictionary.model.SentiWordNet;
import com.darg.NLPOperations.dictionary.model.SentiWordNetWord;
import com.darg.NLPOperations.dictionary.model.SynsetTerm;
import com.darg.NLPOperations.pos.util.POSTagWordNet;

public class SentiWordNetTests 
{
	
	public static SentiWordNetDictionary dict = SentiWordNetDictionary.getInstance("/home/erhan/Desktop/TEST-FOR-THESIS/sentiwordnet/SentiWordNet_3.0.0_20130122.txt", 
									SentiWordNet.VERSION_30, true);
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			
	/*----------------------------------------------------------------------------------*/
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		if(!dict.loadDictionary())
		{
			fail("cannot load the library");
		}
		
		System.out.println("adjectives: " + dict.getAdjectiveCount());
		System.out.println("nouns: " + dict.getNounCount());
		System.out.println("adverbs: " + dict.getAdverbCount());
		System.out.println("verbs: " + dict.getVerbCount());
		System.out.println("total words: " + dict.getWordCount());
	}

	
	
	
	
	
	@Test
	public void searchTests() 
	{
		//658052
		SentiWordNetWord word = dict.findWord(658052);
		assertEquals((Integer)658052, word.getId());
		
		word = dict.findWord(2772310);
		assertEquals((Integer)2772310, word.getId());
		
		word = dict.findWord(1740, POSTagWordNet.VERB);
		assertEquals((Integer)1740, word.getId());
		
		ArrayList<SynsetTerm> words = dict.findSynset(658052, POSTagWordNet.VERB);
		for (int i = 0; i < words.size(); i++)
		{
			System.out.println(words.get(i).getTerm());
		}
		assertEquals(6, words.size());
		
	}
	
	
	
	
	
	@Test
	public void scoreSearchTests()
	{
		ArrayList<SentiWordNetWord> words = dict.getWordsWithPositiveScore(0.5, false);
		System.out.println("positive word count over 0.5: " + words.size());
		
		words = dict.getWordsWithNegativeScore(0.5, false);
		System.out.println("negative word count over 0.5: " + words.size());
		
		words = dict.getObjectiveWords(0.5, false);
		System.out.println("objective word count over 0.5: " + words.size());
		
		
		words = dict.getOverallPositiveWords(0.5, false, POSTagWordNet.NOUN);
		System.out.println("words with pos - neg over 0.5: " + words.size());
		
		
		for (int i=0; i<words.size(); i++)
		{
			System.out.println("word " +i+ ": " + words.get(i).toString());
		}

	}

}
