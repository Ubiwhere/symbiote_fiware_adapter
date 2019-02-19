/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.h2020.symbiote.fiwarePlugin;

import eu.h2020.symbiote.fiwarePlugin.helper.RestUtils;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.AirQualityObserved;
import eu.h2020.symbiote.fiwarePlugin.models.fiware.AirQualityObservedSimplified;
import java.util.*;
import eu.h2020.symbiote.model.cim.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;


/**
 *
 * @author Matteo Pardi <m.pardi@nextworks.it>
 */
public class PlatformSpecificPluginFiware extends PlatformPlugin {
    
    private static final Logger log = LoggerFactory.getLogger(PlatformSpecificPluginFiware.class);
    
    private static final boolean PLUGIN_PLATFORM_FILTERS_FLAG = true;
    private static final boolean PLUGIN_PLATFORM_NOTIFICATIONS_FLAG = true;

    public static final String PLUGIN_PLATFORM_ID = "platform_fiware";
    public static final String PLUGIN_RES_ACCESS_QUEUE = "rap-platform-queue_" + PLUGIN_PLATFORM_ID;

    private static final String PLATFORM_TOKEN = "Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String PLATFORM_URL   = "https://fiware";


    public PlatformSpecificPluginFiware(RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        super(rabbitTemplate, exchange, PLUGIN_PLATFORM_ID, PLUGIN_PLATFORM_FILTERS_FLAG, PLUGIN_PLATFORM_NOTIFICATIONS_FLAG);
    }

    public PlatformSpecificPluginFiware(RabbitTemplate rabbitTemplate, TopicExchange exchange, String platformId, boolean hasFilters, boolean hasNotifications) {
        super(rabbitTemplate, exchange, platformId, hasFilters, hasNotifications);
    }

    @Override
    public List<Observation> readResource(String resourceId) {
        List<Observation> obsList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> params = new HashMap<String, String>();

        headers.add("Authorization", PLATFORM_TOKEN);


        AirQualityObservedSimplified airQualityObservedSimplified = readAssetInfo(resourceId, headers);

        if (airQualityObservedSimplified != null ){

            obsList.add(observationValueFromFiwareData(airQualityObservedSimplified));
            }

        return obsList;
    }
    
    @Override
    public String writeResource(String resourceId, String body) {
        // INSERT HERE: call to the platform with internal resource id
        // setting the actuator value
        return "";
    }
    
    @Override
    public List<Observation> readResourceHistory(String resourceId) {
        RestUtils<AirQualityObserved> rest = new RestUtils<>();
        List<Observation> values = new ArrayList();

        // same as readResource()

        return values;
    }
    
    @Override
    public void subscribeResource(String resourceId) {
        // INSERT HERE: call to the platform to subscribe resource
    }
    
    @Override
    public void unsubscribeResource(String resourceId) {
        // INSERT HERE: call to the platform to unsubscribe resource
    }
    
//    /*
//    *   Some sample code for observations
//    */
//    public Observation observationExampleValue () {
//        String sensorId = "symbIoTeID1";
//        ArrayList<String> ldescr = new ArrayList();
//        ldescr.add("City of Zagreb");
//        WGS84Location loc = new WGS84Location(15.9, 45.8, 145, "Spansko", ldescr);
//        TimeZone zoneUTC = TimeZone.getTimeZone("UTC");
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        dateFormat.setTimeZone(zoneUTC);
//        Date date = new Date();
//        String timestamp = dateFormat.format(date);
//        long ms = date.getTime() - 1000;
//        date.setTime(ms);
//        String samplet = dateFormat.format(date);
//        ArrayList<String> pdescr = new ArrayList();
//        pdescr.add("Air temperature");
//        ArrayList<String> umdescr = new ArrayList();
//        umdescr.add("Temperature in degree Celsius");
//        ObservationValue obsval = new ObservationValue("7", new Property("Temperature", pdescr), new UnitOfMeasurement("C", "degree Celsius", umdescr));
//        ArrayList<ObservationValue> obsList = new ArrayList();
//        obsList.add(obsval);
//        Observation obs = new Observation(sensorId, loc, timestamp, samplet , obsList);
//
//        log.debug("Observation: \n" + obs.toString());
//
//        return obs;
//    }





    private AirQualityObservedSimplified readAssetInfo(String keyValues, HttpHeaders  headers ){
        RestUtils<AirQualityObservedSimplified> rest = new RestUtils<>();

        String url = PLATFORM_URL+"?options=" + keyValues;

        List<AirQualityObservedSimplified> airQualityObservedSimplified = null;

        JSONObject json = rest.get(url, null, headers);
        JSONArray ja = new JSONArray();
        ja.put(json);

        airQualityObservedSimplified = rest.parseJson(ja, AirQualityObservedSimplified.class);

        if (airQualityObservedSimplified != null && airQualityObservedSimplified.size() > 0) {
            return airQualityObservedSimplified.get(0);
        }

        return null;
    }



    private Observation observationValueFromFiwareData (AirQualityObservedSimplified fiwareData) {
        WGS84Location loc = null ;

        loc = new WGS84Location(
                fiwareData.location.coordinates[0],
                fiwareData.location.coordinates[1],
                0,
                fiwareData.location.type,
                null);


        ArrayList<ObservationValue> obsList = new ArrayList();
        ObservationValue obsval = null;

        if (fiwareData.temperature != null){
            obsval = new ObservationValue(String.valueOf(fiwareData.temperature),
                    new Property("temperature", Arrays.asList("temperature")),
                    new UnitOfMeasurement("°C", "degree Celsius", Arrays.asList("degree Celsius")));

            obsList.add(obsval);
        }

        if (fiwareData.temperature != null){
            obsval = new ObservationValue(String.valueOf(fiwareData.temperature),
                    new Property("temperature", Arrays.asList("temperature")),
                    new UnitOfMeasurement("°C", "degree Celsius", Arrays.asList("degree Celsius")));

            obsList.add(obsval);
        }

        if (fiwareData.windSpeed != null){
            obsval = new ObservationValue(String.valueOf(fiwareData.windSpeed),
                    new Property("Wind Speed", Arrays.asList("Wind Speed")),
                    new UnitOfMeasurement("km/h", "km/h", Arrays.asList("km/h")));

            obsList.add(obsval);
        }

        // repeat the above process for all observations

        return new Observation(
                fiwareData.id,
                loc,
                fiwareData.dateObserved,
                new Date().toString()
                , obsList
        );
    }

}
