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
package com.darg.NLPOperations.utils;

public enum OPType 
{
	POS(10), LIST_STEM(20), HASH_STEM(21), LEMM(30), AVE_SENT_AL(40), POS_SENT_PIPE(41), OTHER(99);
	
	
	public transient final int value;
	
	
	OPType(final int value)
	{
		this.value = value;
	}

}
