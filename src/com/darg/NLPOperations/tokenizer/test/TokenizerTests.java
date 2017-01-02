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
package com.darg.NLPOperations.tokenizer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.util.Version;
import org.junit.Test;

import com.darg.NLPOperations.tokenizer.KeywordGenerator;
import com.darg.NLPOperations.tokenizer.Tokenizer;
import com.darg.fileOperations.utils.FileUtils;

import edu.stanford.nlp.ling.Word;

public class TokenizerTests 
{
	private static String path = new String("/home/erhan/Desktop/TEST-FOR-THESIS/fortokenizer.txt");
	private static String path2 = new String("/home/erhan/Desktop/TEST-FOR-THESIS/forsentencetokenizer.txt");
	

	@Test
	public void tokenizePTBTest()
	{
		ArrayList<Word> words = Tokenizer.tokenizeText(path, true);

		assertEquals(31, words.size());
	}

	
	
	@Test
	public void tokenizeWhitespaceTest()
	{
		ArrayList<Word> words = Tokenizer.tokenizeText(path, false);

		assertEquals(22, words.size());
	}

	
	
	@Test
	public void extractKeywordTest()
	{
		KeywordGenerator kg = new KeywordGenerator(Version.LUCENE_42);
		File file = new File(path);
		HashMap<String, Long> keywords = kg.extractKeywords(file);
		
		System.out.println(keywords.toString());
		
		assertEquals(15, keywords.size());
	}
	
	
	
	@Test
	public void tokenizeIntoSentencesTest()
	{
		ArrayList<String> sentences = Tokenizer.tokenizeIntoSentences(FileUtils.readFile(path2));
		
		for (int i=0; i<sentences.size(); i++)
		{
			System.out.println(sentences.get(i));
		}
		
		assertEquals(4, sentences.size());
	}
	
	
	
	/*----------------------------------------------------------------------*/
	//				FAIL SCENARIOS
	/*----------------------------------------------------------------------*/
	@Test
	public void tokenizefailTest()
	{
		ArrayList<Word> words = Tokenizer.tokenizeText("/home/erhan/Desktop/TEST-FOR-THESIS/nonexisting.txt", false);
		
		assertEquals(null, words);
	}

}
