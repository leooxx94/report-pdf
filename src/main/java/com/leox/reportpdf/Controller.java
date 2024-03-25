package com.leox.reportpdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.text.Position;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leox.reportpdf.Models.Model;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.image.BufferedImage;

@RestController
public class Controller {
	
	@PostMapping(value = "/creanalisi", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> post(@RequestBody Model m) throws IOException, DocumentException, NoSuchAlgorithmException, WriterException {
        return createPdf(m);
    }

    public ResponseEntity<byte[]> createPdf(Model m) throws IOException, DocumentException, NoSuchAlgorithmException, WriterException{
        String sha = "";
        String campoX = "X";

        // Create output PDF
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream streamDoc = new ByteArrayOutputStream();
        File file = new File("/home/leox/data_pdf_analisi/analisi_edit.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        //Generating QR CODE
        int size = 250;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(m.getQrCode(), BarcodeFormat.QR_CODE, size, size, hints);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Generating a QR code image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        baos.flush();
        byte[] qrImageData = baos.toByteArray();
        baos.close();

        // Load existing PDF
        PdfReader reader = new PdfReader("/home/leox/data_pdf_analisi/modulo_analisi.pdf");
        PdfImportedPage page = writer.getImportedPage(reader, 1);

        // Copy first page of existing PDF into output PDF
        document.newPage();
        cb.addTemplate(page, 0, 0);

        // Insert the QR code image into the PDF
        Image qrImageInPdf = Image.getInstance(qrImageData);
        qrImageInPdf.setAbsolutePosition(60, 707);
        qrImageInPdf.scaleAbsolute(55, 55);
        cb.addImage(qrImageInPdf);

        if(m.getNumeroAnalisi().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getNumeroAnalisi() != "null"){
            positioning(m.getNumeroAnalisi(), 293, 682, cb);
        }

        if(m.getDataAnalisi().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getDataAnalisi() != "null"){
            positioning(m.getDataAnalisi(), 428, 683, cb);
        }

        if(m.getCliente().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getCliente() != "null"){
            positioning(m.getCliente(), 285, 644, cb);
        }

        if(m.getReferenteDife().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getReferenteDife() != "null"){
            positioning(m.getReferenteDife(), 285, 622, cb);
        }

        if(m.getCampionePrelevatoDa().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getCampionePrelevatoDa() != "null"){
            positioning(m.getCampionePrelevatoDa(), 285, 602, cb);
        }

        if(m.getDataPrelievo().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getDataPrelievo() != "null"){
            positioning(m.getDataPrelievo(), 285, 580, cb);
        }

        if(m.getProduttoreRifiuto().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getProduttoreRifiuto() != "null"){

            if(m.getProduttoreRifiuto().length()<40){
                positioning(m.getProduttoreRifiuto(), 285, 561, cb);
            }else if(m.getProduttoreRifiuto().length()>40 && m.getProduttoreRifiuto().length()<150){
                String str = m.getProduttoreRifiuto();
                int num = 150 - m.getProduttoreRifiuto().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, 150);

                positioning(ragSoc1, 285, 561, cb);
                positioning(ragSoc2, 285, 548, cb);
                positioning(ragSoc3, 285, 535, cb);
            }else if(m.getProduttoreRifiuto().length()>150){
                String str = m.getProduttoreRifiuto();
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, m.getProduttoreRifiuto().length());

                positioning(ragSoc1, 285, 561, cb);
                positioning(ragSoc2, 285, 548, cb);
                positioning(ragSoc3, 285, 535, cb);
            }
        }

        if(m.getIndirizzoProduttore().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getIndirizzoProduttore() != "null"){

            if(m.getIndirizzoProduttore().length()<40){
                positioning(m.getIndirizzoProduttore(), 285, 526, cb);
            }else if(m.getIndirizzoProduttore().length()>40 && m.getIndirizzoProduttore().length()<150){
                String str = m.getIndirizzoProduttore();
                int num = 150 - m.getIndirizzoProduttore().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, 150);

                positioning(ragSoc1, 285, 526, cb);
                positioning(ragSoc2, 285, 514, cb);
                positioning(ragSoc3, 285, 502, cb);
            }else if(m.getIndirizzoProduttore().length()>150){
                String str = m.getIndirizzoProduttore();
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, m.getIndirizzoProduttore().length());

                positioning(ragSoc1, 285, 526, cb);
                positioning(ragSoc2, 285, 514, cb);
                positioning(ragSoc3, 285, 502, cb);
            }
        }

        if(m.getIndirizzoProvenienza().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getIndirizzoProvenienza() != "null"){

            if(m.getIndirizzoProvenienza().length()<40){
                positioning(m.getIndirizzoProvenienza(), 285, 495, cb);
            }else if(m.getIndirizzoProvenienza().length()>40 && m.getIndirizzoProvenienza().length()<150){
                String str = m.getIndirizzoProvenienza();
                int num = 150 - m.getIndirizzoProvenienza().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, 150);

                positioning(ragSoc1, 285, 495, cb);
                positioning(ragSoc2, 285, 484, cb);
                positioning(ragSoc3, 285, 473, cb);
            }else if(m.getIndirizzoProvenienza().length()>150){
                String str = m.getIndirizzoProvenienza();
                String ragSoc1 = str.substring(0, 40);
                String ragSoc2 = str.substring(40, 80);
                String ragSoc3 = str.substring(80, m.getIndirizzoProvenienza().length());

                positioning(ragSoc1, 285, 495, cb);
                positioning(ragSoc2, 285, 484, cb);
                positioning(ragSoc3, 285, 473, cb);
            }
        }

        if(m.getCodiceCer().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getCodiceCer() != "null"){
            positioning(m.getCodiceCer(), 285, 457, cb);
        }

        if(m.getDescrizioneCampione().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getDescrizioneCampione() != "null"){

            if(m.getDescrizioneCampione().length()<50){
                positioning(m.getDescrizioneCampione(), 285, 435, cb);
            }else if(m.getDescrizioneCampione().length()>50 && m.getDescrizioneCampione().length()<200){
                String str = m.getDescrizioneCampione();
                int num = 200 - m.getDescrizioneCampione().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 50);
                String ragSoc2 = str.substring(50, 100);
                String ragSoc3 = str.substring(100, 150);
                String ragSoc4 = str.substring(150, 200);

                positioning(ragSoc1, 285, 435, cb);
                positioning(ragSoc2, 285, 425, cb);
                positioning(ragSoc3, 285, 415, cb);
                positioning(ragSoc4, 285, 405, cb);
            }
        }

        if(m.getCostoAnalisi().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getCostoAnalisi() != "null"){
            positioning(m.getCostoAnalisi(), 285, 396, cb);
        }
        
        if(m.getDataRisultati().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getDataRisultati() != "null"){
            positioning(m.getDataRisultati(), 285, 375, cb);
        }

        if(m.getCicloProd().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getCicloProd() != "null"){

            if(m.getCicloProd().length()<45){
                positioning(m.getCicloProd(), 285, 356, cb);
            }else if(m.getCicloProd().length()>45 && m.getCicloProd().length()<150){
                String str = m.getCicloProd();
                int num = 150 - m.getCicloProd().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 45);
                String ragSoc2 = str.substring(45, 90);
                String ragSoc3 = str.substring(90, 150);

                positioning(ragSoc1, 285, 356, cb);
                positioning(ragSoc2, 285, 344, cb);
                positioning(ragSoc3, 285, 332, cb);
            }
        }

        if(m.getAnalisiClass().equalsIgnoreCase("si")){
            positioning(campoX, 406, 291, cb);
        }else if (m.getAnalisiClass().equalsIgnoreCase("no")){
            positioning(campoX, 469, 291, cb);
        }else if (m.getAnalisiClass().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }

        if(m.getAnalisiSmalt().equalsIgnoreCase("si")){
            positioning(campoX, 406, 268, cb);
        }else if (m.getAnalisiSmalt().equalsIgnoreCase("no")){
            positioning(campoX, 469, 268, cb);
        }else if (m.getAnalisiSmalt().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }

        if(m.getClassiPeric().equalsIgnoreCase("si")){
            positioning(campoX, 406, 247, cb);
        }else if (m.getClassiPeric().equalsIgnoreCase("no")){
            positioning(campoX, 469, 247, cb);
        }else if (m.getClassiPeric().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }

        if(m.getFrasiRischio().equalsIgnoreCase("si")){
            positioning(campoX, 406, 225, cb);
        }else if (m.getFrasiRischio().equalsIgnoreCase("no")){
            positioning(campoX, 469, 225, cb);
        }else if (m.getFrasiRischio().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }

        if(m.getFrasiSic().equalsIgnoreCase("si")){
            positioning(campoX, 406, 204, cb);
        }else if (m.getFrasiSic().equalsIgnoreCase("no")){
            positioning(campoX, 469, 204, cb);
        }else if (m.getFrasiSic().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }
        
        if(m.getClassTraspAdr().equalsIgnoreCase("si")){
            positioning(campoX, 406, 182, cb);
        }else if (m.getClassTraspAdr().equalsIgnoreCase("no")){
            positioning(campoX, 469, 182, cb);
        }else if (m.getClassTraspAdr().equalsIgnoreCase("null")){
            positioning("", 0, 0, cb);
        }

        if(m.getRicercheSpec().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getRicercheSpec() != "null"){

            if(m.getRicercheSpec().length()<45){
                positioning(m.getRicercheSpec(), 285, 162, cb);
            }else if(m.getRicercheSpec().length()>45 && m.getRicercheSpec().length()<150){
                String str = m.getRicercheSpec();
                int num = 150 - m.getRicercheSpec().length();
                for(int i = 0; i<num; i++){
                    str += " ";
                }
                String ragSoc1 = str.substring(0, 45);
                String ragSoc2 = str.substring(45, 90);
                String ragSoc3 = str.substring(90, 150);

                positioning(ragSoc1, 285, 162, cb);
                positioning(ragSoc2, 285, 150, cb);
                positioning(ragSoc3, 285, 138, cb);
            }
        }

        if(m.getAllegato1().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getAllegato1() != "null"){
            positioning(m.getAllegato1(), 405, 82, cb);
        }

        if(m.getAllegato2().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getAllegato2() != "null"){
            positioning(m.getAllegato2(), 405, 59, cb);
        }

        if(m.getAllegatoTesto1().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getAllegatoTesto1() != "null"){
            positioning(m.getAllegatoTesto1(), 100, 82, cb);
        }

        if(m.getAllegatoTesto2().equals("null")){
            positioning("", 0, 0, cb);
        }else if(m.getAllegatoTesto2() != "null"){
            positioning(m.getAllegatoTesto2(), 100, 59, cb);
        }

        document.close();

        //header
        HttpHeaders header = new HttpHeaders();
        ContentDisposition contentDisp = ContentDisposition.builder("inline").filename("richiesta_analisi_"+m.getNumeroAnalisi()+".pdf").build();
        header.setContentDisposition(contentDisp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();
        formatter.format(date);
        header.add("Date", date.toString());
        File f = new File("/home/leox/data_pdf_analisi/analisi_edit.pdf");
        FileInputStream fis = new FileInputStream(f);

        //hash
        try (InputStream is = Files.newInputStream(Paths.get("/home/leox/data_pdf_analisi/analisi_edit.pdf"))) {
            sha = org.apache.commons.codec.digest.DigestUtils.sha256Hex(is);
        }
        header.add("Hash", sha);
        byte[] targetArray = new byte[fis.available()];
        fis.read(targetArray);
        fis.close();

        return new ResponseEntity<byte[]>(targetArray, header, HttpStatus.OK);
    }

    private void positioning(String text, int x, int y, PdfContentByte cb) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        
        cb.saveState();
        cb.beginText();
        cb.moveText(x, y);
        cb.setFontAndSize(bf, 11);
        if(text.equals("X")){     
            cb.setFontAndSize(bf, 12);
        }   
        cb.showText(text);
        cb.endText();
        cb.restoreState();
    }

}
