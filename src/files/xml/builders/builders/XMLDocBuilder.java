package files.xml.builders.builders;

import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Created by forando on 14.06.15.<br>
 *     Implement this interface to add factory method
 *     to build XML Document object.
 */
public interface XMLDocBuilder {
    /**
     * Converts external .xml file of a specific module(application)
     * into {@link Document} object.
     * @return {@link Document} object
     * @throws IOException
     */
    Document build() throws IOException;
}
