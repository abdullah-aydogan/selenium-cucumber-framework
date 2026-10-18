package tr.abdullah.utils;

import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

/**
 * DataGenerator provides randomized, realistic test data using DataFaker.
 */
public class DataGenerator {

    private static final Logger logger = LogManager.getLogger(DataGenerator.class);
    private static final Faker faker = new Faker(Locale.US);

    private DataGenerator() {}

    /**
     * Generates a random realistic first name.
     *
     * @return First name string
     */
    public static String getFirstName() {
        String firstName = faker.name().firstName();
        logger.debug("Generated fake first name: {}", firstName);
        return firstName;
    }

    /**
     * Generates a random realistic last name.
     *
     * @return Last name string
     */
    public static String getLastName() {
        String lastName = faker.name().lastName();
        logger.debug("Generated fake last name: {}", lastName);
        return lastName;
    }

    /**
     * Generates a valid US zip code format.
     *
     * @return 5-digit postal code string
     */
    public static String getPostalCode() {
        String postalCode = faker.address().zipCode();
        logger.debug("Generated fake postal code: {}", postalCode);
        return postalCode;
    }
}