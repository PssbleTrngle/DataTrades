plugins {
    idea
    id("com.possible-triangle.gradle") version ("0.1.0")
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