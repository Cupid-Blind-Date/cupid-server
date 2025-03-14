package cupid.support.db;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataClearExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        DataCleaner dataCleaner = getDataCleaner(context);
        dataCleaner.clear();
    }

    private DataCleaner getDataCleaner(ExtensionContext extensionContext) {
        return SpringExtension.getApplicationContext(extensionContext)
                .getBean(DataCleaner.class);
    }
}
