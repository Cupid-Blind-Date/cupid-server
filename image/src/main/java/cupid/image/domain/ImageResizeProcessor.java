package cupid.image.domain;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class ImageResizeProcessor {

    public BufferedImage resizeImage(BufferedImage originalImage) {
        int maxWidth = 300;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width > maxWidth) {
            double ratio = (double) maxWidth / width;
            int newWidth = maxWidth;
            int newHeight = (int) (height * ratio);

            Image tmp = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
            BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            originalImage = resized;
        }
        return originalImage;
    }
}
