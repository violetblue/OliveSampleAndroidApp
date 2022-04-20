import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    id("org.graalvm.buildtools.native") version "0.9.7.1"
    application
}

group = "com.kakao.olive"
version = "0.6.0"

repositories {
    mavenCentral()
//    mavenLocal()
    gradlePluginPortal()
    maven {
        url = uri("https://repo.daumkakao.io/content/groups/kakao-osa-group")
        isAllowInsecureProtocol = true
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("info.picocli:picocli:4.6.2")
    implementation("info.picocli:picocli-shell-jline2:4.6.2")
    kapt("info.picocli:picocli-codegen:4.6.2")

    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")

    //Logger
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("ch.qos.logback:logback-core:1.2.10")

    implementation("khttp:khttp:1.0.0")

    testImplementation(kotlin("test"))

    //Apache commons Lang 3
    implementation("org.apache.commons:commons-lang3:3.11")

    //Apache commons Exec
    implementation("org.apache.commons:commons-exec:1.3")

    //Toml Parser
    implementation("com.moandjiezana.toml:toml4j:0.7.2")

    // Use JUnit test framework
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito:mockito-core:3.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.mockito:mockito-inline:2.13.0")

    testImplementation("org.assertj:assertj-core:3.12.2")

    // CLI 테이블 출력 라이브러리
    implementation("de.m3y.kformat:kformat:0.9")

    // olive-analyzer
    implementation("com.kakao.osa:olive-analyzer:1.17.0") {
        exclude(group = "org.apache.logging.log4j")
    }

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

application {
    mainClass.set("com.kakao.olive.OliveCliCommand")
}


kapt {
    correctErrorTypes = true
    arguments {
        arg("project", "${project.group}/${project.name}")
    }

}

// native-gradle-plugin : https://graalvm.github.io/native-build-tools/0.9.7.1/index.html
graalvmNative {
    binaries {
        named("main") {
            // Main options
            imageName.set("olive-cli") // The name of the native image, defaults to the project name
            mainClass.set("com.kakao.olive.OliveCliCommand") // The main class to use, defaults to the application.mainClass
            debug.set(true) // Determines if debug info should be generated, defaults to false
            verbose.set(true) // Add verbose output, defaults to false
            fallback.set(true) // Sets the fallback mode of native-image, defaults to false
            sharedLibrary.set(false) // Determines if image is a shared library, defaults to false if `java-library` plugin isn't included

            //systemProperties.putAll(mapOf(name1 to "value1", name2 to "value2")) // Sets the system properties to use for the native image builder
            //configurationFileDirectories.from(file("resources/main/reflect-config.json")) // Adds a native image configuration file directory, containing files like reflection configuration

            // Advanced options
            buildArgs.add("-H:-CheckToolchain")
            buildArgs.add("--enable-https")
            buildArgs.add("--enable-http")
            buildArgs.add("-H:ReflectionConfigurationFiles=../../resources/main/reflect-config.json")

            // Development options
            agent.set(true) // Enables the reflection agent. Can be also set on command line using '-Pagent'

            useFatJar.set(true) // Instead of passing each jar individually, builds a fat jar
        }
    }
}
