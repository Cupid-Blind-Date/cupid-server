package test.async.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestEventPublisher {

    public void publish(TestDomainEvent domainEvent) {
        try {
            log.info("메세지 브로커에 전송 시도. id: {}", domainEvent.getId());
            //Thread.sleep(250);
            Thread.sleep(50);
            log.info("메세지 브로커에 전송 완료. id: {}", domainEvent.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
