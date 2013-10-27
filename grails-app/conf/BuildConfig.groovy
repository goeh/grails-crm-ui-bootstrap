grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.repos.default = "crm"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }
    log "warn"
    legacyResolve false
    repositories {
        grailsCentral()
        mavenRepo "http://labs.technipelago.se/repo/crm-releases-local/"
        mavenRepo "http://labs.technipelago.se/repo/plugins-releases-local/"
        mavenCentral()
    }
    dependencies {
        compile "org.ocpsoft.prettytime:prettytime:3.1.0.Final"
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
        test(":codenarc:0.18.1") { export = false }
        test(":code-coverage:1.2.6") { export = false }

        runtime ":resources:1.2"
        runtime ":less-resources:1.3.3.2"
        runtime ":jquery:1.10.2"
        runtime ":twitter-bootstrap:2.3.2"
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

codenarc {
    reports = {
        CrmXmlReport('xml') {
            outputFile = 'target/test-reports/CodeNarcReport.xml'
            title = 'Grails CRM CodeNarc Report'
        }
        CrmHtmlReport('html') {
            outputFile = 'target/test-reports/CodeNarcReport.html'
            title = 'Grails CRM CodeNarc Report'

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

