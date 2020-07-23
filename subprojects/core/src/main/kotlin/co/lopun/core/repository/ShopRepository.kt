package co.lopun.core.repository

import co.lopun.core.model.Shop
import co.lopun.core.model.gen.tables.Shop.SHOP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.Record5
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ShopRepository(
  private val dslMaster: DSLContext,
  private val dslReplica: DSLContext
) {
  suspend fun findByIdAsync(id: Int): Shop? = withContext(Dispatchers.IO) {
    dslReplica
      .select(
        SHOP.ID,
        SHOP.NAME,
        SHOP.URL,
        SHOP.CREATED_AT,
        SHOP.UPDATED_AT
      )
      .from(SHOP)
      .where(SHOP.ID.eq(id))
      .fetchOne()
      ?.toShop()
  }

  fun findByIdSync(id: Int): Shop? = dslReplica
    .select(
      SHOP.ID,
      SHOP.NAME,
      SHOP.URL,
      SHOP.CREATED_AT,
      SHOP.UPDATED_AT
    )
    .from(SHOP)
    .where(SHOP.ID.eq(id))
    .fetchOne()
    ?.toShop()

  private fun Record5<Int, String, String, LocalDateTime, LocalDateTime>.toShop(): Shop = Shop(
    id = this[SHOP.ID],
    name = this[SHOP.NAME],
    url = this[SHOP.URL],
    createdAt = this[SHOP.CREATED_AT],
    updatedAt = this[SHOP.UPDATED_AT]
  )
}

