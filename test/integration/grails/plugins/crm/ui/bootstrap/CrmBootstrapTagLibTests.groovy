package grails.plugins.crm.ui.bootstrap

import grails.test.GroovyPagesTestCase

/**
 * Tests for crm-ui-bootstrap tag library.
 */
class CrmBootstrapTagLibTests extends GroovyPagesTestCase {

    def grailsApplication
    def crmSecurityService
    def selectionRepositoryService

    void testSelectionMenu() {
        // Initialize a known config.
        grailsApplication.config.selection.uri.encoding = 'base64'
        grailsApplication.config.selection.uri.parameter = 'q'

        def user = crmSecurityService.createUser(username: "test", name: "Test User", email: "test@test.com", enabled: true)

        crmSecurityService.runAs(user.username, 0L) {
            selectionRepositoryService.put(new URI("gorm://testEntity/list?name=A*"), "testEntity", user.username, "A people", null, 0L)
            selectionRepositoryService.put(new URI("gorm://testEntity/list?name=B*"), "testEntity", user.username, "B people", null, 0L)
            selectionRepositoryService.put(new URI("gorm://testEntity/list?name=C*"), "testEntity", user.username, "C people", null, 0L)

            def template = '<crm:selectionMenu location="testEntity" controller="foo" action="bar"/>'
            def result = applyTemplate(template)
            assert result.contains('A people</a>')
            assert result.contains('B people</a>')
            assert result.contains('C people</a>')
            assert result.contains('<a href="/foo/bar?q=')
        }
    }
}
