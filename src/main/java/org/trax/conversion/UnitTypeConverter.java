package org.trax.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.dao.BaseUnitTypeDao;
import org.trax.model.BaseUnitType;


@Component("conversionService")
public class UnitTypeConverter implements Converter<String,  BaseUnitType> 
{
	@Autowired
	private BaseUnitTypeDao baseUnitTypeDao;
    
    public BaseUnitType convert(String id)
    {
    	return baseUnitTypeDao.findById(Long.parseLong(id), false);
    }
}
