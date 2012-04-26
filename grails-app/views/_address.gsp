<g:each in="${bean.address}" var="a" status="i">
  <span class="value" title="${a.name.encodeAsHTML()}">
    <g:if test="${a.latitude && a.longitude}">
      <a href="http://maps.google.com/?ll=${a.latitude},${a.longitude}&z=16" target="map"
         title="${a.latitude},${a.longitude}"><img src="${fam.icon(name: 'map')}" alt="${a.name.encodeAsHTML()}"/> ${a.encodeAsHTML()}</a>
    </g:if>
    <g:unless test="${a.latitude && a.longitude}">
      <a href="http://maps.google.com/?q=${a.getAddress(false).encodeAsHTML()}, ${a.country ? '' : 'Sverige'}&z=16"
         target="map"><img src="${fam.icon(name: 'map')}" alt="${a.name.encodeAsHTML()}"/> ${a.encodeAsHTML()}</a>
    </g:unless>
  </span>
</g:each>
<g:each in="${bean.telephone}" var="a" status="i">
  <span class="value" title="${a.name.encodeAsHTML()}"><a href="sip:${a}"><img src="${fam.icon(name: 'telephone')}" alt="${a.name.encodeAsHTML()}"/>
    ${a.encodeAsHTML()}</a>
  </span>
</g:each>
<g:each in="${bean.email}" var="a" status="i">
  <span class="value" title="${a.name.encodeAsHTML()}"><a href="mailto:${a}"><img src="${fam.icon(name: 'email')}" alt="${a.name.encodeAsHTML()}"/>
    ${a.encodeAsHTML()}</a>
  </span>
</g:each>
<g:each in="${bean.url}" var="a" status="i">
  <span class="value" title="${a.name.encodeAsHTML()}"><a href="${a.toURL()}"><img src="${fam.icon(name: 'world')}" alt="${a.name.encodeAsHTML()}"/>
    ${a.encodeAsHTML()}</a>
  </span>
</g:each>
