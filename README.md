# report-pdf

This application generates a PDF report directly from code without the use of specific tools for report generation such as JasperReports. It utilizes the iText PDF manipulation library, Zxing for QR Code generation, along with Spring Boot libraries. The report represents a form for requesting chemical analyses for a specific waste material

To generate the report, make a POST API call to http://your_ip:port/creanalisi, inserting this JSON into the body:

{
  "qrCode": "ABC123",
  "numeroAnalisi": "AN2024-001",
  "dataAnalisi": "2024-03-25",
  "cliente": "ACME Corporation",
  "referenteDife": "John Doe",
  "campionePrelevatoDa": "Jane Smith",
  "dataPrelievo": "2024-03-24",
  "produttoreRifiuto": "XYZ Company",
  "indirizzoProduttore": "123 Main Street, City",
  "indirizzoProvenienza": "456 Elm Avenue, Town",
  "codiceCer": "ABCD123456",
  "descrizioneCampione": "Sample A",
  "costoAnalisi": "100.00",
  "dataRisultati": "2024-03-28",
  "cicloProd": "Batch 123",
  "analisiClass": "SI",
  "analisiSmalt": "SI",
  "classiPeric": "NO",
  "frasiRischio": "NO",
  "frasiSic": "SI",
  "classTraspAdr": "SI",
  "ricercheSpec": "Analysis XYZ",
  "allegato1": "file1.pdf",
  "allegato2": "file2.pdf",
  "allegatoTesto1": "Additional info 1",
  "allegatoTesto2": "Additional info 2"
}
