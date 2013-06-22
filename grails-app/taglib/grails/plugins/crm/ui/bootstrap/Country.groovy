package grails.plugins.crm.ui.bootstrap

import groovy.transform.Immutable

/**
 * Country code/name placeholder, used by GrailsPatchedTagLib.
 */
@Immutable
//@CompileStatic
class Country {
    String code
    String name

    String toString() {
        name
    }
}
