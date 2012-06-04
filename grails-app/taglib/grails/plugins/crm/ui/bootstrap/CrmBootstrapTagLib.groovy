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
package grails.plugins.crm.ui.bootstrap

import grails.plugins.crm.core.DateUtils
import grails.util.GrailsNameUtils
import grails.plugins.crm.core.TenantUtils

class CrmBootstrapTagLib {

    static namespace = "crm"

    def grailsApplication
    def crmSecurityService
    def navigationService
    def userTagService
    def selectionService
    def selectionRepositoryService

    def menu = {attrs, body ->
        out << '<ul class="navigation" id="navigation_main">'
        out << '<li><g:link controller="home" action="index">Hem</g:link></li>'
        out << '<li><g:link controller="company" action="index">Företag</g:link></li>'
        out << '<li><g:link controller="person" action="index">Person</g:link></li>'
        out << "</ul>"
    }

    def submenu = {attrs, body ->
        def entityName = g.message(code: controllerName + '.label', default: GrailsNameUtils.getNaturalName(controllerName))
        def menuTitle = attrs.title ?: entityName
        def items = navigationService.byGroup[attrs.group ?: controllerName]
        def dlg = new MenuVisibilityDelegate(grailsApplication, pageScope, [
                session: session,
                request: request,
                controllerName: controllerName,
                actionName: actionName,
                flash: flash,
                params: params
        ])
        def activePath = attrs.activePath ?: navigationService.reverseMapActivePathFor(controllerName, actionName, params)
        if (activePath instanceof String) {
            activePath = activePath.tokenize('/')
        }

        def empty = true

        items?.eachWithIndex { item, i ->

            if (!crmSecurityService.isPermitted((item.controller ?: controllerName) + ':' + item.action)) {
                return
            }

            // isVisible is closure or null/false
            def isVisible = item['isVisible']

            if (isVisible instanceof Closure) {
                isVisible = isVisible.clone()
                isVisible.delegate = dlg
                isVisible.resolveStrategy = Closure.DELEGATE_FIRST
            }

            if ((isVisible == null) || (isVisible == true) || isVisible.call()) {
                def id = item.id
                if (id instanceof Closure) {
                    id = id.clone();
                    id.delegate = dlg
                    id.resolveStrategy = Closure.DELEGATE_FIRST
                    id = id.call()
                }

                def active = pathIsActive(item.path, activePath)
                def title = message(code: item.controller + '.' + item.action + '.help', default: '')
                def icon = item.icon ?: message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action) + '_icon', default: message(code: ('default.' + item.action) + '_icon', default: 'icon-asterisk'))
                def label = g.message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])
                def elementId = "menu_${item.controller ?: controllerName}_${item.action}"
                def link = g.link(controller: item.controller ?: controllerName, action: item.action, id: id, elementId: elementId, title: title, "<i class=\"${icon}\"></i> " + label)

                if (empty) {
                    out << """<div class="well sidebar-nav">
            <ul class="nav nav-list">
                <li class="nav-header">${menuTitle}</li>
"""
                    empty = false
                }
                out << """<li class="${active ? 'active' : ''}">$link</li>"""
            }
        }

        def bodyContent = body()?.toString()?.trim()
        if (bodyContent) {
            if (empty) {
                out << """<div class="well sidebar-nav">
            <ul class="nav nav-list">
                <li class="nav-header">${menuTitle}</li>
"""
            }
            out << bodyContent
            empty = false
        }

        if (!empty) {
            out << "</ul></div>"
        }
    }

    protected pathIsActive(itemPath, currentPath) {
        def itemSize = itemPath?.size() ?: 0
        def activeSize = currentPath?.size() ?: 0
        if (itemSize && activeSize && (itemSize <= activeSize)) {
            return itemPath[0..itemSize - 1] == currentPath[0..itemSize - 1]
        } else {
            return false
        }
    }

    def selectionMenu = {attrs, body ->
        def username = crmSecurityService.currentUser?.username
        if (!username) {
            return
        }
        def location = attrs.location ?: controllerName
        def savedSelections = selectionRepositoryService.list(location, username, TenantUtils.tenant)
        def selection = attrs.selection ?: pageScope.selection
        def splitButton = (savedSelections || selection)
        def bodyContent = body()?.trim()
        if (!splitButton && !bodyContent) {
            return
        }

        out << """<div class="btn-group">"""

        if (bodyContent) {
            out << bodyContent
        }

        if (splitButton) {
            out << """<button class="btn ${attrs.visual ? 'btn-' + attrs.visual : ''} dropdown-toggle" data-toggle="dropdown">"""
            if (!bodyContent) {
                out << """<i class="icon-search ${attrs.visual ? 'icon-white' : ''}"></i> Sökningar """
            }
            out << """<span class="caret"></span></button>\n"""
            out << """<ul class="dropdown-menu">\n"""
            if (selection) {
                out << """<li>${link(action: "index", "Ny sökning")}</li>"""
                if(crmSecurityService.isPermitted("selectionRepository:create")) {
                    out << """<li>${
                        link(controller: 'selectionRepository', action: 'create',
                                params: [location: location,
                                        username: username,
                                        tenant: TenantUtils.tenant,
                                        uri: selectionService.encodeSelection(selection),
                                        referer: attrs.referer ?: request.forwardURI],
                                message(code: 'selectionRepository.save.label', default: 'Save')
                        )}
                    </li>"""
                }
            }
            if (savedSelections) {
                out << """<li><a href="#">Hantera sparade sökningar</a></li>"""
                out << """<li class="divider"></li>"""
                for (sel in savedSelections) {
                    out << "<li>"
                    out << select.link(controller: location, action: attrs.action ?: 'list', selection: sel.uri, sel.name)
                    out << "</li>"
                }
            }
            out << "</ul>"
        }
        out << "</div>"
        /*
            <g:link controller="selectionRepository" action="delete" id="${sel.id}"
                    class="pull-right delete" style="display:none;"
                    onclick="return confirm('${message(code:'default.button.delete.confirm.message', default:'Are you sure?')}')">
                <i class="icon-trash"></i></g:link>
          */
    }

    def header = {attrs, body ->
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

    def favoriteIcon = {attrs ->
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
        attrs.each {key, value ->
            if (value != null) {
                s << " ${key.encodeAsURL()}=\"${value.encodeAsURL()}\""
            }
        }
        s.toString()
    }

    def button = {attrs, body ->
        def props = takeAttributes(attrs, ['type', 'action', 'visual', 'class', 'icon', 'label', 'title', 'args',
                'confirm', 'style', 'controller', 'id', 'params', 'href', 'target', 'permission','group'])
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
        if(props.group) {
            out << "<div class=\"btn-group\">\n"
        }
        switch (type) {
            case 'button':
                out << '<button type="submit"'
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
                out << g.link(linkAttrs + attrs, label)
                break;
            case 'url':
                out << "<a href=\"${props.href}\""
                out << " class=\"${css.encodeAsHTML()}\""
                if (confirm) {
                    out << " onclick=\"return confirm('$confirm')\""
                }
                if (props.style) {
                    out << " style=\"${props.style}\""
                }
                if (title) {
                    out << " title=\"${title}\""
                }
                if (props.target) {
                    out << " target=\"${props.target}\""
                }
                out << "${renderAttributes(attrs)}>"
                if (icon) {
                    out << "<i class=\"${icon.encodeAsHTML()}\"></i> "
                }
                out << label
                out << "</a>\n"
                break
        }
        if(props.group) {
            out << "</div>\n"
        }
    }

    def iconSelect = { attrs ->
        attrs.from = SilkIcons.icons
        out << select(attrs) // use generic select
    }

    public static final Map<String, String> mimeTypeIconMap = [
            'text': 'page_white_text',
            'image': 'image',
            'video': 'film',
            'audio': 'cd',
            'message': 'email',
            'application/pdf': 'page_white_acrobat',
            'application/zip': 'page_white_compressed',
            'application/javascript': 'page_white_code',
            'application/json': 'page_white_code',
            'application/rtf': 'page_white_text',
            'application/msword': 'page_white_word',
            'application/vnd.ms-excel': 'page_white_excel',
            'application/vnd.ms-powerpoint': 'page_white_powerpoint',
            'application/vnd.ms-project': 'page_white_office',
            'application/vnd.oasis.opendocument.chart': 'chart_line',
            'application/vnd.oasis.opendocument.chart-template': 'chart_line',
            'application/vnd.oasis.opendocument.database': 'database',
            'application/vnd.oasis.opendocument.formula': 'sum',
            'application/vnd.oasis.opendocument.formula-template': 'sum',
            'application/vnd.oasis.opendocument.graphics': 'image',
            'application/vnd.oasis.opendocument.graphics-template': 'image',
            'application/vnd.oasis.opendocument.image': 'image',
            'application/vnd.oasis.opendocument.image-template': 'image',
            'application/vnd.oasis.opendocument.presentation': 'page_white_powerpoint',
            'application/vnd.oasis.opendocument.presentation-template': 'page_white_powerpoint',
            'application/vnd.oasis.opendocument.spreadsheet': 'page_white_excel',
            'application/vnd.oasis.opendocument.spreadsheet-template': 'page_white_excel',
            'application/vnd.oasis.opendocument.text': 'page_white_text',
            'application/vnd.oasis.opendocument.text-master': 'page_white_text',
            'application/vnd.oasis.opendocument.text-template': 'page_white_text',
            'application/vnd.oasis.opendocument.text-web': 'page_white_world'
    ]

    def fileIcon = {attrs ->
        def contentType = attrs.contentType
        if (!contentType) {
            throwTagError("Tag [fileIcon] is missing required attribute [contentType]")
        }
        def icon = mimeTypeIconMap[contentType]
        if (!icon) {
            icon = mimeTypeIconMap[contentType.split('/')[0]]
        }
        // Microsoft Office
        if ((!icon) && contentType.startsWith('application/vnd.openxmlformats-officedocument')) {
            if (contentType.contains('presentationml')) {
                icon = 'page_white_powerpoint'
            } else if (contentType.contains('spreadsheetml')) {
                icon = 'page_white_excel'
            } else if (contentType.contains('wordprocessingml')) {
                icon = 'page_white_word'
            } else {
                icon = 'page_white_office'
            }
        }
        if (!icon) {
            icon = attrs.default ?: 'page_white'
        }
        out << fam.icon(name: icon)
    }

    def weekNumber = { attrs ->
        def date = attrs.date
        if (!date) {
            throwTagError("Tag [weekNumber] is missing required attribute [date]")
        }
        if (!(date instanceof Date)) {
            date = DateUtils.parseDate(date.toString())
        }
        def locale = attrs.locale ?: request.locale
        def cal = Calendar.getInstance(locale)
        cal.setTime(date)
        out << cal.get(Calendar.WEEK_OF_YEAR).toString()
    }

    def countIndicator = { attrs ->
        def count = attrs.count ?: 0
        if (count instanceof Collection) {
            count = count.size()
        }
        if (count) {
            out << " ($count)"
        }
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