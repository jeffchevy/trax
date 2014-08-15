package org.trax.dao;

import org.trax.model.BaseUnitType;
import org.trax.model.Organization;

public interface OrganizationDao extends GenericDao<Organization, Long>
{
	public Organization getOrganization(Integer unitNumber, String councilName, BaseUnitType baseUnitType);
	public Organization saveOrg(Organization org);
	//public Unit getUnitByOrgAndType(Organization organization, BaseUnitType typeOfUnit);
}
