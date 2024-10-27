package com.example.uploading_files.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

// propertiesの値をバインドできる (yamlとか)
@ConfigurationProperties("storage")
public class StorageProperties {

    /*
     * Folder location for storaging files
     * */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
