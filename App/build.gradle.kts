// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}

subprojects {
    apply(plugin = "checkstyle")

    configure<CheckstyleExtension> {
        toolVersion = "10.12.1"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = false
    }

    tasks.register<Checkstyle>("checkstyle") {
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        source = fileTree("src/main/java")
        include("**/*.java")
        classpath = files()
    }
}