/*
 * Copyright 2012 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.crm.ui.bootstrap

import groovy.transform.Immutable

/**
 * Country code/name placeholder, used by GrailsPatchedTagLib.
 */
@Immutable
// TODO CompileStatic breaks the countrySelect input tag in crm-ui-bootstrap!?!?!
//CompileStatic
class Country {
    String code
    String name

    def asType(Class clazz) {
        if(clazz.isAssignableFrom(Map)) {
            return [code: code, name: name]
        }
        throw new ClassCastException("${this.class.name} cannot be cast to $clazz")
    }

    @Override
    String toString() {
        name.toString()
    }
}
