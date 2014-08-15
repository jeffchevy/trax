package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.trax.model.BaseUnitType;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("C")//C for Cub scout
public class CubUnitType extends BaseUnitType
{

}
