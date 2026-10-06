package com.bank;

import com.bank.model.Deposit;
import com.bank.model.DepositComparators;
import com.bank.parser.DepositParser;
import com.bank.parser.DomDepositParser;
import com.bank.parser.SaxDepositParser;
import com.bank.parser.StaxDepositParser;
import com.bank.util.XmlValidator;
import com.bank.util.XsltTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Шляхи до файлів ресурсів (відносно кореня проєкту)
        String xmlPath = "src/main/resources/bank.xml";
        String xsdPath = "src/main/resources/bank.xsd";
        String xslPath = "src/main/resources/bank.xsl";
        String outputXmlPath = "src/main/resources/bank_grouped.xml";

        logger.info("--- Starting XML Processing Application ---");

        // 1. Валідація XML за допомогою XSD
        logger.info("Step 1: Validating XML...");
        boolean isValid = XmlValidator.validate(xmlPath, xsdPath);
        if (!isValid) {
            logger.error("XML is not valid against XSD. Exiting...");
            return; // Перериваємо виконання, якщо файл невалідний
        }

        // 2. Парсинг
        logger.info("\nStep 2: Parsing XML...");
        
        // Перевіряємо DOM
        logger.info("=> Executing DOM Parser:");
        DepositParser domParser = new DomDepositParser();
        List<Deposit> domDeposits = domParser.parse(xmlPath);
        domDeposits.forEach(d -> logger.info("Parsed by DOM: {}", d.toString()));

        // Перевіряємо SAX
        logger.info("=> Executing SAX Parser:");
        DepositParser saxParser = new SaxDepositParser();
        List<Deposit> saxDeposits = saxParser.parse(xmlPath);

        // Перевіряємо StAX
        logger.info("=> Executing StAX Parser:");
        DepositParser staxParser = new StaxDepositParser();
        List<Deposit> staxDeposits = staxParser.parse(xmlPath);
        staxDeposits.forEach(d -> logger.info("Parsed by StAX: {}", d.toString()));

        // 3. Сортування (на прикладі колекції, розпарсеної через SAX)
        logger.info("\nStep 3: Sorting Deposits...");
        
        logger.info("--- Sorted by Amount (Descending) ---");
        saxDeposits.sort(DepositComparators.BY_AMOUNT_DESC);
        saxDeposits.forEach(d -> logger.info(d.toString()));

        logger.info("--- Sorted by Profitability (Ascending) ---");
        saxDeposits.sort(DepositComparators.BY_PROFITABILITY);
        saxDeposits.forEach(d -> logger.info(d.toString()));

        // 4. XSLT-Трансформація
        logger.info("\nStep 4: XSLT Transformation...");
        XsltTransformer.transform(xmlPath, xslPath, outputXmlPath);
        
        logger.info("--- Application Finished Successfully ---");
    }
}