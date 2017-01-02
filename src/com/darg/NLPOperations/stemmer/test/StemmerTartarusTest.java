/*
 * Written by Mustafa Toprak
 * Contact <mustafatoprak@iyte.edu.tr> for comments and bug reports
 *
 * Copyright (C) 2014 Mustafa Toprak
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
package com.darg.NLPOperations.stemmer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;


import org.junit.BeforeClass;
import org.junit.Test;



import com.darg.NLPOperations.stemmer.StemmerTartarus;

public class StemmerTartarusTest {

	private static StemmerTartarus stemmer;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		stemmer = new StemmerTartarus();
	}


	//Tests for StemWordTartarus method
	@Test
	public void testStemWordTartarus_validTerm(){
		System.out.println("testStemWordTartarus_validTerm");
		String term = "universities";
		String result = stemmer.stemWordTartarus(term);
		assertTrue(result.equals("univers"));
	}
	
	@Test
	public void testStemWordTartarus_nullTerm(){
		System.out.println("testStemWordTartarus_nullTerm");
		String term = null;
		String result = stemmer.stemWordTartarus(term);
		assertTrue(result == null);
	}
	
	//Tests for StemWordsTartarus method
	@Test
	public void testStemWordsTartarus_nullHashmap(){
		System.out.println("testStemWordsTartarus_nullHashmap");
		HashMap<String, Long> result = stemmer.stemWordsTartarus(null);
		assertTrue(result == null);
	}
	
	@Test
	public void testStemWordsTartarus_emptyHashmap(){
		System.out.println("testStemWordsTartarus_emptyHashmap");
		HashMap<String, Long> result = stemmer.stemWordsTartarus(new HashMap<String, Long>());
		assertTrue(result == null);
	}
	
	@Test
	public void testStemWordsTartarus_validHashmap(){
		System.out.println("testStemWordsTartarus_validHashmap");
		HashMap<String, Long> terms = new HashMap<>();
		terms.put("universities", 12L);
		terms.put("university", 3L);
		terms.put("universe", 1L);
		terms.put("vietnamization", 2L);
		
		HashMap<String, Long> result = stemmer.stemWordsTartarus(terms);
		
		assertTrue(result.size() == 2);
		assertTrue(result.containsKey("univers"));
		assertTrue(result.get("univers") == 16);
		assertTrue(result.containsKey("vietnam"));
		assertTrue(result.get("vietnam") == 2);
	}
	
	//Tests for StemWordListTartarus method
	@Test
	public void testStemWordList_nullHashmap(){
		System.out.println("testStemWordList_nullHashmap");
		ArrayList<String> result = stemmer.stemWordListTartarus(null);
		assertTrue(result == null);
	}
	
	@Test
	public void testStemWordList_emptyHashmap(){
		System.out.println("testStemWordList_emptyHashmap");
		ArrayList<String> result = stemmer.stemWordListTartarus(new ArrayList<String>());
		assertTrue(result == null);
	}
	
	@Test
	public void testStemWordList_validHashmap(){
		System.out.println("testStemWordList_validHashmap");
		ArrayList<String> terms = new ArrayList<>();
		terms.add("universities");
		terms.add("university");
		terms.add("universe");
		terms.add("vietnamization");
		
		ArrayList<String> result = stemmer.stemWordListTartarus(terms);
		
		assertTrue(result.size() == 2);
		assertTrue(result.contains("univers"));
		assertTrue(result.contains("vietnam"));
	}
}
