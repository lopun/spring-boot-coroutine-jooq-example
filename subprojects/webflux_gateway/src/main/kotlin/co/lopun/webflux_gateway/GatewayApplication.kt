package co.lopun.webflux_gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import co.lopun.core.config.EnableDatabase
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["co.lopun"])
@EnableDatabase
class GatewayApplication

fun main() {
  System.setProperty(IO_PARALLELISM_PROPERTY_NAME, "64")
  runApplication<GatewayApplication>()
  println("available processors: ${Runtime.getRuntime().availableProcessors()}")
}
