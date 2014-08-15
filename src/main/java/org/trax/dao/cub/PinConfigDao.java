package org.trax.dao.cub;

import org.trax.dao.GenericDao;
import org.trax.model.cub.PinConfig;

public interface PinConfigDao extends GenericDao<PinConfig, Long>
{

	PinConfig getByName(String awardName);
}
