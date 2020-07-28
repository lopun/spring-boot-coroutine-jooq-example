package co.lopun.biz.services

import co.lopun.core.model.Shop
import co.lopun.core.repository.JDBCJooqShopRepository
import co.lopun.core.repository.R2DBCJooqRepository
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ShopService(
  private val jdbcJooqShopRepository: JDBCJooqShopRepository,
  private val r2DBCJooqRepository: R2DBCJooqRepository
) {
  suspend fun getShopByIdJDBCAsync(id: Int): Shop? = jdbcJooqShopRepository.findByIdAsync(id)

  fun getShopByIdJDBCSync(id: Int): Shop? = jdbcJooqShopRepository.findByIdSync(id)

  suspend fun getShopByIdR2DBCAsync(id: Int): Shop? = r2DBCJooqRepository.findByIdAsync(id)

  suspend fun getShopByIdsR2DBCAsync(ids: List<Int>): Flow<Shop> = r2DBCJooqRepository.findByIdsAsync(ids)

  companion object {
    val logger = LoggerFactory.getLogger(this::class.java)
  }
}
