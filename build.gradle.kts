plugins {
    idea
    id("com.possible-triangle.gradle") version ("0.2.4")
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