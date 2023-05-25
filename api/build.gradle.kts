plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnlyApi(libs.velocity)
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.advntr.dev/api/4.13.1/",
            "https://jd.advntr.dev/text-minimessage/4.13.1/",
            "https://jd.papermc.io/velocity/3.0.0/",
            "https://checkerframework.org/api/",
            "https://javadoc.io/doc/org.jetbrains/annotations/24.0.1/index.html",
        )
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            repositories {
//                maven {
//                    credentials(PasswordCredentials::class)
//                    val central = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//                    val snapshots = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
//                    if (project.version.toString().endsWith("SNAPSHOT")) {
//                        name = "SonatypeSnapshots"
//                        setUrl(snapshots)
//                    } else {
//                        name = "OSSRH"
//                        setUrl(central)
//                    }
//                }
//            }
//            from(components["java"])
//            pom {
//                url.set("https://github.com/4drian3d/ChatRegulator")
//                licenses {
//                    license {
//                        name.set("GNU General Public License version 3 or later")
//                        url.set("https://opensource.org/licenses/GPL-3.0")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:https://github.com/4drian3d/ChatRegulator.git")
//                    developerConnection.set("scm:git:ssh://git@github.com/4drian3d/ChatRegulator.git")
//                    url.set("https://github.com/4drian3d/ChatRegulator")
//                }
//                developers {
//                    developer {
//                        id.set("4drian3d")
//                        name.set("Adrian Gonzales")
//                        email.set("adriangonzalesval@gmail.com")
//                    }
//                }
//                issueManagement {
//                    name.set("GitHub")
//                    url.set("https://github.com/4drian3d/ChatRegulator/issues")
//                }
//                ciManagement {
//                    name.set("GitHub Actions")
//                    url.set("https://github.com/4drian3d/ChatRegulator/actions")
//                }
//                name.set(project.name)
//                description.set(project.description)
//                url.set("https://github.com/4drian3d/ChatRegulator")
//            }
//            artifactId = "chatregulator-api"
//        }
//    }
//}
//signing {
//    useGpgCmd()
//    sign(configurations.archives.get())
//    sign(publishing.publications["maven"])
//}
