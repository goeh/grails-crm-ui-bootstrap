package grails.plugins.crm.ui.bootstrap

import grails.test.mixin.TestFor
import org.junit.Test

@TestFor(CrmBootstrapTagLib)
class PrettyTimeTests {

    @Test
    void testOneDayAgo() {
        assert applyTemplate('<crm:prettyTime date="\${date}" locale="\${locale}"/>', [locale: new Locale('en'), date: new Date() - 1]).trim() == "1 day ago"

        assert applyTemplate('<crm:prettyTime date="\${date}" locale="\${locale}"/>', [locale: new Locale('sv'), date: new Date() - 1]).trim() == "1 dag sedan"
        assert applyTemplate('<crm:prettyTime date="\${date}" locale="sv"/>', [date: new Date() - 2]).trim() == "2 dagar sedan"

        assert applyTemplate('<crm:prettyTime date="\${date}" locale="\${locale}"/>', [locale: new Locale('fi'), date: new Date() - 1]).trim() == "eilen"
        assert applyTemplate('<crm:prettyTime date="\${date}" locale="fi"/>', [date: new Date() - 2]).trim() == "2 päivää sitten"
    }
}
