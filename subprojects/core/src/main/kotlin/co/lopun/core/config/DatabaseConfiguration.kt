package co.lopun.core.config

import com.zaxxer.hikari.HikariDataSource
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import org.jooq.ConnectionProvider
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.TransactionProvider
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource


@EnableR2dbcRepositories
@Configuration
class DatabaseConfiguration {
  /**
   * == properties from application.yml ====================================
   */
  @Value("\${spring.datasource.master.url}")
  private lateinit var jdbcMasterUrl: String

  @Value("\${spring.datasource.master.url}")
  private lateinit var jdbcReplicaUrl: String

  @Value("\${spring.datasource.driver-class-name}")
  private lateinit var jdbcDriverClassName: String

  @Value("\${custom.r2dbc.url.master}")
  private lateinit var r2dbcMasterUrl: String

  @Value("\${custom.r2dbc.url.replica}")
  private lateinit var r2dbcReplicaUrl: String

  @Value("\${spring.datasource.username}")
  private lateinit var commonUserName: String

  @Value("\${spring.datasource.password}")
  private lateinit var commonPassword: String

  /**
   * == jooq configuration ====================================
   */
  @Primary
  @Bean
  fun dataSourceMaster(): DataSource = HikariDataSource().apply {
    driverClassName = jdbcDriverClassName
    username = commonUserName
    password = commonPassword
    jdbcUrl = jdbcMasterUrl
  }

  @Primary
  @Bean
  fun transactionManagerMaster(): DataSourceTransactionManager {
    return DataSourceTransactionManager(dataSourceMaster())
  }

  @Primary
  @Bean
  fun transactionProviderMaster(): TransactionProvider {
    return SpringTransactionProvider(transactionManagerMaster())
  }

  @Primary
  @Bean
  fun connectionProviderMaster(): ConnectionProvider {
    return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSourceMaster()))
  }

  @Primary
  @Bean
  fun dslMaster(): DSLContext {
    val configuration = DefaultConfiguration()
      .derive(connectionProviderMaster())
      .derive(transactionProviderMaster())
      .derive(SQLDialect.MYSQL)
    return DefaultDSLContext(configuration)
  }

  @Bean
  fun dataSourceReplica(): DataSource = HikariDataSource().apply {
    driverClassName = jdbcDriverClassName
    username = commonUserName
    password = commonPassword
    jdbcUrl = jdbcReplicaUrl
  }

  @Bean
  fun transactionManagerReplica(): DataSourceTransactionManager {
    return DataSourceTransactionManager(dataSourceReplica())
  }

  @Bean
  fun transactionProviderReplica(): TransactionProvider {
    return SpringTransactionProvider(transactionManagerReplica())
  }

  @Bean
  fun connectionProviderReplica(): ConnectionProvider {
    return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSourceReplica()))
  }

  @Bean
  fun dslReplica(): DSLContext {
    val configuration = DefaultConfiguration()
      .derive(connectionProviderReplica())
      .derive(transactionProviderReplica())
      .derive(SQLDialect.MYSQL)
    return DefaultDSLContext(configuration)
  }

  /**
   * == r2dbc configuration ====================================
   */

  @Bean
  fun reactiveDataAccessStrategy(): ReactiveDataAccessStrategy? {
    return DefaultReactiveDataAccessStrategy(MySqlDialect())
  }

  @Bean
  fun connectionFactoryMaster(): ConnectionFactory {
    return ConnectionFactories.get(r2dbcMasterUrl)
  }

  @Bean
  fun databaseClientMaster(): DatabaseClient {
    return DatabaseClient.create(connectionFactoryMaster())
  }

  @Bean
  fun connectionFactoryReplica(): ConnectionFactory = ConnectionPool(
    ConnectionPoolConfiguration.builder()
      .connectionFactory(ConnectionFactories.get(r2dbcReplicaUrl))
      .initialSize(10)
      .maxSize(100)
      .validationQuery("SELECT 1")
      .build()
  )

  @Bean
  fun databaseClientReplica(): DatabaseClient {
    return DatabaseClient.create(connectionFactoryReplica())
  }
}
