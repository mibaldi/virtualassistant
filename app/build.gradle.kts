import com.mibaldi.virtualassistant.buildsrc.Libs
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mibaldi.virtualassistant"
    compileSdk = 34
    signingConfigs {
        create("api") {
            storeFile = file("D:\\PROGRAMACION\\CLAVES\\mibaldicalendar.jks")
            storePassword = "mibaldicalendar"
            keyAlias = "mibaldicalendar"
            keyPassword = "mibaldicalendar"
        }
    }
    defaultConfig {
        applicationId = "com.mibaldi.virtualassistant"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            signingConfig =  signingConfigs.getByName("api")
        }
        release {
            signingConfig =  signingConfigs.getByName("api")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":usecases"))



    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.recyclerView)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.Activity.ktx)
    implementation(Libs.AndroidX.Lifecycle.viewmodelKtx)
    implementation(Libs.AndroidX.Lifecycle.runtimeKtx)
    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    kapt(Libs.AndroidX.Room.compiler)
    implementation(Libs.Glide.glide)
    kapt(Libs.Glide.compiler)
    implementation(Libs.OkHttp3.loginInterceptor)
    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.converterGson)
    implementation(Libs.Arrow.core)

    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)

    implementation (Libs.AndroidX.Compose.UI.ui)
    implementation (Libs.AndroidX.Compose.UI.toolingPreview)
    implementation (Libs.AndroidX.Compose.Material3.material3)
    implementation (Libs.AndroidX.Compose.Material3.materialIconsExtended)
    implementation (Libs.AndroidX.Activity.compose)
    implementation (Libs.AndroidX.Lifecycle.viewmodelCompose)
    implementation (Libs.AndroidX.Navigation.compose)
    implementation (Libs.Coil.compose)
    implementation (Libs.Coil.gif)
    implementation (Libs.Hilt.navigationCompose)
    debugImplementation (Libs.AndroidX.Compose.UI.tooling)
    implementation("androidx.credentials:credentials:1.2.0-beta01")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0-beta01")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
    //for google auth and calendar integrations
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    implementation ("com.google.apis:google-api-services-calendar:v3-rev305-1.23.0")

    //to avoid conflicts in libraries
    implementation ("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    implementation("com.google.api-client:google-api-client-android:1.23.0") {
        exclude ("org.apache.httpcomponents")
    }

    //so that we can easily control permissions
    implementation ("pub.devrel:easypermissions:3.0.0")


    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    /*testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")*/
}