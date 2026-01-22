/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2026-01-05 13:48:19.

export interface AggregatedStats {
    highestTemp: Day;
    lowestTemp: Day;
    mostRain: Day;
    mostWind: Day;
    avgTemp: number;
    totalRain: number;
}

export interface DailyStats {
    date: Date;
    avg: TypeStats;
    min: TypeStats;
    max: TypeStats;
    stdv: TypeStats;
}

export interface ExtendedMeasurement extends Measurements {
    heatIndex: number;
}

export interface Location {
}

export interface MeasurementPojo {
    ambientTemp: number;
    groundTemp: number;
    pressure: number;
    humidity: number;
    windAverage: number;
    windSpeed: number;
    windGust: number;
    rainfall: number;
    piTemp: number;
    lux: number;
    loftTemp: number;
    loftHumidity: number;
    created: string;
}

export interface MetNoForecast {
}

export interface OpenWeathermapForecast {
}

export interface RainfallDeleteRequest {
    start: string;
    end: string;
    uuid: string;
}

export interface Aggregated extends Serializable {
    minAmbientTemp: number;
    minGroundTemp: number;
    minPiTemp: number;
    minLux: number;
    minLoftHumidity: number;
    minLoftTemp: number;
    minPressure: number;
    minHumidity: number;
    minWindAverage: number;
    minWindSpeed: number;
    minWindGust: number;
    maxAmbientTemp: number;
    maxGroundTemp: number;
    maxPiTemp: number;
    maxLux: number;
    maxLoftHumidity: number;
    maxLoftTemp: number;
    maxPressure: number;
    maxHumidity: number;
    maxWindAverage: number;
    maxWindSpeed: number;
    maxWindGust: number;
    avgAmbientTemp: number;
    avgGroundTemp: number;
    avgPiTemp: number;
    avgLux: number;
    avgLoftHumidity: number;
    avgLoftTemp: number;
    avgPressure: number;
    avgHumidity: number;
    avgWindAverage: number;
    avgWindSpeed: number;
    avgWindGust: number;
    stdAmbientTemp: number;
    stdGroundTemp: number;
    stdPiTemp: number;
    stdLux: number;
    stdLoftHumidity: number;
    stdLoftTemp: number;
    stdPressure: number;
    stdHumidity: number;
    stdWindAverage: number;
    stdWindSpeed: number;
    stdWindGust: number;
    sumRainfall: number;
    date: Date;
}

export interface AggregatedYearMonth extends Serializable {
    avgAmbientTemp: number;
    avgGroundTemp: number;
    avgLux: number;
    avgPressure: number;
    avgHumidity: number;
    avgWindSpeed: number;
    avgWindGust: number;
    sumRainfall: number;
    year: number;
    month: number;
}

export interface Measurements extends Serializable {
    id: number;
    ambientTemp: number;
    groundTemp: number;
    pressure: number;
    humidity: number;
    windAverage: number;
    windSpeed: number;
    windGust: number;
    rainfall: number;
    piTemp: number;
    loftHumidity: number;
    loftTemp: number;
    lux: number;
    uploadedWu: boolean;
    created: Date;
}

export interface SchemaVersion extends Serializable {
    installedRank: number;
    version: string;
    description: string;
    type: string;
    script: string;
    checksum: number;
    installedBy: string;
    installedOn: Date;
    executionTime: number;
    success: boolean;
}

export interface Day {
    date: Date;
    value: number;
}

export interface TypeStats {
    ambientTemp: number;
    groundTemp: number;
    loftTemp: number;
    piTemp: number;
    pressure: number;
    humidity: number;
    loftHumidity: number;
    windAverage: number;
    windSpeed: number;
    windGust: number;
    rainfall: number;
    lux: number;
}

export interface Serializable {
}
