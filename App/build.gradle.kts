plugins {
    alias(libs.plugins.android.application) apply false
    id("checkstyle") apply false  // Убрана версия
}

subprojects {
    apply(plugin = "checkstyle")

    configure<CheckstyleExtension> {
        toolVersion = "10.12.1"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = false
    }

    tasks.register<Checkstyle>("checkstyleMain") {
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        source = fileTree("src/main/java")
        include("**/*.java")
        classpath = files()
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}