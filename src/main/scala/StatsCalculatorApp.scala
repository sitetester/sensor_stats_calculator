import java.io.File
import java.nio.file.Paths

import csv.StatsCalculator

object StatsCalculatorApp extends App {

  // TODO: Program takes one argument: a path to directory
  val path = Paths.get("/Users/xxx/IdeaProjects/temp/sensor_statistics_task/src/main/resources")

  val file = new File(path.toString)

  if (file.exists && file.isDirectory) {
    val stats = StatsCalculator.calculateStats(file)

    println()
    println("Num of processed files: " + stats.filesProcessed)
    println("Num of processed measurements: " + stats.measurementsProcessed)
    println("Num of failed measurements: " + stats.filesProcessed)
    println("sensor-id,min,avg,max: ")

    stats.sensorHumidityMap.toSeq.sortWith((x, y) => x._2._2 > y._2._2)
      .foreach(x => printf("%s,%d,%d,%d\n", x._1, x._2._1,x._2._2,x._2._3))

    stats.nanSensorHumidityMap.foreach(x => {
      printf("%s,%s,%s,%s\n", x._1, x._2._1,x._2._2,x._2._3)
    })

  } else {
    println("Given path doesn't exists")
  }

}

