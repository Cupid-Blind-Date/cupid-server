package cupid.image.presentation;

import cupid.image.application.ImageService;
import cupid.image.application.ImageUploadService;
import cupid.image.presentation.response.UploadImageResponse;
import cupid.image.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageUploadService imageUploadService;
    private final ImageService imageService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(200);

    /**
     * 이미지 블러처리 및 원본 이미지와 함께 업로드
     */
    @PostMapping("/images/blur")
    public ResponseEntity<UploadImageResponse> upload(@RequestParam("image") MultipartFile image) throws Exception {
        // 파일 확장자 가져오기
        String extension = ImageUtils.getExtension(image.getOriginalFilename());
        byte[] bytes = image.getBytes();

        // 아이폰 사진 업로드 시 회전처리되는 현상 해결
        Future<String> uploadOriginalImage = executorService.submit(() -> {
            BufferedImage rotate = imageService.rotate(bytes);
            return imageUploadService.uploadImageWithRandomName(rotate, extension);
        });

        // 이미지 블러처리
        Future<String> uploadBlurredImage = executorService.submit(() -> {
            BufferedImage blurred = imageService.blur(bytes);
            return imageUploadService.uploadImageWithRandomName(blurred, extension);
        });

        String originalImageName = uploadOriginalImage.get();
        String blurredImageName = uploadBlurredImage.get();
        return ResponseEntity.ok(new UploadImageResponse(originalImageName, blurredImageName));
    }
}
