rootProject.name = "A-Hytale-Mod"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.15-dev7"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline(System.getenv("HYTALE_TAG") ?: "release")
    useVersion("latest")

    repositories {
        // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
    }

    dependencies {
        // Any external dependency you also want to include
    }

    manifest {
        Group = "dev.hytalemodding"
        Name = "AHytaleMod"
        Main = "dev.hytalemodding.AHytaleMod"
        Version = System.getenv("MOD_VERSION") ?: "0.0.1-SNAPSHOT"
    }
}
