package com.jetlyn.springbatchtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class SpringbatchtestApplication {

	public static void main(String[] args){

		SpringApplication.run(SpringbatchtestApplication.class, args);
		//Enter data using BufferReader
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
