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
 * under the License.
 */

package grails.plugins.crm.ui.bootstrap

import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 *
 */
class GrailsPatchedTagLib extends ApplicationTagLib {

    /**
     * A helper tag for creating locale selects.<br/>
     *
     * eg. &lt;g:localeSelect name="myLocale" value="${locale}" /&gt;
     *
     * @emptyTag
     *
     * @attr name REQUIRED The name of the select
     * @attr value The set locale, defaults to the current request locale if not specified
     * @attr from List of Locale instances or ISO639 _ ISO3166 locale codes, default to all available locales on the server
     * @attr sort field so sort on (see Locale javadoc for available fields) Default is to sort on language+country
     * @attr skipCountry if set to true only locales without country will be included (i.e. "en" instead of "en_US" and "en_GB")
     */
    Closure localeSelect = { attrs ->
        def from = attrs.from ?: grailsApplication.config.grails.localeSelect.from
        if (from) {
            attrs.from = from.collect {arg ->
                (arg instanceof Locale) ? arg : new Locale(* arg.toString().split('_'))
            }
        } else {
            attrs.from = Locale.getAvailableLocales()
        }
        def value = (attrs.value ?: RCU.getLocale(request))?.toString()
        if (attrs.skipCountry || grailsApplication.config.grails.localeSelect.skipCountry) {
            attrs.from = attrs.from.findAll {!it.country}
            value = value.split('_')[0]
        }
        attrs.value = value
        def sort = attrs.sort
        attrs.from = attrs.from.toList().sort {sort ? it."${sort}" : it.toString()}
        // set the key as a closure that formats the locale
        attrs.optionKey = { it.country ? "${it.language}_${it.country}" : it.language }
        // set the option value as a closure that formats the locale for display
        attrs.optionValue = {it.country ? "${it.language}, ${it.country},  ${it.displayName}" : "${it.language}, ${it.displayName}" }

        // use generic select
        out << select(attrs)
    }

    Closure countrySelect = {attrs ->
        def locales = Locale.getAvailableLocales()
        def from = attrs.from ?: grailsApplication.config.grails.countrySelect.from
        if (from) {
            attrs.from = from.collect {code ->
                def locale = locales.find {it.country == code}
                if (locale) {
                    return new Country(code: locale.country, name: locale.displayCountry)
                }
                return new Country(code: code, name: code)
            }
        } else {
            attrs.from = locales.collect {locale -> new Country(code: locale.country, name: locale.displayCountry)}.unique()
        }
        if (!attrs.value) {
            attrs.value = RCU.getLocale(request)?.country
        }
        def sort = attrs.sort
        attrs.from = attrs.from.toList().sort {sort ? it."${sort}" : it.toString()}
        if (!attrs.optionKey) {
            attrs.optionKey = 'code'
        }
        if (!attrs.optionValue) {
            attrs.optionValue = 'name'
        }
        // use generic select
        out << select(attrs)
    }
}

