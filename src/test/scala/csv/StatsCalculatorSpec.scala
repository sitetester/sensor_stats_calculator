package csv

import java.io.File
import java.nio.file.Paths

import org.scalatest.flatspec.AnyFlatSpec

class StatsCalculatorSpec extends AnyFlatSpec {

  it should "find NAN value" in {

    val resourceDirectory = Paths.get("src", "test", "resources")
    val absolutePath = resourceDirectory.toFile.getAbsolutePath

    val file = new File(absolutePath)

    val stats = StatsCalculator.calculateStats(file)

    assert(stats.filesProcessed == 1)
    assert(stats.measurementsProcessed == 5)
    assert(stats.measurementsFailed == 2)
    assert(stats.nanSensorHumidityMap.contains("s3") && stats.nanSensorHumidityMap.contains("s5") )
  }
}
