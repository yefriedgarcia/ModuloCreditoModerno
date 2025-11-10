plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
}

android {
    namespace = "com.garcia.modulocredito"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.garcia.modulocredito"
        minSdk = 26
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
        debug {
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

afterEvaluate {
    tasks.withType<Test>().configureEach {
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    tasks.register<JacocoReport>("jacocoTestReport") {
        val buildDir = layout.buildDirectory.get().asFile

        dependsOn(tasks.withType<Test>())

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        classDirectories.setFrom(
            fileTree("$buildDir/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class",
                    "**/R\$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*"
                )
            }
        )
        sourceDirectories.setFrom(files("src/main/java"))
        executionData.setFrom(
            fileTree(buildDir) {
                include("**/jacoco/test*.exec", "**/outputs/unit_test_code_coverage/**/*.ec")
            }
        )
    }
}


dependencies {
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.koin)
    implementation(libs.coroutines)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}