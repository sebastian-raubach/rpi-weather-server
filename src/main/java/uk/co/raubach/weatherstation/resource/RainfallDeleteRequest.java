package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class RainfallDeleteRequest
{
	private String start;
	private String end;
	private String uuid;
}
