package org.trax.form;


public class UnitForm
{
	private String unitTypeName;
	
	public UnitForm(){}
	
	public UnitForm(String unitTypeName2)
	{
		this.unitTypeName = unitTypeName2;
	}
	public String getUnitTypeName()
	{
		return unitTypeName;
	}
	public void setUnitTypeName(String unitTypeName)
	{
		this.unitTypeName = unitTypeName;
	}
}
