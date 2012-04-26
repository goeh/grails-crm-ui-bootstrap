<!DOCTYPE html>
<html>
  <head>
    <g:set var="entityName" value="${message(code: controllerName + '.label', default: controllerName)}" />
    <title><g:layoutTitle default="Grails CRM" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <link rel="apple-touch-icon" href="${resource(dir:'images',file:'apple-touch-icon.png')}" />
    <bp:blueprintCss/>
    <g:javascript library="jquery" plugin="jquery"/>
    <jqui:resources themeCss="${resource(dir:'css/start',file:'jquery-ui-custom.css', plugin:'crm-base')}" />
    <script type="text/javascript" src="${resource(plugin:'jquery-ui', dir:'js/jquery/i18n', file:'jquery.ui.datepicker-sv.js')}"></script>
    <nav:resources override="true"/>
    <link rel="stylesheet" href="${resource(dir:'css',file:'crm.css', plugin:'crm-base')}" media="screen,projection"/>
    <script type="text/javascript" src="${resource(dir:'js',file:'crm-base.js', plugin:'crm-base')}"></script>

    <script type="text/javascript"><!--
      jQuery(document).ready(function(){
        var msgboard = $('#msgboard');
        msgboard.click(function() {
          $(this).slideUp(500, 'easeInOutQuad');
        });
        $('.msgboard_toggle').click(function(){
          $('#msgboard').slideToggle(500, 'easeInOutQuad');
          return false;
        });
        if(jQuery.trim(msgboard.text()) != "") {
          msgboard.show();
        }
        $('#content :input:visible:enabled:first').focus();
      });
    // --></script>
    <g:layoutHead />
    <style type="text/css">
      #content {
        margin-right: 10px;
      }
    </style>
    <g:pageProperty name="page.head" />
  </head>
<body>
  <div id="wrapper" data-role="page">
    <div id="header" data-role="header">

      <div class="top_bar">
        <form id="search" name="searchForm" style="float:right;margin:0 1em;" method="post" action="${createLink(controller:'searchable', action:'search')}">
          <a href="javascript:document.searchForm.submit();" id="searchsubmit" value="Go"><img src='${fam.icon(name: 'find')}' align="middle"/></a>
          <input type="text" size="30" maxlength="255" name="q" value="${params.q ?: 'Sök'}" onclick="clickclear(this,'Sök')" onblur="clickrecall(this,'Sök')"/>
        </form>
        <span id="current_user" style="float:right;margin:0 1em;padding:3px 0;">Göran Ehrsson</span>
        <span id="inbox_status" style="float:right;margin:0 1em;padding:3px 0;">Meddelanden (5)</span>
        <h3 id="appname"><span class="part1"><g:message code="application.name.1" default="Grails"/></span> <span class="part2"><g:message code="application.name.2" default="CRM"/></span></h3>
        <div class="clear"></div>
      </div>

      <div class="nav_bar">
        <div class="left msgboard_toggle">&nbsp;</div>
        <div class="right msgboard_toggle">&nbsp;</div>
        <div class="center">
        <nav:ifHasItems group="main">
          <ul class="navigation" id="navigation_main">
          <nav:eachItem group="main" var="item">
            <li>
              <g:link controller="${item.controller ?: controllerName}" action="${item.action}" title="${item.controller + '.' + item.action}">
                ${message(code: item.title ?: (item.controller + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
              </g:link>
            </li>
          </nav:eachItem>
          </ul>
        </nav:ifHasItems>
        </div>

        <div class="clear"></div>
      </div>


      <div id="msgboard" style="display:none;">
        <g:pageProperty name="page.crm-msgboard" />
        <g:if test="${flash.message}"><p class="flash">${message(code:flash.message, 'default':flash.defaultMessage ?: flash.message)}</p></g:if>
      </div>

    </div>

    <div id="container" data-role="content">

      <div id="leftside">

        <nav:ifHasItems group="${controllerName}">
          <ul class="navigation" id="navigation_${controllerName}">
          <nav:eachItem group="${controllerName}" var="item">
            <li>
              <g:link controller="${item.controller ?: controllerName}" action="${item.action}" id="${item.action?.startsWith('edit') ? params.id : null}">
                <img width="16" height="16" src="${fam.icon(name: message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action) + '._icon', default: message(code: (item.title ?: item.action) + '._icon', default:'application')))}" alt=""/>
                ${message(code: (item.title ?: (item.controller ?: controllerName) + '.' + item.action), default: message(code: item.controller, default: item.title ?: (item.controller + '.' + item.action)), args: [entityName])}
              </g:link>
            </li>
          </nav:eachItem>
          </ul>
        </nav:ifHasItems>

        <g:pageProperty name="page.crm-left" />

      </div>

      <div id="content">
        <g:layoutBody />
        <g:pageProperty name="page.crm-content" />
      </div>

    </div>

    <div id="footer" data-role="footer">
      <p class="small quiet center">
      <g:message code="application.name.1" default="Grails"/>
      <g:message code="application.name.2" default="CRM"/>
      - Copyright &copy ${new Date().year + 1900}
      <g:message code="application.company" default="Company Name"/> </p>
    </div>

  </div>
</body>
</html>