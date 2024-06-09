package com.example.user.services.freelance.utils.pdf;

import java.io.IOException;

public interface IMilestonePdfGenerator {

    void createPdf(String fileName, String titleDoc, String[] bodyParagraphs) throws IOException;
}
