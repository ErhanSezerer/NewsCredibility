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
package com.darg.NLPOperations.pos.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import com.darg.NLPOperations.pos.POSTagger;
import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.pos.util.POSTagEnglish;
import com.darg.NLPOperations.pos.util.POSTagTokenizer;
import com.darg.NLPOperations.pos.util.POSTagWordNet;
import com.darg.NLPOperations.pos.util.POSTagConverter;
import com.darg.fileOperations.utils.FileUtils;

import edu.stanford.nlp.ling.WordTag;



/** TEST FOR CLASSES:
 * TaggedWord, POSTAGEnglish, POSTagger and POSTagTokenizer
 * 
 * @author erhan sezerer
 *
 */
public class POSTests 
{
	private static String path = "/home/erhan/Desktop/TEST-FOR-THESIS/forpostag.txt";
	private static String modelPath = "/home/erhan/Desktop/TEST-FOR-THESIS/stanford-postagger-full-2014-01-04/models/english-left3words-distsim.tagger";
	private static String destPath = "/home/erhan/Desktop/";
	private static String taggedPath = "/home/erhan/Desktop/TEST-FOR-THESIS/postags.txt";
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			POSTagger TESTS
	/*----------------------------------------------------------------------------------*/

	/**For function(s): POSTagger.tagString() and  POSTagTokenizer.tokenizeTaggedTextWordTag()
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTaggerTest() 
	{
		POSTagger tagger = new POSTagger(modelPath);
		String content = FileUtils.readFile(path);
		
		
		//POSTagger
		String taggedText = tagger.tagString(content);
		
		FileUtils.writeFile(destPath, "postags.txt", taggedText);
		
		//POSTagTokenizer
		ArrayList<WordTag> wordtag = POSTagTokenizer.tokenizeTaggedTextWordTag(new File(destPath , "postags.txt").getAbsolutePath(), "_");
		
		for (int i=0; i<wordtag.size(); i++)
		{
			System.out.println(wordtag.get(i).word() + " - " + wordtag.get(i).tag());
		}
		
		
		fail("impossible to check with assertEquals! control them manually!");
	}
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			POSTagEnglish TESTS
	/*----------------------------------------------------------------------------------*/
	
	/**For POSTagEnglish functions
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagEnglishBooleanTests()
	{
		assertTrue(POSTagEnglish.isAdjective(POSTagEnglish.JJ));
		assertTrue(POSTagEnglish.isNoun(POSTagEnglish.NN));
		assertTrue(POSTagEnglish.isAdverb(POSTagEnglish.RBR));
		assertTrue(POSTagEnglish.isVerb(POSTagEnglish.VBN));
		assertTrue(POSTagEnglish.isPronoun(POSTagEnglish.WP$));
		
		assertTrue(POSTagEnglish.isAdjective("JJR"));
		assertTrue(POSTagEnglish.isNoun("NNP"));
		assertTrue(POSTagEnglish.isAdverb("WRB"));
		assertTrue(POSTagEnglish.isVerb("VB"));
		assertTrue(POSTagEnglish.isPronoun("PRP"));
		
		assertTrue(POSTagEnglish.isEndOfSentence(POSTagEnglish.DOT));
		assertTrue(POSTagEnglish.isEndOfSentence("."));
		assertTrue(POSTagEnglish.isPunctuation(POSTagEnglish.DOLSIGN));
		assertTrue(POSTagEnglish.isPunctuation("-LCB-"));
	}
	
	
	
	
	/**For POSTagEnglish functions
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagEnglishTests()
	{
		assertEquals("Cardinal Number",POSTagEnglish.getDescription(POSTagEnglish.CD));
		assertEquals("Particle",POSTagEnglish.getDescription("RP"));
		
		assertEquals(POSTagEnglish.LRB,POSTagEnglish.findTag("-LRB-"));
		assertEquals(POSTagEnglish.NNS,POSTagEnglish.findTag("NNS"));
		assertEquals(POSTagEnglish.DOT,POSTagEnglish.findTag("."));
	}
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			POSTagTokenizer TESTS
	/*----------------------------------------------------------------------------------*/
	
	/**For function POSTagTokenizer.tokenizeTaggedText()
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagTokenizerTest1()
	{
		ArrayList<TaggedWord> words = POSTagTokenizer.tokenizeTaggedText(taggedPath, "_");
		assertEquals(118, words.size());
		
		for (int i=0; i<words.size(); i++)
		{
			System.out.println("Word: " + words.get(i).getWord() + "\tTag: " + words.get(i).getTag());
		}
	}
	
	
	
	/**For function POSTagTokenizer.tokenizeTaggedTextWordTag()
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagTokenizerTest2()
	{
		ArrayList<WordTag> words = POSTagTokenizer.tokenizeTaggedTextWordTag(taggedPath, "_");
		assertEquals(118, words.size());	
		
		for (int i=0; i<words.size(); i++)
		{
			System.out.println("Word: " + words.get(i).word() + "\tTag: " + words.get(i).tag());
		}
	}

	
	
	/**For function POSTagTokenizer.tokenizeTaggedTextIntoSentences(ArrayList)
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagTokenizerTest3()
	{
		ArrayList<TaggedWord> words = POSTagTokenizer.tokenizeTaggedText(taggedPath, "_");
		ArrayList<TaggedSentence> sentences = POSTagTokenizer.tokenizeTaggedTextIntoSentences(words);
		
		
		
		for (int i=0; i<sentences.size(); i++)
		{
			System.out.println(sentences.get(i).toString() + "\n");
		}
		
		assertEquals(5, sentences.size());
	}

	
	
	
	/**For function: POSTagTokenizer.tokenizeTaggedTextIntoSentences(String)
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void posTagTokenizerTest4()
	{
		ArrayList<TaggedSentence> sentences = POSTagTokenizer.tokenizeTaggedTextIntoSentences(taggedPath, "_");
		
		assertEquals(5, sentences.size());
		
		for (int i=0; i<sentences.size(); i++)
		{
			System.out.println(sentences.get(i).toString() + "\n");
		}
	}

	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			TaggedWord TESTS
	/*----------------------------------------------------------------------------------*/

	/**Test for TaggedWord Class
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void taggedWordTest()
	{
		TaggedWord tw = new TaggedWord();
		tw.setPair("Erhan_NNS", "_");
		
		assertEquals("Erhan", tw.getWord());
		assertEquals("NNS", tw.getTagString());
		assertEquals(POSTagEnglish.NNS, tw.getTag());
	}
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			POSTagWordNEt TESTS
	/*----------------------------------------------------------------------------------*/
	@Test
	public void posTagWordNetTest()
	{
		assertEquals('a', POSTagWordNet.getChar(POSTagWordNet.ADJECTIVE));
		assertEquals('r', POSTagWordNet.getChar(POSTagWordNet.ADVERB));
		assertEquals('?', POSTagWordNet.getChar(POSTagWordNet.OTHER));
		assertEquals(POSTagWordNet.VERB, POSTagWordNet.getPosType('v'));
		assertEquals(POSTagWordNet.NOUN, POSTagWordNet.getPosType('n'));
	}
	
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			POSTagConverter TESTS
	/*----------------------------------------------------------------------------------*/
	@Test
	public void posTagConverterTest()
	{
		assertEquals(POSTagWordNet.NOUN, POSTagConverter.getPOSType("NNS"));
		assertEquals(POSTagWordNet.ADJECTIVE, POSTagConverter.getPOSType(POSTagEnglish.JJ));
	}
	
	
	
	
	
	
	
	/*----------------------------------------------------------------------------------*/
	//			FAIL SCENARIOS
	/*----------------------------------------------------------------------------------*/
	@Test
	public void posTagEnglishFailTests()
	{
		assertEquals(null,POSTagEnglish.getDescription("zart"));
		assertEquals(null,POSTagEnglish.getDescription("LBR"));
		
		assertEquals(POSTagEnglish.UNKNOWN,POSTagEnglish.findTag("-LRB"));
		assertEquals(POSTagEnglish.UNKNOWN,POSTagEnglish.findTag(";"));
		assertEquals(null,POSTagEnglish.findTag(null));
		
		assertFalse(POSTagEnglish.isAdjective(POSTagEnglish.COMMA));
		assertFalse(POSTagEnglish.isNoun(POSTagEnglish.RCB));
		assertFalse(POSTagEnglish.isAdverb(POSTagEnglish.SEMICOLON));
		assertFalse(POSTagEnglish.isVerb(POSTagEnglish.EX));
		assertFalse(POSTagEnglish.isPronoun(POSTagEnglish.JJ));
		
		assertFalse(POSTagEnglish.isAdjective("asd"));
		assertFalse(POSTagEnglish.isNoun("JJ"));
		
		assertFalse(POSTagEnglish.isEndOfSentence(POSTagEnglish.FW));
		assertFalse(POSTagEnglish.isEndOfSentence("{"));
		assertFalse(POSTagEnglish.isPunctuation(POSTagEnglish.PDT));
		assertFalse(POSTagEnglish.isPunctuation("-LC"));
	}


}
