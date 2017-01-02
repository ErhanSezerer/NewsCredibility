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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.darg.NLPOperations.dictionary.StopWordsDictionary;

public class StopWordsTests 
{
	private static String path = new String("/home/erhan/Desktop/TEST-FOR-THESIS");
	private static String exportPath = new String("/home/erhan/Desktop/");
	
	

	@Test
	public void filterStopWordsTest() 
	{
		HashMap<String, Long> asd = new HashMap<String, Long>();
		ArrayList<String> stop = new ArrayList<String>();
		HashMap<String, Long> newMap = null;
		
		
		asd.put("to", (long) 2);
		asd.put("university", (long) 2);
		asd.put("cascade", (long) 2);
		asd.put("be", (long) 2);
		asd.put("are", (long) 2);
		asd.put("medieval", (long) 2);
		
		stop.add("to");
		stop.add("be");
		stop.add("are");
		stop.add("zart");
		

		StopWordsDictionary sw = new StopWordsDictionary(stop);
		newMap = (HashMap<String, Long>) sw.filterStopWords(asd);
		
		
		assertEquals(3, newMap.size());
	}
	
	
	
	public void filterStopWordsFromArrayTest() 
	{
		ArrayList<String> asd = new ArrayList<String>();
		ArrayList<String> stop = new ArrayList<String>();
		ArrayList<String> newMap = new ArrayList<String>();
		
		
		asd.add("to");
		asd.add("university");
		asd.add("cascade");
		asd.add("be");
		asd.add("are");
		asd.add("medieval");
		
		stop.add("to");
		stop.add("be");
		stop.add("are");
		stop.add("zart");
		

		StopWordsDictionary sw = new StopWordsDictionary(stop);
		newMap = sw.filterStopWords(asd);
		
		
		assertEquals(3, newMap.size());
	}


	
	
	@Test
	public void isStopWordTest()
	{
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		assertTrue(sw.isStopWord("to") == true);
		assertTrue(sw.isStopWord("five") == true);
	}
	
	
	
	@Test
	public void addStopWordsTest()
	{
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		ArrayList<String> stopwords = new ArrayList<String>();
		stopwords.add("stopwords1");
		stopwords.add("stopwords2");
		stopwords.add("stopwords3");
		
		long count = sw.getStopWordsCount();
		sw.addStopWords(stopwords);
		
		assertEquals(count+3, sw.getStopWordsCount());
	}
	
	
	@Test
	public void loadStopWordsTest()
	{
		File file = new File(path, "stopWords2.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		sw.loadStopWords(file.getAbsolutePath());
		
		assertEquals(4, sw.getStopWordsCount());
	}
	
	
	@Test
	public void loadStopWordsWithCommentTest()
	{
		File file = new File(path, "stopwords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		sw.loadStopWords(file.getAbsolutePath());
		
		assertEquals(720, sw.getStopWordsCount());
	}

	
	
	
	@Test
	public void exportStopWordsTest()
	{
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		sw.addStopWord("newWord");
		sw.exportStopWords(exportPath);
		sw.loadStopWords(new File(exportPath, "stopwords.txt").getAbsolutePath());
		
		assertEquals(720, sw.getStopWordsCount());
	}
	
	
	
	
	@Test
	public void appendStopWordsTest()
	{
		long count = 0;
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		count = sw.getStopWordsCount();
		sw.appendStopWords(new File(path, "stopWords2.txt").getAbsolutePath());
		
		assertEquals(count+4, sw.getStopWordsCount());

	}
	
	
	@Test
	public void removeStopWordsTest()
	{
		long count = 0;
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		ArrayList<String> words = new ArrayList<String>();
		words.add("to");
		words.add("b");
		words.add("therefore");
		
		count = sw.getStopWordsCount();
		sw.removeStopWords(words);
		
		assertEquals(count-3, sw.getStopWordsCount());
	}
	
	
	
	
	/*------------------------------------------------------------------------------------------------------*/
	// 						FAIL SCENARIOS
	/*------------------------------------------------------------------------------------------------------*/
	@Test
	public void isStopWordFailTest()
	{
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		assertTrue(sw.isStopWord("party") == false);
		assertTrue(sw.isStopWord("finance") == false);
	}
	
	
	
	public void appendStopWordFailTest()
	{
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		
		
		assertEquals(false, sw.appendStopWords(new File(path, "stopWords85.txt").getAbsolutePath()));
	}
	
	
	
	@Test
	public void removeStopWordFailTest()
	{
		long count = 0;
		File file = new File(path, "stopWords.txt");
		StopWordsDictionary sw = new StopWordsDictionary(file.getAbsolutePath());
		ArrayList<String> words = new ArrayList<String>();
		words.add("data");
		words.add("mining");
		words.add("tool");
		
		count = sw.getStopWordsCount();
		sw.removeStopWords(words);
		
		assertEquals(count, sw.getStopWordsCount());
	}
	
	
	
	
	
}





















