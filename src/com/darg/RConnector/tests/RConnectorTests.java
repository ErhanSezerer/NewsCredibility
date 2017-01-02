package com.darg.RConnector.tests;

import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScoreType;
import com.darg.RConnector.REngineConnector;

public class RConnectorTests {

	public static void main(String[] args) 
	{
		REngineConnector r = REngineConnector.getInstance();
		r.createConnection(args);
		r.setEnvForSental("/home/erhan/Desktop/pos.txt", "/home/erhan/Desktop/neg.txt", "/home/erhan/Desktop/obj.txt");
		//System.out.println("-0.02 OBJ:" + r.evaluateScore(-0.02, SentimentScoreType.OBJ));
		System.out.println("0.0 OBJ:" + r.evaluateScore(0.0, SentimentScoreType.OBJ));
		System.out.println("0.1 OBJ:" + r.evaluateScore(0.1, SentimentScoreType.OBJ));
		System.out.println("0.25 OBJ:" + r.evaluateScore(0.25, SentimentScoreType.OBJ));
		System.out.println("0.5 OBJ:" + r.evaluateScore(0.5, SentimentScoreType.OBJ));
		System.out.println("0.75 OBJ:" + r.evaluateScore(0.75, SentimentScoreType.OBJ));
		System.out.println("0.9 OBJ:" + r.evaluateScore(0.9, SentimentScoreType.OBJ));
		System.out.println("0.91 OBJ:" + r.evaluateScore(0.91, SentimentScoreType.OBJ));
		System.out.println("0.95 OBJ:" + r.evaluateScore(0.95, SentimentScoreType.OBJ));
		System.out.println("1.0 OBJ:" + r.evaluateScore(1.0, SentimentScoreType.OBJ));
		//System.out.println("1.05 OBJ:" + r.evaluateScore(1.05, SentimentScoreType.OBJ));
		
		
		//System.out.println("1.02 NEG:" + r.evaluateScore(1.02, SentimentScoreType.NEG));
		System.out.println("1.0 NEG:" + r.evaluateScore(1.0, SentimentScoreType.NEG));
		System.out.println("0.9 NEG:" + r.evaluateScore(0.9, SentimentScoreType.NEG));
		System.out.println("0.75 NEG:" + r.evaluateScore(0.75, SentimentScoreType.NEG));
		System.out.println("0.5 NEG:" + r.evaluateScore(0.5, SentimentScoreType.NEG));
		System.out.println("0.25 NEG:" + r.evaluateScore(0.25, SentimentScoreType.NEG));
		System.out.println("0.1 NEG:" + r.evaluateScore(0.1, SentimentScoreType.NEG));
		System.out.println("0.05 NEG:" + r.evaluateScore(0.05, SentimentScoreType.NEG));
		System.out.println("0.0 NEG:" + r.evaluateScore(0.0, SentimentScoreType.NEG));
		//System.out.println("-0.02 NEG:" + r.evaluateScore(-0.02, SentimentScoreType.NEG));
	
		
		//System.out.println("1.02 POS:" + r.evaluateScore(1.02, SentimentScoreType.POS));
		System.out.println("1.0 POS:" + r.evaluateScore(1.0, SentimentScoreType.POS));
		System.out.println("0.9 POS:" + r.evaluateScore(0.9, SentimentScoreType.POS));
		System.out.println("0.75 POS:" + r.evaluateScore(0.75, SentimentScoreType.POS));
		System.out.println("0.5 POS:" + r.evaluateScore(0.5, SentimentScoreType.POS));
		System.out.println("0.25 POS:" + r.evaluateScore(0.25, SentimentScoreType.POS));
		System.out.println("0.1 POS:" + r.evaluateScore(0.1, SentimentScoreType.POS));
		System.out.println("0.05 POS:" + r.evaluateScore(0.05, SentimentScoreType.POS));
		System.out.println("0.0 POS:" + r.evaluateScore(0.0, SentimentScoreType.POS));
		//System.out.println("-0.02 POS:" + r.evaluateScore(-0.02, SentimentScoreType.POS));


	}

}
