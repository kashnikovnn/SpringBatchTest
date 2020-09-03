package com.jetlyn.springbatchtest.dataloaders;

import com.jetlyn.springbatchtest.entities.CountryEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class DataLoaderHttpImpl implements DataLoader {

    final Logger logger = LoggerFactory.getLogger(DataLoaderHttpImpl.class);
    @Value("${countrynames.url}")
    String countryNamesUrl;
    @Value("${phonecodes.url}")
    String phonecodesUrl;
    private final HttpClient httpClient = new HttpClient();

    @Override
    public List<CountryEntity> getCountryData() {

        List<CountryEntity> countryEntityList = new ArrayList<>();
        JSONObject countrynamesJson;
        JSONObject phonecodesJson;
        try {
            countrynamesJson = new JSONObject(httpClient.getJson(countryNamesUrl));
            phonecodesJson = new JSONObject(httpClient.getJson(phonecodesUrl));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Iterator<String> keysIterator = countrynamesJson.keys();

        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            CountryEntity countryEntity = new CountryEntity();
            countryEntity.setCode(key);
            countryEntity.setName(countrynamesJson.getString(key));
            countryEntity.setPhoneCode(phonecodesJson.getString(key));
            countryEntityList.add(countryEntity);
        }

        logger.debug("Loaded list: " + countryEntityList);
        return countryEntityList;
    }
}
