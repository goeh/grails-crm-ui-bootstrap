<script type="text/javascript"><!--
function silkIcon(name) {
  return "<img width=\"16\" height=\"16\" src=\"${resource(dir:'/images/icons/', plugin:'famfamfam')}" + name + ".png\"/>";
}
jQuery(document).ready(function() {
  // Add icon preview image when selected.
  $("${selector ?: 'input[name=\'icon\']"'}).autocomplete({
    source:"${createLink(controller:'silk', action:'autocomplete')}",
    select: function(event, ui) {
      $(this).next('span.icon').html(silkIcon(ui.item.value));
    }
  });
  // Show icon preview image if field is non-null.
  var preview = $('span.icon');
  if(preview) {
    var name = preview.prev('input').val();
    if(name) {
      preview.html(silkIcon(name));
    }
  }
});
// --></script>