grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.repos.default = "crm"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn"
    repositories {
        grailsHome()
        grailsCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
        mavenCentral()
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:1.0.8.Final"
    }

    plugins {
        build(":tomcat:$grailsVersion",
                ":release:2.2.0") {
            export = false
        }
        test(":hibernate:$grailsVersion") {
            export = false
        }

        test(":codenarc:0.17") { export = false }
        compile ":resources:1.2.RC2"

        runtime ":twitter-bootstrap:2.2.2"
        runtime ":jquery:1.8.3"
        runtime ":fields:1.3"
        runtime ":navigation:1.3.2"
        runtime ":famfamfam:1.0.1"
        runtime ":content-buffer:1.0.1"

        compile "grails.crm:crm-core:latest.integration"
        runtime "grails.crm:crm-notification:latest.integration"

        runtime ":recent-domain:latest.integration"
        runtime ":user-tag:latest.integration"
    }
}
