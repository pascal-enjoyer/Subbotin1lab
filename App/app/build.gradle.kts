plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.checkstyle)
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

checkstyle {
    toolVersion = "10.18.0"
    configFile = rootProject.file("checkstyle.xml")
    isIgnoreFailures = false
    isShowViolations = true
}

tasks.withType<com.puppycrawl.tools.checkstyle.CheckstyleTask>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.get().destination = file("$buildDir/reports/checkstyle/checkstyle.html")
    }
}