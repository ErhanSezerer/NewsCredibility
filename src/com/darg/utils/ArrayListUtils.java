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

import java.util.ArrayList;





/**Contains necessary methods for Arraylist operations
 * 
 * @author erhan sezerer
 *
 */
public final class ArrayListUtils 
{
	
	
	//constructor - private to prevent users from calling
	private ArrayListUtils()
	{
		
	}
	
	
	
	
	/**searches the given array list for the string. Ignores the case inequalities
	 * 
	 * @author erhan sezerer
	 *
	 * @param list - an instance to an array list to search for
	 * @param elem - an element to search for
 	 *
	 * @return boolean - true if the element is found in the array list
	 */
	public static boolean containsIgnoresCase(final ArrayList<String> list, final String elem)
	{
		boolean retVal = false;
		int size;
		
		if(list == null || elem == null)
		{
			retVal = false;
		}
		else
		{
			size = list.size();
			
			for (int i=0; i<size; i++)
			{	
				if(list.get(i).equalsIgnoreCase(elem))
				{
					retVal = true;
					break;
				}
				
			}
		}
		
		
		return retVal;
	}
}
