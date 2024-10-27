package com.example.uploading_files;

import com.example.uploading_files.storage.StorageProperties;
import com.example.uploading_files.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/*
 * EnableConfigurationProperties があることによって
 * 引数にわたしたPropertyコンポーネント(@ConfigurationPropertiesを持つ)をBeanに登録する
 * */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadingFilesApplication.class, args);
	}

	// 起動時に保存ディレクトリの再作成を行う
	// CommandLineRunner はアプリケーション起動時にはじめに実行される
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return args -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
