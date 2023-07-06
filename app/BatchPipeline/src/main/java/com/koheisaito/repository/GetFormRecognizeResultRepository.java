package com.koheisaito.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.core.credential.AzureKeyCredential;

public class GetFormRecognizeResultRepository {
    Logger logger = Logger.getLogger(GetFormRecognizeResultRepository.class.getName());

    private static final String MODEL_NAME = "prebuilt-document";

    private Properties properties;
    private DocumentAnalysisClient documentAnalysisClient;

    String contents = "";

    public GetFormRecognizeResultRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.documentAnalysisClient = new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(properties.getProperty("azure.formrecognizer.apikey")))
                .endpoint(properties.getProperty("azure.formrecognizer.url"))
                .buildClient();
    }

    public List<String> getFormRecognizeResult(List<String> documentUrls) {
        List<String> documentWords = new ArrayList<String>();
        documentUrls.forEach(documentUrl -> {
            AnalyzeResult analyzeResult = documentAnalysisClient.beginAnalyzeDocumentFromUrl(MODEL_NAME, documentUrl)
                    .getFinalResult();
            analyzeResult.getPages().forEach(documentPage -> {
                documentPage.getWords().forEach(documentWord -> {
                    documentPage.getWords().toString();
                    contents += documentWord.getContent();
                });
                documentWords.add(contents);
                contents = "";
            });
        });
        return documentWords;
    }
}
