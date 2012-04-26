<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}"/>
  <title><g:message code="${beanName}.list.label" args="[entityName]"/></title>
</head>

<body>

<div class="dialog pull">
  <h1><g:message code="${beanName}.list.title" args="[entityName]"/></h1>

  <table class="list">
    <thead>
    <tr>
      <g:sortableColumn property="name" title="${message(code: beanName + '.name.label', default: 'Name')}"/>
      <g:sortableColumn property="orderIndex"
                        title="${message(code: beanName + '.orderIndex.label', default: 'Sort')}"/>
      <g:sortableColumn property="param" title="${message(code: beanName + '.param.label', default: 'Parameter')}"/>
      <g:sortableColumn property="icon" title="${message(code: beanName + '.icon.label', default: 'Icon')}"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${result}" status="i" var="m">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td><g:link action="show" id="${m.id}">${fieldValue(bean: m, field: "name")}</g:link></td>
        <td>${fieldValue(bean: m, field: "orderIndex")}</td>
        <td>${fieldValue(bean: m, field: "param")}</td>
        <td>${fieldValue(bean: m, field: "icon")}
          <g:if test="${m.icon}">
            <span class="icon"><img width="16" height="16" src="${fam.icon(name: m.icon)}" alt="icon"/></span>
          </g:if>
        </td>
      </tr>
    </g:each>
    </tbody>
    <tfoot class="paginateButtons">
    <tr>
      <td colspan="4"><g:paginate total="${totalCount}"/><span class="totalCount"><g:message
              code="default.totalCount.label" default="{0} records" args="[totalCount]"/></span></td>
    </tr>
    </tfoot>
  </table>

</div>

</body>
</html>
