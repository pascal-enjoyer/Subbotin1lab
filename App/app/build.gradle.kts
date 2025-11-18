plugins {
    alias(libs.plugins.android.application)
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Checkstyle configuration for app module
checkstyle {
    toolVersion = "10.12.1"
    config = resources.text.fromUri("https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml")
    isIgnoreFailures = false
}

tasks.register<Checkstyle>("checkstyle") {
    group = "verification"
    description = "Run checkstyle on main source set"
    source = fileTree("src/main/java")
    include("**/*.java")
    classpath = files()
}