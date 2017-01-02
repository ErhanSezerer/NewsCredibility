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
import java.util.Collections;

public class Statistics 
{
	
	/**Returns the mean calculated from the given data
	 * 
	 * @author erhan sezerer
	 *
	 * @param data
	 * @return
	 */
	public static double getMean(final ArrayList<Double> data)
	{
		double mean = 0;
		int size = data.size();
		
		for (int i=0; i<size; i++)
		{
			mean += data.get(i);
		}
		
		return mean/size;
	}
	
	
	
	
	
	/**Returns the median calculated from the given data
	 * 
	 * @author erhan sezerer
	 *
	 * @param data
	 * @return
	 */
	public static double getMedian(final ArrayList<Double> data)
	{
		int size = data.size();
		double median = 0;
		
		Collections.sort(data);
		
		if(size%2 == 0)
		{
			median = (data.get(size/2 - 1) + data.get(size/2)) / 2;
		}
		else
		{
			median = data.get(data.size()/2);
		}
		
		return median;
	}
	
	
	
	
	
	
	
	
	
	/** returns the variance calculated from the given mean and data
	 * 
	 * @author erhan sezerer
	 *
	 * @param data
	 * @return
	 */
	public static double getVariance(final ArrayList<Double> data)
    {
        double temp = 0;
        int size = data.size();
        double mean = getMean(data);
        
        for(double a :data)
        {
            temp += (mean-a)*(mean-a);
        }
        
        return temp/size;
    }

	
	
    /** returns the standard deviation calculated from the given mean and data
     * 
     * @author erhan sezerer
     *
     * @param data
     * @return
     */
    public static double getStdDev(final ArrayList<Double> data)
    {
        return Math.sqrt(getVariance(data));
    }
  
}
