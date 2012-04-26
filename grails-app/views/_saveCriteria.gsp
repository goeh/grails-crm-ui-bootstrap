<g:content tag="head">
  <script type="text/javascript"><!--
  $(document).ready(function() {
    var $dialog = $("#dialog").dialog({
        autoOpen: false,
        modal: true,
        title: "Spara sökfråga"
    });
    $(".menu-saveCriteria").click(function() {
        $dialog.dialog("open");
        return false; // prevent the default action, e.g., following a link
    });
    $("#cancel_dialog").click(function() {
        $dialog.dialog("close");
        return false; // prevent the default action, e.g., following a link
    });
  });
  // --></script>
</g:content>

<div id="dialog" style="display:none">
    <g:form action="saveCriteria">
      <g:hiddenField name="id" value="${params.id}"/>
      <g:hiddenField name="group" value="${controllerName}"/>
      <g:hiddenField name="username" value="unknown"/>
      <g:textField name="name" size="32" maxlength="100" value="${macro?.name}"/>
      <textarea name="description" cols="30" rows="3">
        <macro:macro macro="${params.id}">${description ?: name}</macro:macro>
      </textarea>
      <div class="buttons">
        <crm:button class="positive" action="saveCriteria" icon="disk" message="default.button.save.label" accesskey="S"/>
        <crm:button class="negative" id="cancel_dialog" icon="cancel" message="default.button.cancel.label" accesskey="B"/>
      </div>
    </g:form>
</div>