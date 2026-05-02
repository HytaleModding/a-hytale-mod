import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * NOTE: This is entirely optional and basics can be done in `settings.gradle.kts`
 */

val hytaleServerVersion = "2026.03.26-89796e57b"

tasks.named("generateManifest") {
    doLast {
        val manifestFile = file("src/main/resources/manifest.json")
        @Suppress("UNCHECKED_CAST")
        val manifest = JsonSlurper().parse(manifestFile) as MutableMap<String, Any?>
        manifest["IncludesAssetPack"] = true
        manifest["ServerVersion"] = hytaleServerVersion
        val json = JsonOutput.prettyPrint(JsonOutput.toJson(manifest))
            .replace(Regex("\\[\\s+]"), "[]")
        manifestFile.writeText(json + System.lineSeparator())
    }
}

repositories {
    // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
}

dependencies {
    // Any external dependency you also want to include
}
