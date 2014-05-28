grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target"
grails.project.target.level = 1.6

grails.project.repos.default = "crm"

grails.project.fork = [
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") {
      excludes 'ehcache-core'
    }
    log "warn"
    repositories {
        grailsCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:3.2.5.Final"
    }

    plugins {
        build(":release:3.0.1",
                ":rest-client-builder:1.0.3") {
            export = false
        }
        test(":hibernate:3.6.10.15") {
            export = false
        }

        test(":codenarc:0.21") { export = false }
        test(":code-coverage:1.2.7") { export = false }

        runtime ":resources:1.2.8"
        runtime ":less-resources:1.3.3.2"
        runtime ":jquery:1.11.1"
        runtime ":twitter-bootstrap:2.3.2"
        runtime ":fields:1.3.1-SNAPSHOT"
        runtime ":navigation:1.3.3-SNAPSHOT"
        runtime ":famfamfam:1.0.1"
        runtime ":content-buffer:1.0.1"

//        compile "grails.crm:crm-core:latest.integration"
//        runtime "grails.crm:crm-notification:latest.integration"
//        runtime "grails.crm:crm-security:latest.integration"

        runtime ":recent-domain:latest.integration"
        runtime ":user-tag:latest.integration"

        test(":selection-repository:latest.integration") {
            export = false
        }
    }
}

grails.plugin.location.'crm-core' = '../crm-core'
grails.plugin.location.'crm-notification' = '../crm-notification'
grails.plugin.location.'crm-security' = '../crm-security'

codenarc {
    reports = {
        CrmXmlReport('xml') {
            outputFile = 'target/CodeNarcReport.xml'
            title = 'GR8 CRM CodeNarc Report'
        }
        CrmHtmlReport('html') {
            outputFile = 'target/CodeNarcReport.html'
            title = 'GR8 CRM CodeNarc Report'

        }
    }
    properties = {
        GrailsPublicControllerMethod.enabled = false
        CatchException.enabled = false
        CatchThrowable.enabled = false
        ThrowException.enabled = false
        ThrowRuntimeException.enabled = false
        GrailsStatelessService.enabled = false
        GrailsStatelessService.ignoreFieldNames = "dataSource,scope,sessionFactory,transactional,*Service,messageSource,grailsApplication,applicationContext,expose"
    }
    processTestUnit = false
    processTestIntegration = false
}

coverage {
    exclusions = ['**/radar/**']
}

