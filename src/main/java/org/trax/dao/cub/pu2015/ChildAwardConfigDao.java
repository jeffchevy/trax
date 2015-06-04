package org.trax.dao.cub.pu2015;

import java.util.List;

import org.trax.dao.GenericDao;
import org.trax.model.cub.pu2015.ChildAwardConfig;

public interface ChildAwardConfigDao extends GenericDao<ChildAwardConfig, Long>
{
	List<ChildAwardConfig> getchildAwardConfigs(long parentAwardConfigId);
}
