package co.lopun.core.config

import com.zaxxer.hikari.HikariDataSource
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JooqConfiguration {
  @Value("\${spring.datasource.master.url}")
  private lateinit var masterUrl: String

  @Value("\${spring.datasource.master.url}")
  private lateinit var replicaUrl: String

  @Value("\${spring.datasource.driver-class-name}")
  private lateinit var commonDriverClassName: String

  @Value("\${spring.datasource.username}")
  private lateinit var commonUserName: String

  @Value("\${spring.datasource.password}")
  private lateinit var commonPassword: String

  @Primary
  @Bean
  fun dataSourceMaster(): DataSource = HikariDataSource().apply {
    driverClassName = commonDriverClassName
    username = commonUserName
    password = commonPassword
    jdbcUrl = masterUrl
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
    driverClassName = commonDriverClassName
    username = commonUserName
    password = commonPassword
    jdbcUrl = replicaUrl
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
}
