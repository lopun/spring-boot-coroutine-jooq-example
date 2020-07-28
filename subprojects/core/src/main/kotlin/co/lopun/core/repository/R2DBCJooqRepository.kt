package co.lopun.core.repository

import co.lopun.core.model.Shop
import co.lopun.core.model.gen.Tables.SHOP
import co.lopun.coroutines.Dispatchers
import io.r2dbc.spi.Row
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.jooq.DSLContext
import org.jooq.Record5
import org.jooq.conf.ParamType
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class R2DBCJooqRepository(
  private val dslMaster: DSLContext,
  private val dslReplica: DSLContext,
  private val databaseClientMaster: DatabaseClient,
  private val databaseClientReplica: DatabaseClient
) {
  suspend fun findByIdAsync(id: Int): Shop? = withContext(Dispatchers.IO.dispatcher) {
    withTimeout(10_000) {
      val plainSql = dslReplica
        .select(SHOP.ID, SHOP.NAME, SHOP.URL, SHOP.CREATED_AT, SHOP.UPDATED_AT)
        .from(SHOP)
        .where(SHOP.ID.eq(id))
        .getSQL(ParamType.INLINED)

      databaseClientReplica
        .execute(plainSql)
        .map { row, _ -> row.toShop() }
        .one()
        .awaitFirstOrNull()
    }
  }

  suspend fun findByIdsAsync(ids: List<Int>): Flow<Shop> = withTimeout(10_000) {
    val plainSql = dslReplica
      .select(SHOP.ID, SHOP.NAME, SHOP.URL, SHOP.CREATED_AT, SHOP.UPDATED_AT)
      .from(SHOP)
      .where(SHOP.ID.`in`(ids))
      .getSQL(ParamType.INLINED)

    databaseClientReplica
      .execute(plainSql)
      .map { row, _ -> row.toShop() }
      .all()
      .asFlow()
  }

  private fun Record5<Int, String, String, LocalDateTime, LocalDateTime>.toShop(): Shop = Shop(
    id = this[SHOP.ID],
    name = this[SHOP.NAME],
    url = this[SHOP.URL],
    createdAt = this[SHOP.CREATED_AT],
    updatedAt = this[SHOP.UPDATED_AT]
  )

  private fun Row.toShop(): Shop = Shop(
    id = this.get(SHOP.ID.name, SHOP.ID.type),
    name = this.get(SHOP.NAME.name, SHOP.NAME.type)!!,
    url = this.get(SHOP.URL.name, SHOP.URL.type)!!,
    createdAt = this.get(SHOP.CREATED_AT.name, SHOP.CREATED_AT.type)!!,
    updatedAt = this.get(SHOP.UPDATED_AT.name, SHOP.UPDATED_AT.type)!!
  )

  companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }
}
