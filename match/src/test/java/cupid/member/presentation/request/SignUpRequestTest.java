package cupid.member.presentation.request;

import cupid.member.domain.Gender;
import cupid.member.presentation.request.SignUpRequest.ProfileImageRequest;
import cupid.support.RequestDataTest;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SignUpRequestTest extends RequestDataTest<SignUpRequest> {

    @Nested
    class 아이디는 {

        @ParameterizedTest
        @ValueSource(ints = {
                4, 5, 9, 10, 11, 20, 30, 40, 49, 50
        })
        void 네글자_이상_50글자_이내여야_한다(int length) {
            // given
            String username = "a".repeat(length);
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                0, 1, 2, 3, 51, 100
        })
        void 네글자_이상_50글자_이내가_아니면_예외(int length) {
            // given
            String username = "a".repeat(length);
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "aaaa",
                "AAAA",
                "AAA1",
        })
        void 영어_숫자만_입력_가능하다(String username) {
            // given
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @Test
        void 숫자로만_이루어지면_예외() {
            // given
            String username = "1111";
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "abc!",
                "abc❤️❤️",
                "abc!@",
                "abc한글",
        })
        void 영어_숫자_외_다른_문자가_입력되면_예외(String username) {
            // given
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "\n",
                "   ",
                "a b c d",
                "  a  b  c d",
        })
        void 공백이_들어오면_예외(String username) {
            // given
            SignUpRequest request = new SignUpRequest(
                    username,
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }
    }

    @Nested
    class 비밀번호는 {

        @ParameterizedTest
        @ValueSource(strings = {
                "Ab1@Ab1@",
                "Ab1@Ab1@1",
                "Aa@!b000001234567890123456789012345678901234567890", // 50글자
        })
        void 여덟글자_이상_50글자_이내여야_한다(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "A",
                "Aa1@",
                "Aa1@567",
                "Aa@!b0000012345678901234567890123456789012345678901", // 51 글자
        })
        void 여덟글자_이상_50글자_이내가_아니면_예외(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,       // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "Aa1@5678",
        })
        void 영어_숫자_특수문자만_입력_가능하다(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "Aa1@567가",
                "Aa1@567❤️",
        })
        void 영어_숫자_특수문자_이외_다른_문자가_입력되면_예외(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "A a 1 @ 1111",
                "  A  a 1 @ 1 1  11 ",
        })
        void 공백이_들어오면_예외(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "aaaaaaa1@",  // 대문자 없음
                "AAAAAAA1@",  // 소문자 없음
                "@AAAaaaa@",  // 숫자 없음
                "1AAAaaaa1",  // 숫자 없음
        })
        void 최소_한_개_이상의_숫자_소문자_대문자_특수문자를_포함하지_않으면_예외(String password) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    password,
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }
    }

    @Nested
    class 닉네임은 {

        @ParameterizedTest
        @ValueSource(ints = {
                2, 3, 4, 5, 6, 7, 8, 9, 10
        })
        void 두글자_이상_10글자_이내여야_한다(int length) {
            // given
            String nickname = "a".repeat(length);
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    nickname,
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 11, 20, 100
        })
        void 두글자_이상_10글자_이내가_아니면_예외(int length) {
            // given
            String nickname = "a".repeat(length);
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    nickname,
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "aa",
                "AA",
                "11",
                "가가",
                "aA1가",
        })
        void 한글_영어_숫자만_입력_가능하다(String nickname) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    nickname,
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "!!",
                "❤️❤️",
                "a@",
                "@a",
                "a@a",
        })
        void 한글_영어_숫자_이외_다른_문자가_입력되면_예외(String nickname) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    nickname,
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "\n",
                "  ",
                "안 녕",
                "안 녕",
                "  안  녕  ",
        })
        void 공백이_들어오면_예외(String nickname) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    nickname,
                    25,                // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }
    }

    @Nested
    class 나이는 {

        @Test
        void 음수면_예외() {
            // given
            int age = -1;
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    age,
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateFailWithMessage(request);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                0, 1, 20, 99, 100,
        })
        void 나이는_0_이상의_정수여야_한다(int age) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",          // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",    // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",         // ✅ 2~10자, 한글/영문/숫자 포함
                    age,
                    Gender.MALE,        // ✅ NotNull
                    List.of(new ProfileImageRequest("1", "1")) // ✅ 이미지 1장 이상
            );

            // when & then
            validateSuccess(request);
        }
    }

    @Nested
    class 프로필_사진은 {

        @ParameterizedTest
        @NullAndEmptySource
        void 없으면_예외(List<ProfileImageRequest> images) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",  // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",     // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",          // ✅ 2~10자, 한글/영문/숫자 포함
                    20,                 // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    images
            );

            // when & then
            validateFailWithMessage(request);
        }

        @Test
        void 개수가_6개_초과면_예외() {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",  // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",     // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",          // ✅ 2~10자, 한글/영문/숫자 포함
                    20,                 // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1")
                    )
            );

            // when & then
            validateFailWithMessage(request);
        }

        @NullAndEmptySource
        @ParameterizedTest
        void 원본_이미지_중_이름이_없는게_있다면_예외(String name) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",  // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",     // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",          // ✅ 2~10자, 한글/영문/숫자 포함
                    20,                 // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest(name, "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1")
                    )
            );

            // when & then
            validateFailWithMessage(request);
        }

        @NullAndEmptySource
        @ParameterizedTest
        void 블러처리된_이미지_중_이름이_없는게_있다면_예외(String name) {
            // given
            SignUpRequest request = new SignUpRequest(
                    "username",  // ✅ 4~50자, 대소문자+숫자 포함
                    "Aa1!password",     // ✅ 8~50자, 대소문자+숫자+특수문자 포함
                    "닉네임123",          // ✅ 2~10자, 한글/영문/숫자 포함
                    20,                 // ✅ 0 이상
                    Gender.MALE,        // ✅ NotNull
                    List.of(
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", name),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1"),
                            new ProfileImageRequest("1", "1")
                    )
            );

            // when & then
            validateFailWithMessage(request);
        }
    }
}
