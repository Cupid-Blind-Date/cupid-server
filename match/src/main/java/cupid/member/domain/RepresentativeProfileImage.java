package cupid.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RepresentativeProfileImage {

    @Column(unique = true, nullable = true)
    private String originalImageName;

    @Column(unique = true, nullable = true)
    private String blurredImageName;

    public RepresentativeProfileImage(String originalImageName, String blurredImageName) {
        this.originalImageName = originalImageName;
        this.blurredImageName = blurredImageName;
    }

    public RepresentativeProfileImage(ProfileImage profileImage) {
        this.originalImageName = profileImage.getOriginalImageName();
        this.blurredImageName = profileImage.getBlurredImageName();
    }
}
