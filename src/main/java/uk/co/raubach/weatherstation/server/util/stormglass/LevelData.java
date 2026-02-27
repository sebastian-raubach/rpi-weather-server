package uk.co.raubach.weatherstation.server.util.stormglass;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class LevelData
{
	private Double sg;
	private Date   time;
}
