package com.jetlyn.springbatchtest.batch.readers;

import com.jetlyn.springbatchtest.dataloaders.DataLoader;
import com.jetlyn.springbatchtest.entities.CountryEntity;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.ListIterator;


public class CountriesHttpItemReader implements ItemReader<CountryEntity> {

    private DataLoader dataLoader;

    private List<CountryEntity> countryEntities;

    private final ListIterator<CountryEntity> countryEntityListIterator;

    public CountriesHttpItemReader(DataLoader dataLoader)  {
        this.dataLoader = dataLoader;

            countryEntities = dataLoader.getCountryData();

        countryEntityListIterator = countryEntities.listIterator();
    }

    @Override
    public CountryEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (countryEntityListIterator.hasNext()){
            return countryEntityListIterator.next();
        }else {
            return null;
        }
    }
}
