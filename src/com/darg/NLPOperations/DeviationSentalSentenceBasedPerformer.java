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

import com.darg.utils.Statistics;

import com.darg.NLPOperations.pos.model.TaggedSentence;
import com.darg.NLPOperations.pos.util.POSTagTokenizer;
import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScore;
import com.darg.NLPOperations.utils.DeviationSentalParam;
import com.darg.fileOperations.utils.FileUtils;

public class DeviationSentalSentenceBasedPerformer implements Runnable
{
	private DeviationSentalParam parameters;


	
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			constructors 
	//------------------------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private DeviationSentalSentenceBasedPerformer()
	{
		
	}
	public DeviationSentalSentenceBasedPerformer(final DeviationSentalParam parameters) 
	{
		this.parameters = parameters;
	}

	
	
	
	
	
	
	/*------------------------------------------------------------------------------------------------------------------------------
	 *			Thread Work
	 * performs average score sentimental analysis with the given parameters and store the output to results variable.
	 * 
	 * 
	 * WARNING: can only work with uncompressed files, if your file retriever runs through the compressed files do not use it
	 * 
	 * @author erhan sezerer
	/------------------------------------------------------------------------------------------------------------------------------*/
	@Override
	public void run()
	{
		int subFolder = 0;
		File tempFile;
		ArrayList<Double> probObj = new ArrayList<Double>();
		ArrayList<Double> probNeg = new ArrayList<Double>();
		ArrayList<Double> probPos = new ArrayList<Double>();
		
		
		try
		{
			System.out.println("processing file " + parameters.getDocID());
			
			
			File file = parameters.getFile();
			ArrayList<TaggedSentence> wordList = POSTagTokenizer.tokenizeTaggedTextIntoSentences(file.getCanonicalPath(), "_");
			
			if(wordList != null && !wordList.isEmpty())//if the article is not empty and there is no error parsing it
			{
				ArrayList<SentimentScore> scores = parameters.getAnalyzer().sentenceBasedAnalysis(wordList);
				
				if(scores != null)//if we were able to get the score without errors
				{
					subFolder = parameters.getDocID()/1000;
					tempFile = new File(parameters.getOutputFolder() + File.separator + subFolder, parameters.getFile().getName().replaceAll("_POS", "_SCORE"));
					
					//construct the output strings
					String tempString = "";
					String tempArticle = "";
					tempString = tempString.concat("sentence#\tPROBobj\tPROBpos\tneg\n\n");
					for (int i=0; i<scores.size(); i++)
					{
						tempString = tempString.concat(i + "\t" + scores.get(i).getObjectiveProb() + "\t" + scores.get(i).getPositiveProb() + "\t" + scores.get(i).getNegativeProb() + "\n");
						tempArticle = tempArticle.concat(scores.get(i).getArticle() + "\n");
						
						parameters.getStorage().updateDataNeg(scores.get(i).getNegativeProb());
						parameters.getStorage().updateDataObj(scores.get(i).getObjectiveProb());
						parameters.getStorage().updateDataPos(scores.get(i).getPositiveProb());
						probNeg.add(scores.get(i).getNegativeProb());
						probPos.add(scores.get(i).getPositiveProb());
						probObj.add(scores.get(i).getObjectiveProb());
					}
					
					tempString = tempString.concat("\n\n\nmean obj: " + Statistics.getMean(probObj) + "\nmedian obj: " + Statistics.getMedian(probObj));
					tempString = tempString.concat("\nmean neg: " + Statistics.getMean(probNeg) + "\nmedian neg: " + Statistics.getMedian(probNeg));
					tempString = tempString.concat("\nmean pos: " + Statistics.getMean(probPos) + "\nmedian pos: " + Statistics.getMedian(probPos));
					
					FileUtils.writeFile(tempFile, tempString);
					tempFile = new File(parameters.getArticleOutputFolder() + File.separator + subFolder, parameters.getFile().getName().replaceAll("_POS", "_SCORE"));
					FileUtils.writeFile(tempFile, tempArticle);
					
					
				}
			}
			
			
			if(parameters.getDocID()%10000 == 0)
			{
				System.out.println("Processing file " + parameters.getDocID() + "...");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	

}
