package xml.builders;

import org.w3c.dom.Document;

/**
 * Created by forando on 14.06.15.<br>
 *     Implement this interface to add factory method
 *     to build XML Document object.
 */
public interface XMLDocBuilder {
    Document build();
}
