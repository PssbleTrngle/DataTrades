forge {
    enableMixins()

    dependOn(project(":common"))
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}