/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files;

import java.io.IOException;

/**
 * Created by forando on 14.06.15.<br>
 *     This interface has to be implemented in order
 *     to construct an object from external file.
 */
public interface ObjectFromFileBuilder<T,E> {
    /**
     * Factory method that constructs object of predefined type
     * from external file.
     * @param element Any element that may be needed to construct a {@link T} object
     * @return An object of desired type
     * @throws IOException
     */
    T build(E element)throws IOException;
}
