/*
 *  Copyright 2012 Goran Ehrsson.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
modules = {
    modernizr {
        resource url:'js/modernizr.js', disposition: 'head'
    }
    crm {
        // We depend only on bootstrap JavaScript, not the bootstrap styles. Add styles as described above.
        dependsOn 'jquery, modernizr, bootstrap-js'

        resource url: [id: 'crm-ui-bootstrap-less', plugin: 'crm-ui-bootstrap', dir: 'less', file: 'crm-ui-bootstrap.less'], attrs: [rel: "stylesheet/less", type: 'css', order: 110], disposition: 'head'
        resource url: 'js/crm-ui-bootstrap.js'

        resource url: 'js/jquery.dropdownPlain.js'
        resource url: 'js/jquery.hoverIntent-min.js'
        resource url: 'js/jquery.notifier.js'
    }

    autocomplete {
        dependsOn 'jquery'
        resource url: 'js/jquery.autocomplete.js'
    }

    select2 {
        dependsOn 'jquery'
        resource url: 'js/select2.js'
        resource url: 'css/select2.css'
    }

    dateformat {
        resource url: 'js/rfc3339date.js'
        resource url: 'js/date.format.js'
    }

    datepicker {
        dependsOn 'bootstrap-js'
        resource url: 'js/datepicker/bootstrap-datepicker.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.sv.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.da.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.no.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.fi.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.pl.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.de.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.fr.js'
        resource url: 'js/datepicker/locales/bootstrap-datepicker.es.js'
        resource url: [plugin: 'crm-ui-bootstrap', dir: 'less', file: 'datepicker.less'], attrs: [rel: "stylesheet/less", type: 'css', order: 190], disposition: 'head'
    }

    timepicker {
        dependsOn 'bootstrap-js'
        resource url: 'js/bootstrap-timepicker.js'
        resource url: [plugin: 'crm-ui-bootstrap', dir: 'less', file: 'timepicker.less'], attrs: [rel: "stylesheet/less", type: 'css', order: 192], disposition: 'head'
    }
    aligndates {
        dependsOn 'dateformat'
        resource url:'js/aligndates.js'
    }
    html5 {
        resource url: 'js/html5.js'
    }
}
