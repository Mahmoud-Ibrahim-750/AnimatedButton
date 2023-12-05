pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

//buildscript {
//    repositories {
//        jcenter()
//    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:3.2.0")
//        classpath("com.github.dcendents:android-maven-gradle-plugin:1.5")
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}

/*
buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
 */

rootProject.name = "AnimatedButton"
include(":app")
include(":animatedbutton")
