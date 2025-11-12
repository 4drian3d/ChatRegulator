import java.time.LocalDate

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.35.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    compileOnlyApi(libs.adventure.api)
    compileOnly(libs.caffeine)
    compileOnlyApi(libs.checkerqual)
}

tasks {
    compileJava {
        options.release.set(21)
        options.encoding = "UTF-8"
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/",
            "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/",
            "https://javadoc.io/doc/org.jetbrains/annotations/24.0.1/index.html",
        )
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "chatregulator-api", project.version as String)

    pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set(LocalDate.now().year.toString())
        url.set("https://github.com/4drian3d/ChatRegulator")
        licenses {
            license {
                name.set("GNU General Public License version 3 or later")
                url.set("https://opensource.org/licenses/GPL-3.0")
            }
        }
        developers {
            developer {
                id.set("4drian3d")
                name.set("Adrian Gonzales")
                email.set("adriangonzalesval@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/4drian3d/ChatRegulator.git")
            developerConnection.set("scm:git:ssh://git@github.com/4drian3d/ChatRegulator.git")
            url.set("https://github.com/4drian3d/ChatRegulator")
        }
        ciManagement {
            name.set("GitHub Actions")
            url.set("https://github.com/4drian3d/ChatRegulator")
        }
        issueManagement {
            name.set("GitHub")
            url.set("https://github.com/4drian3d/ChatRegulator/issues")
        }
    }
}

