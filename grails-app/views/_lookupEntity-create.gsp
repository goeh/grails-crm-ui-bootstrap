<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}" />
  <title><g:message code="${beanName}.create.label" args="[entityName]" /></title>
  <g:render template="/iconAutocompleteScript" plugin="crm-base"/>
</head>
<body>

  <div class="dialog">
    <h1><g:message code="${beanName}.create.title" args="[entityName]" /></h1>

    <g:hasErrors bean="${bean}">
      <g:content tag="crm-alert">
        <div class="errors">
          <g:renderErrors bean="${bean}" as="list" />
        </div>
      </g:content>
    </g:hasErrors>

    <g:form action="save" >
      <fieldset>
        <legend><g:message code="${beanName}.details.label" default="Details"/></legend>
        <label for="name"><g:message code="${beanName}.name.label"/></label>
        <span class="value"><g:textField name="name" value="${bean.name}"/></span>
        <label for="description"><g:message code="${beanName}.description.label"/></label>
        <span class="value"><g:textArea name="description" cols="60" rows="3" value="${bean.description}"/></span>
        <label for="orderIndex"><g:message code="${beanName}.orderIndex.label"/></label>
        <span class="value"><g:textField name="orderIndex" value="${bean.orderIndex}"/></span>
        <label for="icon"><g:message code="${beanName}.icon.label"/></label>
        <span class="value"><g:textField name="icon" value="${bean.icon}"/><span class="icon"></span></span>
        <label for="param"><g:message code="${beanName}.param.label"/></label>
        <span class="value"><g:textField name="param" value="${bean.param}"/></span>
        <label for="enabled"><g:message code="${beanName}.enabled.label"/></label>
        <span class="value"><g:checkBox name="enabled" value="${bean.enabled}"/></span>
      </fieldset>

      <div class="buttons">
        <crm:button class="positive" action="save" icon="disk" message="${beanName}.button.save.label"/>
      </div>

    </g:form>
  </div>
</body>
</html>
