class GroovySyntaxTest extends GroovyTestCase {
    void testJSON() {
        def obj = [1, 2, 3, [a: 1, b: 2]]

        assertEquals 1, obj[0]
        assertEquals 2, obj[3]['b']
        assertEquals 1, obj[3]['a']
    }

    void testJSONParsing() {
        def builder = new groovy.json.JsonBuilder()
        def root = builder.people {
            person {
                firstName 'Guillame'
                lastName 'Laforge'
                // Named arguments are valid values for objects too
                address(
                        city: 'Paris',
                        country: 'France',
                        zip: 12345,
                )
                married true
                // a list of values
                conferences 'JavaOne', 'Gr8conf'
            }
        }
        assertTrue root instanceof Map
        assertEquals builder.toString(), '{"people":{"person":{"firstName":"Guillame","lastName":"Laforge","address":{"city":"Paris","country":"France","zip":12345},"married":true,"conferences":["JavaOne","Gr8conf"]}}}'


        def object = new groovy.json.JsonSlurper().parseText '''
            { "simple": 123,
              "fraction": 123.66,
              "exponential": 123e12
            }'''
        assert object instanceof Map
        assert object.simple.class == Integer
        assert object.simple == 123
        assert object.fraction.class == BigDecimal
        assert object.exponential.class == BigDecimal
    }

    void testForLoopOnNull() {
        def l

        def list = Arrays.asList(1, 2, 3)
        for (o in list) {
            print('~' + o)
        }

        l = null
        for (o in l) {
            println(o)
        }
    }

    class Animal {
        String name
        BigDecimal price
        String farmer

        String toString() { name }
    }

    def func(a, b = 2, c = null) {
        if (c == null) {
            'c is null'
        } else {
            a + b + c
        }
    }

    void testGroovySyntax() {
        def h = "hello"
        3.times { it ->
            print "time-${it}-($h)  "
        }

        def l = [5, 6, 3, 7, 1, 4, 9]
        def sortedL = l.sort {
            a, b -> -a.compareTo(b)
        }
        assertEquals([9, 7, 6, 5, 4, 3, 1], sortedL)

        def reverse = [
                equals : false,
                compare: { Object[] args -> args[1].compareTo(args[0]) }
        ] as Comparator
        def list = [5, 3, 9]
        Collections.sort(list, reverse)

        def d = [a: 99, b: 88]
        assertEquals 99, d.a
        assertEquals 99, d["a"]

        assertEquals 6, func(1, 2, 3)
        assertEquals 'c is null', func(1)


        def animals = []
        animals << new Animal(name: "Buttercup", price: 2, farmer: "john")
        animals << new Animal(name: "Carmella", price: 5, farmer: "dick")
        animals << new Animal(name: "Cinnamon", price: 2, farmer: "dick")
        println animals
        assertEquals 9, animals.sum { it.price }
        println animals.collect { it.name.toUpperCase() }
        println animals.groupBy { it.farmer }
        println animals.groupBy { it.farmer }.collectEntries { k, v -> [k, v.price.sum()] }

    }
}