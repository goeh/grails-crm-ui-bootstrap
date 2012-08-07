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
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        //mavenLocal()
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        compile "org.ocpsoft.prettytime:prettytime:1.0.8.Final"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        build(":tomcat:$grailsVersion",
                ":release:2.0.3") {
            export = false
        }
        runtime ":jquery:1.7.1"
        runtime(":twitter-bootstrap:2.0.2.24") {
            excludes 'resources'
        }
        runtime ":resources:1.1.6"
        runtime ":fields:1.2"
        runtime ":navigation:1.3.2"
        runtime ":famfamfam:latest.integration"
        runtime ":content-buffer:latest.integration"
        runtime ":user-tag:latest.integration"
        runtime ":recent-domain:latest.integration"

        test ":codenarc:latest.integration"

        compile "grails.crm:crm-core:latest.integration"
    }
}

//grails.plugin.location.'crm-core'="../crm-core"
