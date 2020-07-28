import org.springframework.boot.gradle.tasks.run.BootRun

tasks {
  withType<BootRun> {
    systemProperties = System.getProperties()
      .map { (key, value) -> (key as String) to value }
      .toMap()
  }
}

dependencies {
  implementation(project(":subprojects:common"))
  implementation(project(":subprojects:core"))
  implementation(project(":subprojects:biz"))
}
