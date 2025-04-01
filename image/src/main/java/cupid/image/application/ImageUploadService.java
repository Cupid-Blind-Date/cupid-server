package cupid.image.application;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.common.exception.ApplicationException;
import cupid.s3.S3Client;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageUploadService {

    private final S3Client s3Client;

    public String uploadImageWithRandomName(BufferedImage image, String extension) {
        byte[] imageByte = bufferedImageToByteArray(image, extension);
        String name = UUID.randomUUID() + "." + extension;
        s3Client.uploadByteUsingStream(imageByte, name);
        return name;
    }

    private byte[] bufferedImageToByteArray(BufferedImage image, String formatName) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, formatName, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Unexpected exception while image to byte array. e: {}, message: {}", e.getClass(),
                    e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
