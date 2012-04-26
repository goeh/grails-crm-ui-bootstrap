<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <g:set var="entityName" value="${message(code: controllerName + '.label', default: controllerName)}"/>
    <title><g:layoutTitle
            default="${message(code:'application.name.1', default:'Grails')} ${message(code:'application.name.2', default:'CRM')}"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <r:require modules="crmUiStd"/>

    <r:script disposition="defer">

    jQuery(document).ready(function() {

        var msgboard = $("#msgboard");
        if (jQuery.trim(msgboard.text()) != "") {
            $.Growl.show(msgboard.html(), {
                'timeout': 3000
            });
        }

        var errorPanel = $("#alert");
        if (jQuery.trim(errorPanel.text()) != "") {
            errorPanel.show();
        }

        // Slide down tag panel when mouse is over.
        $("#tags").hoverIntent(function(event) {
            $("form", $(this)).slideDown('fast', function() {
                $("input:first", $(this)).focus();
            });
        }, function(event) {
            $("form", $(this)).slideUp('fast');
        });

        // Put cursor in first enabled input field.
        $('#content form :input:visible:enabled:first').focus();
    });
    </r:script>
    <g:layoutHead/>
    <r:layoutResources/>
    <%--
    <crm:pluginViews location="head"/>
    --%>
    <g:pageProperty name="page.head"/>
</head>

<body>
<div id="wrapper" data-role="page">

    <div id="header" data-role="header">

        <div id="top_bar" class="dark_gradient">
            <crm:user>
                <form id="search" class="right" name="searchForm" style="margin:0 1em;" method="post"
                      action="${createLink(controller: 'searchable')}">
                    <a href="javascript:document.searchForm.submit();" id="searchsubmit" value="Go" class="dismiss"><img
                            src='${fam.icon(name: 'find')}' align="middle"/></a>
                    <input type="text" size="30" maxlength="255" name="q" value="${params.q ?: 'Sök'}"
                           onclick="clickclear(this, 'Sök')" onblur="clickrecall(this, 'Sök')"/>
                </form>
                <ul id="current_user" class="dropdown right">
                    <li><a href="#">${name}<img src='${fam.icon(name: 'user')}' alt=""/></a>
                        <ul class="sub_menu">
                            <li><g:link mapping="logout"><img src='${fam.icon(name: 'door_open')}' alt=""/><g:message
                                    code="user.logout.label" default="Logout"/></g:link></li>
                            <li><g:link mapping="settings"><img src='${fam.icon(name: 'wrench')}' alt=""/><g:message
                                    code="user.settings.label" default="Settings"/></g:link></li>
                        </ul>
                    </li>
                </ul>
                <!--
                <span id="inbox_status" class="right" style="margin:0 1em;padding:3px 0;">Meddelanden (5)</span>
                -->
            </crm:user>
<%--
            <crm:hasPlugin name="macro">
                <macro:hasMacro category="${controllerName}">
                    <ul id="filters" class="dropdown right" style="display:none;"><!-- TODO!!! -->
                        <li><a href="#"><g:message code="default.filter.title" default="Filters"/>
                            <img src='${fam.icon(name: 'bullet_arrow_down')}' alt=""/></a>
                            <ul id="saved_macros" class="sub_menu">
                                <macro:eachMacro category="${controllerName}" var="m" status="i">
                                    <li><g:link action="loadMacro" id="${m.id}"><img
                                            src='${fam.icon(name: (m.icon ?: 'folder_find'))}'
                                            alt="Find"/>${m}</g:link></li>
                                </macro:eachMacro>
                            </ul>
                        </li>
                    </ul>
                </macro:hasMacro>
            </crm:hasPlugin>
--%>
            <g:pageProperty name="page.crm-top"/>

            <h3 id="appname">
                <span class="part1"><g:message code="application.name.1" default="Grails"/></span>
                <span class="part2"><g:message code="application.name.2" default="CRM"/></span>
                <img id="spinner" style="margin-left:1em;vertical-align:top;display:none;" width="128" height="15"
                     src="${resource(dir: 'images', file: 'ajax-loader.gif', plugin: 'crm-base')}" alt="....."/>
            </h3>

            <div class="clear"></div>

        </div>

        <div id="nav_bar">
            <crm:menu/>
<%--
            <ul class="navigation" id="navigation_main">
                <li><g:link mapping="home"><g:message code="default.home.label" default="Home"/></g:link></li>
                <nav:ifHasItems group="main">
                    <crm:user>
                        <nav:eachItem group="main" var="item">
                            <li>
                                <g:link controller="${item.controller ?: controllerName}" action="${item.action}"
                                        id="${item.id}"
                                        title="${message(code:item.controller + '.' + item.action + '.help')}">
                                    ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                </g:link>
                            </li>
                        </nav:eachItem>
                    </crm:user>
                </nav:ifHasItems>
                
            </ul>
--%>
            <div class="clear"></div>
        </div>

        <div id="alert" style="display:none;">
            <g:pageProperty name="page.crm-alert"/>
            <g:if test="${flash.error}"><p
                    class="flash">${message(code: flash.error, 'default': flash.defaultError ?: flash.error, args: flash.args)}</p></g:if>
        </div>

        <div id="msgboard" style="display:none;">
            <g:pageProperty name="page.crm-msgboard"/>
            <g:if test="${flash.message}"><p
                    class="flash">${message(code: flash.message, 'default': flash.defaultMessage ?: flash.message, args: flash.args)}</p></g:if>
        </div>

    </div> <!-- end of header -->

    <div id="container" data-role="content">

        <div class="leftside">
<%--
            <nav:ifHasItems group="${controllerName ?: 'home'}">
                <div class="crm-menu">
                    <h3><g:message code="default.function.title" default="Commands"/></h3>
                    <ul id="navigation_${controllerName}">
                        <nav:eachItem group="${controllerName ?: 'home'}" var="item">
                            <li>
                                <g:link controller="${item.controller ?: controllerName}" action="${item.action}"
                                        id="${item.id}"
                                        elementId="menu_${item.controller ?: controllerName}_${item.action}"
                                        class="menu-${item.action}">
                                    <img width="16" height="16"
                                         src="${fam.icon(name: message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action) + '._icon', default: message(code: (item.title ?: item.action) + '._icon', default: 'application')))}"
                                         alt=""/>
                                    ${message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                </g:link>
                            </li>
                        </nav:eachItem>
                    </ul>
                </div>
            </nav:ifHasItems>
--%>
            <crm:user>
                <crm:hasPlugin name="domainTracker">
                    <g:render template="/domainTracker" plugin="crm-base"/>
                </crm:hasPlugin>
            </crm:user>

            <%-- <crm:pluginViews location="left"/> --%>

            <g:pageProperty name="page.crm-left"/>

        </div>

        <div class="rightside">

            <%-- <crm:pluginViews location="right"/> --%>

            <g:pageProperty name="page.crm-right"/>

        </div>

        <div id="content" class="middle">
            <g:layoutBody/>
            <g:pageProperty name="page.crm-content"/>
            <%-- <crm:pluginViews location="content"/> --%>
        </div>

        <div class="clear"></div>

    </div> <!-- end of container -->
</div> <!-- end of wrapper -->

<div id="footer" data-role="footer">
    <div class="leftside">
        <ul>
            <li>
                <a href="http://www.technipelago.se/">
                    About
                    <g:message code="application.name.1" default="Grails"/>
                    <g:message code="application.name.2" default="CRM"/>
                </a>
            </li>
            <li>
                <a href="http://www.technipelago.se/">Technipelago AB</a>
            </li>
        </ul>
    </div>

    <div class="rightside">
        <ul>
        </ul>
    </div>

    <div id="footer-content" class="middle center">
        <g:message code="application.name.1" default="Grails"/>
        <g:message code="application.name.2" default="CRM"/>
        - Copyright &copy ${new Date().year + 1900}
        <g:message code="application.company" default="Company Name"/>
    </div>
</div>

<div id="fbg"></div>
<g:javascript library="application"/>
<r:layoutResources/>
</body>
</html>