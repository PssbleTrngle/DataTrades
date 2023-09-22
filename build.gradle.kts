plugins {
    idea
    id("net.somethingcatchy.gradle") version ("0.0.7")
}

subprojects {
    enablePublishing {
        repositories {
            githubPackages(this@subprojects)
        }
    }
}

allprojects {
    tasks.withType<Jar> {
        exclude("datapacks")
    }
}

enableSonarQube()