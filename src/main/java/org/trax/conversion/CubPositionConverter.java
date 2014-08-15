package org.trax.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trax.model.Scout;
import org.trax.model.Scout.CubPosition;


@Component("conversionService")
public class CubPositionConverter implements Converter<String, Scout.CubPosition> 
{
    public Scout.CubPosition convert(String value)
    {
        Scout.CubPosition type = null;
        System.out.println("in CubPosition conversion:"+ value);
        CubPosition[] positions = Scout.CubPosition.values();
        for (CubPosition CubPosition : positions)
        {
            if (CubPosition.getPositionName().equalsIgnoreCase(value))
            {
                type = CubPosition;
                break;
            }
        }
        return type;
    }
}
