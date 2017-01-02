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



public final class Normalizer 
{
	
	
	/**normalizes the elements in the given list to a total sum, 
	 * where the sum of resulting list will be equal to normValue. 
	 * NormValue can be given 1 or 100 to normalize to percentages.
	 * 
	 * 
	 * NOTE: this function uses absolute values to preserve the sign.
	 *
	 *
	 * exp: calling the function with {0.2, -0.05, -0.15} and 1 result in;
	 * 		{0.5, -0,125, -0.375}
	 * 
	 * 
	 * @author erhan sezerer
	 *
	 * @param list
	 * @param normValue
	 * @return
	 */
	public static double[] normalizeSumAbsolute(final double list[], final double normValue)
	{
		double sum = 0;
		double normConst = 0;
		
		
		for(int i=0; i<list.length; i++)
		{
			sum +=  Math.abs(list[i]);
		}
		
		normConst = normValue/sum;
		
		for(int i=0; i<list.length; i++)
		{
			list[i] *= normConst;
		}
		
		return list;
	}
	
	
	
	
	/**normalizes the elements in the given list to a total sum, 
	 * where the sum of resulting list will be equal to normValue. 
	 * NormValue can be given 1 or 100 to normalize to percentages.
	 * 
	 * 
	 * NOTE: this function does not preserve the sign.
	 *
	 *
	 * exp: calling the function with {0.2, -0.05, -0.15} and 1 result in;
	 * 		{0.5, -0,125, -0.375}
	 * @author erhan sezerer
	 *
	 * @param list
	 * @param normValue
	 * @return
	 */
	public static double[] normalizeSum(final double list[], final double normValue)
	{
		double sum = 0;
		double normConst = 0;
		double min = Double.POSITIVE_INFINITY;
		
		for(int i=0; i<list.length; i++)
		{
			if(list[i] <= min)
			{
				min = list[i];
			}
		}
		
		if(min <= 0)
		{
			double temp = Math.abs(min);
			for(int i=0; i<list.length; i++)
			{
				list[i] += temp;
			}
		}
		
		for(int i=0; i<list.length; i++)
		{
			sum += list[i];
		}
		
		normConst = normValue/sum;
		
		for(int i=0; i<list.length; i++)
		{
			list[i] *= normConst;
		}
		
		return list;
	}

	
	
	
	
	
	
	
}
