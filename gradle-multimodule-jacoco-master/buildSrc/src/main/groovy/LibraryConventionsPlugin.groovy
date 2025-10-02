import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.JavaVersion

class LibraryConventionsPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Apply necessary plugins
        project.plugins.apply('java-library')
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

        // Configure JaCoCo with correct version
        project.jacoco {
            toolVersion = "0.8.11"
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
            // Add Lombok with compatible version
            project.getConfigurations().getByName("compileOnly").getDependencies().add(
                    project.getDependencies().create('org.projectlombok:lombok:1.18.30')
            )
            project.getConfigurations().getByName("annotationProcessor").getDependencies().add(
                    project.getDependencies().create('org.projectlombok:lombok:1.18.30')
            )

            // Test dependencies with updated versions
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.junit.jupiter:junit-jupiter:5.10.0')
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.mockito:mockito-core:5.17.0')
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.mockito:mockito-junit-jupiter:5.3.1')
            )
            project.getConfigurations().getByName("testImplementation").getDependencies().add(
                    project.getDependencies().create('org.assertj:assertj-core:3.24.2')
            )
        }
    }
}