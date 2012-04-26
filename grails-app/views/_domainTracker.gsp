<domaintracker:hasHistory>
<div class="crm-menu">
  <h3><g:message code="domaintracker.history" default="Crumb Trail"/></h3>
  <ul id="crumbTrail">
    <domaintracker:each reverse="true" var="m" max="10">
      <li>
        <g:link controller="${m.controller}" action="${m.action}" id="${m.id}"><img src="${fam.icon(name: m.icon ?: 'bullet_black')}" alt=""/>${m.abbreviate(20)}</g:link>
      </li>
    </domaintracker:each>
  </ul>
</div>
</domaintracker:hasHistory>