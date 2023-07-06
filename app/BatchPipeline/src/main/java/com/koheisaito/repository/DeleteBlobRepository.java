package com.koheisaito.repository;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;

public class DeleteBlobRepository {
    Logger logger = Logger.getLogger(DeleteBlobRepository.class.getName());

    private Properties properties;

    public DeleteBlobRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void deleteBlobs(List<String> urls) {
        urls.forEach(url -> {
            try {
                URL u = new URL(url);
                BlobClient blobClient = buildBlobClient(FilenameUtils.getName(u.getPath()));
                blobClient.delete();
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        });
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
