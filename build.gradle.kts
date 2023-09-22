plugins {
    idea
    id("com.possible-triangle.gradle") version ("0.1.1")
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