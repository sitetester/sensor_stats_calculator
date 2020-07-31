package csv

import scala.collection.mutable

class Stats {

  private var _filesProcessed = 0
  private var _measurementsProcessed = 0
  private var _measurementsFailed = 0

  private var _nanSensorHumidityMap: mutable.Map[String, (String, String, String)] = mutable.Map[String, (String, String, String)]()
  private var _sensorHumidityMap: mutable.Map[String, (Int, Int, Int)] = mutable.Map[String, (Int, Int, Int)]()

  def sensorHumidityMap(sensorHumidityMap: mutable.Map[String, (Int, Int, Int)]): Unit = {
    _sensorHumidityMap = sensorHumidityMap
  }

  def nanSensorHumidityMap(nanSensorHumidityMap: mutable.Map[String, (String, String, String)]): Unit = {
    _nanSensorHumidityMap = nanSensorHumidityMap
  }

  def filesProcessed(num: Int): Unit = {
    _filesProcessed = num
  }

  def incrementProcessedCountBy(num: Int): Unit = {
    _measurementsProcessed += num
  }

  def incrementFailedCount(): Unit = {
    _measurementsFailed += 1
  }

  def filesProcessed: Int = _filesProcessed

  def measurementsProcessed: Int = _measurementsProcessed

  def measurementsFailed: Int = _measurementsFailed

  def sensorHumidityMap: mutable.Map[String, (Int, Int, Int)] = _sensorHumidityMap

  def nanSensorHumidityMap: mutable.Map[String, (String, String, String)] = _nanSensorHumidityMap

}
