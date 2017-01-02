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
package com.darg.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.darg.utils.Statistics;

public class StatisticsTests 
{
	public static ArrayList<Double> data = new ArrayList<Double>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		
		data.add(0.5);
		data.add(0.3);
		data.add(0.3);
		data.add(0.2);
		data.add(0.7);
		data.add(0.75);
		data.add(0.8);
		data.add(0.92);
	}

	

	@Test
	public void meanTest() 
	{
		assertEquals(0.55875, Statistics.getMean(data), 0.00001);
	}
	
	
	
	@Test
	public void medianTest() 
	{
		assertEquals(0.60, Statistics.getMedian(data), 0.01);
	}
	
	
	
	@Test
	public void varianceTest() 
	{
		assertEquals(0.06391, Statistics.getVariance(data), 0.0001);
	}
	
	
	
	@Test
	public void deviationTest() 
	{
		assertEquals(0.25280, Statistics.getStdDev(data), 0.0001);
	}

}
