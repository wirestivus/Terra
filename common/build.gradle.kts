import com.dfsek.terra.configureCommon
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.expr.StringLiteralExpr
import com.github.javaparser.ast.type.PrimitiveType
import com.github.javaparser.ast.type.PrimitiveType.Primitive
import com.github.javaparser.ast.type.Type

plugins {
    `java-library`
    `maven-publish`
}

buildscript {
    dependencies {
        classpath("com.github.javaparser:javaparser-symbol-solver-core:3.20.2")
    }
}

configureCommon()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.4")

    "shadedApi"("com.dfsek:Paralithic:0.3.2")
    "shadedApi"("com.dfsek:Tectonic:1.2.3")
    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")
    "shadedApi"("org.ow2.asm:asm:9.0")
    "shadedApi"("commons-io:commons-io:2.6")

    "shadedApi"("com.googlecode.json-simple:json-simple:1.1.1")
    "shadedApi"("org.yaml:snakeyaml:1.27")

    "compileOnly"("com.google.guava:guava:30.0-jre")

    "testImplementation"("com.google.guava:guava:30.0-jre")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }

    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"
        val mavenSnapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"

        maven(mavenUrl) {
            val mavenUsername: String? by project
            val mavenPassword: String? by project
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

sourceSets {
    create("tectonic") {

    }
}

tasks.create<SourceTask>("tectonicDocs") {
    group = "terra"
    println("Scanning sources...")


    val docs = HashMap<String, String>()

    val refactor = HashMap<String, String>()

    val sources = HashMap<String, CompilationUnit>()

    sourceSets.main.get().java.forEach {
        sources[it.name.substring(0, it.name.length - 5)] = StaticJavaParser.parse(it)
    }

    sources.forEach { (name, unit) ->
        unit.getClassByName(name).ifPresent { declaration ->
            if (declaration.isAnnotationPresent("AutoDocAlias")) {
                refactor[name] = (declaration.getAnnotationByName("AutoDocAlias").get().childNodes[1] as StringLiteralExpr).asString()
                println("Refactoring $name to ${refactor[name]}.")
            }
        }
    }

    val children = HashMap<String, MutableList<com.github.javaparser.ast.Node>>()
    sources.forEach { (name, unit) ->
        unit.getClassByName(name).ifPresent { declaration ->
            declaration.extendedTypes.forEach { classOrInterfaceType ->
                val inherit = classOrInterfaceType.name.asString()
                if (!children.containsKey(inherit)) children[inherit] = ArrayList()
                children[inherit]!!.add(declaration.name)
            }
        }
    }
    sources.forEach { (name, unit) ->
        val doc = StringBuilder()
        doc.append("# ${generify(name, refactor)}\n")

        unit.getClassByName(name).ifPresent { declaration ->
            declaration.javadoc.ifPresent {
                doc.append("${sanitizeJavadoc(it.toText())}    \n")
            }
            declaration.extendedTypes.forEach {
                if (!it.name.asString().equals("AbstractableTemplate")) {
                    doc.append("Inherits from ${parseTypeLink(it, refactor)}    \n")
                }
            }
            if (children.containsKey(name)) {
                doc.append("Children: \n")
                children[name]!!.forEach {
                    doc.append("* ${parseTypeLink(it, refactor)}\n")
                }
                doc.append("    \n\n")
            }
            doc.append("\n")
        }

        var applicable = false
        unit.findAll(FieldDeclaration::class.java).filter { it.isAnnotationPresent("Value") }.forEach { fieldDeclaration ->
            doc.append("## ${(fieldDeclaration.getAnnotationByName("Value").get().childNodes[1] as StringLiteralExpr).asString()}\n")

            if (fieldDeclaration.isAnnotationPresent("Default")) {
                doc.append("* Default value: ${fieldDeclaration.variables[0]}    \n")
            }
            val type = fieldDeclaration.commonType
            doc.append("* Type: ${parseTypeLink(type, refactor)}    \n")
            doc.append("\n")

            fieldDeclaration.javadoc.ifPresent {
                doc.append(sanitizeJavadoc(it.toText()))
            }
            doc.append("\n\n")
            applicable = true
        }
        val s = doc.toString()
        if (s.isNotEmpty() && applicable) {
            docs[generify(name, refactor)] = s
        }
    }
    println("Done. Generated ${docs.size} files")

    val docsDir = File(buildDir, "tectonic")
    docsDir.mkdirs()
    docs.forEach {
        val save = File(docsDir, "${it.key}.md")
        if (save.exists()) save.delete()
        save.createNewFile()
        save.writeText(it.value)
    }

    sourceSets["tectonic"].resources.forEach {
        it.copyTo(File(docsDir, it.name), true)
    }
}

fun parseTypeLink(type: com.github.javaparser.ast.Node, refactor: Map<String, String>): String {
    val st = parseType(type, refactor)

    if (st.contains('<')) {
        val outer = generify(type.childNodes[0].toString(), refactor)

        val builder = StringBuilder()
        builder.append("[$outer](./$outer)\\<")

        for (i in 1 until type.childNodes.size) {
            builder.append(parseTypeLink(type.childNodes[i] as Type, refactor))
            if (i != type.childNodes.size - 1) builder.append(", ")
        }

        builder.append("\\>")

        return builder.toString()
    }
    return "[$st](./$st)"
}

fun parseType(type: com.github.javaparser.ast.Node, refactor: Map<String, String>): String {
    if (type is PrimitiveType) {
        return when (type.type) {
            Primitive.BOOLEAN -> "Boolean"
            Primitive.BYTE -> "Byte"
            Primitive.DOUBLE -> "Double"
            Primitive.INT -> "Integer"
            Primitive.CHAR -> "Char"
            Primitive.FLOAT -> "Float"
            Primitive.SHORT -> "Short"
            Primitive.LONG -> "Long"
            else -> type.asString()
        }
    }
    return generify(type.toString(), refactor)
}

fun generify(type: String, refactor: Map<String, String>): String {
    return when (type) {
        "HashMap", "LinkedHashMap" -> "Map"
        "ArrayList", "LinkedList", "GlueList" -> "List"
        "HashSet" -> "Set"
        else -> refactor.getOrDefault(type, type)
    }
}

fun sanitizeJavadoc(doc: String): String {
    return doc
            .replace("<p>", "")
}