package cupid.image.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
public class ImageBlurProcessor {

    private final ForkJoinPool pool = new ForkJoinPool(); // 병렬 실행 풀

    public BufferedImage process(BufferedImage src) {
        int kernelSize = 19;
        int edge = kernelSize / 2;
        int width = src.getWidth();
        int height = src.getHeight();

        // 평균 필터의 가장자리 처리를 위해 패딩
        BufferedImage padded = padding(src, width, edge, height);

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Fork join pool 을 사용하여 이미지를 4등분한뒤 분할정복한다.
        BlurTask blurTask = BlurTask.builder()
                .src(padded)
                .dest(result)
                .startX(edge)
                .endX(width + edge)
                .startY(edge)
                .endY(height + edge)
                .kernelSize(kernelSize)
                .build();
        pool.invoke(blurTask);
        return result;
    }

    private BufferedImage padding(BufferedImage src, int width, int edge, int height) {
        BufferedImage padded = new BufferedImage(width + edge * 2, height + edge * 2, BufferedImage.TYPE_INT_RGB);

        // 중앙 원본 이미지 복사
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                padded.setRGB(x + edge, y + edge, src.getRGB(x, y));
            }
        }

        // 왼쪽 / 오른쪽 테두리 복사
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < edge; i++) {
                padded.setRGB(i, y + edge, src.getRGB(0, y));
                padded.setRGB(width + edge + i, y + edge, src.getRGB(width - 1, y));
            }
        }

        // 위/아래 테두리 복사
        for (int x = 0; x < width; x++) {
            int rgbTop = src.getRGB(x, 0);
            int rgbBottom = src.getRGB(x, height - 1);
            for (int i = 0; i < edge; i++) {
                padded.setRGB(x + edge, i, rgbTop);
                padded.setRGB(x + edge, height + edge + i, rgbBottom);
            }
        }
        return padded;
    }

    // 병렬 블러 처리 클래스
    @Builder
    @AllArgsConstructor
    private static class BlurTask extends RecursiveAction {

        private static final int THRESHOLD = 50;
        private final BufferedImage src;
        private final BufferedImage dest;
        private final int startX;
        private final int endX;
        private final int startY;
        private final int endY;
        private final int kernelSize;

        @Override
        protected void compute() {
            int width = endX - startX;
            int height = endY - startY;

            if (height <= THRESHOLD || width <= THRESHOLD) {
                blurSection();
            } else {
                int midY = startY + height / 2;
                int midX = startX + width / 2;
                invokeAll(
                        BlurTask.builder()
                                .src(src)
                                .dest(dest)
                                .startX(startX)
                                .endX(midX)
                                .startY(startY)
                                .endY(midY)
                                .kernelSize(kernelSize)
                                .build(),
                        BlurTask.builder()
                                .src(src)
                                .dest(dest)
                                .startX(startX)
                                .endX(midX)
                                .startY(midY)
                                .endY(endY)
                                .kernelSize(kernelSize)
                                .build(),
                        BlurTask.builder()
                                .src(src)
                                .dest(dest)
                                .startX(midX)
                                .endX(endX)
                                .startY(startY)
                                .endY(midY)
                                .kernelSize(kernelSize)
                                .build(),
                        BlurTask.builder()
                                .src(src)
                                .dest(dest)
                                .startX(midX)
                                .endX(endX)
                                .startY(midY)
                                .endY(endY)
                                .kernelSize(kernelSize)
                                .build()
                );
            }
        }

        private void blurSection() {
            int edge = kernelSize / 2;

            for (int y = Math.max(startY, edge); y < Math.min(endY, src.getHeight() - edge); y++) {
                for (int x = Math.max(startX, edge); x < Math.min(endX, src.getWidth() - edge); x++) {
                    int rSum = 0, gSum = 0, bSum = 0;

                    for (int dy = -edge; dy <= edge; dy++) {
                        for (int dx = -edge; dx <= edge; dx++) {
                            Color color = new Color(src.getRGB(x + dx, y + dy));
                            rSum += color.getRed();
                            gSum += color.getGreen();
                            bSum += color.getBlue();
                        }
                    }

                    int pixelCount = kernelSize * kernelSize;
                    Color blurredColor = new Color(
                            rSum / pixelCount,
                            gSum / pixelCount,
                            bSum / pixelCount
                    );

                    dest.setRGB(x - edge, y - edge, blurredColor.getRGB());
                }
            }
        }
    }
}
