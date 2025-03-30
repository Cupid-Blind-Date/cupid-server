package cupid.s3;

import cupid.image.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"test", "local"})
@Component
public class MockS3Client extends S3Client {

    private final Map<String, byte[]> store = new HashMap<>();

    public MockS3Client() {
        super(null, new S3Property("", "", "", ""));
        log.info("Mock S3 Client use!");
    }

    @Override
    public void uploadByteUsingStream(byte[] fileContent, String fileName) {
        store.put(fileName, fileContent);
        try {
            File outputFile = new File(fileName);
            InputStream imageStream = new ByteArrayInputStream(fileContent);
            BufferedImage image = ImageIO.read(imageStream);
            ImageIO.write(image, ImageUtils.getExtension(fileName), outputFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        store.remove(fileName);
    }
}
