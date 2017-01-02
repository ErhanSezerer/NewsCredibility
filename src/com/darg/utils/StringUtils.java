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
package com.darg.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;




/**Contains the necessary methods for some string manipulation operations
 * 
 * 
 * @author erhan
 *
 */
public final class StringUtils 
{

	//constructor - private to prevent users from calling it
	private StringUtils()
	{
		
	}
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//necessary functions for turning a date to a string and vice versa								  //
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/**transforms the date into a string using a desired date format
	 * 
	 * @author erhan
	 *
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String dateToString(final Date date, final DateFormat dateFormat)
	{
		return dateFormat.format(date);
	}

	
	
	/**transforms the string into a date using a desired date format
	 * @author erhan
	 *
	 * @param date
	 * @param dateFormat
	 * @return Date - null if there is an error
	 */
	public static Date stringToDate(final String date, final DateFormat dateFormat)
	{
		Date retVal = null;

		try 
		{
			retVal = dateFormat.parse(date);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}


		return retVal;
	}


}
