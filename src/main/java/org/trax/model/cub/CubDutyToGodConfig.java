package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.trax.model.DutyToGodConfig;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("H")
public class CubDutyToGodConfig extends DutyToGodConfig
{
}
