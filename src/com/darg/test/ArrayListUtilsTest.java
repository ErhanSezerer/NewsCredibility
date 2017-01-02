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

import org.junit.Test;

import com.darg.utils.ArrayListUtils;

public class ArrayListUtilsTest {

	/**check whether we can successfully find the element we are searching for
	 * 
	 * @author erhan sezerer
	 *
	 */
	@Test
	public void containsIgnoresCaseTest() 
	{
		ArrayList<String> ext = new ArrayList<String>();
		ext.add(".zip");
		ext.add(".tXt");
		ext.add(".RAR");
		ext.add("TGZ");
		
		
		assertEquals(true, ArrayListUtils.containsIgnoresCase(ext, ".ZIP"));
		assertEquals(false, ArrayListUtils.containsIgnoresCase(ext, "ZIP"));
		assertEquals(true, ArrayListUtils.containsIgnoresCase(ext, "tgz"));
		
	}

}
