<%@ page import="grails.plugins.crm.core.TenantUtils; grails.util.GrailsNameUtils;" %><!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <r:require modules="application, crm"/>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <g:each in="${grailsApplication.config.crm.ui.apple.icons ?: ['': 'apple-touch-icon.png']}" var="icon">
        <link rel="apple-touch-icon" sizes="${icon.key}" href="${resource(dir: 'images', file: icon.value)}">
    </g:each>

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

    <% if (flash.alert) { %>
        $(document).ready(function () {
            $('#alertModal').modal({show:true});
        });
    <% } %>
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
                <span class="brand"><crm:logo/></span>
            </div>
        </div>

        <div class="span8">

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

            <g:link mapping="home" class="brand hidden-desktop"><g:message code="app.name"
                                                                           default="GR8 CRM"/></g:link>

            <div class="nav-collapse collapse">
                <crm:user>
                    <ul class="nav pull-right">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                ${name.encodeAsHTML()}<b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">

                                <li><g:link controller="auth" action="logout"
                                            title="${name.encodeAsHTML()}">
                                    <g:message code="auth.logout.label"
                                               default="Logout"/>
                                </g:link>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </crm:user>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">

    <g:pageProperty name="page.hero"/>

    <div class="controller-${controllerName ?: 'home'} action-${actionName ?: 'index'}" id="content-wrapper"
         role="main">
        <g:layoutBody/>
    </div>

    <div id="footer-wrapper">
        <g:render template="/footer"/>
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

<r:layoutResources/>

</body>
</html>
