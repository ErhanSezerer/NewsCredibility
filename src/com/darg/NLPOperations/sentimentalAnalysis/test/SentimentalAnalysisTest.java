package com.darg.NLPOperations.sentimentalAnalysis.test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

import org.junit.BeforeClass;

import com.darg.NLPOperations.dictionary.SentiWordNetDictionary;
import com.darg.NLPOperations.dictionary.WordNetDictionary;
import com.darg.NLPOperations.dictionary.model.SentiWordNet;
import com.darg.NLPOperations.dictionary.model.WordNet;
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.pos.util.POSTagEnglish;
import com.darg.NLPOperations.sentimentalAnalysis.AverageScoreSentimentAnalyzer;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;

public class SentimentalAnalysisTest 
{
	
	
	
	
	public static SentiWordNetDictionary dict = SentiWordNetDictionary.getInstance("/home/erhan/Desktop/TEST-FOR-THESIS/sentiwordnet/SentiWordNet_3.0.0_20130122.txt", 
			SentiWordNet.VERSION_30, true);
	private static String path = "/home/erhan/Desktop/TEST-FOR-THESIS/WordNet-3.0/dict";
	private static WordNetDictionary dict2 = WordNetDictionary.getInstance(path , WordNet.VERSION_30);	

	
	
	/*----------------------------------------------------------------------------------*/
	//			
	/*----------------------------------------------------------------------------------*/
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		if(!dict.loadDictionary())
		{
			fail("cannot load the sentiwordnet library");
		}
		if(!dict2.loadDictionary())
		{
			fail("cannot load the wordnet library");
		}
		
		System.out.println("adjectives: " + dict.getAdjectiveCount());
		System.out.println("nouns: " + dict.getNounCount());
		System.out.println("adverbs: " + dict.getAdverbCount());
		System.out.println("verbs: " + dict.getVerbCount());
		System.out.println("total words: " + dict.getWordCount());
	}
	
	
	@Test
	public void documentBasedAnalysisTest()
	{
		ArrayList<TaggedWord> list = new ArrayList<TaggedWord>();
		list.add(new TaggedWord("i" , POSTagEnglish.NN, "NN"));
		list.add(new TaggedWord("was" , POSTagEnglish.VB, "VB"));
		list.add(new TaggedWord("unable" , POSTagEnglish.JJR, "JJR"));
		list.add(new TaggedWord("to" , POSTagEnglish.TO, "TO"));
		list.add(new TaggedWord("test" , POSTagEnglish.VBD, "VBD"));
		list.add(new TaggedWord("the" , POSTagEnglish.IN, "IN"));
		list.add(new TaggedWord("code" , POSTagEnglish.NN, "NN"));
		list.add(new TaggedWord("." , POSTagEnglish.DOT, "."));
		
		AverageScoreSentimentAnalyzer analyzer = new AverageScoreSentimentAnalyzer(dict2, dict);
		SentimentScore score = analyzer.documentBasedAnalysis(list, false);
		
		if(score != null)
		{

			assertEquals(4 , score.getWordsFound());
			assertEquals(8 , score.getWordCount());
			System.out.println("pos=" + score.getPositiveProb());
			System.out.println("neg=" + score.getNegativeProb());
			System.out.println("obj=" + score.getObjectiveProb());
			System.out.println("found=" + score.getWordsFound());
			System.out.println("count=" + score.getWordCount());
			fail("control manually");
		}
		else
		{
			fail("cannot calculate the score");
		}
		
	}


}








