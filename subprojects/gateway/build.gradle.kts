import org.springframework.boot.gradle.tasks.run.BootRun
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME

tasks {
  withType<BootRun> {
    systemProperties = System.getProperties()
      .plus(IO_PARALLELISM_PROPERTY_NAME to 512)
      .map { (key, value) -> (key as String) to value }
      .toMap()
  }
}

dependencies {
  implementation(project(":subprojects:core"))
  implementation(project(":subprojects:biz"))
}
