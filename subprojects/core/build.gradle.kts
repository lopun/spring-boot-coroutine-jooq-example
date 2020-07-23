import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  id(Plugin.jooq) version "4.2" apply false
}

System.setProperty("jooqVersion", Ver.jooq)
JooqConfig.run {
  System.setProperty("jooqURL", jooqUrl)
  System.setProperty("jooqUser", jooqUser)
  System.setProperty("jooqPassword", jooqPassword)
}
System.setProperty("generatorDatabaseIncludes", ".*")
System.setProperty("jooqPackageName", "co.lopun.core.model.gen")

apply(plugin = Plugin.jooq)
apply(from = "$rootDir/buildSrc/src/main/resources/useJooq.gradle")

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
  implementation(springBoot("spring-boot-starter-jooq"))
}
