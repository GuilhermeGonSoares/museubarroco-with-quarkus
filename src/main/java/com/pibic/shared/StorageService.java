package com.pibic.shared;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.pibic.shared.abstraction.IStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.nio.file.Paths;


@ApplicationScoped
public class StorageService implements IStorageService {


    private final BlobServiceClient blobServiceClient;

    @Inject
    public StorageService(@ConfigProperty(name = "azure.blob-storage.connection") String connectionString) {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Override
    public String uploadFile(String blobContainer, String blobName, InputStream fileContent) {
        var blobClient = blobServiceClient
                .getBlobContainerClient(blobContainer)
                .getBlobClient(blobName);
        var imageExtension = blobName.split("\\.")[1];
        blobClient.upload(fileContent, true);
        blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType("image/"+imageExtension));
        return blobClient.getBlobUrl();
    }

    @Override
    public void deleteFile(String blobContainer, String url) {
        var blobClient = blobServiceClient
                .getBlobContainerClient(blobContainer)
                .getBlobClient(getBlobNameFromUrl(url));
        blobClient.delete();
    }

    private static String getBlobNameFromUrl(String url)
    {
        var urlSegments = url.split("/");
        return urlSegments[urlSegments.length - 1];
    }
}
