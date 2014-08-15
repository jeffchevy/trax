package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.Requirement;


@Repository("requirementDao")
public class RequirementJpaDao extends GenericJpaDao<Requirement, Long> implements RequirementDao
{

}
