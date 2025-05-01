package cupid.gracefulshutdown;

import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Runner implements CommandLineRunner {

    private final Executor virtualThreadGracefulShutdown;
    public boolean a = true;


    @Override
    public void run(String... args) throws Exception {
        virtualThreadGracefulShutdown.execute(() -> {
            System.out.println("START");
//            while (a) {
//
//            }
            System.out.println("START");
        });
    }
}
