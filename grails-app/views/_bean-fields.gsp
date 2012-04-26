<!-- bean-fields templates -->
<bean:maxInputLength>40</bean:maxInputLength>

<bean:labelTemplate>
<label for="${fieldId}">${message(code:beanName + '.' + propertyName + '.label', 'default':beanName + ' ' + propertyName)}${required}</label>
</bean:labelTemplate>

<bean:inputTemplate>
  <div title="${message(code:beanName + '.' + propertyName + '.help', 'default':beanName + ' ' + propertyName)}">
  ${label}${field}
  </div>
</bean:inputTemplate>

<bean:selectTemplate>
  <div title="${message(code:beanName + '.' + propertyName + '.help', 'default':beanName + ' ' + propertyName)}">
  ${label}${field}
  </div>
</bean:selectTemplate>

<bean:textAreaTemplate>
  <div title="${message(code:beanName + '.' + propertyName + '.help', 'default':beanName + ' ' + propertyName)}">
  ${label}${field}
  </div>
</bean:textAreaTemplate>

<bean:checkBoxTemplate>
  <div title="${message(code:beanName + '.' + propertyName + '.help', 'default':beanName + ' ' + propertyName)}">
  ${label}${field}
  </div>
</bean:checkBoxTemplate>