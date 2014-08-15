package org.trax.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.model.Leader;
import org.trax.model.Leader.CubLeaderPosition;


@Component("conversionService")
public class CubLeaderPositionConverter implements Converter<String, Leader.CubLeaderPosition> 
{
    public Leader.CubLeaderPosition convert(String value)
    {
        Leader.CubLeaderPosition type = null;
        System.out.println("in CubLeaderPosition conversion:"+ value);
        CubLeaderPosition[] positions = Leader.CubLeaderPosition.values();
        for (CubLeaderPosition CubLeaderPosition : positions)
        {
            if (CubLeaderPosition.getPositionName().equalsIgnoreCase(value))
            {
                type = CubLeaderPosition;
                break;
            }
        }
        return type;
    }
}
