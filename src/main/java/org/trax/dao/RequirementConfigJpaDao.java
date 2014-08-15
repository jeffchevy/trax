package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.RequirementConfig;


@Repository("requirementConfigDao")
public class RequirementConfigJpaDao extends GenericJpaDao<RequirementConfig, Long> implements RequirementConfigDao
{
}
