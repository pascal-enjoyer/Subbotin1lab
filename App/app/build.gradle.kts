plugins {
    alias(libs.plugins.android.application)
    id("checkstyle")
}

android {
    namespace = "com.example.morse_messenger"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.morse_messenger"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("test") {
            java.srcDirs("src/test/java")
        }
        getByName("androidTest") {
            java.srcDirs("src/androidTest/java")
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

checkstyle {
    toolVersion = "10.18.0"
    configFile = rootProject.file("checkstyle.xml")
    isIgnoreFailures = false
    isShowViolations = true
}

val checkstyleMain by tasks.registering(org.gradle.api.plugins.quality.Checkstyle::class) {
    group = "verification"
    description = "Run Checkstyle on main source set"

    source = fileTree("src/main/java") {
        include("**/*.java")
    }
    classpath = files()

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.file("reports/checkstyle/main.html"))
    }
}

val checkstyleTest by tasks.registering(org.gradle.api.plugins.quality.Checkstyle::class) {
    group = "verification"
    description = "Run Checkstyle on test source set"

    source = fileTree("src/test/java") {
        include("**/*.java")
    }
    classpath = files()

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.file("reports/checkstyle/test.html"))
    }
}

val checkstyleAndroidTest by tasks.registering(org.gradle.api.plugins.quality.Checkstyle::class) {
    group = "verification"
    description = "Run Checkstyle on androidTest source set"

    source = fileTree("src/androidTest/java") {
        include("**/*.java")
    }
    classpath = files()

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.file("reports/checkstyle/androidTest.html"))
    }
}

val checkstyleAll by tasks.registering {
    group = "verification"
    description = "Run all Checkstyle tasks"
    dependsOn(checkstyleMain, checkstyleTest, checkstyleAndroidTest)
}

tasks.named("check") {
    dependsOn(checkstyleAll)
}