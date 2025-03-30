plugins {
    id("java")
}

group = "ru.lewis"
version = "1.2.1-SNAPSHOT"

repositories {
    mavenCentral()
    // paper
    maven("https://repo.papermc.io/repository/maven-public/")
    // worldguard
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${Versions.PAPER}")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:${Versions.WORLD_GUARD}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(23))
}

object Versions {
    const val PAPER = "1.19.2-R0.1-SNAPSHOT"
    const val WORLD_GUARD = "7.0.5";
}