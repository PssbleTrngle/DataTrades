plugins {
    idea
    id("com.possible-triangle.gradle") version ("0.0.0-dev")
}

subprojects {
    enablePublishing {
        githubPackages()
    }
}

allprojects {
    tasks.withType<Jar> {
        exclude("datapacks")
    }
}

enableSonarQube()