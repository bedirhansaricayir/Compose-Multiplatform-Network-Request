plugins {
    kotlin("multiplatform")
    //kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.21"

}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation("media.kamel:kamel-image:0.7.3")
                implementation("io.ktor:ktor-client-core:2.3.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                api("dev.icerock.moko:mvvm-core:0.16.1") // only ViewModel, EventsDispatcher, Dispatchers.UI
                api("dev.icerock.moko:mvvm-compose:0.16.1") // api mvvm-core, getViewModel for Compose Multiplatfrom



            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.3.1")

            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.1")
                dependsOn(commonMain)
                iosX64Main.dependsOn
                iosArm64Main.dependsOn
                iosSimulatorArm64Main.dependsOn
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.migren.composemultiplatform"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
}