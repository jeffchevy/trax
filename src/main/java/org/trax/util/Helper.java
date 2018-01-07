package org.trax.util;

import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.model.cub.pu2015.Cub2015RankElectiveConfig;

public class Helper
{
	/**
	 * its not an either 2015 or classic, awards are shared by both
	 * @param award
	 * @return
	 */
	public static boolean isNewCubsAward(Award award)
	{
		boolean isNewCubsAward = ! (award.getAwardConfig() instanceof CubRankConfig 
						||  award.getAwardConfig() instanceof CubRankElectiveConfig
						||  award.getAwardConfig() instanceof PinConfig
						||  award.getAwardConfig() instanceof BeltLoopConfig
						||  award.getAwardConfig() instanceof ActivityBadgeConfig);
		return isNewCubsAward;
	}

	/**
	 * its not an either 2015 or classic, awards are shared by both
	 * @param award
	 * @return
	 */
	public static boolean isClassicCubAward(Award award)
	{
		boolean isClassicCub = !(award.getAwardConfig() instanceof ChildAwardConfig 
						||  award.getAwardConfig() instanceof Cub2015RankConfig 
						||  award.getAwardConfig() instanceof Cub2015RankElectiveConfig);
		return isClassicCub;
	}

	public static boolean isAnyCubAward(AwardConfig awardConfig)
	{
		boolean isCubAward = awardConfig instanceof BeltLoopConfig
				|| awardConfig instanceof PinConfig
				|| awardConfig instanceof CubRankElectiveConfig
				|| awardConfig instanceof ActivityBadgeConfig
				|| awardConfig instanceof CubRankConfig
				|| awardConfig instanceof Cub2015RankConfig
				|| awardConfig instanceof Cub2015RankElectiveConfig
				|| awardConfig instanceof ChildAwardConfig
				|| awardConfig instanceof CubDutyToGodConfig
				|| awardConfig instanceof CubAwardConfig;
		return isCubAward;
	}

}
