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

import com.darg.NLPOperations.dictionary.WordNetDictionary;
import com.darg.NLPOperations.dictionary.model.WordNet;
import com.darg.NLPOperations.dictionary.model.WordNetWord;
import com.darg.NLPOperations.pos.util.POSTagWordNet;


public class WordNetTests 
{
	private static String path = "/home/erhan/Desktop/TEST-FOR-THESIS/WordNet-3.0/dict";
	private static WordNetDictionary dict = WordNetDictionary.getInstance(path , WordNet.VERSION_30);	
	
	
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
		System.out.println("sense words: " + dict.getSenseCount());
	}
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			INDEX FILES TESTS
	/*----------------------------------------------------------------------------------*/

	/**Tests the findIDs() function
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDsTest()
	{
		ArrayList<Integer> ids = dict.findIDs("unable", POSTagWordNet.ADJECTIVE);
		assertEquals(3, ids.size());
			
		for (int i=0; i<ids.size(); i++)
		{
			System.out.println("index id: " + ids.get(i));
		}
	}
	
	
	/**Test findWord() function
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findWordTest()
	{
		String word = dict.findWord(14760447, POSTagWordNet.NOUN);
		assertEquals("alligator", word);
	}
	
	
	
	
	/**Tests findID() function.
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDTest()
	{
		int id = dict.findID("unable", POSTagWordNet.ADJECTIVE, 2);
		assertEquals(307794, id);
	}
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			SENSE FILE TESTS
	/*----------------------------------------------------------------------------------*/
	/**Tests findIDsFromSenseSlow() function. Uses brute force search. 
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDsFromSenseSlowTest()
	{
		ArrayList<Integer> ids = dict.findIDsFromSenseSlow("unable", POSTagWordNet.ADJECTIVE);
		assertEquals(3, ids.size());
			
		for (int i=0; i<ids.size(); i++)
		{
			System.out.println("slow id: " + ids.get(i));
		}
	}
	
	
	
	
	
	/**Tests findIDFromSenseSlow() function.
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDFromSenseSlowTest()
	{
		int id = dict.findIDFromSenseSlow("unable", POSTagWordNet.ADJECTIVE, 2);
		assertEquals(307794, id);

	}

	
	
	
	
	
	/**Tests findIDFromSense() function. Uses binary search. 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDsFromSenseTest()
	{
		ArrayList<Integer> ids = dict.findIDsFromSense("unable", POSTagWordNet.ADJECTIVE);
			
		for (int i=0; i<ids.size(); i++)
		{
			System.out.println("fast id: " + ids.get(i));
		}
			
		assertEquals(3, ids.size());
	}

	
	
	
	
	/**Test findIDFromSense() function.
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findIDFromSenseTest()
	{
		int id = dict.findIDFromSense("unable", POSTagWordNet.ADJECTIVE, 2);			
		assertEquals(307794, id);
	}
	
	
	
	
	
	/**Tests findWordFromSense() function. 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void findWordFromSenseTest()
	{
		String word = dict.findWordFromSense(14760447, POSTagWordNet.NOUN);
		assertEquals("alligator", word);	
	}
	
	
	
	
	/**test of addWord() function
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void addWordTest()
	{
		int count = dict.getAdjectiveCount();
		WordNetWord word = new WordNetWord("asd", POSTagWordNet.ADJECTIVE);
		dict.addIndexWord(word);
		
		assertEquals(count+1, dict.getAdjectiveCount());
	}

	
	
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			FAIL TESTS
	/*----------------------------------------------------------------------------------*/
	/**tests whether the singleton pattern works or not
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void singletonTest()
	{
		//dict already created
		assertEquals(null, WordNetDictionary.getInstance(path, WordNet.VERSION_30));
	}
	
	
	
	
	/**tests how the system behaves when it encounters an unknown word
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void unknownWordTest()
	{
		assertEquals(-1, dict.findID("asdasdasd", POSTagWordNet.ADJECTIVE, 1));
		assertEquals(-1, dict.findID("unable", POSTagWordNet.OTHER, 1));
		assertEquals(-1, dict.findID("unable", POSTagWordNet.ADJECTIVE, 16541));
	}
	
	
}
