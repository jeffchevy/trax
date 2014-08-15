package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.trax.model.RankConfig;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("C")
public class CubRankConfig extends RankConfig
{
}