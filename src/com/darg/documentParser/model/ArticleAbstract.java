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

import java.util.Date;

/**
 * @author mustafa toprak
 *
 */
public abstract class ArticleAbstract {
	//unique id of article
	protected int guid;
	
	//content of article
	protected String body;
	
	//headline of article
	protected String headline;
	
	//publication date of article
	protected Date publicationDate;

	
	//empty constructor
	public ArticleAbstract() {
		this.guid = -1;
		this.body = null;
		this.headline = null;
		this.publicationDate = null;
	}

	//toString method for representation of article as text
	@Override
	public String toString()
	{
		return null;
	}
	
	//Getters & Setters
	public int getGuid() {
		return guid;
	}

	public void setGuid(int guid) {
		this.guid = guid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headLine) {
		this.headline = headLine;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	
}
