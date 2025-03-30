package cupid.image.domain;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class ImageRotator {

    public BufferedImage rotateImage(BufferedImage image, int orientation) {
        AffineTransform transform = new AffineTransform();

        int width = image.getWidth();
        int height = image.getHeight();

        switch (orientation) {
            case 2: // Flip X
                transform.scale(-1.0, 1.0);
                transform.translate(-width, 0);
                break;
            case 3: // PI rotation
                transform.translate(width, height);
                transform.rotate(Math.PI);
                break;
            case 4: // Flip Y
                transform.scale(1.0, -1.0);
                transform.translate(0, -height);
                break;
            case 5: // -PI/2 and Flip X
                transform.rotate(-Math.PI / 2);
                transform.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 rotation
                transform.translate(height, 0);
                transform.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                transform.scale(-1.0, 1.0);
                transform.translate(-height, 0);
                transform.translate(0, width);
                transform.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                transform.translate(0, width);
                transform.rotate(3 * Math.PI / 2);
                break;
            default:
                return image;
        }

        // 회전 또는 반전된 새 이미지 생성
        BufferedImage newImage = new BufferedImage(
                (orientation >= 5 && orientation <= 8) ? height : width,
                (orientation >= 5 && orientation <= 8) ? width : height,
                image.getType()
        );

        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
}
