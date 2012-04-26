<%@ page import="grails.plugins.crm.core.TenantUtils; grails.util.GrailsNameUtils; org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <r:require modules="application, style, crmUiBootstrap"/>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114"
          href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <r:layoutResources/>
    <g:layoutHead/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
          <style type="text/css">
            button.btn-dummy {
              visibility: visible;
              height:22px;
              padding: 1px 3px 6px 3px;
              color: white;
              background-color: #00a9dd;
              border: none;
            }
            </style>
    <![endif]-->

</head>

<body class="${controllerName ?: 'home'}-body">

<div class="container-fluid">

<div class="row-fluid">
    <div class="span4">
        <div id="brand" class="visible-desktop"></div>
    </div>

    <div class="span8">
        <div id="global-message" class="hide">
            <g:if test="${flash.info || flash.message}">
                <div class="alert-info">
                    ${flash.info ?: flash.message}
                </div>
            </g:if>
            <g:if test="${flash.success}">
                <div class="alert-success">
                    ${flash.success}
                </div>
            </g:if>
            <g:if test="${flash.warning}">
                <div class="alert-warning">
                    ${flash.warning}
                </div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert-error">
                    ${flash.error}
                </div>
            </g:if>
        </div>
    </div>
</div>

<div class="navbar">
    <div class="navbar-inner">
        <div class="row-fluid">

            <crm:user>
                <g:link class="btn btn-navbar pull-right hidden-desktop" style="padding: 5px 12px 2px 12px;"
                        mapping="logout">
                    <span class="icon-off icon-white" style="height:13px;line-height:13px;"></span>
                </g:link>
            </crm:user>

            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>

            <span class="hidden-desktop"><a class="brand" href="${createLink(uri: '/')}"><g:message code="app.name"
                                                                                                    default="Grails CRM"/></a>
            </span>

            <div class="nav-collapse">

                <nav:ifHasItems group="main">
                    <ul class="nav" id="navigation_main">
                        <nav:eachItem group="main" var="item">
                            <crm:hasPermission permission="${item.controller + ':' + item.action}">
                                <li class="${item.active || (item.controller == controllerName) ? 'active' : ''}">
                                    <g:link controller="${item.controller ?: controllerName}" action="${item.action}"
                                            id="${item.id}"
                                            title="${message(code:item.controller + '.' + item.action + '.help')}">
                                        ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                    </g:link>
                                </li>
                            </crm:hasPermission>
                        </nav:eachItem>
                    </ul>
                </nav:ifHasItems>

                <crm:user>
                    <g:set var="fullname" value="${name}"/>
                    <crm:tenant>
                        <ul class="nav pull-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${fullname.encodeAsHTML()}<b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu">

                                    <li><g:link controller="auth" action="logout"
                                                title="${fullname.encodeAsHTML()}">
                                        <g:message code="auth.logout.label"
                                                   default="Logout"/>
                                    </g:link>
                                    </li>
                                    <li><g:link controller="settings" action="index">
                                        ${message(code: 'user.settings.label', default: 'Settings')}
                                    </g:link>
                                    </li>

                                    <li class="divider"></li>

                                    <li><g:link controller="account" action="index"><g:message
                                            code="account.index.label"
                                            default="Accounts"/></g:link>
                                    </li>
                                    <crm:eachAccount var="a">
                                        <li>
                                            <g:link controller="account" action="activate"
                                                    id="${a.id}">${a.name.encodeAsHTML()}
                                                <g:if test="${a.current}"><i class="icon-arrow-left"></i></g:if>
                                            </g:link>
                                        </li>
                                    </crm:eachAccount>
                                </ul>
                            </li>
                        </ul>
                    </crm:tenant>

                    <ul class="nav pull-right" id="navigation_favorites">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <g:message code="default.favorites.menu.label"/><b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <g:set var="prevFav" value=""/>
                                <usertag:eachTagged tag="favorite" tenant="${TenantUtils.tenant}" username="${username}"
                                                    var="fav">
                                    <g:set var="thisFav"
                                           value="${GrailsNameUtils.getPropertyName(fav.class)}"/>
                                    <g:if test="${thisFav != prevFav}">
                                        <g:set var="prevFav" value="${thisFav}"/>
                                        <li style="color:#999;padding-left:16px;font-size:10px;text-transform:uppercase;">${message(code: thisFav + '.label', default: GrailsNameUtils.getNaturalName(thisFav))}</li>
                                    </g:if>
                                    <li>
                                        <g:link controller="${grails.util.GrailsNameUtils.getPropertyName(fav.class)}"
                                                action="show" id="${fav.id}">${fav.encodeAsHTML()}</g:link>
                                    </li>
                                </usertag:eachTagged>
                                <nav:ifHasItems group="public">
                                    <g:if test="${prevFav}">
                                        <li class="divider"></li>
                                    </g:if>
                                    <nav:eachItem group="public" var="item">
                                        <li>
                                            <g:link controller="${item.controller ?: controllerName}"
                                                    action="${item.action}"
                                                    id="${item.id}"
                                                    title="${message(code:item.controller + '.' + item.action + '.help')}">
                                                ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                            </g:link>
                                        </li>
                                    </nav:eachItem>
                                </nav:ifHasItems>
                            </ul>
                        </li>
                    </ul>

                    <nav:ifHasItems group="admin">
                        <ul class="nav pull-right" id="navigation_admin">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <g:message code="default.admin.menu.label"/><b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu">

                                    <nav:eachItem group="admin" var="item">
                                        <crm:hasPermission permission="${item.controller + ':' + item.action}">
                                            <li class="${item.active || (item.controller == controllerName && item.action == actionName) ? 'active' : ''}">
                                                <g:link controller="${item.controller ?: controllerName}"
                                                        action="${item.action}"
                                                        id="${item.id}"
                                                        title="${message(code:item.controller + '.' + item.action + '.help')}">
                                                    ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                                </g:link>
                                            </li>
                                        </crm:hasPermission>
                                    </nav:eachItem>
                                </ul>
                            </li>
                        </ul>
                    </nav:ifHasItems>

                    <crm:hasPermission permission="search:global">
                        <g:form class="navbar-search pull-right">
                            <g:textField name="q" class="search-query span2" placeholder="Sök"/>
                            <button id="search-button" class="btn-dummy" type="submit">&raquo;</button>
                        </g:form>
                    </crm:hasPermission>

                </crm:user>


                <crm:noUser>

                    <nav:ifHasItems group="public">
                        <ul class="nav" id="navigation_public">
                            <nav:eachItem group="public" var="item">
                                <li class="${item.active || (item.controller == controllerName && item.action == actionName) ? 'active' : ''}">
                                    <g:link controller="${item.controller ?: controllerName}"
                                            action="${item.action}"
                                            id="${item.id}"
                                            title="${message(code:item.controller + '.' + item.action + '.help')}">
                                        ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                    </g:link>
                                </li>
                            </nav:eachItem>
                        </ul>
                    </nav:ifHasItems>

                    <g:form controller="auth" action="signIn" name="loginBar"
                            class="navbar-search pull-right visible-desktop">
                        <input type="hidden" name="targetUri" value="${targetUri}"/>
                        <g:textField id="login-username" name="username" value="${username}"
                                     placeholder="${message(code:'auth.login.username', default:'Username...')}"
                                     class="search-query span2"/>
                        <g:passwordField id="login-password" name="password" value=""
                                         placeholder="${message(code:'auth.login.password', default:'Password...')}"
                                         class="search-query span2"/>
                        <button id="login-button" class="btn-dummy" type="submit">&raquo;</button>
                    </g:form>

                </crm:noUser>

                <g:pageProperty name="page.navbar"/>

            </div>
        </div>
    </div>
</div>

<div class="row-fluid">

    <div class="span9">

        <div id="content" class="${controllerName ?: 'home'}-content" role="main">
            <g:layoutBody/>
        </div>

    </div>

    <div class="span3">

        <nav:ifHasItems group="${controllerName ?: 'home'}">
            <div class="well sidebar-nav">

                <ul id="navigation_${controllerName}" class="nav nav-list">
                    <li class="nav-header"><g:message code="sidebar.commands.label"/></li>

                    <nav:eachItem group="${controllerName ?: 'home'}" var="item">
                        <li class="${item.active ? 'active' : ''}">
                            <g:set var="icon"
                                   value="${message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action) + '._icon', default: message(code: (item.title ?: item.action) + '._icon', default: ''))}"/>
                            <g:link controller="${item.controller ?: controllerName}" action="${item.action}"
                                    id="${item.id}"
                                    elementId="menu_${item.controller ?: controllerName}_${item.action}"
                                    class="menu-${item.action}">
                                <g:if test="${icon}"><i class="${icon}"></i></g:if>
                                ${message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                            </g:link>
                        </li>
                    </nav:eachItem>

                </ul>
            </div>

        </nav:ifHasItems>

        <g:pageProperty name="page.sidebar"/>

    </div>

</div>

<div class="row-fluid">
    <footer>

        <div class="pull-right">&nbsp;[${TenantUtils.tenant}]</div>

        <div id="copyright" class="pull-right"><g:message code="app.copyright.message" default=""/></div>
        <!--
        <crm:user>
            <nav:ifHasItems group="public">
                <ul class="nav nav-pills" id="navigation_footer">
                <nav:eachItem group="public" var="item">
                    <li>
                    <g:link controller="${item.controller ?: controllerName}"
                            action="${item.action}"
                            id="${item.id}"
                            title="${message(code:item.controller + '.' + item.action + '.help')}">
                        ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                    </g:link>
                    </li>
                </nav:eachItem>
                </ul>
            </nav:ifHasItems>
        </crm:user>
        -->
    </footer>
</div>

</div>

<r:layoutResources/>

</body>
</html>