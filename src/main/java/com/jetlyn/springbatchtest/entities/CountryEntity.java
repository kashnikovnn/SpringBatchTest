package com.jetlyn.springbatchtest.entities;

import lombok.Data;

@Data
public class CountryEntity {
    String code;

    String name;

    String phoneCode;

    @Override
    public String toString() {
        return "CountryEntity{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                '}';
    }
}
