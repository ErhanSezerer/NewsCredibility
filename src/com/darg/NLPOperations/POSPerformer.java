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

import com.darg.NLPOperations.pos.POSTagger;
import com.darg.NLPOperations.utils.PosParam;
import com.darg.fileOperations.utils.FileUtils;



public class POSPerformer implements Runnable
{
	PosParam parameters;

	
	//constructors
	@SuppressWarnings("unused")
	private POSPerformer()
	{
		
	}
	public POSPerformer(final PosParam parameters) 
	{
		this.parameters = parameters;
	}

	
	
	
	
	@Override
	public void run() 
	{
		int subFolder = 0;
		File file;
		
		try
		{
			//get the model
			POSTagger tagger = parameters.getTagger();
			
			//tag the string
			String taggedString = tagger.tagString(parameters.getText());
			
			
			//check the results for error
			if(taggedString == null || taggedString.isEmpty())
			{
				throw new Exception("Cannot tag the text! -> " + parameters.getFileName());
			}

			
			//save the tagged string to a file in the output folder 
			subFolder = parameters.getDocID()/1000;
			file = new File(parameters.getOutputFolder() + File.separator + subFolder, parameters.getFileName() + "_POS");
			FileUtils.writeFile(file, taggedString);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	

}
