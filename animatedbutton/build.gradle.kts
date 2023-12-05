plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

group = "com.github.Mahmoud-Ibrahim-750"
version = "v1.0.1"

android {
    namespace = "com.mis.animatedbutton"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        aarMetadata {
            minCompileSdk = 21
        }



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


//group = "com.mis"
//version = "1.0.0"
//
//task sourceJar(type: Jar) {
//    from sourceSets.main.allJava
//}
//
//publishing {
//    publications {
//        mavenJava(MavenPublication) {
//            artifactId = 'huka-common'
//            from components.java
//                    versionMapping {
//                        usage('java-api') {
//                            fromResolutionOf('runtimeClasspath')
//                        }
//                        usage('java-runtime') {
//                            fromResolutionResult()
//                        }
//                    }
//            pom {
//                name = 'Huka common files'
//                description = 'Huka common files'
//                licenses {
//                    license {
//                        name = 'The Apache License, Version 2.0'
//                        url = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
//                    }
//                }
//                developers {
//                    developer {
//                        id = 'huka'
//                        name = 'Huka'
//                        email = 'mr.kharhums@gmail.com'
//                    }
//                }
//            }
//        }
//    }
//    repositories {
//        maven {
//            def releasesRepoUrl = layout.buildDirectory.dir('repos/releases')
//            def snapshotsRepoUrl = layout.buildDirectory.dir('repos/snapshots')
//            url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
//        }
//    }
//}



dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}



//afterEvaluate {
//    publishing {
//        publications {
//            // Creates a Maven publication called "release".
//            release(MavenPublication) {
//                from components.release
//                        groupId = 'com.github.jitpack'
//                artifactId = 'android-example'
//                version = '1.0'
//            }
//        }
//    }
//}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.github.Mahmoud-Ibrahim-750"
            artifactId = "animated-button"
            version = "v1.0.1"
        }
    }
}
//afterEvaluate {
//    publishing {
//        publications {
//            register<MavenPublication>("release") {
//                groupId = "com.github.Mahmoud-Ibrahim-750"
//                artifactId = "animated-button"
//                version = "1.0.0"
//
//                afterEvaluate {
//                    from(components["release"])
//                }
//            }
//        }
//    }

//    publishing {
//        publications {
//            release(MavenPublication) {
//                from components.release
//
//                        groupId 'com.tazkiyatech'
//                artifactId 'android-utils'
//                version '1.0.0'
//            }
//        }
//
//        repositories {
//            maven {
//                name = 'BuildFolder'
//                url = "${project.buildDir}/repository"
//            }
//        }
//    }
//}