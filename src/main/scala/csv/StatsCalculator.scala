package csv

import java.io.File

import scala.collection.mutable
import scala.io.Source

object StatsCalculator {

  var sensorDataMap: mutable.Map[String, (Long, Int)] = mutable.Map[String, (Long, Int)]()

  def calculateStats(file: File): Stats = {

    val csvFiles = file.listFiles.filter(file => {
      file.toString.split("\\.").last.equals("csv")
    })

    if (csvFiles.isEmpty) {
      throw new Exception("No CSV files found!")
    }

    println("Processing files...")

    val stats = new Stats()
    stats.filesProcessed(csvFiles.length)

    csvFiles.foreach(file => {
      parseFile(file, stats)
    })

    stats
  }

  def parseFile(file: File, stats: Stats): Unit = {

    val source = Source.fromFile(file.getAbsolutePath)

    var counter = 0
    source.getLines().drop(1).foreach(line => {
      counter += 1

      val data = line.split(",")

      if (data.last == "NaN") {
        stats.incrementFailedCount()
      }

      if (data.last != "NaN") {
        calculateSenorStats(data, stats: Stats)

      } else if (!stats.nanSensorHumidityMap.contains(data.head) && !stats.sensorHumidityMap.contains(data.head)) {
        stats.nanSensorHumidityMap(data.head) = ("NaN", "NaN", "NaN")
      }
    })

    stats.incrementProcessedCountBy(counter)
    source.close()
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
