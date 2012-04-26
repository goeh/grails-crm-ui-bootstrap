<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}" />
  <title><g:message code="${beanName}.show.label" args="[entityName]" /></title>
</head>
<body>

  <div class="dialog pull">
    <h1><g:message code="${beanName}.show.title" args="[entityName, bean]" /></h1>

    <fieldset>
      <legend><g:message code="${beanName}.details.label" default="Details"/></legend>
      <label for="name"><g:message code="${beanName}.name.label"/></label>
      <span class="value">${fieldValue(bean:bean, field:'name')}</span>
      <label for="orderIndex"><g:message code="${beanName}.orderIndex.label"/></label>
      <span class="value">${bean.orderIndex}</span>
      <label for="icon"><g:message code="${beanName}.icon.label"/></label>
      <span class="value">${fieldValue(bean:bean, field:'icon')}<span class="icon">
        <g:if test="${bean.icon}">
          <img width="16" height="16" src="${fam.icon(name:bean.icon)}" alt="icon"/>
        </g:if>
      </span></span>
      <label for="param"><g:message code="${beanName}.param.label"/></label>
      <span class="value">${fieldValue(bean:bean, field:'param')}</span>
      <label for="enabled"><g:message code="${beanName}.enabled.label"/></label>
      <span class="value"><g:formatBoolean boolean="${bean.enabled}"/></span>
    </fieldset>

  </div>
</body>
</html>
