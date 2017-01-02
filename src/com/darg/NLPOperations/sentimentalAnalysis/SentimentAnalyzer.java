package com.darg.NLPOperations.sentimentalAnalysis;

import java.util.ArrayList;

import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.model.TaggedWord;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;

public interface SentimentAnalyzer 
{
	
	public SentimentScore documentBasedAnalysis(final ArrayList<TaggedWord> wordList);
	public ArrayList<SentimentScore> sentenceBasedAnalysis(final ArrayList<TaggedSentence> wordList);
	public ArrayList<SentimentScore> aspectBasedAnalysis(final ArrayList<TaggedWord> wordList);
}
