val mod_name: String by extra

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }

        System.getenv()["LOCAL_MAVEN"]?.let {
            maven { url = uri(it) }
        }
    }
}

rootProject.name = mod_name
include("common", "fabric", "forge")