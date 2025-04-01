package cupid.image.domain;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class ImageMetadataExtractor {

    public int extractOrientation(byte[] bytes) throws Exception {
        InputStream metaStream = new ByteArrayInputStream(bytes);
        Metadata metadata = ImageMetadataReader.readMetadata(metaStream);
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        int orientation = 1; // 기본 방향

        if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        }
        return orientation;
    }
}
