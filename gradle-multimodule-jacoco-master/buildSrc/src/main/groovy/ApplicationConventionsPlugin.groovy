import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.JavaVersion

class ApplicationConventionsPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Apply necessary plugins
        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('io.spring.dependency-management')
        project.plugins.apply('java')
        project.plugins.apply('jacoco')

        // Set up Java conventions
        project.java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        // Configure test tasks
        project.tasks.withType(Test) {
            useJUnitPlatform()
            testLogging {
                events "passed", "skipped", "failed"
            }
            finalizedBy project.tasks.jacocoTestReport
        }

        // Configure JaCoCo - updated to latest version compatible with Java 21
        project.jacoco {
            toolVersion = "0.8.11" // Fixed: JaCoCo doesn't have version 0.8.12, the latest is 0.8.11
        }

        project.jacocoTestReport {
            reports {
                xml.required = true
                html.required = true
            }
            dependsOn project.test
        }

        // Add Lombok configuration to exclude generated code from JaCoCo
        project.afterEvaluate {
            def lombokConfig = new File(project.projectDir, "lombok.config")
            if (!lombokConfig.exists()) {
                lombokConfig.text = """
config.stopBubbling = true
lombok.addLombokGeneratedAnnotation = true
"""
            }
        }

        // Apply common dependencies with updated versions
        project.dependencies {
            // Spring Boot dependencies
            project.getConfigurations().getByName("implementation").getDependencies().add(
                    project.getDependencies().create('org.springframework.boot:spring-boot-starter')
            )
            project.getConfigurations().getByName("implementation").getDependencies().add(
                    project.getDependencies().create('org.springframework.boot:spring-boot-starter-web')
            )
            project.getConfigurations().getByName("implementation").getDependencies().add(
                    project.getDependencies().create('org.springframework.boot:spring-boot-starter-actuator')
            )

            // Add Lombok with compatible version
            project.getConfigurations().getByName("compileOnly").getDependencies().add(
                    project.getDependencies().create('org.projectlombok:lombok:1.18.30')
            )
            project.getConfigurations().getByName("annotationProcessor").getDependencies().add(
                    project.getDependencies().create('org.projectlombok:lombok:1.18.30')
            )

            // Test dependencies with updated versions
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.springframework.boot:spring-boot-starter-test')
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.junit.jupiter:junit-jupiter:5.10.0') // Updated from 5.9.2
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.assertj:assertj-core:3.24.2')
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.mockito:mockito-core:5.17.0') // Added explicit mockito dependency
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.testcontainers:junit-jupiter:1.19.3') // Updated from 1.17.6
            )
        }
    }
}