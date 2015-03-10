grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.fork = [
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    test: false,
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        grailsCentral()
        mavenLocal()
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
        mavenCentral()
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:3.2.7.Final"
        // See https://jira.grails.org/browse/GPHIB-30
        test("javax.validation:validation-api:1.1.0.Final") { export = false }
        test("org.hibernate:hibernate-validator:5.0.3.Final") { export = false }
    }

    plugins {
        build(":release:3.0.1", ":rest-client-builder:1.0.3") {
            export = false
        }
        test(":hibernate4:4.3.6.1") {
            excludes "net.sf.ehcache:ehcache-core"  // remove this when http://jira.grails.org/browse/GPHIB-18 is resolved
            export = false
        }

        test(":codenarc:0.22") { export = false }
        test(":code-coverage:2.0.3-3") { export = false }

        test(":selection-repository:0.9.3") {
            export = false
        }

        compile ":resources:1.2.14"
        compile ":less-resources:1.3.3.3" // TODO https://github.com/groovydev/less-grails-plugin/pull/18
        compile ":jquery:1.11.1"
        compile ":twitter-bootstrap:2.3.2.2"
        compile ":fields:1.4"
        compile ":navigation:1.4.0-SNAPSHOT" // TODO https://github.com/Grailsrocks/grails-navigation/pull/4

        compile ":crm-security:2.4.0"

        compile ":recent-domain:0.6.0"
        compile ":user-tag:0.6"
    }
}
