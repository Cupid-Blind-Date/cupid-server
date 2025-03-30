package cupid.image.application;

import cupid.image.domain.ImageBlurProcessor;
import cupid.image.domain.ImageMetadataExtractor;
import cupid.image.domain.ImageResizeProcessor;
import cupid.image.domain.ImageRotator;
import cupid.image.domain.ImageUpSampler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageResizeProcessor imageResizeProcessor;
    private final ImageRotator imageRotator;
    private final ImageMetadataExtractor imageMetadataExtractor;
    private final ImageBlurProcessor imageBlurProcessor;
    private final ImageUpSampler imageUpSampler;

    public BufferedImage blur(MultipartFile image) throws Exception {
        // 1. 바이트 배열로 읽기
        byte[] bytes = image.getBytes();

        // 2. EXIF 메타데이터 읽기
        int orientation = imageMetadataExtractor.extractOrientation(bytes);

        // 3. 이미지 로딩
        InputStream imageStream = new ByteArrayInputStream(bytes);
        BufferedImage originalImage = ImageIO.read(imageStream);

        // 4. 리사이징
        originalImage = imageResizeProcessor.resizeImage(originalImage);

        // 5. EXIF orientation 기준 회전 및 반전 보정
        BufferedImage rotatedImage = imageRotator.rotateImage(originalImage, orientation);

        // 6. 평균 필터
        BufferedImage blurredImage = imageBlurProcessor.process(rotatedImage);

        // 7. 업샘플링
        BufferedImage bufferedImage = imageUpSampler.upSampling(blurredImage);
        return bufferedImage;
    }

}
