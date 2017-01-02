package com.darg.utils;

import java.util.ArrayList;

public class TempStorage 
{
	private  ArrayList<Double> dataobj = new ArrayList<Double>();
	private  ArrayList<Double> dataneg = new ArrayList<Double>();
	private  ArrayList<Double> datapos = new ArrayList<Double>();
	private  ArrayList<Integer> noobj = new ArrayList<Integer>();
	private  ArrayList<Integer> noneg = new ArrayList<Integer>();
	private  ArrayList<Integer> nopos = new ArrayList<Integer>();
	private  ArrayList<Integer> nosent = new ArrayList<Integer>();
	private ArrayList<String> articleName = new ArrayList<String>();
	
	
	
	public synchronized void updateName(final String ratio)
	{
		articleName.add(ratio);
	}
	public synchronized void updateDataObj(final Double ratio)
	{
		dataobj.add(ratio);
	}
	public synchronized void updateDataNeg(final Double ratio)
	{
		dataneg.add(ratio);
	}
	public synchronized void updateDataPos(final Double ratio)
	{
		datapos.add(ratio);
	}
	public synchronized void updateNoObj(final int ratio)
	{
		noobj.add(ratio);
	}
	public synchronized void updateNoNeg(final int ratio)
	{
		noneg.add(ratio);
	}
	public synchronized void updateNoPos(final int ratio)
	{
		nopos.add(ratio);
	}
	public synchronized void updateNoSent(final int ratio)
	{
		nosent.add(ratio);
	}

	
	
	
	
	public ArrayList<Integer> getNosent() 
	{
		return nosent;
	}
	public ArrayList<String> getArticleName() 
	{
		return articleName;
	}
	public ArrayList<Integer> getNoobj() 
	{
		return noobj;
	}
	public ArrayList<Integer> getNoneg() 
	{
		return noneg;
	}
	public ArrayList<Integer> getNopos() 
	{
		return nopos;
	}
	public ArrayList<Double> getDataobj() 
	{
		return dataobj;
	}
	public ArrayList<Double> getDataneg() 
	{
		return dataneg;
	}
	public ArrayList<Double> getDatapos() 
	{
		return datapos;
	}
	
	

	
	
}
