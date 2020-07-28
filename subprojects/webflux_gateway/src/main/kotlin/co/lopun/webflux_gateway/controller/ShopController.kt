package co.lopun.webflux_gateway.controller

import co.lopun.biz.services.ShopService
import co.lopun.core.model.Shop
import co.lopun.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
class ShopController(
  private val shopService: ShopService
) {
  @GetMapping("/shop/jdbc_sync_ten")
  fun get100ShopsJDBCSync() = random10Numbers().map { shopService.getShopByIdJDBCSync(it) }

  @GetMapping("/shop/jdbc_async_ten")
  fun get10ShopsJDBCAsync(): List<Shop> = runBlocking {
    var result: List<Shop> = emptyList()
    val time = withTimeoutOrNull(10_000) {
      measureTimeMillis {
        result = random10Numbers().map { async { shopService.getShopByIdJDBCAsync(it) } }.awaitAll().filterNotNull()
      }
    }
    logger.info("time was $time ms")
    result
  }

  @GetMapping("/shop/r2dbc_async_ten")
  fun get10ShopsR2DBCAsync(): List<Shop> = runBlocking {
    var result: List<Shop> = emptyList()
    val time = withTimeoutOrNull(10_000) {
      measureTimeMillis {
        result = random10Numbers().map { async { shopService.getShopByIdR2DBCAsync(it) } }.awaitAll().filterNotNull()
      }
    }
    logger.info("time was $time ms")
    result
  }

  @GetMapping("/shop/jdbc_sync/{id}")
  fun getShopByIdJDBCSync(@PathVariable id: Int) = shopService.getShopByIdJDBCSync(id)

  @GetMapping("/shop/jdbc_async/{id}")
  fun getShopByIdJDBCAsync(@PathVariable id: Int) = runBlocking {
    shopService.getShopByIdJDBCAsync(id)
  }

  @GetMapping("/shop/r2dbc_async/{id}")
  fun getShopByIdR2DBCAsync(@PathVariable id: Int) = runBlocking {
    shopService.getShopByIdR2DBCAsync(id)
  }

  private fun random10Numbers() = (0 until 100).shuffled().take(10)

  companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }
}
