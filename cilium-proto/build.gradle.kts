import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    java
    id("com.google.protobuf")
}

group = "dev.strrl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    api("com.google.protobuf:protobuf-java:3.21.12")
    api("io.grpc:grpc-all:1.52.1")
    api("javax.annotation:javax.annotation-api:1.3.2")

    // Extra proto source files besides the ones residing under
    // "src/main".
//    protobuf(files("lib/protos.tar.gz"))
//    protobuf(files("ext/"))
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
java {
    sourceSets.getByName("main").resources.srcDir("src/main/proto")
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.54.1"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}
