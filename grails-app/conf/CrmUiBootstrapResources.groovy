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
    crmUiBootstrap {
        dependsOn 'jquery'

        resource url: 'js/modernizr.js', disposition: 'head'
        resource url: 'js/crm-ui-bootstrap.js'
        resource url: 'js/bootstrap-datepicker.js'
        resource url:'js/jquery.dropdownPlain.js'
        resource url:'js/jquery.hoverIntent-min.js'
        resource url:'js/jquery.notifier.js'
    }

    autocomplete {
        dependsOn 'jquery'
        resource url:'js/jquery.autocomplete.js'
    }
    application {

    }
    style {}
}
