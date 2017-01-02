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
package com.darg.RConnector;


import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import com.darg.NLPOperations.sentimentalAnalysis.model.SentimentScoreType;
import com.darg.RConnector.model.RException;
import com.darg.fileOperations.utils.FileUtils;

/**singleton class, since the connection to R cannot be multiplied. Only one connection can exist.
 * 
 * @author erhan sezerer
 *
 */
public class REngineConnector 
{
	private Rengine rengine;
	private static boolean instanceExist = false;
	
	
	
	//constructor
	private REngineConnector() 
	{
	}
	/**returns the singleton object REngineConnector if it is not created before, returns null if it is the second call to the function.
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @return
	 */
	public static REngineConnector getInstance()
	{
		REngineConnector retVal;
		
		if(!instanceExist)
		{
			retVal = new REngineConnector();
		}
		else
		{
			retVal = null;
		}
			
		return retVal;
	}
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////			INITIALIZATION FUNCTIONS      /////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**creates the R connection that belongs to the newly created R. 
	 * 
	 * @author erhan sezerer
	 *
	 * @param args - cmd arguments
	 * @return boolean - false if something went wrong during initialization
	 */
	public boolean createConnection(String args[])
	{
		boolean retVal = true;
		
		
		try
		{
			// just making sure we have the right version of everything
			if (!Rengine.versionCheck()) 
			{
			    throw new RException("** Version mismatch - Java files don't match library version.");
			}
			
		        System.err.println("Creating Rengine (with arguments)");
		        
				// 1) we pass the arguments from the command line
				// 2) we won't use the main loop at first, we'll start it later
				//    (that's the "false" as second argument)
				// 3) the callbacks are implemented by the TextConsole class above
				rengine = new Rengine(args, false, new TextConsole());
				
		        System.err.println("Rengine created, waiting for R");
		        
		        
				// the engine creates R is a new thread, so we should wait until it's ready
		        if (!rengine.waitForR()) {
		        	throw new RException("Cannot load R");
		        }
		}
		catch(RException e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		return retVal;
	}
	
	
	
	
	
	
	
	/** Sets the environment and initializes the graphs needed for average score sentimental analysis, from the list of scores.
	 *  Files should only contain scores separated with "\n"
	 *  
	 *  WARNING: Do not use it without calling createConnection() first
	 * 
	 * @author erhan sezerer
	 *
	 * @param posDataPath - path to a list of positive scores of all words.
	 * @param negDataPath - path to a list of negative scores of all words.
	 * @param objDataPath - path to a list of objective scores of all words.
	 * 
	 * @return boolean - true if the operation is successful
	 */
	public boolean setEnvForSental(final String posDataPath, final String negDataPath, final String objDataPath)
	{
		boolean retVal = true;
		
		try
		{
			//temporary variables
			String tempString;
			String[] rawScores;
			double[] scoreList;
			
			
			//add the necessary library
			rengine.eval("require(zoo)");
			
			
			
			//for objective scores
			/////////////////////////////////////////////////////////////////////////////
			
			//read the scores list
			tempString = FileUtils.readFile(objDataPath);
			
			if(tempString == null)
			{
				throw new Exception("Cannot read the contents of: " + objDataPath);
			}
			
			//parse it to a double array
			rawScores = tempString.split("\n");
			scoreList = new double[rawScores.length];
			
			for (int i=0; i<rawScores.length; i++)
			{
				scoreList[i] = Double.parseDouble(rawScores[i]);
			}
			
			//send it to R
			rengine.assign("X", scoreList);
			rengine.eval("OBJ <- density(X)", true);
			rengine.eval("cdfOBJ <- cumsum(OBJ$y * diff(OBJ$x[1:2]))");
			rengine.eval("cdfOBJ <- cdfOBJ / max(cdfOBJ)");
			
			
			//for positive scores
			/////////////////////////////////////////////////////////////////////////////
			
			//read the scores list
			tempString = FileUtils.readFile(posDataPath);
			
			if(tempString == null)
			{
				throw new Exception("Cannot read the contents of: " + posDataPath);
			}
			
			//parse it to a double array
			rawScores = tempString.split("\n");
			scoreList = new double[rawScores.length];
			
			for (int i=0; i<rawScores.length; i++)
			{
				scoreList[i] = Double.parseDouble(rawScores[i]);
			}
			
			//send it to R
			rengine.assign("X", scoreList);
			rengine.eval("POS <- density(X)", true);
			rengine.eval("cdfPOS <- cumsum(POS$y * diff(POS$x[1:2]))");
			rengine.eval("cdfPOS <- cdfPOS / max(cdfPOS)");
			
			//for negative scores
			/////////////////////////////////////////////////////////////////////////////
			
			//read the scores list
			tempString = FileUtils.readFile(negDataPath);
			
			if(tempString == null)
			{
				throw new Exception("Cannot read the contents of: " + negDataPath);
			}
			
			//parse it to a double array
			rawScores = tempString.split("\n");
			scoreList = new double[rawScores.length];
			
			for (int i=0; i<rawScores.length; i++)
			{
				scoreList[i] = Double.parseDouble(rawScores[i]);
			}
			
			//send it to R
			rengine.assign("X", scoreList);
			rengine.eval("NEG <- density(X)", true);
			rengine.eval("cdfNEG <- cumsum(NEG$y * diff(NEG$x[1:2]))");
			rengine.eval("cdfNEG <- cdfNEG / max(cdfNEG)");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = false;
		}
		
		return retVal;
	}
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////			SCORING FUNCTIONS         /////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**evaluates the score of word. Gives the probability of that word having that score.
	 * 
	 * WARNING: do not use this function without calling setEnvForSental() first.
	 * 
	 * @author erhan sezerer
	 *
	 * @param wordScore
	 * @param SentimentScoreType - type(pos, neg, obj) of score that is searched
	 * 
	 * @return double - score of the word in given type, -1 if there is an error
	 */
	public double evaluateScore(final double wordScore, final SentimentScoreType type)
	{
		double score = -1;
		REXP rexp;
		
		try
		{
			
			//calculate the probability from the appropriate graph(graoh of pos scores, graph of neg scores, etc.)
			switch(type)
			{
				case POS: rengine.eval("avgPosition <- " + (512* (wordScore/1) ));
						  rexp = rengine.eval("cdfPOS[avgPosition]", true);
						  break;
				
				case NEG: rengine.eval("avgPosition <- " + (512* (wordScore/1) ));
				  		  rexp = rengine.eval("cdfNEG[avgPosition]", true);
				  		  break;
					
				case OBJ: rengine.eval("avgPosition <- " + (512* (wordScore/1) ));
				  		  rexp = rengine.eval("cdfOBJ[avgPosition]", true);
				  		  break;
					
					
				default:  throw new Exception("unknown score type\n");
						
			}
			
			//get the area under the graph
			score = rexp.asDouble();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			score = -1;
		}
		
		
		
		return score;
	}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////			OTHER   FUNCTIONS         /////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void closeConnection()
	{
		rengine.end();
	}
}
