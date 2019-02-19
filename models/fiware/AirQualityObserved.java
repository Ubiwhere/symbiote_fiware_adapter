package eu.h2020.symbiote.fiwarePlugin.models.fiware;

import eu.h2020.symbiote.fiwarePlugin.models.BaseModel;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.StringValue;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.AirQualityLevel;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.Temperature;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.Address;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.Location;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.RefPointOfInterest;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.AirQualityIndex;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.CO;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.CO_Level;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.NO;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.NO2;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.NOx;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.Precipitation;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.RelativeHumidity;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.Reliability;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.SO2;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.WindDirection;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.observations.WindSpeed;
/**
 * Based on
 *      https://fiware-datamodels.readthedocs.io/en/latest/Environment/AirQualityObserved/doc/spec/index.html#normalized-example
 */
public class AirQualityObserved extends BaseModel {
    public String id;
    public String type;
    public StringValue dateObserved;
    public AirQualityLevel airQualityLevel;
    public CO CO;
    public Temperature temperature;
    public NO NO;
    public RefPointOfInterest refPointOfInterest;
    public WindDirection windDirection;
    public StringValue source;
    public WindSpeed windSpeed;
    public SO2 SO2;
    public NOx NOx;
    public Location location;
    public AirQualityIndex airQualityIndex;
    public Address address;
    public Reliability reliability;
    public RelativeHumidity relativeHumidity;
    public Precipitation precipitation;
    public NO2 NO2;
    public CO_Level CO_Level;
}
