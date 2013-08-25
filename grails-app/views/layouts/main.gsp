<%@ page import="org.apache.commons.lang.StringUtils; grails.plugins.crm.core.TenantUtils; grails.util.GrailsNameUtils;" %><!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta charset="utf-8">
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <r:require modules="application, crm"/>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <link rel="apple-touch-icon" sizes="228x228" href="${resource(dir: 'images', file: 'apple-touch-icon-228.png')}">
    <link rel="apple-touch-icon" sizes="144x144" href="${resource(dir: 'images', file: 'apple-touch-icon-144.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-114.png')}">
    <link rel="apple-touch-icon" sizes="72x72" href="${resource(dir: 'images', file: 'apple-touch-icon-72.png')}">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon-57.png')}">

    <r:script>

        $(document).ajaxError(function(e, xhr, settings, exception) {

            var message = '';

            if (xhr.status == 0) {
                message = 'You are offline!\n Please check your network.';
            } else if (xhr.status == 403) {
                window.location.href = "${createLink(mapping: 'start', absolute: true)}";
                return;
            } else if (xhr.status == 404) {
                message = 'Requested URL not found.';
            } else if (xhr.status == 500) {
                message = xhr.responseText;
            } else if (errStatus == 'parsererror') {
                message = 'Error.\nParsing JSON Request failed.';
            } else if (errStatus == 'timeout') {
                message = 'Request timed out.\nPlease try later';
            } else {
                message = ('Unknown Error.\n' + xhr.responseText);
            }

            alert(message);
        });

        $(document).ready(function() {
            $("#navigation_notifications .notification-delete a").click(function(ev) {
                var item = $(this).closest(".notification-item");
                $.post("${createLink(controller:'crmNotification', action:'delete')}", {id:item.data('crm-id')}, function(data) {
                    item.remove();
                    var count = data.count;
                    if(count > 0) {
                        $("#notifictions-unread-count").text(count);
                    } else {
                        $("#navigation_notifications").remove();
                    }
                });
            });

            $(".recent-clear").click(function(ev) {
                ev.stopImmediatePropagation();
                $.post($(this).attr('href'), function(data) {
                    location.reload();
                });
                return false;
            });

<% if(flash.alert) { %>
            $('#alertModal').modal({show:true});
<% } %>
        });
    </r:script>
    <r:layoutResources/>
    <g:layoutHead/>
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
          <script src="${resource(dir:'js', file: 'html5.js')}"></script>
    <![endif]-->
</head>

<body class="${controllerName ?: 'home'}-body">

    <div id="head-wrapper" class="clearfix">
        <div class="row-fluid">
            <div class="span4">
                <div id="brand" class="visible-desktop">
                    <g:link uri="/"><crm:logo size="large"/></g:link>
                </div>
            </div>
            <div class="span8">
                <recent:hasHistory>
                    <div class="recent-list pull-right clearfix">
                        <recent:each var="m" max="5" reverse="true">
                            <span class="${m.tags ? 'label' : ''}">
                                <g:link class="${m.controller}"
                                        controller="${m.controller}" action="${m.action}" id="${m.id}"
                                        title="${message(code:m.controller + '.click.to.show.label', default:'Click to show {0}', args:[m])}">
                                        <i class="${m.icon ?: 'icon-chevron-right'}"></i><g:decorate include="abbreviate" max="20">${m}</g:decorate>
                                </g:link>
                                <g:if test="${m.tags}">
                                    <g:link controller="recentDomain" action="clear" params="${[type: m.type, id: m.id]}" class="recent-clear">&times;</g:link>
                                </g:if>
                            </span>
                        </recent:each>
                    </div>
                </recent:hasHistory>

                <g:pageProperty name="page.top"/>

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
    </div>

    <div class="navbar" id="navigation-wrapper">
        <div class="navbar-inner">
            <div class="container">

                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

                <g:link mapping="home" class="brand hidden-desktop"><g:message code="app.name" default="Grails CRM"/></g:link>

                <div class="nav-collapse">

                    <nav:ifHasItems group="main">
                        <ul class="nav" id="navigation_main">
                            <crm:user>
                            <li><g:link mapping="home"><i class="icon-home icon-white"></i></g:link></li>
                            </crm:user>
                            <nav:eachItem group="main" var="item">
                                <crm:hasPermission permission="${item.controller + ':' + item.action + (item.id ? ':' + item.id : '')}">
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

                    <crm:tenant><g:set var="tenantName" value="${name}"/></crm:tenant>

                    <crm:user>
    <%--
                        <form class="navbar-search pull-right" action="${createLink(mapping:'logout')}">
                            <button id="logout-button" class="btn btn-small"><i class="icon-off"></i></button>
                        </form>
    --%>
                        <ul class="nav pull-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${(tenantName ?: name).encodeAsHTML()}<b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu">

                                    <li><g:link mapping="logout" title="${message(code: 'auth.logout.title', default: 'Logout {0}', args:[name])}">
                                        <g:message code="auth.logout.label" default="Logout" args="${[name]}"/>
                                    </g:link>
                                    </li>
                                    <li><g:link mapping="crm-user-settings" title="${message(code:'crmSettings.index.help', default:'User Settings')}">
                                        ${message(code: 'crmSettings.label', default: 'Settings', args:[name])}
                                    </g:link>
                                    </li>

                                    <nav:eachItem group="settings" var="item">
                                        <crm:hasPermission permission="${item.controller + ':' + item.action + (item.id ? ':' + item.id : '')}">
                                            <li class="${item.active || (item.controller == controllerName) ? 'active' : ''}">
                                                <g:link controller="${item.controller ?: controllerName}" action="${item.action}"
                                                        id="${item.id}"
                                                        title="${message(code:item.controller + '.' + item.action + '.help')}">
                                                    ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
                                                </g:link>
                                            </li>
                                        </crm:hasPermission>
                                    </nav:eachItem>

                                    <li class="divider"></li>

                                    <li><g:link mapping="crm-tenant" title="${message(code:'crmTenant.index.help', default:'Tenant Settings')}"><g:message code="crmTenant.index.label" default="Tenants"/></g:link></li>

                                    <li class="divider"></li>

                                    <crm:eachTenant var="a">
                                        <li class="${a.current ? 'current' : ''}">
                                            <g:link mapping="crm-tenant-activate" id="${a.id}">
                                                ${a.name.encodeAsHTML()}
                                            </g:link>
                                        </li>
                                    </crm:eachTenant>
                                </ul>
                            </li>
                        </ul>

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

                        <crm:tenant>
                            <nav:ifHasItems group="admin">
                                <ul class="nav pull-right" id="navigation_admin">
                                    <li class="dropdown">
                                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                            <g:message code="default.admin.menu.label"/><b class="caret"></b>
                                        </a>
                                        <ul class="dropdown-menu">

                                            <nav:eachItem group="admin" var="item">
                                                <crm:hasPermission permission="${item.controller + ':' + item.action + (item.id ? ':' + item.id : '')}">
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
                        </crm:tenant>

                        <plugin:isAvailable name="crm-notification">
                            <crm:hasUnreadNotifications username="${username}" tenant="${TenantUtils.tenant}">
                                <ul class="nav pull-right" id="navigation_notifications" role="menu">
                                    <li class="dropdown">
                                        <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">
                                            <span class="badge badge-important" id="notifictions-unread-count">${count}</span>
                                        </a>
                                        <ul class="dropdown-menu" role="menu">
                                            <crm:eachNotification username="${username}" tenant="${TenantUtils.tenant}" var="n">
                                                <li class="dropdown-submenu notification-item" data-crm-id="${n.id}">
                                                    <a href="javascript:void(0);" tabindex="-1">${n.dateCreated.format('d MMM HH:mm')} - ${StringUtils.abbreviate(n.subject, 30)}</a>
                                                    <ul class="dropdown-menu">
                                                        <g:each in="${n.payload?.links}" var="l">
                                                            <li>
                                                                <g:link controller="${l.controller ?: controllerName}" action="${l.action ?: ''}" title="${l.title ?: ''}">
                                                                    <i class="${l.icon ?: 'icon-chevron-right'}"></i>
                                                                    ${l.label}
                                                                </g:link>
                                                            </li>
                                                        </g:each>
                                                        <li class="notification-delete">
                                                            <a href="javascript:void(0);">
                                                                <i class="icon-trash"></i>
                                                                <g:message code="crmNotification.button.delete.label" default="Delete"/>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </li>
                                            </crm:eachNotification>
                                        </ul>
                                    </li>
                                </ul>
                            </crm:hasUnreadNotifications>
                        </plugin:isAvailable>
                    </crm:user>


                    <crm:noUser>

                        <g:form controller="auth" action="signIn" name="loginBar" class="navbar-search pull-right">
                            <input type="hidden" name="targetUri" value="${targetUri}"/>
                            <!--[if lt IE 10]>
                            <span><g:message code="auth.login.username"/></span>
                            <![endif]-->
                            <g:textField id="login-username" name="username" value="${username}" autocapitalize="off"
                                         placeholder="${message(code:'auth.login.username', default:'Username...')}"
                                         class="search-query input-small"/>
                            <!--[if lt IE 10]>
                            <span><g:message code="auth.login.password"/></span>
                            <![endif]-->
                            <g:passwordField id="login-password" name="password" value=""
                                             placeholder="${message(code:'auth.login.password', default:'Password...')}"
                                             class="search-query input-small"/>
                            <button id="login-button" type="submit" class="btn btn-small" style="margin-top:0px;"><i class="icon-play"></i></button>
                        </g:form>

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

                    </crm:noUser>

                    <g:pageProperty name="page.navbar"/>

                </div>
            </div>
        </div>
    </div>

    <div class="${grailsApplication.config.crm.ui.bootstrap.fluid ? 'container-fluid' : 'container'}">

        <g:pageProperty name="page.hero"/>

        <div class="controller-${controllerName ?: 'home'} action-${actionName ?: 'index'}" id="content-wrapper"
             role="main">
            <g:layoutBody/>
        </div>

        <div id="footer-wrapper">
            <footer>
                <div id="copyright"><g:message code="app.copyright.message" default=""/> (${TenantUtils.tenant})</div>
            </footer>
        </div>
    </div>

<g:if test="${flash.alert}">
    <div class="modal hide fade" id="alertModal">

        <div class="modal-header">
            <a class="close" data-dismiss="modal">×</a>

            <h3><g:message code="alert.title" default="Message"/></h3>
        </div>

        <div class="modal-body">
            <p>${flash.alert.encodeAsHTML()}</p>
        </div>

        <div class="modal-footer">
            <a href="#" class="btn btn-primary" data-dismiss="modal"><g:message
                    code="default.button.ok.label" default="Ok"/></a>
        </div>

    </div>
</g:if>

<div id="spinner" class="spinner" style="display:none;"><g:img dir="images" file="spinner.gif" alt="${message(code: 'spinner.alt', default: 'Loading&hellip;')}"/></div>

<r:layoutResources/>

</body>
</html>
