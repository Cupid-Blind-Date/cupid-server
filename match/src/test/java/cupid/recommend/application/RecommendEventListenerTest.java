package cupid.recommend.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.common.value.Point;
import cupid.filter.event.FilterUpdateEvent;
import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.RecentActiveInfo;
import cupid.member.domain.RepresentativeProfileImage;
import cupid.support.ApplicationTest;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("RecommendEventListener 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendEventListenerTest extends ApplicationTest {

    @Autowired
    private RecommendEventListener recommendEventListener;

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockitoBean
    private RecommendService recommendService;

    @MockitoBean
    private MemberRepository memberRepository;

    @Test
    void 필터_업데이트_이벤트를_받아_회원_추천_캐시를_업데이트한다() throws InterruptedException {
        // given
        Member member = new Member("", "", "", 20, Gender.FEMALE, new RepresentativeProfileImage("1", "2"));
        member.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point()));
        given(memberRepository.getById(1L))
                .willReturn(member);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mockito.doAnswer(invocationOnMock -> {
                    countDownLatch.countDown();
                    return null;
                }).when(recommendService)
                .reloadCache(anyLong(), any(Point.class));

        // when
        publisher.publishEvent(new FilterUpdateEvent(1L));
        countDownLatch.await();

        // then
        verify(recommendService, times(1))
                .reloadCache(anyLong(), any(Point.class));
    }
}
