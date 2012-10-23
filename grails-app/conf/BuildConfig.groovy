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
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:1.0.8.Final"
    }

    plugins {
        build(":tomcat:$grailsVersion",
                ":release:2.0.4") {
            export = false
        }
        test(":hibernate:$grailsVersion") {
            export = false
        }
        test(":codenarc:latest.integration") { export = false }

        compile ":resources:1.2.RC2"
        //runtime ":less-resources:1.3.0.3"

        compile "grails.crm:crm-core:latest.integration"

        runtime ":twitter-bootstrap:latest.integration"
        runtime ":recent-domain:latest.integration"
        runtime "grails.crm:crm-notification:latest.integration"
        runtime ":jquery:1.8.0"
        runtime ":fields:1.3"
        runtime ":navigation:1.3.2"
        runtime ":famfamfam:latest.integration"
        runtime ":content-buffer:latest.integration"
        runtime ":user-tag:latest.integration"
    }
}
