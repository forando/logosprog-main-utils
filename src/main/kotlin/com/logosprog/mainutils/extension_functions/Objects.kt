package com.logosprog.mainutils.extension_functions

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
    try {
        input = ObjectInputStream(bis)
        val obj = input.readObject()
        if (obj is T)
            return obj
        else
            return null
    }finally {
        try {
            input?.close()
        }catch (ex: IOException){
            // ignore close exception
        }
    }
}

fun ByteArray.convertToStringRepresentation(): String?{
    val K: Long = 1024
    val M: Long = K*K
    val G: Long = M*K
    val T: Long = G*K
    val dividers = arrayOf(T, G, M, K, 1L)
    val units = arrayOf("TB", "GB", "MB", "KB", "B")

    fun format(value: Double, divider: Double, unit: String): String{
        val result: Double
        if (divider > 1)
            result = value/divider
        else
            result = value
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

