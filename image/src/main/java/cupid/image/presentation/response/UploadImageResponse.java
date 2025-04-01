package cupid.image.presentation.response;

public record UploadImageResponse(
        String originalImageName,
        String blurredImageName
) {
}
