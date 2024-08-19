neoforge {
    dependOn(project(":common"))
}

tasks.test {
    enabled = false
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}