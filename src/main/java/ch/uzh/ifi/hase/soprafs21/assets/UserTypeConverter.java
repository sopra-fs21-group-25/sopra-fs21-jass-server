package ch.uzh.ifi.hase.soprafs21.assets;

import ch.uzh.ifi.hase.soprafs21.constant.UserType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType userType) {
        if(userType == null) {
            return null;
        }
        return userType.getType();
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct <code>dbData</code> type for the corresponding
     * column for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param dbType the userType String data from the database
     *               column to be converted
     * @return the converted UserType Enum to be stored in the entity
     * attribute
     */
    @Override
    public UserType convertToEntityAttribute(String dbType) {
        if(dbType == null) {
            return null;
        }

        return Stream.of(UserType.values())
                .filter(type -> type.getType().equals(dbType))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
