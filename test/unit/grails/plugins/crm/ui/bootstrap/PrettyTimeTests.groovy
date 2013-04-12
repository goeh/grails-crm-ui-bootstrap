package grails.plugins.crm.ui.bootstrap

import grails.test.mixin.TestFor
import org.junit.Test

@TestFor(CrmBootstrapTagLib)
class PrettyTimeTests {

    @Test
    void testOneDayAgo() {
        assert applyTemplate('<crm:prettyTime date="\${date}"/>', [date:new Date() - 1]).trim() == "1 day ago"
        request.addPreferredLocale(new Locale('sv_SE'))
        assert applyTemplate('<crm:prettyTime date="\${date}"/>', [date:new Date() - 1]).trim() == "1 dag sedan"
        assert applyTemplate('<crm:prettyTime date="\${date}"/>', [date:new Date() - 2]).trim() == "2 dagar sedan"
    }
}
