package com.example.user.services.freelance.utils.pdf;


import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;



@Service
public class MilestonePdfGenerator implements IMilestonePdfGenerator{
    public  final String OUTPUT_DIR = "output/";
    String aliceStory = "I am ...";
    String paulStory = "I am Paul ..";

    @Override
    public void createPdf(String fileName, String titleDoc, String [] bodyParagraphs) throws IOException {

        PdfWriter writer = new PdfWriter(this.OUTPUT_DIR+fileName);
        PdfDocument pdf = new PdfDocument(writer);
        try (Document document = new Document(pdf, PageSize.A4, false)) {

            Paragraph title = new Paragraph(titleDoc).
                    setFont(PdfFontFactory.createFont(StandardFonts.COURIER_BOLD));
            document.add(title);

            for (String paragraph : bodyParagraphs) {
                document.add(new Paragraph(paragraph)
                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)));
            }

            document.close();
        }
    }

}
