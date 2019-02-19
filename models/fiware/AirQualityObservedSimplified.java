package eu.h2020.symbiote.fiwarePlugin.models.fiware;

import eu.h2020.symbiote.fiwarePlugin.models.BaseModel;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.AddressValue;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.aux.Position;

/**
 * Based on
 *      https://fiware-datamodels.readthedocs.io/en/latest/Environment/AirQualityObserved/doc/spec/index.html#key-value-pairs-example
 */
public class AirQualityObservedSimplified extends BaseModel {
    public String id;
    public String type;
    public AddressValue address;
    public String dateObserved;
    public Position location;
    public String source;
    public Long precipitation;
    public Long relativeHumidity;
    public Long temperature;
    public Long windDirection;
    public Long windSpeed;
    public String airQualityLevel;
    public Long reliability;
    public Long CO;
    public Long NO;
    public Long NO2;
    public Long NOx;
    public Long SO2;
    public String CO_Level;
    public String NO_Level;
    public String refPointOfInterest;

}
