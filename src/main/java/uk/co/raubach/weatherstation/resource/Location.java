package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(fluent = true)
public class Location
{
	private double latitude;
	private double longitude;
}
