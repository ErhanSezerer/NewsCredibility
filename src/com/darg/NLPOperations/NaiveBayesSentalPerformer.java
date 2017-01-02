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
import com.darg.NLPOperations.utils.NaiveBayesSentalParam;
import com.darg.fileOperations.utils.FileUtils;



public class NaiveBayesSentalPerformer implements Runnable
{
	
	private NaiveBayesSentalParam parameters;
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------
	//			constructors 
	//------------------------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private NaiveBayesSentalPerformer()
	{
		
	}
	public NaiveBayesSentalPerformer(final NaiveBayesSentalParam sentiParam) 
	{
		this.parameters = sentiParam;
	}



	


	/*------------------------------------------------------------------------------------------------------------------------------
	 *			Thread Work
	 * performs average score sentimental analysis with the given parameters and store the output to results variable.
	 * 
	 * 
	 * WARNING: can only work with uncompressed files, if your file retriever runs through the compress files do not use it
	 * 
	 * @author erhan sezerer
	/------------------------------------------------------------------------------------------------------------------------------*/
	@Override
	public void run() 
	{
		int subFolder = 0;
		File tempFile;
		ArrayList<Double> probs = new ArrayList<Double>();
		
		
		try
		{
			File file = parameters.getFile();
			ArrayList<TaggedSentence> wordList = POSTagTokenizer.tokenizeTaggedTextIntoSentences(file.getCanonicalPath(), "_");
			
			if(wordList != null && !wordList.isEmpty())
			{
				ArrayList<SentimentScore> scores = parameters.getAnalyzer().sentenceBasedAnalysis(wordList);
				
				if(scores != null)
				{
					//prepare the output folders
					subFolder = parameters.getDocID()/1000;
					tempFile = new File(parameters.getOutputFolder() + File.separator + subFolder, parameters.getFile().getName().replaceAll("_POS", "_SCORE"));
					
					
					
					
					//construct the output strings
					String tempString = "";
					String tempArticle = "";
					tempString = tempString.concat("sentence#\tobj\tpos\tneg\tratio(pos/neg)\n");
					for (int i=0; i<scores.size(); i++)
					{
						tempString = tempString.concat(i + "\t" + scores.get(i).getObjectiveProb() + "\t" + scores.get(i).getPositiveProb() + "\t" + scores.get(i).getNegativeProb() + "\t" + scores.get(i).getPositiveProb()/scores.get(i).getNegativeProb() +"\n");
						tempArticle = tempArticle.concat(scores.get(i).getArticle() + "\n");
						probs.add(scores.get(i).getPositiveProb()/scores.get(i).getNegativeProb());
					}
					
					
					tempString = tempString.concat("\n\n\nmean: " + Statistics.getMean(probs) + "\nmedian: " + Statistics.getMedian(probs));
					
					
					FileUtils.writeFile(tempFile, tempString);
					tempFile = new File(parameters.getArticleOutputFolder() + File.separator + subFolder, parameters.getFile().getName().replaceAll("_POS", "_SCORE"));
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
