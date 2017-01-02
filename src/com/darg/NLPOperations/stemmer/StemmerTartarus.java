/*
 * Written by Mustafa Toprak
 * Contact <mustafatoprak@iyte.edu.tr> for comments and bug reports
 *
 * Copyright (C) 2014 Mustafa Toprak
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
package com.darg.NLPOperations.stemmer;

import java.util.ArrayList;
import java.util.HashMap;

import org.tartarus.snowball.ext.EnglishStemmer;

public class StemmerTartarus 
{
	
	/**This method stems a given term (just one term) as a string by using
	 * Tartarus Stemmer. If the process is successful returns stemmed term,
	 * otherwise returns null pointer.
	 * 
	 * @author mustafa toprak
	 * 
	 * @param term
	 * @return String
	 */
	public String stemWordTartarus(String term) 
	{
		String stemmedTerm = null;
		
		try
		{
			EnglishStemmer stemmer = new EnglishStemmer();
		    stemmer.setCurrent(term);
		    
		    if(stemmer.stem())
		    {
		    	stemmedTerm =  stemmer.getCurrent();
		    }
		}
		catch(Exception exc)
		{
			stemmedTerm = null;
			exc.printStackTrace();
		}
	    return stemmedTerm;
	}
	
	
	
	/**This method stems keywords given as hashmap (keys are keywords and values are frequencies).
	 * If stemmed version of two different keywords are the same, frequencies of these two words
	 * are summed. (Tartarus Stemmer is used for the stemming process). If the process is
	 * successfully finalized, hashmap of stemmed terms are returned, otherwise null pointer
	 * is returned.
	 * 
	 * @author mustafa toprak
	 * 
	 * 
	 * @param keywords
	 * @return HashMap<String, Long>
	 */
	public HashMap<String, Long> stemWordsTartarus(HashMap<String, Long> keywords)
	{
		HashMap<String, Long> stemmedKeywords = new HashMap<>();
		
		try
		{
			if(keywords != null && keywords.size()>0)
			{
				String stemmedKeyword;
				long tempFreq;
				
				for(String keyword : keywords.keySet())
				{
					stemmedKeyword = stemWordTartarus(keyword);
					tempFreq = 0;
					if(stemmedKeywords.containsKey(stemmedKeyword))
					{
						tempFreq = stemmedKeywords.get(stemmedKeyword) + keywords.get(keyword);
						stemmedKeywords.put(stemmedKeyword, tempFreq);
					}
					else
					{
						stemmedKeywords.put(stemmedKeyword, keywords.get(keyword));
					}
				}
				
			}else
			{
				stemmedKeywords = null;
			}
			
		}
		catch(Exception exc)
		{
			stemmedKeywords = null;
			exc.printStackTrace();
		}
		
		return stemmedKeywords;
	}
	
	
	
	/**This method stems keywords given as list.. If the process is
	 * successfully finalized, list of stemmed terms are returned, otherwise 
	 * null pointer is returned. Before stemming process, keyword converted
	 * to lower case.
	 * 
	 * @author mustafa toprak
	 * 
	 * 
	 * @param keywords
	 * @return ArrayList<String>
	 */
	public ArrayList<String> stemWordListTartarus(ArrayList<String> keywords)
	{
		ArrayList<String> stemmedKeywords = new ArrayList<>();
		
		try
		{
			if(keywords != null && keywords.size()>0)
			{
				String stemmedKeyword;
				for(String keyword : keywords)
				{
					stemmedKeyword = stemWordTartarus(keyword);
					if(!stemmedKeywords.contains(stemmedKeyword))
					{
						stemmedKeywords.add(stemmedKeyword);
					}
				}
				
			}
			else
			{
				stemmedKeywords = null;
			}
		}
		catch(Exception exc)
		{
			stemmedKeywords = null;
			exc.printStackTrace();
		}
		
		return stemmedKeywords;
	}
	
}
