package co.lopun.gateway.controller

import co.lopun.biz.services.ShopService
import co.lopun.core.model.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
class ShopController(
  private val shopService: ShopService
) {
  @GetMapping("/shop/sync/{id}")
  fun getShopByIdSync(@PathVariable id: Int) = shopService.getShopByIdSync(id)

  @GetMapping("/shop/async/{id}")
  suspend fun getShopByIdAsync(@PathVariable id: Int) = withContext(Dispatchers.IO) {
    shopService.getShopByIdAsync(id)
  }

  @GetMapping("/shop/sync/hundred")
  fun get100ShopsSync() = (0 until 100).map { shopService.getShopByIdSync(it) }

  @GetMapping("/shop/async/hundred")
  suspend fun get100ShopsAsync() = withContext(Dispatchers.IO) {
//    println("get100ShopsAsync called!")
    var result: List<Shop?> = emptyList()
    val time = measureTimeMillis {
      result = (0 until 100).map { async(Dispatchers.IO) { shopService.getShopByIdAsync(it) } }.awaitAll()
    }
    println("time was $time ms")
    result
  }
}
