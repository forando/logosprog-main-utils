/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

import java.awt.print.*
import javax.print.DocPrintJob
import javax.print.attribute.standard.PrinterName


open class Printer(private val printerJob: PrinterJob, private val pageFormat: PageFormat,
                   val defaultPrinter: Boolean = false){

    /**
     * @param printable An object that draws a picture to be printed
     */
    open fun print(printable: Printable){
        printerJob.setPrintable(printable, pageFormat)
        if (!defaultPrinter) {
            if (!printerJob.printDialog()) return
        }
        try {
            printerJob.print()
        } catch (e: PrinterException) {
            e.printStackTrace()
        }
    }
}

/**
 * @param printerName **OPTIONAL** The printer to be used. If NULL is passed, a default one will be used
 * @throws [PrinterException] When [printerName] is defined and it is impossible to create a printerJob
 */
fun getPrinter(paperWidth: Int, paperHeight: Int, printerName: String? = null):Printer{
    val orientation: Int = PageFormat.PORTRAIT
    val printerJob = PrinterJob.getPrinterJob()
    printerJob.jobName = "another_print_job"
    if (printerName != null){
        val services = PrinterJob.lookupPrintServices()
        var docPrintJob: DocPrintJob? = null
        services.forEach {if (it.name.equals(printerName, true)) docPrintJob = it.createPrintJob()}
        printerJob.printService = docPrintJob?.printService ?:
                throw PrinterException("Printer $printerName was not found")
    }
    val pageFormat = printerJob.defaultPage()
    pageFormat.paper = getPaper(paperWidth, paperHeight)
    pageFormat.orientation = orientation
    println("New printer created")
    return Printer(printerJob, pageFormat, printerName != null)
}

private fun getPaper(width: Int, height: Int): Paper{
    val margin = 1.0
    val paper = Paper()
    paper.setSize(width.toDouble(), height.toDouble())
    paper.setImageableArea(margin, margin, width - margin * 2, height - margin * 2)
    return paper
}