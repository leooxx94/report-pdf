package com.leox.reportpdf;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itextpdf.text.DocumentException;

@SpringBootApplication
public class ReportpdfApplication {

	public static void main(String[] args) throws IOException, DocumentException {
		SpringApplication.run(ReportpdfApplication.class, args);
	}

}
