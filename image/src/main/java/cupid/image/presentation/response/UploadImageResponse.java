package cupid.image.presentation.response;

public record UploadImageResponse(
        String originalImageUUID,
        String blurredImageUUID
) {
}
