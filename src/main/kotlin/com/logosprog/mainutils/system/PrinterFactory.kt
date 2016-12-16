/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

package com.logosprog.mainutils.system

import java.awt.print.*
import javax.print.DocPrintJob
import javax.print.attribute.standard.PrinterName


open class Printer(private val printerJob: PrinterJob, private val pageFormat: PageFormat){
    /**
     * @param printable An object that draws a picture to be printed
     */
    open fun print(printable: Printable){
        printerJob.setPrintable(printable, pageFormat)
        try {
            printerJob.print()
        } catch (e: PrinterException) {
            e.printStackTrace()
        }
    }
}

/**
 * @throws [PrinterException] When it is impossible to create a printerJob
 */
fun getPrinter(printerName: PrinterName, paperWidth: Int, paperHeight: Int,
               orientation: Int = PageFormat.PORTRAIT, paperMargin: Double):Printer{
    val services = PrinterJob.lookupPrintServices()
    var docPrintJob: DocPrintJob? = null

    services.forEach {if (it.name.equals(printerName.name, true)) docPrintJob = it.createPrintJob()}

    val printerJob = PrinterJob.getPrinterJob()
    printerJob.jobName = "another_print_job"
    printerJob.printService = docPrintJob?.printService ?:
            throw PrinterException("Printer ${printerName.name} was not found")
    val pageFormat = printerJob.defaultPage()
    pageFormat.paper = getPaper(paperWidth, paperHeight, paperMargin)
    pageFormat.orientation = orientation
    println("New printer created")
    return Printer(printerJob, pageFormat)
}

private fun getPaper(width: Int, height: Int, margin: Double): Paper{
    val paper = Paper()
    paper.setSize(width.toDouble(), height.toDouble())
    paper.setImageableArea(margin, margin, width - margin * 2, height - margin * 2)
    return paper
}