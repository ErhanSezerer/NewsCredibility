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
package com.darg.fileOperations.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.darg.fileOperations.utils.FileUtils;

public class FileUtilsTest 
{
	
	String path = "/home/erhan/Desktop/TEST-FOR-THESIS/data/yhyh.txt";

	@Test
	public void readFileTest() 
	{
		String text = FileUtils.readFile(path);
		assertEquals("asd\n", text);
	}

}
