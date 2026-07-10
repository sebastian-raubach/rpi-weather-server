package uk.co.raubach.weatherstation.resource;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Day
{
	private Date       date;
	private BigDecimal value;
}