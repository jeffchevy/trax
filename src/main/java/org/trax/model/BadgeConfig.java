package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("B")
public class BadgeConfig extends AwardConfig
{
}
