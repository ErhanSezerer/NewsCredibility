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

import org.junit.Test;

import com.darg.utils.Normalizer;

public class NormalizerTest 
{
	
	

	@Test
	public void test() 
	{
		double list[] = {0.2,  -0.05, -0.15};
 		double listNormalized[] = Normalizer.normalizeSumAbsolute(list , 1);
 		

		assertEquals(listNormalized[0], 0.5, 0.01);
		assertEquals(listNormalized[1], -0.125, 0.0001);
		assertEquals(listNormalized[2], -0.375, 0.0001);
	}

}
