package uk.co.raubach.weatherstation.server.util.stormglass;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Meta
{
	private Integer dailyQuota;
	private Integer cost;
	private Integer requestCount;
}
