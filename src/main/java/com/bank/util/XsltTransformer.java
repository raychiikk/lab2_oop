package com.bank.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XsltTransformer {
    private static final Logger logger = LogManager.getLogger(XsltTransformer.class);

    /**
     * перетворює XML-документ за допомогою XSLT-скрипта.
     *
     * @param xmlPath шлях до вхідного XML-файлу
     * @param xslPath шлях до файлу трансформації XSL
     * @param outputPath шлях для збереження результату
     */
    public static void transform(String xmlPath, String xslPath, String outputPath) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(xslPath));
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(new File(xmlPath));
            transformer.transform(text, new StreamResult(new File(outputPath)));

            logger.info("XSLT Transformation successful. Saved to: {}", outputPath);
        } catch (TransformerException e) {
            logger.error("Error during XSLT transformation", e);
        }
    }
}