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

/**
 *
 */
class GrailsPatchedTagLib {

    static namespace = "g"

    def grailsApplication

    /**
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
     *
     * This g:paginate tag fix is based on:
     * https://github.com/grails/grails-core/blob/master/grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/RenderTagLib.groovy
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr params A map containing request parameters
     * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
     * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr offset Used only if params.offset is empty
     * @attr fragment The link fragment (often called anchor tag) to use
     * @attr prefix If you include multiple paginators on the same page they must have unique prefixes.
     */
    def paginate = { attrs ->
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }

        def messageSource = grailsAttributes.messageSource
        def locale = RCU.getLocale(request)
        def prefix = attrs.prefix ?: ''
        def total = attrs.int('total') ?: 0
        def action = (attrs.action ? attrs.action : (params[prefix + 'action'] ? params[prefix + 'action'] : "list"))
        def offset = params.int(prefix + 'offset') ?: 0
        def max = params.int(prefix + 'max')
        def maxsteps = (attrs.int('maxsteps') ?: 10)

        if (!offset) offset = (attrs.int('offset') ?: 0)
        if (!max) max = (attrs.int('max') ?: 10)

        if(total <= max) {
            return
        }

        def linkParams = [:]
        if (attrs.params) linkParams.putAll(attrs.params)
        linkParams[prefix + 'offset'] = offset - max
        linkParams[prefix + 'max'] = max
        if (params[prefix + 'id']) linkParams[prefix + 'id'] = params[prefix + 'id']
        if (params[prefix + 'sort']) linkParams[prefix + 'sort'] = params[prefix + 'sort']
        if (params[prefix + 'order']) linkParams[prefix + 'order'] = params[prefix + 'order']

        def linkTagAttrs = [action: action]
        if (attrs.controller) {
            linkTagAttrs.controller = attrs.controller
        }
        if (attrs.id != null) {
            linkTagAttrs.id = attrs.id
        }
        if (attrs.fragment != null) {
            linkTagAttrs.fragment = attrs.fragment
        }
        linkTagAttrs.params = linkParams

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        writer << '<div class="pagination"><ul>'
        // display previous link when not on firststep
        if (currentstep > firststep) {
            linkTagAttrs.class = 'prevLink'
            linkParams[prefix + 'offset'] = offset - max
            writer << '<li class="prev">'
            writer << link(linkTagAttrs.clone()) {
                (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            }
            writer << '</li>'
        } else {
            writer << '<li class="prev disabled">'
            writer << '<span>'
            writer << (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            writer << '</span>'
            writer << '</li>'
        }
        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
            linkTagAttrs.class = 'step'

            // determine begin and endstep paging variables
            int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            int endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep) {
                linkParams[prefix + 'offset'] = 0
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) {firststep.toString()}
                writer << '</li>'
                writer << '<li class="disabled"><span>...</span></li>'
            }

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><span>${i}</span></li>"
                }
                else {
                    linkParams[prefix + 'offset'] = (i - 1) * max
                    writer << "<li>"
                    writer << link(linkTagAttrs.clone()) {i.toString()}
                    writer << "</li>"
                }
            }

            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                writer << '<li class="disabled"><span>...</span></li>'
                linkParams[prefix + 'offset'] = (laststep - 1) * max
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) { laststep.toString() }
                writer << '</li>'
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkTagAttrs.class = 'nextLink'
            linkParams[prefix + 'offset'] = offset + max
            writer << '<li class="next">'
            writer << link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            }
            writer << '</li>'
        } else {
            linkParams[prefix + 'offset'] = offset + max
            writer << '<li class="next disabled">'
            writer << '<span>'
            writer << (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            writer << '</span>'
            writer << '</li>'
        }

        writer << '</ul></div>'
    }

    /**
     * Renders a sortable column to support sorting in list views.<br/>
     *
     * Attribute title or titleKey is required. When both attributes are specified then titleKey takes precedence,
     * resulting in the title caption to be resolved against the message source. In case when the message could
     * not be resolved, the title will be used as title caption.<br/>
     *
     * Examples:<br/>
     *
     * &lt;g:sortableColumn property="title" title="Title" /&gt;<br/>
     * &lt;g:sortableColumn property="title" title="Title" style="width: 200px" /&gt;<br/>
     * &lt;g:sortableColumn property="title" titleKey="book.title" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" titleKey="book.releaseDate" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr property - name of the property relating to the field
     * @attr defaultOrder default order for the property; choose between asc (default if not provided) and desc
     * @attr title title caption for the column
     * @attr titleKey title key to use for the column, resolved against the message source
     * @attr params a map containing request parameters
     * @attr action the name of the action to use in the link, if not specified the list action will be linked
     * @attr params A map containing URL query parameters
     * @attr class CSS class name
     * @attr prefix If you include multiple lists on the same page they must have unique prefixes.
     */
    def sortableColumn = { attrs ->
        def writer = out
        if (!attrs.property) {
            throwTagError("Tag [sortableColumn] is missing required attribute [property]")
        }

        if (!attrs.title && !attrs.titleKey) {
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")
        }

        def prefix = attrs.remove("prefix") ?: ''
        def property = attrs.remove("property")
        def action = attrs.remove("action") ?: (actionName ?: "list")

        def defaultOrder = attrs.remove("defaultOrder")
        if (defaultOrder != "desc") defaultOrder = "asc"

        // current sorting property and order
        def sort = params[prefix + 'sort']
        def order = params[prefix + 'order']

        // add sorting property and params to link params
        def linkParams = [:]
        if (params[prefix + 'id']) linkParams.put(prefix + 'id', params[prefix + 'id'])
        if (attrs.params) linkParams.putAll(attrs.remove("params"))
        if (attrs.id != null) {
            linkParams.id = attrs.id
        }
        if (attrs.fragment != null) {
            linkParams.fragment = attrs.fragment
        }
        linkParams[prefix + 'sort'] = property

        // add offset and max so we don't loose them when re-sorting.
        linkParams[prefix + 'offset'] = 0
        linkParams[prefix + 'max'] = params[prefix + 'max'] ?: 10

        // determine and add sorting order for this column to link params
        attrs.class = (attrs.class ? "${attrs.class} sortable" : "sortable")
        if (property == sort) {
            attrs.class = attrs.class + " sorted " + order
            if (order == "asc") {
                linkParams[prefix + 'order'] = "desc"
            }
            else {
                linkParams[prefix + 'order'] = "asc"
            }
        }
        else {
            linkParams[prefix + 'order'] = defaultOrder
        }

        // determine column title
        def title = attrs.remove("title")
        def titleKey = attrs.remove("titleKey")
        if (titleKey) {
            if (!title) title = titleKey
            def messageSource = grailsAttributes.messageSource
            def locale = RCU.getLocale(request)
            title = messageSource.getMessage(titleKey, null, title, locale)
        }
        writer << "<th "
        // process remaining attributes
        attrs.each { k, v ->
            writer << "${k}=\"${v.encodeAsHTML()}\" "
        }
        writer << ">${link(action: action, params: linkParams) { title }}</th>"
    }

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
                    return new Country(locale.country, locale.displayCountry)
                }
                return new Country(code, code)
            }
        } else {
            attrs.from = locales.collect {locale -> new Country(locale.country, locale.displayCountry)}.unique()
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
println "from=${attrs.from}"
        // use generic select
        out << select(attrs)
    }
}


private class Country {
    String code
    String name

    public Country(String code, String name) {
        this.code = code
        this.name = name
    }

    String toString() {
        name
    }

    boolean equals(other) {
        code == other.code
    }
}