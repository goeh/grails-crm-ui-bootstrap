grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
    inherits("global") {}
    log "warn"
    legacyResolve false
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:3.2.5.Final"
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    }

    plugins {
        build(":tomcat:$grailsVersion",
                ":release:2.2.1",
                ":rest-client-builder:1.0.3") {
            export = false
        }
        test(":hibernate:$grailsVersion") {
            export = false
        }

        test(":spock:0.7") {
            export = false
            exclude "spock-grails-support"
        }
        test(":codenarc:0.21") { export = false }
        test(":code-coverage:1.2.7") { export = false }

        test(":selection-repository:0.9.3") {
            export = false
        }

        compile ":resources:1.2.7"
        compile ":less-resources:1.3.3.2"
        compile ":jquery:1.10.2"
        compile ":twitter-bootstrap:2.3.2"
        compile ":fields:1.3"
        compile ":navigation:1.3.2"
        compile ":famfamfam:1.0.1"
        compile ":content-buffer:1.0.1"

        compile ":crm-core:2.0.0"
        compile ":crm-security:2.0.0"

        compile ":recent-domain:0.6.0"
        compile ":user-tag:0.6"
    }
}