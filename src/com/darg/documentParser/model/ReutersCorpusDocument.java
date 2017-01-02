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
package com.darg.documentParser.model;


import java.util.LinkedList;


public class ReutersCorpusDocument extends ArticleAbstract {
	private String language;
	private String title;
	private String byline;
	private String dateline;
	private LinkedList<String> regions;
	private LinkedList<String> topics;
	private LinkedList<String> industries;
	
	
	/** Empty Constructor
	 * 
	 */
	public ReutersCorpusDocument() {
		this.language = null;
		this.title = null;
		this.headline = null;
		this.byline = null;
		this.dateline = null;
		this.regions = new LinkedList<>();
		this.topics = new LinkedList<>();
		this.industries = new LinkedList<>();
	}


	/** Full Constructor
	 * 	
	 * @param itemId
	 * @param date
	 * @param language
	 * @param title
	 * @param headline
	 * @param byline
	 * @param dateline
	 * @param content
	 */
	public ReutersCorpusDocument(int itemId, String date, String language, String title,
			String headline, String byline, String dateline, String content) {
		
		this.language = language;
		this.title = title;
		this.byline = byline;
		this.dateline = dateline;
	}
	
	/**This method adds a region tag as String to list.
	 * 
	 * @param region
	 * @return boolean
	 */
	public boolean addRegion(String region)
	{
		boolean result = false;
		try{
			this.regions.add(region);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	/**This method appends a region tag list to already existing list.
	 * 
	 * @param regions
	 * @return boolean
	 */
	public boolean addRegions(LinkedList<String> regions)
	{
		boolean result = false;
		try{
			this.regions.addAll(regions);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	/**This method adds a topic tag as String to list.
	 * 
	 * @param topic
	 * @return boolean
	 */
	public boolean addTopic(String topic)
	{
		boolean result = false;
		try{
			this.topics.add(topic);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	/**This method appends a topic tag list to already existing list.
	 * 
	 * @param topics
	 * @return boolean
	 */
	public boolean addTopics(LinkedList<String> topics)
	{
		boolean result = false;
		try{
			this.topics.addAll(topics);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	/**This method adds a industry tag as String to list.
	 * 
	 * @param industry
	 * @return boolean
	 */
	public boolean addIndustry(String industry)
	{
		boolean result = false;
		try{
			this.industries.add(industry);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	/**This method appends a industry tag list to already existing list.
	 * 
	 * @param industries
	 * @return boolean
	 */
	public boolean addIndustries(LinkedList<String> industries)
	{
		boolean result = false;
		try{
			this.industries.addAll(industries);
			result = true;
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return result;
	}
	
	//Getters & Setters
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getByline() {
		return byline;
	}
	public void setByline(String byline) {
		this.byline = byline;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}


	public LinkedList<String> getRegions() {
		return regions;
	}


	public void setRegions(LinkedList<String> regions) {
		this.regions = regions;
	}


	public LinkedList<String> getTopics() {
		return topics;
	}


	public void setTopics(LinkedList<String> topics) {
		this.topics = topics;
	}


	public LinkedList<String> getIndustries() {
		return industries;
	}


	public void setIndustries(LinkedList<String> industries) {
		this.industries = industries;
	}
	
	/**
	 * This method returns string representation of article.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		appendProperty(sb, "body", body);
		appendProperty(sb, "byline", byline);
		appendProperty(sb, "dateline", dateline);
		appendProperty(sb, "guid", guid);
		appendProperty(sb, "headline", headline);
		appendProperty(sb, "industries", industries);
		appendProperty(sb, "language", language);
		appendProperty(sb, "publicationDate", publicationDate);
		appendProperty(sb, "regions", regions);
		appendProperty(sb, "title", title);		
		appendProperty(sb, "topics", topics);
		
		return sb.toString();
	}
	
	/**
	 * (c) this method taken from NYTimes parser
	 * 
	 * Append a property to the specified string.
	 * 
	 * @param sb
	 * @param propertyName
	 * @param propertyValue
	 */
	private void appendProperty(StringBuffer sb, String propertyName,
			Object propertyValue) {

		if (propertyValue != null) {
			propertyValue = propertyValue.toString().replaceAll("\\s+", " ")
					.trim();
		}
		sb.append(ljust(propertyName + ":", 45) + propertyValue + "\n");
	}
	
	/**
	 * (c) this method taken from NYTimes parser
	 * 
	 * Left justify a string by forcing it to be the specified length. This is
	 * done by concatonating space characters to the end of the string until the
	 * string is of the specified length. If, however, the string is initially
	 * longer than the specified length then the original string is returned.
	 * 
	 * @param s
	 *            A string.
	 * @param length
	 *            The target length for the string.
	 * @return A left-justified string.
	 */
	private String ljust(String s, Integer length) {
		if (s.length() >= length) {
			return s;
		}
		length -= s.length();
		StringBuffer sb = new StringBuffer();
		for (Integer i = 0; i < length; i++) {
			sb.append(" ");
		}
		return s + sb.toString();
	}
}
