neoforge {
    dependOn(project(":common"))
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}