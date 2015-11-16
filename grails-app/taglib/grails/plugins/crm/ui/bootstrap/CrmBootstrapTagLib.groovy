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

import grails.plugins.crm.core.DateUtils
import grails.util.GrailsNameUtils
import grails.plugins.crm.core.TenantUtils
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.ocpsoft.prettytime.PrettyTime

import java.text.DateFormat

class CrmBootstrapTagLib {

    static namespace = "crm"

    static CALLBACK_CONTEXT_VARS = [
        'grailsApplication',
        'pageScope',
        'session',
        'request',
        'controllerName',
        'actionName',
        'flash',
        'params'
    ]

    def grailsApplication
    def crmSecurityService
    def grailsNavigation
    def userTagService
    def selectionService
    def selectionRepositoryService
    def crmThemeService
    def grailsLinkGenerator

    def logo = { attrs, body ->
        def image
        try {
            def size = attrs.size ?: 'medium'
            image = crmThemeService.getLogo(TenantUtils.tenant, size) ?: "/images/grails_logo.png"
        } catch (Exception e) {
            log.error("Failed to render logo", e)
            image = "/images/grails_logo.png"
        }
        if (Boolean.valueOf(attrs.absolute)) {
            image = grailsLinkGenerator.getServerBaseURL() + image
        }

        out << g.img(uri: image)
    }

    /**
     * Renders a submenu.
     *
     * Since version 2.4.0+ this tag just delegates to nav:secondary (navigation feature in the platform-core plugin)
     */
    def submenu = { attrs, body ->
        out << nav.secondary(attrs, body)
    }

    /**
     * Render tag body conditionally if navigation scope contain nodes (navigation items).
     *
     * TODO: This tag really should be included in platform-core NavigationtagLib.
     */
    def hasNavigationItems = { attrs, body ->
        def pathNodes = grailsNavigation.nodesForPath(attrs.path ?: grailsNavigation.getActivePath(request))

        if (pathNodes?.size()) {
            out << body()
        }
    }

    /**
     * Render navigation items for a given scope and path.
     *
     * @attr scope Optional scope to render menu for. Defaults to "app", but could be any valid scope i.e. "app/messages/archive"
     * @attr path Optional activation path indicating what is currently active.
     */
    def eachNavigationItem = { attrs, body ->
        def scope = attrs.scope
        if (!scope) {
            def requestPath = attrs.path ?: grailsNavigation.getActivePath(request)
            def pathScope = nav.scopeForActivationPath(path:requestPath)
            scope = pathScope ?: grailsNavigation.getDefaultScope(request)
        }
        if (!(scope instanceof String)) {
            scope = scope.name
        }

        if (log.debugEnabled) {
            log.debug "Rendering items for scope [${scope}]"
        }

        def activeNodes = grailsNavigation.nodesForPath(attrs.path ?: grailsNavigation.getActivePath(request))

        def callbackContext = [:]
        for (varName in CALLBACK_CONTEXT_VARS) {
            callbackContext[varName] = this."$varName"
        }

        def scopeNode = grailsNavigation.nodeForId(scope)
        if (scopeNode) {
            if (log.debugEnabled) {
                log.debug "Rendering items for scope [${scope}] which has children ${scopeNode.children.name}"
            }
            for (n in scopeNode.children) {
                def nodeContext = callbackContext + [item:n]
                if (n.isVisible(nodeContext)) {
                    def active = activeNodes.contains(n)
                    def enabled = n.isEnabled(nodeContext)

                    def linkArgs = new HashMap(n.linkArgs) // Clone! naughty g.link changes them otherwise. Naughty g.link!
                    // Always give custom body a clone of the link args as they can't use the ones from item
                    // Custom body is responsible for rendering nested items
                    out << body([item:n, linkArgs:linkArgs, active:active, enabled:enabled])
                }
            }
        } else if (log.debugEnabled) {
            log.debug "Attempt to render items for scope [${scope}] but there was no navigation node found for that scope."
        }
    }

    /**
     * Renders a selection menu.
     */
    def selectionMenu = { attrs, body ->
        def bodyContent = body()?.trim()
        def username = crmSecurityService.currentUser?.username
        if (!username) {
            if (bodyContent) {
                out << bodyContent
            }
            return
        }
        def tenant = TenantUtils.tenant
        def location = attrs.location ?: controllerName
        def savedSelections = selectionRepositoryService?.list(location, username, tenant)
        def selection = attrs.selection ?: pageScope.selection
        def splitButton = (savedSelections || selection)
        if (!splitButton && !bodyContent) {
            return
        }

        if (splitButton) {
            out << """<div class="btn-group">"""
        }

        if (bodyContent) {
            out << bodyContent
        }

        if (splitButton) {
            out << """<button class="btn ${
                attrs.visual ? 'btn-' + attrs.visual : ''
            } dropdown-toggle" data-toggle="dropdown">"""
            if (!bodyContent) {
                def label = message(code: controllerName + '.button.selections.label', default: 'Filters')
                out << """<i class="icon-search ${attrs.visual ? 'icon-white' : ''}"></i> $label """
            }
            out << """<span class="caret"></span></button>\n"""
            out << """<ul class="dropdown-menu">\n"""
            if (selection) {
                def label = message(code: controllerName + '.button.search.again.label', default: 'New Query')
                out << """<li>${link(controller: attrs.controller ?: location, action: "index", label)}</li>"""
                if (crmSecurityService.isPermitted("selectionRepository:create")) {
                    out << "<li>"
                    out << link(controller: 'selectionRepository', action: 'create',
                            params: [location: location,
                                    username: username,
                                    tenant: tenant,
                                    uri: selectionService.encodeSelection(selection),
                                    referer: attrs.referer ?: ''],
                            message(code: 'selectionRepository.save.label', default: 'Save')
                    )
                    out << "</li>"
                }
            }
            if (savedSelections) {
                if (crmSecurityService.isPermitted("selectionRepository:list")) {
                    out << "<li>"
                    out << link(controller: "selectionRepository", action: "list",
                            params: [location: location,
                                    username: username,
                                    tenant: tenant,
                                    referer: attrs.referer ?: request.forwardURI],
                            message(code: 'selectionRepository.list.label', default: 'Manage Selections', args: ['Selection'])
                    )

                    out << "</li>"
                }
                out << """<li class="divider"></li>"""
                for (sel in savedSelections) {
                    out << "<li>"
                    out << select.link(controller: attrs.controller ?: location, action: attrs.action ?: 'list', selection: sel.uri, sel.name)
                    out << "</li>"
                }
            }
            out << "</ul>"
            out << "</div>"
        }
    }

    /**
     * Render a page header.
     *
     * @attr title page title
     * @attr subtitle optional subtitle.
     * @attr args arguments sent to i18n message lookup.
     * @attr tag type of header tag. Default is 'h1'.
     */
    def header = { attrs, body ->
        def args = attrs.remove('args')
        def tag = attrs.remove('tag') ?: 'h1'
        def h1 = attrs.remove('title')
        if (h1) {
            h1 = h1.toString()
        } else {
            throwTagError("Tag [header] is missing required attribute [title]")
        }
        def favorite = attrs.remove('favorite')
        if (favorite) {
            favorite = favoriteIcon(bean: favorite)
        } else {
            favorite = ''
        }
        def small = attrs.remove('subtitle')
        if (small) {
            small = small.toString()
            small = ' <small>' + g.message(code: small, default: small, args: args) + '</small>\n'
        } else {
            small = ''
        }
        out << '<header class="page-header">\n'
        out << "<${tag}${renderAttributes(attrs)}>" + g.message(code: h1, default: h1, args: args) + favorite + small + "</${tag}>\n"

        out << body()

        out << '</header>\n'
    }

    /**
     * Renders a favorite icon (star) if a bean is tagged as favorite.
     *
     * @attr bean REQUIRED the bean to check if tagged
     */
    def favoriteIcon = { attrs ->
        if (!attrs.bean) {
            throwTagError("Tag [favorite] is missing required attribute [bean]")
        }
        def username = crmSecurityService.currentUser?.username
        def tag = grailsApplication.config.crm.tag.favorite ?: 'favorite'
        def icon = grailsApplication.config.crm.favorite.icon ?: 'icon-bookmark'
        if (userTagService.isTagged(attrs.bean, tag, username, TenantUtils.tenant)) {
            out << '<i class="' + icon + '"></i>'
        }
    }

    private Map takeAttributes(Map takeFrom, List attributeNames) {
        def result = [:]
        for (a in attributeNames) {
            def v = takeFrom.remove(a)
            if (v != null) {
                result[a] = v
            }
        }
        return result
    }

    private String renderAttributes(Map attrs) {
        def s = new StringBuilder()
        attrs.each { key, value ->
            if (value != null) {
                s << " ${key.encodeAsURL()}=\"${value.encodeAsURL()}\""
            }
        }
        s.toString()
    }

    /**
     * Renders a button or anchor with Twitter Bootstrap style.
     *
     * @attr type the button type ('button', 'link' or 'url'). Default is 'button'.
     * @attr action controller action to call
     * @attr permission permission required to see the button.
     * @attr visual twitter bootstrap btn- css style. 'primary', 'info', 'warning', 'danger'.
     * @attr class optional css class.
     * @attr style optional css style.
     * @attr icon optional button icon (twitter bootstrap icon class)
     * @attr label button label, can be a i18n message key or the text to use as label
     * @attr title button title, can be a i18n message key or the text to use as title
     * @attr confirm optional confirmation message if the button action requires user confirmation (delete, etc.)
     * @attr group true if this button is the parent button in a button group
     * @attrs params optional link parameters
     * @attrs href href attribute if type is 'url'.
     * @attrs target hyperlink target (_blank, _new, etc.) if type is 'url'
     */
    def button = { attrs, body ->
        def props = takeAttributes(attrs, ['type', 'action', 'visual', 'class', 'icon', 'label', 'title', 'args',
                'confirm', 'style', 'controller', 'id', 'params', 'href', 'target', 'permission', 'group', 'elementId'])
        if (props.permission && !crmSecurityService.isPermitted(props.permission)) {
            return
        }
        def type = props.type ?: 'button'
        def action = props.action
        def visual = props.visual
        def css = "btn ${visual ? 'btn-' + visual : ''} ${props['class'] ?: ''}"
        def icon = props.icon
        def label = props.label ? g.message(code: props.label, default: props.label, args: props.args) : body()
        def title = props.title ? g.message(code: props.title, default: props.title, args: props.args) : null
        def confirm = props.confirm ? g.message(code: props.confirm, default: props.confirm, args: props.args) : null
        if (props.group) {
            out << "<div class=\"btn-group\">\n"
        }
        switch (type) {
            case 'button':
                out << '<button type="submit"'
                if (props.elementId) {
                    out << " id=\"${props.elementId}\""
                }
                if (action) {
                    out << " name=\"_action_${action.encodeAsHTML()}\""
                }
                out << " class=\"${css.encodeAsHTML()}\""
                if (title) {
                    out << " title=\"${title}\""
                }
                if (confirm) {
                    out << " onclick=\"return confirm('$confirm')\""
                }
                if (props.style) {
                    out << " style=\"${props.style}\""
                }
                out << "${renderAttributes(attrs)}>"
                if (icon) {
                    out << "<i class=\"${icon.encodeAsHTML()}\"></i> "
                }
                out << label
                out << "</button>\n"
                break;
            case 'link':
                def linkAttrs = [controller: props.controller, action: action, id: props.id, 'class': css, style: props.style ?: '']
                if (icon) {
                    label = "<i class=\"$icon\"></i> $label"
                }
                if (confirm) {
                    linkAttrs.onclick = "return confirm('$confirm')"
                }
                if (props.params) {
                    linkAttrs.params = props.params
                }
                if (title) {
                    linkAttrs.title = title
                }
                if (props.elementId) {
                    linkAttrs.elementId = props.elementId
                }
                out << g.link(linkAttrs + attrs, label)
                break;
            case 'url':
                out << "<a href=\"${props.href}\""
                if (props.elementId) {
                    out << " id=\"${props.elementId}\""
                }
                out << " class=\"${css.encodeAsHTML()}\""
                if (props.style) {
                    out << " style=\"${props.style}\""
                }
                if (title) {
                    out << " title=\"${title}\""
                }
                if (props.target) {
                    out << " target=\"${props.target}\""
                }
                if (confirm) {
                    out << " onclick=\"return confirm('$confirm')\""
                }
                out << "${renderAttributes(attrs)}>"
                if (icon) {
                    out << "<i class=\"${icon.encodeAsHTML()}\"></i> "
                }
                out << label
                out << "</a>\n"
                break
        }
        if (props.group) {
            out << body()
            out << "</div>\n"
        }
    }

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
    Closure paginate = { attrs ->
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
        if (total <= max) {
            return
        }

        def linkParams = [:]
        if (attrs.params) linkParams.putAll(attrs.params)
        linkParams[prefix + 'offset'] = offset - max
        linkParams[prefix + 'max'] = max
        if (params[prefix + 'id']) linkParams[prefix + 'id'] = params[prefix + 'id']
        if (params[prefix + 'sort']) linkParams[prefix + 'sort'] = params[prefix + 'sort']
        if (params[prefix + 'order']) linkParams[prefix + 'order'] = params[prefix + 'order']

        def uri = pageScope.selection
        if (uri) {
            linkParams.putAll(selectionService.createSelectionParameters(uri))
        }
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
            writer << g.link(linkTagAttrs.clone()) {
                (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            }
            writer << '</li>'
        } else {
            writer << '<li class="prev disabled">'
            writer << '<a href="#">'
            writer << (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            writer << '</a>'
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
                writer << g.link(linkTagAttrs.clone()) { firststep.toString() }
                writer << '</li>'
                writer << '<li class="disabled"><a href=\"#\">...</a></li>'
            }

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><a href=\"#\">${i}</a></li>"
                } else {
                    linkParams[prefix + 'offset'] = (i - 1) * max
                    writer << "<li>"
                    writer << g.link(linkTagAttrs.clone()) { i.toString() }
                    writer << "</li>"
                }
            }

            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                writer << '<li class="disabled"><a href=\"#\">...</a></li>'
                linkParams[prefix + 'offset'] = (laststep - 1) * max
                writer << '<li>'
                writer << g.link(linkTagAttrs.clone()) { laststep.toString() }
                writer << '</li>'
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkTagAttrs.class = 'nextLink'
            linkParams[prefix + 'offset'] = offset + max
            writer << '<li class="next">'
            writer << g.link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            }
            writer << '</li>'
        } else {
            //linkParams[prefix + 'offset'] = offset + max
            writer << '<li class="next disabled">'
            writer << '<a href="#">'
            writer << (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            writer << '</a>'
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
    Closure sortableColumn = { attrs ->
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
        def linkAttributes = [:]
        if (attrs.fragment != null) {
            linkAttributes.fragment = attrs.remove('fragment')
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
            } else {
                linkParams[prefix + 'order'] = "asc"
            }
        } else {
            linkParams[prefix + 'order'] = defaultOrder
        }

        def uri = pageScope.selection
        if (uri) {
            linkParams.putAll(selectionService.createSelectionParameters(uri))
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
        linkAttributes.action = action
        linkAttributes.params = linkParams
        writer << ">${link(linkAttributes) { title }}</th>"
    }

    def alert = { attrs, body ->
        out << '<div class="alert alert-block ' << attrs.class.tokenize().join(" ") << '">'
        out << '<a class="close" data-dismiss="alert">&times;</a>'
        out << '<p>' << body() << '</p>'
        out << '</div>'
    }

    /**
     * Renders a select input element populated with all available famfamfam icons (http://www.famfamfam.com/).
     */
    def iconSelect = { attrs ->
        attrs.from = SilkIcons.icons
        out << select(attrs) // use generic select
    }

    /**
     * Render javascript code for initializing a datepicker on the page.
     *
     * &lt;crm:datepicker/> without any options is equivalent to the following:
     * &lt;crm:datepicker selector=".date" format="yyyy-mm-dd" weekStart="1" language="<request.locale>" calendarWeeks="true" todayHighLight="true"/>
     */
    def datepicker = {attrs, body ->
        def locale = attrs.locale ? new Locale(attrs.remove('locale')) : RCU.getLocale(request)
        def config = grailsApplication.config.crm.datepicker
        if(! attrs.selector) attrs.selector = (config.selector ?: '.date')
        if(! attrs.calendarWeeks) attrs.calendarWeeks = (config.calendarWeeks ?: false)
        if(! attrs.todayHighlight) attrs.todayHighlight = (config.todayHighlight == false ? false : true)
        if(! attrs.autoclose) attrs.autoclose = (config.autoclose == false ? false : true)
        if(! attrs.language) {
            attrs.language = locale.getLanguage()
        }

        out << """\$('${attrs.selector}').datepicker({
language: "${attrs.language}",
calendarWeeks: ${attrs.calendarWeeks},
todayHighlight: ${attrs.todayHighlight},
autoclose: ${attrs.autoclose}"""

        if(attrs.format != null) {
            out << ",\nformat: \"${attrs.format}\""
        } else {
            def df = DateFormat.getDateInstance(DateFormat.SHORT, locale)
            out << ",\nformat: \"${df.toPattern().toLowerCase()}\""
        }
        if(attrs.weekStart != null) {
            out << ",\nweekStart: ${attrs.weekStart}"
        }

        if(attrs.todayBtn || config.todayBtn) {
            out << ",\ntodayBtn: true"
        }

        def days = attrs.daysOfWeekDisabled ?: config.daysOfWeekDisabled
        if(days) {
            out << ",\ndaysOfWeekDisabled: \"${days}\""
        }

        out << "\n})${body()};"
    }

    /**
     * Render week number.
     *
     * @attr date REQUIRED the date to render week number for.
     * @attr locale locale to use, since week number are country specific. If not specified the request locale will be used.
     */
    def weekNumber = { attrs ->
        def date = attrs.date
        if (!date) {
            throwTagError("Tag [weekNumber] is missing required attribute [date]")
        }
        if (!(date instanceof Date)) {
            date = DateUtils.parseDate(date.toString())
        }
        def locale = attrs.locale ? new Locale(attrs.locale) : RCU.getLocale(request)
        def cal = Calendar.getInstance(locale)
        cal.setTime(date)
        out << cal.get(Calendar.WEEK_OF_YEAR).toString()
    }

    /**
     * Renders a tab list item.
     *
     * @attr id REQUIRED tab id
     * @attr label REQUIRED tab label
     * @attr count count indicator
     */
    def pluginTab = { attrs, body ->
        out << "<li class=\"nav-${attrs.id}\">"
        out << "<a href=\"#${attrs.id}\" data-toggle=\"tab\">"
        out << message(code: attrs.label, default: attrs.label)
        if (attrs.count) {
            out << crm.countIndicator(count: attrs.count)
        }
        out << "</a>"
        out << "</li>"
    }

    /**
     * Renders a number enclosed by parenthesis, but only if the number &gt; 0.
     *
     * This tag is typically used in tabbed views where a count indicator is wanted in each tab.
     * If the number is 0 or null (groovy false) nothing will be rendered.
     *
     * @attr count REQUIRED the number to render
     */
    def countIndicator = { attrs ->
        def count = attrs.count ?: 0
        if (count instanceof Collection) {
            count = count.size()
        }
        if (count) {
            def badge = attrs.badge
            if (badge) {
                // Render Twitter Bootstrap badge.
                out << """<span class="badge badge-$badge">$count</span>"""
            } else {
                out << """<span class="crm-count"> ($count)</span>"""
            }
        }
    }

    /**
     * Renders a date using the prettytime library.
     * @attr date REQUIRED the date to pretty print
     */
    def prettyTime = { attrs ->
        def date = attrs.date
        if (!date) {
            throwTagError("Tag [prettyTime] is missing required attribute [date]")
        }
        def locale
        if (attrs.locale) {
            if (attrs.locale instanceof Locale) {
                locale = attrs.locale
            } else {
                locale = new Locale(attrs.locale.toString())
            }
        } else {
            locale = RCU.getLocale(request)
            if (!locale) {
                locale = Locale.default
            }
        }
        def pt = new PrettyTime()
        pt.setLocale(locale)
        out << pt.format(date)
    }

    /**
     * Renders "Printed on September 7 10:09:12 by Liza User"
     * The i18n message code is <controllerName>.print.header and it takes two arguments [date, user].
     * If tag body is empty the message is wrapped in a right floated <span> with a gray 13px font.
     * Tag body can use 'date', 'user' and 'text' for custom rendering.
     *
     * @attr date the date to render (default is current time)
     * @attr datetype the 'type' attribute used by g:formatDate (default is datetime)
     * @attr datestyle the 'style' attribute used by g:formatDate (default is LONG)
     */
    def printedBy = { attrs, body ->
        def datetype = attrs.datetype ?: 'datetime'
        def datestyle = attrs.datestyle ?: 'LONG'
        def dateInstance = attrs.date ?: new Date()
        def user = crmSecurityService.currentUser?.name ?: 'unknown'
        def date = formatDate(type: datetype, style: datestyle, date: dateInstance)
        def text = message(code: (controllerName ?: 'default') + '.print.header', default: 'Printed {0} by {1}', args: [date, user])
        def content = body([user: user, date: date, text: text])?.trim()
        if (!content) {
            content = """<span style="float:right;font-size:13px;color:#999999;">""" + text + "</span>"
        }
        out << content
    }

    /**
     * Renders dateCreated and lastUpdated timestamps with CSS class 'muted timestamp'.
     * @attr bean REQUIRED domain instance with dateCreated and lastUpdated properties
     * @attr type type attribute for formatDate, defaults to 'datetime'
     * @attr dateStyle date style attribute for formatDate, defaults to 'long'
     * @attr timeStyle time style attribute for formatDate, defaults to 'short'
     */
    def timestamp = { attrs ->
        def bean = attrs.bean
        if (!bean) {
            throwTagError("Tag [timestamp] is missing required attribute [bean]")
        }
        def dateStyle = attrs.dateStyle ?: 'long'
        def timeStyle = attrs.timeStyle ?: 'short'
        def type = attrs.type ?: 'datetime'

        out << """
        <div class="muted timestamp">
            <span class="date-created">${message(code: controllerName + '.dateCreated.label', default: 'Created')} ${
            formatDate(type: type, dateStyle: dateStyle, timeStyle: timeStyle, date: bean.dateCreated)
        }</span>
            <span class="date-updated">${message(code: controllerName + '.lastUpdated.label', default: 'Updated')} ${
            formatDate(type: type, dateStyle: dateStyle, timeStyle: timeStyle, date: bean.lastUpdated)
        }</span>
        </div>
        """
    }
}

class MenuVisibilityDelegate {
    def props
    def grailsApplication
    def model

    MenuVisibilityDelegate(app, m, Map concreteProps) {
        props = concreteProps
        grailsApplication = app
        model = m
    }

    /**
     * Return a predefined property or bean from the context
     */
    def propertyMissing(String name) {
        if (this.@props.containsKey(name)) {
            return this.@props[name]
        } else if (this.@model[name] != null) {
            return this.@model[name]
        } else {
            return this.@grailsApplication.mainContext.getBean(name)
        }
    }
}