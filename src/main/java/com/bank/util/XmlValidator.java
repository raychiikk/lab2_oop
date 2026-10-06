package com.bank.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlValidator {
    private static final Logger logger = LogManager.getLogger(XmlValidator.class);

    /**
     * перевіряє XML файл на відповідність XSD схемі.
     *
     * @param xmlPath шлях до файлу XML
     * @param xsdPath шлях до файлу XSD
     * @return true, якщо документ валідний, інакше false
     */
    public static boolean validate(String xmlPath, String xsdPath) {
        try {
            // 1. вказуємо мову схеми (W3C XML Schema)
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            // 2. завантажуємо XSD схему з файлу
            Schema schema = factory.newSchema(new File(xsdPath));
            
            // 3. створюємо валідатор на основі схеми
            Validator validator = schema.newValidator();
            
            // 4. виконуємо валідацію XML
            validator.validate(new StreamSource(new File(xmlPath)));
            
            logger.info("Validation successful: document is valid against the schema.");
            return true;
            
        } catch (SAXException e) {
            // помилка парсингу або невідповідність схемі
            logger.error("Validation error: document is NOT valid. Reason: {}", e.getMessage());
            return false;
        } catch (IOException e) {
            // помилка читання файлів
            logger.error("IO Error during validation: {}", e.getMessage());
            return false;
        }
    }
}