package com.rit.matthew.arborlooapp.Export

import com.rit.matthew.arborlooapp.Model.Report
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class ExcelGenerator {
    companion object {
        fun createExcelData(report: Report, infoHashMap: HashMap<String, Any?>, surveyHashMap: HashMap<String, Any?>): HSSFWorkbook{

            val workbook: HSSFWorkbook = HSSFWorkbook()

            val infoSheet = workbook.createSheet("Data Collector Observation")

            val infoHeadingRow = infoSheet.createRow(0)

            infoHeadingRow.createCell(0).setCellValue("Data Collector Observation")

            var infoIndex = 1
            for(infoQuestion in infoHashMap.keys){
                val infoRow = infoSheet.createRow(infoIndex)
                infoRow.createCell(0).setCellValue(infoQuestion)
                infoRow.createCell(1).setCellValue(infoHashMap.get(infoQuestion).toString())
                infoIndex++
            }

            val usageRow = infoSheet.createRow(infoIndex)
            usageRow.createCell(0).setCellValue("Uses")
            usageRow.createCell(1).setCellValue(report.uses.toString())

            val surveySheet = workbook.createSheet("Survey Questions")

            val surveyHeadingRow = surveySheet.createRow(0)

            surveyHeadingRow.createCell(0).setCellValue("Survey Questions")

            var surveyIndex = 1
            for(surveyQuestion in surveyHashMap.keys){
                val surveyRow = surveySheet.createRow(surveyIndex)
                surveyRow.createCell(0).setCellValue(surveyQuestion)
                surveyRow.createCell(1).setCellValue(surveyHashMap.get(surveyQuestion).toString())
                surveyIndex++
            }

            val tempSheet = workbook.createSheet("Temperature Data")

            val tempHeadingRow = tempSheet.createRow(0)

            tempHeadingRow.createCell(0).setCellValue("Temperature Data")
            tempHeadingRow.createCell(1).setCellValue("DateTime")

            for(tempIndex in report.temperatureData.indices){
                val tempRow = tempSheet.createRow(tempIndex + 1)
                tempRow.createCell(0).setCellValue(report.temperatureData.get(tempIndex).data.toString())
                val tempDate = OffsetDateTime.ofInstant(report.temperatureData.get(tempIndex).dateTime?.let { Instant.ofEpochSecond(it) }, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                tempRow.createCell(1).setCellValue(tempDate)
            }

            val moistSheet = workbook.createSheet("Moisture Data")

            val moistHeadingRow = moistSheet.createRow(0)

            moistHeadingRow.createCell(0).setCellValue("Moisture Data")
            moistHeadingRow.createCell(1).setCellValue("DateTime")

            for(moistIndex in report.moistureData.indices){
                val moistRow = moistSheet.createRow(moistIndex + 1)
                moistRow.createCell(0).setCellValue(report.moistureData.get(moistIndex).data.toString())
                val moistDate = OffsetDateTime.ofInstant(report.moistureData.get(moistIndex).dateTime?.let { Instant.ofEpochSecond(it) }, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                moistRow.createCell(1).setCellValue(moistDate)
            }

            return workbook
        }
    }
}