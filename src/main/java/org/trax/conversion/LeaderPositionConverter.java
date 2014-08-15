package org.trax.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.model.Leader;
import org.trax.model.Leader.LeaderPosition;


@Component("conversionService")
public class LeaderPositionConverter implements Converter<String, Leader.LeaderPosition> 
{
    public Leader.LeaderPosition convert(String value)
    {
        Leader.LeaderPosition type = null;
        System.out.println("in leaderPosition conversion:"+ value);
        LeaderPosition[] positions = Leader.LeaderPosition.values();
        for (LeaderPosition leaderPosition : positions)
        {
            if (leaderPosition.getPositionName().equalsIgnoreCase(value))
            {
                type = leaderPosition;
                break;
            }
        }
        return type;
    }
    
}
