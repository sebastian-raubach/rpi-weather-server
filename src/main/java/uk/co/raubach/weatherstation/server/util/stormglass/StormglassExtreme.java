package uk.co.raubach.weatherstation.server.util.stormglass;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class StormglassExtreme
{
	private List<ExtremeData> data;
	private Meta              meta;
}
