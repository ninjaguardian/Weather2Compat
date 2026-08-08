plugins {
    id("net.neoforged.moddev") version "2.0.143"
}

val javaVersion = providers.gradleProperty("java_version")
val mcVersion = providers.gradleProperty("minecraft_version")
val neoVersion = providers.gradleProperty("neo_version")
val loaderVersionRange = providers.gradleProperty("loader_version_range")
val modId = providers.gradleProperty("mod_id")
val modName = providers.gradleProperty("mod_name")
val modLicense = providers.gradleProperty("mod_license")
val modVersion = providers.gradleProperty("mod_version")
val modGroup = providers.gradleProperty("mod_group_id")
val modAuthors = providers.gradleProperty("mod_authors")
val modDescription = providers.gradleProperty("mod_description")
val weatherVersion = providers.gradleProperty("weather_version")

version = modVersion.get()
group = modGroup.get()

base {
    archivesName.set(modId)
}

java {
    val java = JavaVersion.toVersion(javaVersion.get())
    targetCompatibility = java
    sourceCompatibility = java

    toolchain {
        languageVersion.set(javaVersion.map(JavaLanguageVersion::of))
    }

    withSourcesJar()
}

neoForge {
    version = neoVersion.get()

    runs {
        create("client") {
            client()
        }

        create("server") {
            server()
        }
    }

    mods {
        create(modId.get()) {
            sourceSet(sourceSets.main.get())
        }
    }

    parchment {
        minecraftVersion.set(mcVersion)
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven") {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    implementation("maven.modrinth:weather-storms-tornadoes:${weatherVersion.get()}")
}

tasks.processResources {
    val replaceProperties = mapOf(
        "minecraft_version" to mcVersion,
        "neo_version" to neoVersion,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription,
        "weather_version" to weatherVersion,
        "java_version" to javaVersion
    )

    inputs.properties(replaceProperties)

    filteringCharset = "UTF-8"

    filesMatching(listOf("*.mixins.json", "META-INF/neoforge.mods.toml")) {
        expand(replaceProperties.mapValues { it.value.get() })
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    from("LICENSE")
}
