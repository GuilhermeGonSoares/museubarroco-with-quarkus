package com.pibic.shared.abstraction;

import java.io.InputStream;

public interface IStorageService {
    String uploadFile(String blobContainer, String blobName, InputStream fileContent);
    void deleteFile(String blobContainer, String url);
}
