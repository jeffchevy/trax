package org.trax.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.trax.model.BaseUnitType;
import org.trax.model.Organization;
import org.trax.model.ScoutUnitType;
import org.trax.model.Unit;
import org.trax.model.cub.CubUnitType;

@Repository("organizationDao")
public class OrganizationJpaDao extends GenericJpaDao<Organization, Long> implements OrganizationDao
{
	public Organization getOrganization(Integer unitNumber, String councilName, BaseUnitType baseUnitType)
	{
		String hql = "select o from Organization o join o.units u " +
				"where u.number = :unitNumber and o.council = :councilName ";
		List<Organization> orgs  = entityManager.createQuery(hql)
					.setParameter("unitNumber", unitNumber)
					.setParameter("councilName", councilName).getResultList();
		if(orgs != null && orgs.size()>0)
		{
			for (Organization organization : orgs)
			{
				for (Unit unit : organization.getUnits())
				{
					if((unit.isCub() && baseUnitType instanceof CubUnitType)
							|| (!unit.isCub() && (baseUnitType instanceof ScoutUnitType)))
					{
						return organization;
					}
				}
			}
		}
		return null;
	}

	/*public Unit getUnitByOrgAndType(Organization organization, BaseUnitType typeOfUnit)
	{
		
		String sql = "select o  from Organization, unitType ut " +
				"where u.organizationId="+organization.getId()+ " and u.unitTypeId = ut.id and ut.name='"+typeOfUnit.getName()+"'";
		Query query = entityManager.createNativeQuery(sql, Unit.class);
		List<Unit> units = query.getResultList();
		 
		if(units != null && units.size()>0)
		{ 
			return units.get(0); //always get the first
		}
		
		/*
String sql = "select u.*  from Unit u, unitType ut " +
				"where u.organizationId="+organization.getId()+ " and u.unitTypeId = ut.id and ut.name='"+typeOfUnit.getName()+"'";
		Query query = entityManager.createNativeQuery(sql, Unit.class);
		List<Unit> units = query.getResultList();
		 
		if(units != null && units.size()>0)
		{ 
			return units.get(0); //always get the first
		}
		
		
		String hql = "from Unit where organization.id=:organizationId and typeOfUnit.name=:unitTypeName";
		 
		List<Unit> units = entityManager.createQuery(hql)
					.setParameter("unitTypeName", typeOfUnit.getName())
					.setParameter("organizationId", organization.getId()).getResultList();
		
		if(units != null && units.size()>0)
		{
			return units.get(0);
		}
		return null;
	}
	*/
		
	/*public List<ScoutUnitType> getUnitTypes()
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return null;
		if (user.getUnit().isCub())
		{
			
		}
		//String kind =  ;
		String hql = "select ut.* from Unittype ut, unit u " +
				"where u.organizationId = :orgId " +
				"and ut.id=u.unittypeid " +
				"and kind=:kind";
		List<UnitType> unitTypes = null;
		try
		{
			unitTypes = entityManager.createQuery(hql)
					.setParameter("orgId", user.getOrganization().getId())
					.setParameter("kind", user.getUnit().getTypeOfUnit() instanceof UnitType?"C":"A").getResultList();
		}
		catch (NoResultException e)
		{
			//this is normal, ignore it
		}
		catch (EmptyResultDataAccessException e) {
			//not an error
		}
		return unitTypes;
		
	}
	*/
	
	//TODO for some reason, the organization does not get set automatically on the unit, so save it here
	public Organization saveOrg(Organization org)
	{
		for (Unit unit : org.getUnits())
		{
			if (unit.getOrganization()==null)
			{
				unit.setOrganization(org);
			}
		}
		return this.save(org);
	}
}
