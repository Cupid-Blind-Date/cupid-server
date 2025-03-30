package cupid.image.domain;

import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class ImageUpSampler {
    
    public BufferedImage upSampling(BufferedImage src) {
        int targetWidth = src.getWidth() * 2;
        int targetHeight = src.getHeight() * 2;
        BufferedImage upsampled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                // 짝수만 남긴 원래 다운샘플 이미지 기준으로 역매핑
                int srcX = x / 2;
                int srcY = y / 2;

                // 경계 체크
                srcX = Math.min(srcX, src.getWidth() - 1);
                srcY = Math.min(srcY, src.getHeight() - 1);

                int rgb = src.getRGB(srcX, srcY);
                upsampled.setRGB(x, y, rgb);
            }
        }

        return upsampled;
    }
}
