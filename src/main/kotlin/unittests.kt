package org.example

fun testHashTable() {

    val table = HashTable<String, Int>()

    // test inserting and retrieving values
    table["apple"] = 1
    table["banana"] = 2
    check(table["apple"] == 1)
    check(table.size() == 2)
    check("banana" in table)

    // test updating an existing key
    table["apple"] = 10
    check(table["apple"] == 10)
    check(table.size() == 2)

    // test collisions
    table["Aa"] = 3
    table["BB"] = 4
    check(table["Aa"] == 3)
    check(table["BB"] == 4)

    // test removing a key
    check(table.remove("banana"))
    check(table["banana"] == null)
    check(table.size() == 3)

    // Test rehashing by adding many pairs
    for (i in 1..100) {
        table["key$i"] = i
    }
    check(table["key50"] == 50)
    check(table["apple"] == 10)
    println("HashTable tests passed!")
}


fun testLempelZiv() {
    val compressor = LempelZiv()

    // test a repeating pattern
    var original = "ABABABA"
    check(compressor.decode(compressor.encode(original)) == original)

    // test repeated characters
    original = "AAAAAAAAAAAA"
    check(compressor.decode(compressor.encode(original)) == original)

    // test ordinary text
    original = "hello hello!"
    check(compressor.decode(compressor.encode(original)) == original)

    // test an empty string
    original = ""
    check(compressor.decode(compressor.encode(original)) == original)
    println("LempelZiv tests passed!")
}
