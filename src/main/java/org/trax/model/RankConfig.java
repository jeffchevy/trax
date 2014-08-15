package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("R")
public class RankConfig extends AwardConfig
{
}