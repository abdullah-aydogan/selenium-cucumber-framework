package tr.abdullah.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ElementReader parses JSON repository files located under resources/elements/
 * and dynamically constructs Selenium By locator objects.
 */
public class ElementReader {

    private static final Logger logger = LogManager.getLogger(ElementReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Cache to prevent reading JSON files multiple times from disk
    private static final ConcurrentHashMap<String, JsonNode> cache = new ConcurrentHashMap<>();

    /**
     * Loads the target JSON file from classpath into a JsonNode.
     *
     * @param pageName Name of the page JSON file (without extension)
     * @return Root JsonNode of the element definition file
     */
    private static JsonNode loadPageElements(String pageName) {
        return cache.computeIfAbsent(pageName, key -> {
            String filePath = "elements/" + key + ".json";
            try (InputStream is = ElementReader.class.getClassLoader().getResourceAsStream(filePath)) {
                if (is == null) {
                    logger.error("Element file not found: {}", filePath);
                    throw new RuntimeException("Element file not found: " + filePath);
                }
                logger.debug("Loading element JSON file into cache: {}", filePath);
                return objectMapper.readTree(is);
            } catch (Exception e) {
                logger.error("Failed to parse element file [{}]: {}", filePath, e.getMessage());
                throw new RuntimeException("Could not parse JSON element file: " + filePath, e);
            }
        });
    }

    /**
     * Returns a Selenium By locator for a given page and element key.
     *
     * @param pageName    Name of the page file (e.g. "login_page")
     * @param elementName Element identifier key in the JSON file (e.g. "username_input")
     * @return Selenium By instance
     */
    public static By getLocator(String pageName, String elementName) {
        JsonNode rootNode = loadPageElements(pageName);
        JsonNode elementNode = rootNode.get(elementName);

        if (elementNode == null) {
            String errorMsg = String.format("Locator key '%s' not found in page '%s.json'", elementName, pageName);
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        String type = elementNode.get("type").asText().trim().toLowerCase();
        String value = elementNode.get("value").asText();

        logger.debug("Resolving locator for [{}.{}] -> Type: {}, Value: {}", pageName, elementName, type, value);

        return switch (type) {
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "css", "cssselector" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "classname", "class" -> By.className(value);
            case "tagname", "tag" -> By.tagName(value);
            case "linktext" -> By.linkText(value);
            case "partiallinktext" -> By.partialLinkText(value);
            default -> {
                String errorMsg = "Unsupported locator type: " + type + " for element: " + elementName;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        };
    }
}