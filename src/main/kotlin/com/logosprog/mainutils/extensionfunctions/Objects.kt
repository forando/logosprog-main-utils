@file:Suppress("unused")

package com.logosprog.mainutils.extensionfunctions

import java.io.*
import java.text.DecimalFormat

fun <T: Serializable>T.toByteArray(): ByteArray{
    val bos = ByteArrayOutputStream()
    try {
        val out = ObjectOutputStream(bos)
        out.writeObject(this)
        out.flush()
        return bos.toByteArray()
    }finally {
        try {
            bos.close()
        }catch (ex: IOException){
            // ignore close exception
        }
    }
}

inline fun <reified T: Serializable> ByteArray.toObject(): T?{
    val bis = ByteArrayInputStream(this)
    var input: ObjectInputStream? = null
    return try {
        input = ObjectInputStream(bis)
        val obj = input.readObject()
        obj as? T
    }finally {
        try {
            input?.close()
        }catch (ex: IOException){
            // ignore close exception
        }
    }
}

fun ByteArray.convertToStringRepresentation(): String?{
    val k: Long = 1024
    val m: Long = k*k
    val g: Long = m*k
    val t: Long = g*k
    val dividers = arrayOf(t, g, m, k, 1L)
    val units = arrayOf("TB", "GB", "MB", "KB", "B")

    fun format(value: Double, divider: Double, unit: String): String{
        val result: Double = if (divider > 1)
            value/divider
        else
            value
        return DecimalFormat("#,##0.#").format(result)+ " " + unit
    }
    var result: String? = null
    for (i in dividers.indices){
        val divider = dividers[i]
        if (this.size >= divider){
            result = format(this.size.toDouble(), divider.toDouble(), units[i])
            break
        }
    }
    return result
}

