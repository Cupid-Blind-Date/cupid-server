package cupid.softdelete;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import java.util.Optional;

@Converter
public class DeletedAtConverter implements AttributeConverter<Boolean, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(Boolean delete) {
        if (delete) {
            return LocalDateTime.now();
        }
        return null;
    }

    @Override
    public Boolean convertToEntityAttribute(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).isPresent();
    }
}
