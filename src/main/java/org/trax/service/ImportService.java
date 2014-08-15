package org.trax.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.trax.model.AwardConfig;
import org.trax.model.Organization;
import org.trax.model.RequirementConfig;
import org.trax.model.Scout;
import org.trax.model.User;

public interface ImportService
{
	public Organization updateScoutNetData(Organization attribute, CommonsMultipartFile fileData, User user) throws Exception;
	public List<AwardConfig> parseAwards(CommonsMultipartFile fileData) throws Exception;
	public void saveAwardData(Collection<AwardConfig> awardConfigs) throws Exception;
	public Set<RequirementConfig> parseRequirements(CommonsMultipartFile fileData) throws Exception;
	public void saveRequirementConfigs(Collection<RequirementConfig> importRequirements) throws Exception;
	public void updateUserImage(CommonsMultipartFile fileData, Scout scout);
}
