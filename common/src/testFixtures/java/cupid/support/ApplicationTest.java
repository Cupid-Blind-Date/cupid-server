package cupid.support;

import cupid.support.db.DataClearExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(DataClearExtension.class)
@SpringBootTest
public class ApplicationTest extends CommonTest {
}
