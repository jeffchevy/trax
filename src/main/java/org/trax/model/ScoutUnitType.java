/*
 * Created on Oct 18, 2004
 */
package org.trax.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("B") //B for Boy scout
public class ScoutUnitType extends BaseUnitType
{
}
