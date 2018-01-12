@file:Suppress("unused")

package com.logosprog.mainutils.system

import org.slf4j.LoggerFactory

/**
 * Created by Andreylogoshko on 09.03.2017.
 *
 * The RequestContext facilitates the storage of information for the duration of a single request (or web service
 * transaction).
 *
 * RequestContext attributes are stored in ThreadLocal objects.
 */
class RequestContext{
    companion object{
        /**
         * The Logger for this Class.
         */
        private val logger = LoggerFactory.getLogger(RequestContext::class.java)

        /**
         * ThreadLocal storage of username Strings.
         */
        private val userNames = ThreadLocal<String>()

        /**
         * Get the username for the current thread.

         * @return A String username.
         */
        fun getUsername(): String? {
            return userNames.get()
        }

        /**
         * Set the username for the current thread.

         * @param username A String username.
         */
        fun setUsername(username: String) {
            userNames.set(username)
            logger.debug("RequestContext added username {} to current thread", username)
        }

        /**
         * Initialize the ThreadLocal attributes for the current thread.
         */
        fun init() {
            userNames.set(null)
        }
    }
}
