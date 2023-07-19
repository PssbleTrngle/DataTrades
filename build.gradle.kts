plugins {
    idea
    id("net.somethingcatchy.gradle") version ("0.0.6")
}

subprojects {
    enablePublishing {
        repositories {
            githubPackages(this@subprojects)
        }
    }

    tasks.withType<Jar> {
        exclude("datapacks")
    }
}

enableSonarQube()