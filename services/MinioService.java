package informational_systems.lab1.services;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    private MinioClient minioClient;

    private boolean client = false;

    @Autowired
    private Environment env;

    private String minioUrl;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    public void init() {
        // Получение значений из Environment
        this.minioUrl = env.getProperty("minio.url");
        this.accessKey = env.getProperty("minio.access-key");
        this.secretKey = env.getProperty("minio.secret-key");
        this.bucketName = env.getProperty("minio.bucket-name");

        // Инициализация minioClient с полученными значениями
        this.minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    public MinioService() {
    }

    // Метод для загрузки файла в MinIO
    public void uploadFile(MultipartFile file, String fileName) throws MinioException, IOException {
        if (!(client)) {
            minioUrl = env.getProperty("minio.url");
            accessKey = env.getProperty("minio.access-key");
            secretKey = env.getProperty("minio.secret-key");
            bucketName = env.getProperty("minio.bucket-name");
            this.minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();
            client = true;
        }
        try {
            // Проверка существования бакета
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Загрузка файла
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException | IOException e) {
            throw new IOException("Error uploading file to MinIO", e);
        }
        catch (InvalidKeyException e){
            throw new IOException("This key is not in crenditials", e);
        }
        catch (NoSuchAlgorithmException e){
            throw new IOException("No such algorithm", e);
        }
    }

    public byte[] downloadFile(String fileName) throws MinioException, IOException {
        if (!(client)) {
            minioUrl = env.getProperty("minio.url");
            accessKey = env.getProperty("minio.access-key");
            secretKey = env.getProperty("minio.secret-key");
            bucketName = env.getProperty("minio.bucket-name");
            this.minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();
            client = true;
        }
        try {
            // Получаем файл из MinIO
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            try (InputStream inputStream = minioClient.getObject(getObjectArgs)) {
                return inputStream.readAllBytes();
            }
        } catch (MinioException | IOException e) {
            throw new IOException("Error downloading file from MinIO", e);
        }
        catch (InvalidKeyException e){
            throw new IOException("This key is not in crenditials", e);
        }
        catch (NoSuchAlgorithmException e){
            throw new IOException("No such algorithm", e);
        }
    }
}
