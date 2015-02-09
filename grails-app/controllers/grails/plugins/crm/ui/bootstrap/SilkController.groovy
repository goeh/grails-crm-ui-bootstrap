/*
 * Copyright 2014 Goran Ehrsson.
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
package grails.plugins.crm.ui.bootstrap

import grails.converters.JSON

class SilkController {
    def autocomplete = {
        def result
        if(params.term) {
            result = SilkIcons.icons.findAll{it.startsWith(params.term)}
        } else {
            result = []
        }
        def map = result.collect{[label:it, value:it, src:fam.icon(name:it)]}
        render map as JSON
    }
}
