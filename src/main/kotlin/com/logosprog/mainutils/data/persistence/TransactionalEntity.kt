package com.logosprog.mainutils.data.persistence

import com.logosprog.mainutils.system.RequestContext
import org.joda.time.DateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by Andreylogoshko on 09.03.2017.
 *
 * The parent class for all transactional persistent entities.
 */
@MappedSuperclass
class TransactionalEntity{

    /**
     * The default serial version UID.
     */
    private val serialVersionUID = 1L

    /**
     * The primary key identifier.
     */
    @Id
    @GeneratedValue
    var id: Long? = null

    /**
     * A secondary unique identifier which may be used as a reference to this entity by external systems.
     */
    @NotNull
    var referenceId = UUID.randomUUID().toString()

    /**
     * The entity instance version used for optimistic locking.
     */
    @Version
    var version: Int? = null

    /**
     * A reference to the entity or process which created this entity instance.
     */
    @NotNull
    var createdBy: String = "DefaultUser"

    /**
     * The timestamp when this entity instance was created.
     */
    @NotNull
    var createdAt: DateTime = DateTime()

    /**
     * A reference to the entity or process which most recently updated this entity instance.
     */
    var updatedBy: String? = null

    /**
     * The timestamp when this entity instance was most recently updated.
     */
    var updatedAt: DateTime? = null

    /**
     * A listener method which is invoked on instances of TransactionalEntity (or their subclasses) prior to initial
     * persistence. Sets the `created` audit values for the entity. Attempts to obtain this thread's instance
     * of a username from the RequestContext. If none exists, throws an IllegalArgumentException. The username is used
     * to set the `createdBy` value. The `createdAt` value is set to the current timestamp.
     */
    @PrePersist
    fun beforePersist() {
        val username = RequestContext.getUsername() ?:
                throw IllegalArgumentException("Cannot persist a TransactionalEntity without a username " +
                        "in the RequestContext for this thread.")
        createdBy = username
        createdAt = DateTime()
    }

    /**
     * A listener method which is invoked on instances of TransactionalEntity (or their subclasses) prior to being
     * updated. Sets the `updated` audit values for the entity. Attempts to obtain this thread's instance of
     * username from the RequestContext. If none exists, throws an IllegalArgumentException. The username is used to set
     * the `updatedBy` value. The `updatedAt` value is set to the current timestamp.
     */
    @PreUpdate
    fun beforeUpdate() {
        val username = RequestContext.getUsername() ?:
                throw IllegalArgumentException("Cannot update a TransactionalEntity without a username " +
                        "in the RequestContext for this thread.")
        createdBy = username
        createdAt = DateTime()
    }

    /**
     * Determines the equality of two TransactionalEntity objects. If the supplied object is null, returns false. If
     * both objects are of the same class, and their `id` values are populated and equal, return
     * `true`. Otherwise, return `false`.
     * @param other An Object
     * @return A boolean
     * @see java.lang.Object.equals
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (this.javaClass == other.javaClass) {
            val thatEntity = other as TransactionalEntity?
            if (this.id == null || thatEntity!!.id == null) {
                return false
            }
            if (this.id == thatEntity.id) {
                return true
            }
        }
        return false
    }

    /**
     * Returns the hash value of this object.
     * @return An int
     * @see java.lang.Object.hashCode
     */
    override fun hashCode(): Int {
        if (id == null) {
            return -1
        }
        return id!!.hashCode()
    }

}
