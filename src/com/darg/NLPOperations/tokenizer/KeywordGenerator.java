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
package com.darg.NLPOperations.tokenizer;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import com.darg.NLPOperations.tokenizer.VecTextField;



/**NOTE: needs lucene version 4 libraries to work.
 * 
 * Other versions might work but not guaranteed.
 * 
 * @author erhan sezerer
 *
 */
public class KeywordGenerator 
{
	
	/*arguments*/
	private Version luceneVersion;
	private HashMap<String,Long> keywords;
	
	
	
	/*constructors*/
	@SuppressWarnings("unused")
	private KeywordGenerator()
	{
		
	}
	public KeywordGenerator(final Version luceneVersion)
	{
		this.luceneVersion = luceneVersion;
		keywords = new HashMap<String,Long>();
	}
	
	
	
	
	
	//------------------------------------------------------------------------//
	// this method takes the string which holds the article content and       //
	// returns the frequency of the words in that article content as a HashMap//
	//------------------------------------------------------------------------//
	/**this method takes the string which holds the article content and       //
	// returns the frequency of the words in that article content as a HashMap
	 * 
	 * @author erhan sezerer
	 *
	 * @param article - a file instance to a text file that will be tokenized
	 * 
	 * @return HashMap<String,Long> - keywords and their frequencies, if the operation was unsuccessful
	 * 								   returns null
	 */
	public HashMap<String,Long> extractKeywords(final File article)
	{
		HashMap<String, Long> tempSet = null;
		String articleContent = null;
		Directory ramdir = new RAMDirectory();
		StandardAnalyzer analyzer = new StandardAnalyzer(luceneVersion);
		FileInputStream fis = null;
		
		try 
		{
			fis = new FileInputStream(article);
			
			
			try
			{
				articleContent = IOUtils.toString(fis);
				
				if(articleContent != null)
				{
					buildDocument(articleContent, ramdir, analyzer);
					computeFrequencies(ramdir, analyzer);
				}
				else
				{
					throw new Exception("ERROR: article is empty");
				}
			}
			finally
			{
				tempSet = keywords;
				fis.close();
				ramdir.close();
				analyzer.close();
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			tempSet = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tempSet = null;
		}
		
	    
		return tempSet;
	}
	
	
	
	
	/**this method takes the string which holds the article content and 
	 * returns the frequency of the words in that article content as a HashMap
	 * 
	 * @author erhan sezerer
	 *
	 * @param content - a string that will be tokenized
	 * 
	 * @return HashMap<String,Long> - keywords and their frequencies, if the operation was unsuccessful
	 * 								   returns null
	 */
	public HashMap<String,Long> extractKeywords(final String content)
	{
		HashMap<String, Long> tempSet = null;
		Directory ramdir = new RAMDirectory();
		StandardAnalyzer analyzer = new StandardAnalyzer(luceneVersion);
		
		try 
		{
			
			try
			{
				
				if(content != null)
				{
					buildDocument(content, ramdir, analyzer);
					computeFrequencies(ramdir, analyzer);
				}
				else
				{
					throw new Exception("ERROR: article is empty");
				}
			}
			finally
			{
				tempSet = keywords;
				ramdir.close();
				analyzer.close();
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			tempSet = null;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tempSet = null;
		}
		
	    
		return tempSet;
	}

	
	
	
	


	//------------------------------------------------------------------------//
	// this method creates a document in Lucene to search the words for       //
	//------------------------------------------------------------------------//
	private void buildDocument(final String articleContent, final Directory ramdir, final Analyzer analyzer)
	{
		
		try 
		{
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_42, analyzer);
			IndexWriter writer = new IndexWriter(ramdir, conf);

			Document doc = new Document();
			VecTextField text = new VecTextField("title", articleContent, Store.YES);
			doc.add(text);
			doc.add(new StringField("searchloc", "document", Field.Store.YES));
		

			writer.addDocument(doc);
			writer.commit();
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
	}
	

	
	//------------------------------------------------------------------------//
	// this method computes the frequencies in the document we created        //
	// with writer. It also removes some of the stop words with the utility   //
	//of Lucene															      //
	//------------------------------------------------------------------------//
	private void computeFrequencies(final Directory ramdir, final Analyzer analyzer) 
	{

			try 
			{
				int hitsPerPage = 1;
				int docID = 0;
				
				IndexReader reader = DirectoryReader.open(ramdir);
				IndexSearcher searcher = new IndexSearcher(reader);
				Query q = new QueryParser(Version.LUCENE_42, "searchloc", analyzer).parse("document");

			    // 3. search
			  
			    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			    searcher.search(q, collector);
			    ScoreDoc[] hits = collector.topDocs().scoreDocs;
			    

			    if(hits.length <= 0)
			    {
			    	System.out.println("ERROR: cannot retrieve documents!");
			    }
			    else
			    {
			    	docID = hits[0].doc;
					Terms terms = reader.getTermVector(docID, "title");
					
					TermsEnum termsEnum =  terms.iterator(null);
				    BytesRef text;
				    
				    while((text = termsEnum.next()) != null) 
				    {
				    	long count = termsEnum.totalTermFreq();
				    	//System.out.println("text= " + text.utf8ToString() + "freq= " + count);
				    	keywords.put(text.utf8ToString(), count);
				    }
					
			    }
			    
			    reader.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
		
		
	}



	//------------------------------------------------------------------------//
	// necessary setters and getters and other similar functions	          //
	//------------------------------------------------------------------------//
	public void clearKeywords()
	{
		keywords.clear();
	}
	
	@Override
	public String toString()
	{
		String retVal = "";
		long frequency;
		
		Object[] keyword_freq = keywords.keySet().toArray();
		
		for (int i=0; i<keyword_freq.length; i++)
		{
			String key = keyword_freq[i].toString();
			frequency = keywords.get(key);
			retVal = retVal.concat("\tKeyword: " +key+ "\n\t\tFrequency: " +frequency+ "\n");
		}
		
		return retVal;
	}
	
	




	public Version getVersion() 
	{
		return luceneVersion;
	}

	public void setVersion(final Version luceneVersion) 
	{
		this.luceneVersion = luceneVersion;
	}

	public HashMap<String, Long> getKeywords() 
	{
		return keywords;
	}

	public void setKeywords(final HashMap<String, Long> keywords) 
	{
		this.keywords = keywords;
	}
	
	

}
