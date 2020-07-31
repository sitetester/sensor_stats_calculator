package csv

import java.io.File

import scala.collection.mutable
import scala.io.Source

object StatsCalculator {

  var sensorDataMap: mutable.Map[String, (Long, Int)] = mutable.Map[String, (Long, Int)]()

  def calculateStats(file: File): Stats = {

    val stats = new Stats()

    val csvFiles = file.listFiles.filter(file => {
      file.toString.split("\\.").last.equals("csv")
    })

    stats.filesProcessed(csvFiles.length)

    csvFiles.foreach(file => {
      parseFile(file, stats)
    })

    stats
  }

  def parseFile(file: File, stats: Stats): Unit = {

    val source = Source.fromFile(file.getAbsolutePath)
    checkHeader(source.getLines().take(1).toList.mkString(","))

    var counter = 0
    source.getLines().foreach(line => {
      val data = line.split(",")

      if (data.last != "NaN") {
        counter += 1
        calculateSenorStats(data, stats: Stats)
        stats.incrementFailedCount()
      } else if (!stats.nanSensorHumidityMap.contains(data.head) && !stats.sensorHumidityMap.contains(data.head)) {
        stats.nanSensorHumidityMap(data.head) = ("NaN", "NaN", "NaN")
      }
    })

    stats.incrementProcessedCountBy(counter)
    source.close()
  }

  private def checkHeader(header: String): Unit = {

    val columns = header.split(",")
    if (columns.head != "sensor-id" && columns.last != "humidity") {
      throw new Exception("Invalid CSV file provided!")
    }
  }

  private def calculateSenorStats(data: Array[String], stats: Stats) {

    val humidity = data.last.toInt
    val sensor = data.head

    if (!stats.sensorHumidityMap.contains(sensor)) {
      stats.sensorHumidityMap(sensor) = (humidity, humidity, humidity)
      sensorDataMap(sensor) = (humidity.toLong, 1)
    } else {
      val temp = sensorDataMap(data.head)
      sensorDataMap(data.head) = (temp._1 + humidity, temp._2 + 1)

      val senorData = stats.sensorHumidityMap(sensor)
      val min = if (senorData._1 < humidity) senorData._1 else humidity
      val max = if (senorData._3 > humidity) senorData._3 else humidity

      val divBy = sensorDataMap(sensor)._2
      val total = sensorDataMap(data.head)._1
      val avg = total / divBy

      stats.sensorHumidityMap(data.head) = (min, avg.toInt, max.toInt)
    }
  }
}
