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
package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;


import com.darg.NLPOperations.NLPOperations;
import com.darg.RConnector.REngineConnector;




public class Simulation {

	public static void main(String[] args) 
	{
		
		boolean check = true;
		
		
		//for nlp operations
		String pathToFiles = "/home/erhan/Desktop/asd/Thesis";
		String posDestinationPath = "/home/erhan/Desktop/TEST-FINAL/posresults"; 
		String sentalDestinationPath = "/home/erhan/Desktop/TEST-FINAL/sentalresults";
		String sentalArticleDestinationPath = "/home/erhan/Desktop/TEST-FINAL/sentalarticles";
		String posModelPath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/stanford-postagger-full-2014-01-04/models/english-left3words-distsim.tagger";
		String wordNetDictinaryPath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/WordNet-3.0/dict";
		String sentiWordNetDictionaryPath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/sentiwordnet/SentiWordNet_3.0.0_20130122.txt";
		boolean fromReuters = true;
		boolean debug = true;
		
		//for r connection
		String posScorePath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/pos.txt";
		String negScorePath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/neg.txt";
		String objScorePath = "/home/erhan/Desktop/TEST-FINAL/dictionaries/obj.txt";
		
		
		long start = System.currentTimeMillis();
		
		
		
		
		
		
		
		REngineConnector rEngine = REngineConnector.getInstance();
		rEngine.createConnection(args);
		rEngine.setEnvForSental(posScorePath, negScorePath, objScorePath);
		
		
		
		NLPOperations nlpOperations = new NLPOperations(8, true);
		check = nlpOperations.performSentenceBasedDeviationSentimentalAnalysis(pathToFiles, 
															 		 posDestinationPath, 
															 		 sentalDestinationPath, 
															 		 sentalArticleDestinationPath,
															 		 posModelPath, 
															 		 wordNetDictinaryPath, 
															 		 sentiWordNetDictionaryPath,
															 		 rEngine,
															 		 fromReuters, 
															 		 debug);	
		/*
		
		NLPOperations nlpOperations = new NLPOperations(8, true);
		check = nlpOperations.performNaiveBayesSentimentalAnalysis(pathToFiles, 
															 		 posDestinationPath, 
															 		 sentalDestinationPath, 
															 		 sentalArticleDestinationPath,
															 		 posModelPath, 
															 		 wordNetDictinaryPath, 
															 		 sentiWordNetDictionaryPath,
															 		 fromReuters, 
															 		 debug);	
		
		
		*/
		
		
		if(check)
		{
			System.err.println("\n\nPROCESS COMPLETED WITHOUT ERRORS");
		}
		long end = System.currentTimeMillis();

		NumberFormat formatter = new DecimalFormat("#0.00000");
		System.out.print("\n\nExecution time is " + formatter.format((end - start) / 1000d) + " seconds");

		
		rEngine.closeConnection();
	}

}
