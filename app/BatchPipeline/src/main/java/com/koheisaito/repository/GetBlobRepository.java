package com.koheisaito.repository;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

public class GetBlobRepository {
    Logger logger = Logger.getLogger(GetBlobRepository.class.getName());

    private Properties properties;

    public GetBlobRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    public List<String> getBlob() throws Exception {
        List<String> blobNames = new ArrayList<String>();
        List<String> blobUrls = new ArrayList<String>();
        List<BlobItem> blobs = listAllBlobs();
        blobs.forEach(
                blob -> {
                    try {
                        blobNames.add((blob.getName()));
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                });
        blobNames.forEach(
                blob -> {
                    try {
                        BlobClient blobClient = buildBlobClient(blob);
                        String sas = generateSas(blobClient);
                        blobUrls.add(blobClient.getBlobUrl() + "?" + sas);
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                });
        return blobUrls;
    }

    private List<BlobItem> listAllBlobs() throws Exception {
        List<BlobItem> blobs = new ArrayList<BlobItem>();
        BlobContainerClient blobContainerClient = buildBlobContainerClient();
        blobContainerClient.listBlobs().forEach(blobItem -> {
            blobs.add(blobItem);
        });
        return blobs;
    }

    /*
     * 1日有効な SAS（読み取り・削除）を生成する
     */
    private String generateSas(BlobClient blobClient) {
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        BlobSasPermission permission = new BlobSasPermission().setReadPermission(true).setDeletePermission(true);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission)
                .setStartTime(OffsetDateTime.now());
        return blobClient.generateSas(values);
    }

    private BlobContainerClient buildBlobContainerClient() throws Exception {
        try {
            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .connectionString(properties.getProperty("blob.connectionstring"))
                    .containerName(properties.getProperty("blob.container")).buildClient();
            return blobContainerClient;
        } catch (Exception e) {
            logger.warning("Build blobContainerClient has failed.");
            throw new Exception(e.toString());
        }
    }

    private BlobClient buildBlobClient(String blobName) throws Exception {
        try {
            BlobClient blobClient = new BlobClientBuilder()
                    .connectionString(properties.getProperty("blob.connectionstring"))
                    .containerName(properties.getProperty("blob.container"))
                    .blobName(blobName)
                    .buildClient();

            return blobClient;
        } catch (Exception e) {
            logger.warning("Build blobClient has failed.");
            throw new Exception(e.toString());
        }
    }
}
