package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;
import uk.co.raubach.weatherstation.server.util.stormglass.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class TidalInfo
{
	private List<LevelData>   levels;
	private List<ExtremeData> extremes;
}
