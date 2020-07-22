import org.springframework.boot.gradle.tasks.bundling.BootJar

System.setProperty("jooqVersion", Ver.jooq)
JooqConfig.run {
  System.setProperty("jooqURL", jooqUrl)
  System.setProperty("jooqUser", jooqUser)
  System.setProperty("jooqPassword", jooqPassword)
}
System.setProperty("generatorDatabaseIncludes", ".*")
System.setProperty("jooqPackageName", "co.lopun.core.model")

apply(from = "$rootDir/buildSrc/src/main/resources/useJooq.gradle")

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
