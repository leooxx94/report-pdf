package com.leox.reportpdf.Models;


import lombok.Data;

@Data
public class Model {
    
    private String qrCode;
    private String numeroAnalisi;
    private String dataAnalisi;
    private String cliente;
    private String referenteDife;
    private String campionePrelevatoDa;
    private String dataPrelievo;
    private String produttoreRifiuto;
    private String indirizzoProduttore;
    
    private String indirizzoProvenienza;
    private String codiceCer;

    private String descrizioneCampione;
    private String costoAnalisi;
    private String dataRisultati;
    private String cicloProd;

    private String analisiClass;
    private String analisiSmalt;
    private String classiPeric;
    private String frasiRischio;
    private String frasiSic;
    private String classTraspAdr;

    private String ricercheSpec;

    private String allegato1;

    private String allegato2;

    private String allegatoTesto1;
    private String allegatoTesto2;
}
