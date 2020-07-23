package co.lopun.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import co.lopun.core.config.EnableJooqTransactionManagement
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["co.lopun"])
@EnableJooqTransactionManagement
class GatewayApplication

fun main() {
  System.setProperty(IO_PARALLELISM_PROPERTY_NAME, "512")
  runApplication<GatewayApplication>()
  println("available processors: ${Runtime.getRuntime().availableProcessors()}")
}
