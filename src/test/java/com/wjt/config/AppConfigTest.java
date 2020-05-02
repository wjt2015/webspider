package com.wjt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.*;

@Slf4j
public class AppConfigTest {


    @Test
    public void pdf() throws IOException {
        String fileName = "D:\\projs\\data\\x_program.pdf";

        PDDocument pdDocument = PDDocument.load(new File(fileName));

        log.info("page_count={};", pdDocument.getNumberOfPages());

        //PDPage page = pdDocument.getPage(10);


        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        pdfTextStripper.setStartPage(10);
        pdfTextStripper.setEndPage(20);
        String text = pdfTextStripper.getText(pdDocument);
        log.info("text.size={};text={};", text.getBytes().length, text);

/*        PDPageTree pages = pdDocument.getPages();
        pages.forEach(page -> {
            BufferedReader bufferedReader;
            //new InputStreamReader(page.getContents())
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(page.getContents()));
                String pageContent = getPdfContent(bufferedReader);

                log.info("pageContent={};", pageContent);
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                log.error("parse pdf page error!page={};", page);
            }

        });*/
    }

    private String getPdfContent(BufferedReader reader) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.substring(0);
    }

}