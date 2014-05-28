/*
 * Copyright (c) 2012 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class CrmUiBootstrapGrailsPlugin {
    def groupId = "grails.crm"
    def version = "1.4.0-SNAPSHOT"
    def grailsVersion = "2.4 > *"
    def dependsOn = [:]
    def loadAfter = ['crmCore']
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "src/groovy/grails/plugins/crm/ui/bootstrap/TestSecurityDelegate.groovy"
    ]
    def title = "Twitter Bootstrap UI for GR8 CRM"
    def author = "Goran Ehrsson"
    def authorEmail = "goran@technipelago.se"
    def description = '''\
This plugin provides Twitter Bootstrap layout for GR8 CRM.
'''
    def documentation = "https://github.com/goeh/grails-crm-ui-bootstrap"
    def license = "APACHE"
    def organization = [name: "Technipelago AB", url: "http://www.technipelago.se/"]
    def issueManagement = [system: "github", url: "https://github.com/goeh/grails-crm-ui-bootstrap/issues"]
    def scm = [url: "https://github.com/goeh/grails-crm-ui-bootstrap"]
}
