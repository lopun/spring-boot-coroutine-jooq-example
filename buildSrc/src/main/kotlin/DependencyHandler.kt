import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.springBoot(
  module: String
): String = "org.springframework.boot:$module"

fun DependencyHandlerScope.springBootDependencies(): String =
  "org.springframework.boot:spring-boot-dependencies:${Ver.springBoot}"

fun DependencyHandlerScope.gson(): String = "com.google.code.gson:gson:${Ver.gson}"

fun DependencyHandlerScope.mysqlConnectorJava(): String = "mysql:mysql-connector-java"

fun DependencyHandlerScope.kotlinx(module: String): String = "org.jetbrains.kotlinx:$module:${Ver.kotlinx}"
