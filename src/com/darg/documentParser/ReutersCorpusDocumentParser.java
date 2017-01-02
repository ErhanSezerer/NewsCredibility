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
package com.darg.documentParser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.darg.documentParser.model.ArticleAbstract;
import com.darg.documentParser.model.ReutersCorpusDocument;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReutersCorpusDocumentParser implements ParserInterface{

	@Override
	public ArticleAbstract parseArticleDocument(File file, boolean validating) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		ReutersCorpusDocument tempArticle = new ReutersCorpusDocument();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			Document dom = db.parse(file);
			
			//get the root element
			Element docEle = dom.getDocumentElement();

			//TODO:check if values are null or empty
			if(docEle.getAttribute("itemid") != null && !docEle.getAttribute("itemid").isEmpty())
				tempArticle.setGuid(Integer.valueOf(docEle.getAttribute("itemid")));
			if(docEle.getAttribute("date") != null && !docEle.getAttribute("date").isEmpty())
			{
				try{
					tempArticle.setPublicationDate(dateFormat.parse(docEle.getAttribute("date")));
				}catch(Exception exc)
				{
					tempArticle.setPublicationDate(null);
				}
			}
				
			if(docEle.getAttribute("xml:lang") != null && !docEle.getAttribute("xml:lang").isEmpty())
				tempArticle.setLanguage(docEle.getAttribute("xml:lang"));
			if(docEle.getElementsByTagName("title").getLength() > 0)
				tempArticle.setTitle(docEle.getElementsByTagName("title").item(0).getTextContent());
			if(docEle.getElementsByTagName("headline").getLength() > 0)
				tempArticle.setHeadline(docEle.getElementsByTagName("headline").item(0).getTextContent());
			//TODO: null pointer exception
			if(docEle.getElementsByTagName("byline").getLength() > 0)
				tempArticle.setByline(docEle.getElementsByTagName("byline").item(0).getTextContent());
			if(docEle.getElementsByTagName("dateline").getLength() > 0)
				tempArticle.setDateline(docEle.getElementsByTagName("dateline").item(0).getTextContent());
			if(docEle.getElementsByTagName("text").getLength() > 0)
				tempArticle.setBody(docEle.getElementsByTagName("text").item(0).getTextContent());
			
			NodeList codes = docEle.getElementsByTagName("codes");
			int numOfCodes = codes.getLength();
			for(int i=0; i<numOfCodes; i++)
			{
				if(codes.item(i).getAttributes().item(0).getTextContent().contains("topics"))
				{
					Element topicElement = (Element)codes.item(i);
					NodeList topicElements = topicElement.getElementsByTagName("code");
					int numOfTopics = topicElements.getLength();
					for(int j=0; j<numOfTopics; j++)
					{
						String topicCode = topicElements.item(j).getAttributes().item(0).getTextContent();
						tempArticle.addTopic(topicCode);
					}
				}else if(codes.item(i).getAttributes().item(0).getTextContent().contains("countries"))
				{
					Element countryElement = (Element)codes.item(i);
					NodeList countryElements = countryElement.getElementsByTagName("code");
					int numOfCountries = countryElements.getLength();
					for(int j=0; j<numOfCountries; j++)
					{
						String countryCode = countryElements.item(j).getAttributes().item(0).getTextContent();
						tempArticle.addRegion(countryCode);
					}
				}else if(codes.item(i).getAttributes().item(0).getTextContent().contains("industries"))
				{
					Element industryElement = (Element)codes.item(i);
					NodeList industryElements = industryElement.getElementsByTagName("code");
					int numOfIndustries = industryElements.getLength();
					for(int j=0; j<numOfIndustries; j++)
					{
						String countryCode = industryElements.item(j).getAttributes().item(0).getTextContent();
						tempArticle.addRegion(countryCode);
					}
				}
			}
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			tempArticle = null;
		}catch(SAXException se) {
			se.printStackTrace();
			tempArticle = null;
		}catch(IOException ioe) {
			ioe.printStackTrace();
			tempArticle = null;
		}
		
		return tempArticle;
		
	}

}
