package org.trax.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.dao.OrganizationDao;
import org.trax.model.BaseUnitType;
import org.trax.model.Organization;


@Component("conversionService")
public class OrganizationConverter implements Converter<String, Organization> 
{
	@Autowired
	private OrganizationDao organizationDao;
    
    public Organization convert(String id)
    {
    	return organizationDao.findById(Long.parseLong(id), false);
    }
}
