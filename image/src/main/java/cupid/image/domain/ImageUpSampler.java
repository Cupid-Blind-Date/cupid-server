package cupid.image.domain;

import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class ImageUpSampler {

    public BufferedImage upSampling(BufferedImage src) {
        int targetWidth = src.getWidth() * 2;
        int targetHeight = src.getHeight() * 2;
        BufferedImage upsampled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgb = src.getRGB(x, y);

                int dstX = x * 2;
                int dstY = y * 2;
                upsampled.setRGB(dstX, dstY, rgb);
                upsampled.setRGB(dstX + 1, dstY, rgb);
                upsampled.setRGB(dstX, dstY + 1, rgb);
                upsampled.setRGB(dstX + 1, dstY + 1, rgb);
            }
        }

        return upsampled;
    }
}
