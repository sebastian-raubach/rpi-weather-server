package uk.co.raubach.weatherstation.server.util.stormglass;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class ExtremeData
{
	private Double height;
	private Date   time;
	private Type   type;

	public static enum Type
	{
		high,
		low,
	}
}
