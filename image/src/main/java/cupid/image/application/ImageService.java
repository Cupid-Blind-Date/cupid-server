package cupid.image.application;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.common.exception.ApplicationException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageResizeProcessor imageResizeProcessor;
    private final ImageRotator imageRotator;
    private final ImageMetadataExtractor imageMetadataExtractor;
    private final ImageBlurProcessor imageBlurProcessor;
    private final ImageUpSampler imageUpSampler;

    public BufferedImage rotate(byte[] bytes) {
        try {
            // 1. EXIF 메타데이터 읽기
            int orientation = imageMetadataExtractor.extractOrientation(bytes);

            // 2. 이미지 로딩
            InputStream imageStream = new ByteArrayInputStream(bytes);
            BufferedImage originalImage = ImageIO.read(imageStream);

            // 3. EXIF orientation 기준 회전 및 반전 보정
            BufferedImage rotatedImage = imageRotator.rotateImage(originalImage, orientation);
            return rotatedImage;
        } catch (Exception e) {
            log.error("Failed to rotate image. e: {}. message: {}", e.getClass(), e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }

    public BufferedImage blur(byte[] bytes) {
        try {
            // 1. EXIF 메타데이터 읽기
            int orientation = imageMetadataExtractor.extractOrientation(bytes);

            // 2. 이미지 로딩
            InputStream imageStream = new ByteArrayInputStream(bytes);
            BufferedImage originalImage = ImageIO.read(imageStream);

            // 3. 리사이징
            originalImage = imageResizeProcessor.resizeImage(originalImage);

            // 4. EXIF orientation 기준 회전 및 반전 보정 (성능을 위해 사이즈 줄일 후 수행)
            BufferedImage rotatedImage = imageRotator.rotateImage(originalImage, orientation);

            // 5. 평균 필터
            BufferedImage blurredImage = imageBlurProcessor.process(rotatedImage);

            // 6. 업샘플링
            BufferedImage bufferedImage = imageUpSampler.upSampling(blurredImage);
            return bufferedImage;
        } catch (Exception e) {
            log.error("Failed to blurred image. e: {}. message: {}", e.getClass(), e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
