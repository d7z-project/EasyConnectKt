import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.beryx.jlink")
    application
}
java.sourceCompatibility = JavaVersion.VERSION_17

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileKotlin.destinationDirectory.set(compileJava.destinationDirectory.get())

java {
    modularity.inferModulePath.set(true)
}

application {
    // 启动类配置
    mainModule.set("org.d7z.cli")
    mainClass.set("org.d7z.cli.MainKt")
}

dependencies {
    implementation(project(":net-core"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation(libs.toml.get4koma())
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

jlink {
    options.set(
        listOf(
            "--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"
        )
    )
    launcher {
        name = "easy-connect"
        jvmArgs = listOf()
    }
}