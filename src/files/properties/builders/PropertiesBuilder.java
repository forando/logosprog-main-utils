package files.properties.builders;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by forando on 14.06.15.<br>
 * Implement this interface to add factory method that
 * builds Properties object
 */
public interface PropertiesBuilder {
    /**
     * Converts external .txt config file of a specific module(application)
     * into {@link Properties} object.
     * @return {@link Properties} object
     * @throws IOException
     */
    Properties build() throws IOException;
}
