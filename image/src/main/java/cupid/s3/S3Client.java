package cupid.s3;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import cupid.common.exception.ApplicationException;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"stage", "prod"})
@Component
public class S3Client {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Client(AmazonS3 amazonS3, S3Property property) {
        this.amazonS3 = amazonS3;
        this.bucketName = property.bucketName();
    }

    public void uploadByteUsingStream(byte[] fileContent, String fileName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileContent.length);
            log.info("Try to upload file to s3. filename: {}", fileName);
            amazonS3.putObject(bucketName, fileName, new ByteArrayInputStream(fileContent), metadata);
        } catch (Exception e) {
            log.error("Failed to upload file. fileName: {}, errorMessage: {}", fileName, e.getMessage());
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }

    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
            log.info("Successfully deleted file from S3. fileName: {}", fileName);
        } catch (Exception e) {
            log.error("Failed to delete file. fileName: {}, errorMessage: {}", fileName, e.getMessage());
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
