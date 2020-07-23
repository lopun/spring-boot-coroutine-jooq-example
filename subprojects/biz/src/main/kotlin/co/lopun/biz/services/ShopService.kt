package co.lopun.biz.services

import co.lopun.core.model.Shop
import co.lopun.core.repository.ShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ShopService(
  private val shopRepository: ShopRepository
) {
  suspend fun getShopByIdAsync(id: Int): Shop? = withContext(Dispatchers.IO) {
//    logger.info("sleep start with id = $id")
//    delay(1_000)
    val result = shopRepository.findByIdAsync(id)
//    logger.info("sleep end with id = $id")
    result
  }

  fun getShopByIdSync(id: Int): Shop? {
//    logger.info("sleep start with id = $id")
    Thread.sleep(1_000)
    val result = shopRepository.findByIdSync(id)
//    logger.info("sleep end with id = $id")
    return result
  }

  companion object {
    val logger = LoggerFactory.getLogger(this::class.java)
  }
}
