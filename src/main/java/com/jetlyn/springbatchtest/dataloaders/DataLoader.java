package com.jetlyn.springbatchtest.dataloaders;



import com.jetlyn.springbatchtest.entities.CountryEntity;

import java.util.List;

public interface DataLoader {

    List<CountryEntity> getCountryData();

}
