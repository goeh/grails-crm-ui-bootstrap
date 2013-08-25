package grails.plugins.crm.ui.bootstrap

import org.junit.Test

/**
 * Test the Country class.
 */
class CountryTests {

    @Test
    void testCountyClass() {
        def c = new Country("SE", "Sweden")
        assert c.code == "SE"
        assert c.name == "Sweden"
        def map = c as Map
        assert map.code == "SE"
        assert map.name == "Sweden"
    }

    @Test
    void testSort() {
        def locales = Locale.getAvailableLocales().findAll{it.country}
        def from = locales.collect { locale -> new Country(locale.country, locale.displayCountry) }.unique()
        def sortKey = 'code'
        from = from.toList().sort { sortKey ? it."${sortKey}" : it.toString() }
        assert from[0].code == 'AE'
    }
}
