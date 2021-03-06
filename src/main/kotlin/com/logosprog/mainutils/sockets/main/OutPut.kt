/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("MemberVisibilityCanPrivate")

package com.logosprog.mainutils.sockets.main

import java.io.ObjectOutputStream
import java.util.concurrent.Callable

/**
 * Created by forando on 01.10.14.
 * This class uses socket output stream to send messages.
 */
class OutPut @Throws(NullPointerException::class)
constructor(internal var output: ObjectOutputStream?, private val messageObject: Any?) : Callable<Void> {

    init {
        //Sometimes out == NULL:
        if (output == null) throw NullPointerException("ObjectOutputStream cannot be NULL.")
        if (messageObject == null) throw NullPointerException("messageObject cannot be NULL.")
    }

    override fun call(): Void? {
        try {
            /*
            If you are writing multiple objects to the same ObjectOutputStream
            instance on the server side (i.e., multiple writeObject() calls),
            this can result in stream header problems due to potentially multiple
            references to the same objects (typically nested references) when
            they are read by the client's input stream.

            This problem occurs when the object output stream wraps a socket
            output stream since during normal serialization, the second and
            later references to an object do not describe the object but rather
            only use a reference. The client's ObjectInputStream does not
            reconstruct the objects properly for some reason due to a difference
            in the header information it is expecting (it doesn't retain it from
            previous readObject() calls). This problem does not occur with the
            first readObject() call but rather the second and subsequent ones.

            If you want to continue to use the same socket stream to write
            multiple objects, you will need to do out.reset()

            The reset() call re-initializes the stream, ignoring the state of
            any objects previously sent along the stream. This ensures that each
            object is sent in its entirety without the handle-type references
            that are typically used to compress ObjectOutputStream data and
            avoid duplication.
             */
            output?.reset()
            /*
            For some reason out.writeUnshared(messageObject) does not write
             the given object as a new, unique object in the stream.
             That's why we do before out.reset().
             */
            output?.writeUnshared(messageObject)
            output?.flush()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}
