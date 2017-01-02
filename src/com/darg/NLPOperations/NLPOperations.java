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
package com.darg.NLPOperations;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.darg.utils.Statistics;

import com.darg.NLPOperations.dictionary.SentiWordNetDictionary;
import com.darg.NLPOperations.dictionary.WordNetDictionary;
import com.darg.NLPOperations.dictionary.model.SentiWordNet;
import com.darg.NLPOperations.dictionary.model.WordNet;
import com.darg.NLPOperations.pos.POSTagger;
import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.util.POSTagTokenizer;
import com.darg.NLPOperations.sentimentalAnalysis.AverageScoreSentimentAnalyzer;
import com.darg.NLPOperations.sentimentalAnalysis.DeviationSentimentAnalyzer;
import com.darg.NLPOperations.sentimentalAnalysis.NaiveBayesSentimentAnalyzer;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;
import com.darg.NLPOperations.utils.DeviationSentalParam;
import com.darg.NLPOperations.utils.SentiAnalysisParam;
import com.darg.NLPOperations.utils.PosParam;
import com.darg.RConnector.REngineConnector;
import com.darg.documentParser.NYTCorpusDocumentParser;
import com.darg.documentParser.ReutersCorpusDocumentParser;
import com.darg.documentParser.model.ArticleAbstract;
import com.darg.fileOperations.FileRetriever;
import com.darg.fileOperations.utils.FileSystemUtils;
import com.darg.fileOperations.utils.FileUtils;
import com.darg.utils.TempStorage;



public class NLPOperations 
{
	private ExecutorService executor;
	private boolean debug;
	private int threadCount;
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			constructors 
	//------------------------------------------------------------------------------------------------------------------------------
	public NLPOperations()
	{
		this.threadCount = 8;//default thread count is 8
		setDebug(false);
	}
	public NLPOperations(final int threadCount)
	{
		this.threadCount = threadCount;
		setDebug(false);
	}
	public NLPOperations(final int threadCount, final boolean debug)
	{
		this.threadCount = threadCount;
		this.setDebug(debug);
	}
	
	
	
	
	
	
	
	/*------------------------------------------------------------------------------------------------------------------------------
	*			operations 
	*
	*each method below this comments runs a different experimental setup.
	*
	*------------------------------------------------------------------------------------------------------------------------------*/
	/**performs pos tagging and sentimental analysis to the given files
	 * 
	 * @author erhan sezerer
	 *
	 * @param pathToFiles
	 * @param posDestinationPath
	 * @param sentalDestinationPath
	 * @param posModelPath
	 * @param wordNetDictinaryPath
	 * @param sentiWordNetDictionaryPath
	 * @param fromReuters - true if reuters corpus is used false if NYT is used
	 * @param debug
	 * @return
	 */
	public boolean performAverageScoreSentimentalAnalysis(final String pathToFiles, 
														  final String posDestinationPath, 
														  final String sentalDestinationPath,
														  final String sentalArticleDestinationPath,
														  final String posModelPath,
														  final String wordNetDictinaryPath,
														  final String sentiWordNetDictionaryPath,
														  final boolean fromReuters,
														  final boolean debug)
	{
		int articleCount = 0;
		int posCount = 0;
		boolean retVal = true;
		FileRetriever fileRetriever;
		AveScoreSentalPerformer sentalThread;
		POSPerformer posThread;
		
		PosParam posParameters;
		SentiAnalysisParam sentalParameters;
		
		ArticleAbstract article;
		
		
		
		try
		{
			
			//load all the dictionaries
			if(debug)
			{
				System.err.println("LOADING THE DICTIONARIES...\n");
			}
			POSTagger tagger = new POSTagger(posModelPath);
			WordNetDictionary wordNet = WordNetDictionary.getInstance(wordNetDictinaryPath, WordNet.VERSION_30);
			SentiWordNetDictionary sentiWordNet = SentiWordNetDictionary.getInstance(sentiWordNetDictionaryPath, SentiWordNet.VERSION_30, true);
	
			if(sentiWordNet.loadDictionary() == false || wordNet.loadDictionary() == false)
			{
				throw new Exception("cannot load libraries");
			}
			
			
			
			AverageScoreSentimentAnalyzer analyzer = new AverageScoreSentimentAnalyzer(wordNet, sentiWordNet);
			
			
			
			
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//POS TAGGING
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(pathToFiles, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}
			
			

			
			//go through all the files and tag them
			if(debug)
			{
				System.err.println("STARTING POS TAGGING OPERATION...\n");
				executor = Executors.newFixedThreadPool(threadCount);
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				articleCount++;
				
				//parse the file
				if(file != null)
				{
					if(fromReuters)
					{
						ReutersCorpusDocumentParser parser = new ReutersCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}
					else
					{
						NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}
					
					
					//create the parameters and start the thread
					posParameters = new PosParam(articleCount, article.getHeadline() + "\n" + article.getBody(), file.getName(), posDestinationPath, tagger);
					posThread = new POSPerformer(posParameters);
					executor.execute(posThread);
					
				}
				
			}
			
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//wait for all the threads
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.HOURS);
			
			
			
			
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//SENTIMENTAL ANALYSIS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(posDestinationPath, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}

			
			

			//go through all the files and compute their sentimental score
			if(debug)
			{
				System.err.println("STARTING SENTIMENTAL ANALYSIS OPERATION...");
				executor = Executors.newFixedThreadPool(threadCount);
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				posCount++;
				
				if(file != null)
				{
					sentalParameters = new SentiAnalysisParam(analyzer, file, false, sentalDestinationPath, sentalArticleDestinationPath, posCount);
					sentalThread = new AveScoreSentalPerformer(sentalParameters);
					executor.execute(sentalThread);
				}
				
				
			}
			
			
			
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//CHECK FOR ERRORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.MINUTES);
			
			
			int sentalCount = FileSystemUtils.getFileCount(sentalDestinationPath, true);
			
			if(posCount != articleCount || sentalCount != articleCount)
			{
				throw new Exception("MISTAKE IN OPERATION.. PLEASE CHECK:\narticleCount= " 
						+articleCount+ "\nposCount: " + posCount + "\nsentalCount: " + sentalCount);
			}
			else if(debug)
			{
				System.err.println("\n------------------------------------\n"
						+ "article count: " + articleCount
						+ "\npos results: " + posDestinationPath
						+ "\nsental results: " + sentalDestinationPath
						+ "\nsental articles" + sentalArticleDestinationPath);
			}
			

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		return retVal;
	}

	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**performs pos tagging and sentimental analysis to the given files
	 * 
	 * @author erhan sezerer
	 *
	 * @param pathToFiles
	 * @param posDestinationPath
	 * @param sentalDestinationPath
	 * @param sentalArticleDestinationPath
	 * @param posModelPath
	 * @param wordNetDictinaryPath
	 * @param sentiWordNetDictionaryPath
	 * @param fromReuters
	 * @param debug
	 * @return
	 */
	public boolean performNaiveBayesSentimentalAnalysis(final String pathToFiles, 
			  final String posDestinationPath, 
			  final String sentalDestinationPath,
			  final String sentalArticleDestinationPath,
			  final String posModelPath,
			  final String wordNetDictinaryPath,
			  final String sentiWordNetDictionaryPath,
			  final boolean fromReuters,
			  final boolean debug)
	{
		int articleCount = 0;
		int posCount = 0;
		boolean retVal = true;
		FileRetriever fileRetriever;
		int subFolder = 0;
		File tempFile;
		TempStorage storage = new TempStorage();
		ArrayList<Double> probsposneg = new ArrayList<Double>();
		ArrayList<Double> probsobjpos = new ArrayList<Double>();
		ArrayList<Double> probsobjneg = new ArrayList<Double>();
		


		ArticleAbstract article;



		try
		{

			//load all the dictionaries
			if(debug)
			{
				System.err.println("LOADING THE DICTIONARIES...\n");
			}
			POSTagger tagger = new POSTagger(posModelPath);
			WordNetDictionary wordNet = WordNetDictionary.getInstance(wordNetDictinaryPath, WordNet.VERSION_30);
			SentiWordNetDictionary sentiWordNet = SentiWordNetDictionary.getInstance(sentiWordNetDictionaryPath, SentiWordNet.VERSION_30, true);

			if(sentiWordNet.loadDictionary() == false || wordNet.loadDictionary() == false)
			{
				throw new Exception("cannot load libraries");
			}



			NaiveBayesSentimentAnalyzer analyzer = new NaiveBayesSentimentAnalyzer(wordNet, sentiWordNet);





			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//POS TAGGING
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			ArrayList<String> extensions = new ArrayList<String>();
			extensions.add("xml");
			subFolder = 0;
			
			if(!fileRetriever.retrieveFiles(pathToFiles, extensions, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and tag them
			if(debug)
			{
				System.err.println("STARTING POS TAGGING OPERATION...\n");
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				articleCount++;

				//parse the file
				if(file != null)
				{
					if(fromReuters)
					{
						ReutersCorpusDocumentParser parser = new ReutersCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}
					else
					{
						NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}


					
						
					//tag the string
					String taggedString = tagger.tagString(article.getHeadline() + "\n" + article.getBody());
						
						
					//check the results for error
					if(taggedString == null || taggedString.isEmpty())
					{
						throw new Exception("Cannot tag the text! -> " + file.getName());
					}

						
					//save the tagged string to a file in the output folder 
					subFolder = articleCount/1000;
					file = new File(posDestinationPath + File.separator + subFolder, file.getName() + "_POS");
					FileUtils.writeFile(file, taggedString);
					
					
					if(articleCount%100 == 0)
					{
						System.out.println("Processing file " + articleCount + "...");
					}

				}
				
				

			}






			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//SENTIMENTAL ANALYSIS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			subFolder = 0;
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(posDestinationPath, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and compute their sentimental score
			if(debug)
			{
				System.err.println("STARTING SENTIMENTAL ANALYSIS OPERATION...");
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				posCount++;

				if(file != null)
				{
					ArrayList<TaggedSentence> wordList = POSTagTokenizer.tokenizeTaggedTextIntoSentences(file.getCanonicalPath(), "_");
					
					if(wordList != null && !wordList.isEmpty())
					{
						ArrayList<SentimentScore> scores = analyzer.sentenceBasedAnalysis(wordList);
						
						if(scores != null)
						{
							//prepare the output folders
							subFolder = posCount/1000;
							tempFile = new File(sentalDestinationPath + File.separator + subFolder, file.getName().replaceAll("_POS", "_SCORE"));
							
							
							
							
							//construct the output strings
							String tempString = "";
							String tempArticle = "";
							tempString = tempString.concat("sentence#\tobj\tpos\tneg\tratio(pos/neg)\tratio(obj/pos)\tratio(obj/neg)\n");
							for (int i=0; i<scores.size(); i++)
							{
								tempString = tempString.concat(i + "\t" + scores.get(i).getObjectiveProb() + "\t" + scores.get(i).getPositiveProb() + "\t" + scores.get(i).getNegativeProb());
								tempString = tempString.concat("\t" + scores.get(i).getPositiveProb()/scores.get(i).getNegativeProb() + "\t" + scores.get(i).getObjectiveProb()/scores.get(i).getPositiveProb() + "\t" + scores.get(i).getObjectiveProb()/scores.get(i).getNegativeProb() + "\n");
								tempArticle = tempArticle.concat(scores.get(i).getArticle() + "\n");
								probsposneg.add(scores.get(i).getPositiveProb()/scores.get(i).getNegativeProb());
								probsobjpos.add(scores.get(i).getObjectiveProb()/scores.get(i).getPositiveProb());
								probsobjneg.add(scores.get(i).getObjectiveProb()/scores.get(i).getNegativeProb());
							
							}
							
							
							tempString = tempString.concat("\n\n\nmean(pos/neg): " + Statistics.getMean(probsposneg) + "\nmedian(pos/neg): " + Statistics.getMedian(probsposneg));
							tempString = tempString.concat("\n\n\nmean(obj/pos): " + Statistics.getMean(probsobjpos) + "\nmedian(obj/pos): " + Statistics.getMedian(probsobjpos));
							tempString = tempString.concat("\n\n\nmean(obj/neg): " + Statistics.getMean(probsobjneg) + "\nmedian(obj/neg): " + Statistics.getMedian(probsobjneg));
							
							storage.updateDataPos(Statistics.getMean(probsposneg));
							storage.updateDataObj(Statistics.getMean(probsobjpos));
							storage.updateDataNeg(Statistics.getMean(probsobjneg));
							storage.updateName(file.getName().replaceAll("_POS", ""));
							
							probsposneg.clear();
							probsobjneg.clear();
							probsobjpos.clear();
							
							
							
							
							FileUtils.writeFile(tempFile, tempString);
							tempFile = new File(sentalArticleDestinationPath + File.separator + subFolder, file.getName().replaceAll("_POS", "_SCORE"));
							FileUtils.writeFile(tempFile, tempArticle);
							
							
							
							if(posCount%100 == 0)
							{
								System.out.println("Processing file " + posCount + "...");
							}
						}
					}
				}

				
			}




			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//CHECK FOR ERRORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////


			int sentalCount = FileSystemUtils.getFileCount(sentalDestinationPath, true);
			String temp = "#\tpos/neg\tobj/pos\tobj/neg\tarticlename\n\n";
			int fileCount = 1;
			
			for (int i=0; i<storage.getDataneg().size(); i++)
			{
				temp = temp.concat(i + "\t" + storage.getDatapos().get(i) + "\t" + storage.getDataobj().get(i) + "\t" + storage.getDataneg().get(i) + "\t" + storage.getArticleName().get(i) + "\n");
				if((i+1)%50000 == 0)
				{
					FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);
					temp = "#\tpos/neg\tobj/pos\tobj/neg\tarticlename\n\n";
					fileCount++;
				}
			
			}
			FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);


			
			
			if(posCount != articleCount || sentalCount != articleCount)
			{
				throw new Exception("MISTAKE IN OPERATION.. PLEASE CHECK:\narticleCount= " 
						+articleCount+ "\nposCount: " + posCount + "\nsentalCount: " + sentalCount);
			}
			else if(debug)
			{
				System.err.println("\n------------------------------------\n"
						+ "article count: " + articleCount
						+ "\npos results: " + posDestinationPath
						+ "\nsental results: " + sentalDestinationPath
						+ "\nsental articles" + sentalArticleDestinationPath);
			}



		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}

	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**performs pos tagging and sentimental analysis to the given files
	 * 
	 * @author erhan sezerer
	 *
	 * @param pathToFiles
	 * @param posDestinationPath
	 * @param sentalDestinationPath
	 * @param sentalArticleDestinationPath
	 * @param posModelPath
	 * @param wordNetDictinaryPath
	 * @param sentiWordNetDictionaryPath
	 * @param fromReuters
	 * @param debug
	 * @return
	 */
	public boolean performDocumentBasedDeviationSentimentalAnalysis(final String pathToFiles, 
			  final String posDestinationPath, 
			  final String sentalDestinationPath,
			  final String sentalArticleDestinationPath,
			  final String posModelPath,
			  final String wordNetDictinaryPath,
			  final String sentiWordNetDictionaryPath,
			  final REngineConnector rEngine,
			  final boolean fromReuters,
			  final boolean debug)
	{
		
		int articleCount = 0;
		int posCount = 0;
		boolean retVal = true;
		FileRetriever fileRetriever;
		DeviationSentalPerformer sentalThread;
		POSPerformer posThread;

		TempStorage storage = new TempStorage();

		PosParam posParameters;
		DeviationSentalParam sentalParameters;

		ArticleAbstract article;



		try
		{

			//load all the dictionaries
			if(debug)
			{
				System.err.println("LOADING THE DICTIONARIES...\n");
			}
			POSTagger tagger = new POSTagger(posModelPath);
			WordNetDictionary wordNet = WordNetDictionary.getInstance(wordNetDictinaryPath, WordNet.VERSION_30);
			SentiWordNetDictionary sentiWordNet = SentiWordNetDictionary.getInstance(sentiWordNetDictionaryPath, SentiWordNet.VERSION_30, true);

			if(sentiWordNet.loadDictionary() == false || wordNet.loadDictionary() == false)
			{
				throw new Exception("cannot load libraries");
			}



			DeviationSentimentAnalyzer analyzer = new DeviationSentimentAnalyzer(wordNet, sentiWordNet, rEngine);





			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//POS TAGGING
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(pathToFiles, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and tag them
			if(debug)
			{
				System.err.println("STARTING POS TAGGING OPERATION...\n");
				executor = Executors.newFixedThreadPool(threadCount);
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				articleCount++;

				//parse the file
				if(file != null)
				{
					if(fromReuters)
					{
						ReutersCorpusDocumentParser parser = new ReutersCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}
					else
					{
						NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}


					//create the parameters and start the thread
					posParameters = new PosParam(articleCount, article.getHeadline() + "\n" + article.getBody(), file.getName(), posDestinationPath, tagger);
					posThread = new POSPerformer(posParameters);
					executor.execute(posThread);

				}

			}


			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//wait for all the threads
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.HOURS);





			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//SENTIMENTAL ANALYSIS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(posDestinationPath, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and compute their sentimental score
			if(debug)
			{
				System.err.println("STARTING SENTIMENTAL ANALYSIS OPERATION...");
				executor = Executors.newFixedThreadPool(threadCount);
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				posCount++;

				if(file != null)
				{
					sentalParameters = new DeviationSentalParam(posCount, analyzer, file, sentalDestinationPath, sentalArticleDestinationPath, storage);
					sentalThread = new DeviationSentalPerformer(sentalParameters);
					executor.execute(sentalThread);
				}

				if(debug)
				{
					if(posCount%10000 == 0)
					{
						System.out.println("Processing file " + posCount + "...");
					}
				}
			}




			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//CHECK FOR ERRORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.HOURS);


			int sentalCount = FileSystemUtils.getFileCount(sentalDestinationPath, true);
			int fileCount = 1;
			
			String temp = "#\tobjprob\tposprob\tnegprob\n\n";
			for (int i=0; i<storage.getDataneg().size(); i++)
			{
				temp = temp.concat(i + "\t" + storage.getDataobj().get(i) + "\t" + storage.getDatapos().get(i) + "\t" + storage.getDataneg().get(i) + "\t" + storage.getArticleName().get(i) + "\n");
				if((i+1)%50000 == 0)
				{
					FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);
					temp = "#\tobjprob\tposprob\tnegprob\n\n";
					fileCount++;
				}
			
			}
			FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);
			
			
			
			
			if(posCount != articleCount || sentalCount != articleCount)
			{
				throw new Exception("MISTAKE IN OPERATION.. PLEASE CHECK:\narticleCount= " 
						+articleCount+ "\nposCount: " + posCount + "\nsentalCount: " + sentalCount);
			}
			else if(debug)
			{
				System.err.println("\n------------------------------------\n"
						+ "article count: " + articleCount
						+ "\npos results: " + posDestinationPath
						+ "\nsental results: " + sentalDestinationPath
						+ "\nsental articles" + sentalArticleDestinationPath);
			}

			

		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}

	
	
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean performSentenceBasedDeviationSentimentalAnalysis(final String pathToFiles, 
			  final String posDestinationPath, 
			  final String sentalDestinationPath,
			  final String sentalArticleDestinationPath,
			  final String posModelPath,
			  final String wordNetDictinaryPath,
			  final String sentiWordNetDictionaryPath,
			  final REngineConnector rEngine,
			  final boolean fromReuters,
			  final boolean debug)
	{
		
		int articleCount = 0;
		int posCount = 0;
		boolean retVal = true;
		FileRetriever fileRetriever;
		TempStorage storage = new TempStorage();


		ArticleAbstract article;



		try
		{

			//load all the dictionaries
			if(debug)
			{
				System.err.println("LOADING THE DICTIONARIES...\n");
			}
			POSTagger tagger = new POSTagger(posModelPath);
			WordNetDictionary wordNet = WordNetDictionary.getInstance(wordNetDictinaryPath, WordNet.VERSION_30);
			SentiWordNetDictionary sentiWordNet = SentiWordNetDictionary.getInstance(sentiWordNetDictionaryPath, SentiWordNet.VERSION_30, true);

			if(sentiWordNet.loadDictionary() == false || wordNet.loadDictionary() == false)
			{
				throw new Exception("cannot load libraries");
			}



			DeviationSentimentAnalyzer analyzer = new DeviationSentimentAnalyzer(wordNet, sentiWordNet, rEngine);





			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//POS TAGGING
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//retrieve the files
			fileRetriever = new FileRetriever();
			ArrayList<String> extensions = new ArrayList<String>();
			extensions.add("xml");
			int subFolder = 0;
			
			if(!fileRetriever.retrieveFiles(pathToFiles, extensions, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and tag them
			if(debug)
			{
				System.err.println("STARTING POS TAGGING OPERATION...\n");
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				articleCount++;

				//parse the file
				if(file != null)
				{
					if(fromReuters)
					{
						ReutersCorpusDocumentParser parser = new ReutersCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}
					else
					{
						NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
						article = parser.parseArticleDocument(file, false);
					}


					
					//tag the string
					String taggedString = tagger.tagString(article.getHeadline() + "\n" + article.getBody());
						
						
					//check the results for error
					if(taggedString == null || taggedString.isEmpty())
					{
						throw new Exception("Cannot tag the text! -> " + file.getName());
					}

						
					//save the tagged string to a file in the output folder 
					subFolder = articleCount/1000;
					file = new File(posDestinationPath + File.separator + subFolder, file.getName() + "_POS");
					FileUtils.writeFile(file, taggedString);
					
					if(articleCount%100 == 0)
					{
						System.out.println("Processing file " + articleCount + "...");
					}

				}

			}






			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//SENTIMENTAL ANALYSIS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			subFolder = 0;
			//retrieve the files
			fileRetriever = new FileRetriever();
			if(!fileRetriever.retrieveFiles(posDestinationPath, null, true, true))
			{
				throw new Exception("Error: cannot fetch files!");
			}




			//go through all the files and compute their sentimental score
			if(debug)
			{
				System.err.println("STARTING SENTIMENTAL ANALYSIS OPERATION...");
			}
			while(fileRetriever.hasNext())
			{
				File file = fileRetriever.next();
				posCount++;

				if(file != null)
				{
					File tempFile;
					ArrayList<Double> probObj = new ArrayList<Double>();
					ArrayList<Double> probNeg = new ArrayList<Double>();
					ArrayList<Double> probPos = new ArrayList<Double>();
					int nopos = 0;
					int noobj = 0;
					int noneg = 0;
					
					
					try
					{
						if(posCount%100 == 0)
						{
							System.out.println("processing file " + posCount);
						}
						
						
						ArrayList<TaggedSentence> wordList = POSTagTokenizer.tokenizeTaggedTextIntoSentences(file.getCanonicalPath(), "_");
						
						if(wordList != null && !wordList.isEmpty())//if the article is not empty and there is no error parsing it
						{
							ArrayList<SentimentScore> scores = analyzer.sentenceBasedAnalysis(wordList);
							
							if(scores != null)//if we were able to get the score without errors
							{
								subFolder = posCount/1000;
								tempFile = new File(sentalDestinationPath + File.separator + subFolder, file.getName().replaceAll("_POS", "_SCORE"));
								
								//construct the output strings
								String tempString = "";
								String tempArticle = "";
								tempString = tempString.concat("sentence#\tPROBobj\tPROBpos\tneg\n\n");
								for (int i=0; i<scores.size(); i++)
								{
									tempString = tempString.concat(i + "\t" + scores.get(i).getObjectiveProb() + "\t" + scores.get(i).getPositiveProb() + "\t" + scores.get(i).getNegativeProb() + "\n");
									tempArticle = tempArticle.concat(scores.get(i).getArticle() + "\n");
									
									probNeg.add(scores.get(i).getNegativeProb());
									probPos.add(scores.get(i).getPositiveProb());
									probObj.add(scores.get(i).getObjectiveProb());
									
									if(scores.get(i).getNegativeProb() >= 0.34)
									{
										noneg++;
									}
									if(scores.get(i).getPositiveProb() >= 0.34)
									{
										nopos++;
									}
									if(scores.get(i).getObjectiveProb() >= 0.34)
									{
										noobj++;
									}
								}
								
								tempString = tempString.concat("\n\n\nmean obj: " + Statistics.getMean(probObj) + "\nmedian obj: " + Statistics.getMedian(probObj));
								tempString = tempString.concat("\nmean neg: " + Statistics.getMean(probNeg) + "\nmedian neg: " + Statistics.getMedian(probNeg));
								tempString = tempString.concat("\nmean pos: " + Statistics.getMean(probPos) + "\nmedian pos: " + Statistics.getMedian(probPos));
								tempString = tempString.concat("\nno_of_sentences: " + (scores.size()) + "\nno_of_pos_sent: " +nopos+ "\nno_of_neg_sent: " +noneg+ "\nno_of_obj_sent: " +noobj);
								
								
								storage.updateDataNeg(Statistics.getMean(probNeg));
								storage.updateDataObj(Statistics.getMean(probObj));
								storage.updateDataPos(Statistics.getMean(probPos));
								storage.updateNoNeg(noneg);
								storage.updateNoPos(nopos);
								storage.updateNoObj(noobj);
								storage.updateNoSent(scores.size());
								storage.updateName(file.getName().replaceAll("_POS", ""));
								
								FileUtils.writeFile(tempFile, tempString);
								tempFile = new File(sentalArticleDestinationPath + File.separator + subFolder, file.getName().replaceAll("_POS", "_SCORE"));
								FileUtils.writeFile(tempFile, tempArticle);
								
								
							}
						}
						
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}




			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//CHECK FOR ERRORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			int sentalCount = FileSystemUtils.getFileCount(sentalDestinationPath, true);
			int fileCount = 1;
			
			String temp = "#\tobjprob\tposprob\tnegprob\tarticle\n\n";
			String temp2 = "#sentences\t#objective\t#positive\t#negative\tarticle\n\n";
			for (int i=0; i<storage.getDataneg().size(); i++)
			{
				temp = temp.concat(i + "\t" + storage.getDataobj().get(i) + "\t" + storage.getDatapos().get(i) + "\t" + storage.getDataneg().get(i) + "\t" + storage.getArticleName().get(i) + "\n");
				temp2 = temp2.concat( storage.getNosent().get(i)+ "\t" + storage.getNoobj().get(i) + "\t" + storage.getNopos().get(i) + "\t" +storage.getNoneg().get(i) + "\t" + storage.getArticleName().get(i) + "\n");
				if((i+1)%50000 == 0)
				{
					FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);
					temp = "#\tobjprob\tposprob\tnegprob\n\n";
					FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdatanos"+ fileCount +".txt"), temp2);
					temp2 = "#sentences\t#objective\t#positive\t#negative\n\n";
					fileCount++;
				}
			
			}
			FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdata"+ fileCount +".txt"), temp);
			FileUtils.writeFile(new File("/home/erhan/Desktop/TEST-FINAL/corpusdatanos"+ fileCount +".txt"), temp2);

			
			
			if(posCount != articleCount || sentalCount != articleCount)
			{
				throw new Exception("MISTAKE IN OPERATION.. PLEASE CHECK:\narticleCount= " 
						+articleCount+ "\nposCount: " + posCount + "\nsentalCount: " + sentalCount);
			}
			else if(debug)
			{
				System.err.println("\n------------------------------------\n"
						+ "article count: " + articleCount
						+ "\npos results: " + posDestinationPath
						+ "\nsental results: " + sentalDestinationPath
						+ "\nsental articles" + sentalArticleDestinationPath);
			}



		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}

	
	
	
	
	
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			other functions
	//------------------------------------------------------------------------------------------------------------------------------
	public boolean isDebug() 
	{
		return debug;
	}
	public void setDebug(final boolean debug) 
	{
		this.debug = debug;
	}

}
