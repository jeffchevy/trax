package org.trax.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.model.Scout;
import org.trax.model.Scout.ScoutPosition;


@Component("conversionService")
public class ScoutPositionConverter implements Converter<String, Scout.ScoutPosition> 
{
    public Scout.ScoutPosition convert(String value)
    {
        Scout.ScoutPosition type = null;
        System.out.println("in scoutPostion conversion:"+ value);
        ScoutPosition[] positions = Scout.ScoutPosition.values();
        for (ScoutPosition scoutPosition : positions)
        {
            if (scoutPosition.getPositionName().equalsIgnoreCase(value))
            {
                type = scoutPosition;
                break;
            }
        }
        return type;
    }
    
}
