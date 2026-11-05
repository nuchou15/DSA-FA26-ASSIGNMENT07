package org.example

/**
 * Represents a mapping of keys to values.
 * @param K the type of the keys
 * @param V the type of the values
 */
interface AssociativeArray<K, V> {
    /**
     * Insert the mapping from the key, [k], to the value, [v].
     * If the key already maps to a value, replace the mapping.
     */
    operator fun set(k: K, v: V)

    /**
     * @return true if [k] is a key in the associative array
     */
    operator fun contains(k: K): Boolean

    /**
     * @return the value associated with the key [k] or null if it doesn't exist
     */
    operator fun get(k: K): V?

    /**
     * Remove the key, [k], from the associative array
     * @param k the key to remove
     * @return true if the item was successfully removed and false if the element was not found
     */
    fun remove(k: K): Boolean

    /**
     * @return the number of elements stored in the hash table
     */
    fun size(): Int

    /**
     * @return the full list of key value pairs for the associative array
     */
    fun keyValuePairs(): List<Pair<K, V>>
}


/**
 * Implementing a hash table class using the interface provided
 * This implementation uses seprate chaining and division based hashing
 * It also handles rehashing as the number of elements stored in the associative array grows
 */
class HashTable<K, V> : AssociativeArray<K, V> {

    // Prime numbers for sizing
    private val primeSizes = listOf(
        53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157, 98317)

    private var primeIndex = 0 // keep track of which prime size is veing used

    // Buckets to store a list of key-value pairs
    private var buckets =
        Array(primeSizes[primeIndex]) {
            mutableListOf<Pair<K, V>>()
        }

    // track number of pairs stored in  table
    private var count = 0

    private val maxLoadFactor = 0.75 // max load factor allowed before rehashing

    // calculating which bucket a key goes into (division-based hashing function)
    private fun hash(key: K): Int {
        return key.hashCode().mod(buckets.size)
    }

    // inserting a key-value pair and if the key already exists, then we replace its value
    override fun set(k: K, v: V) {

        val index = hash(k) // calculate which bucket should hold key
        val bucket = buckets[index] // get list of pairs stored in that bucket

        // check wehther the key exists
        for (i in bucket.indices) {
            // if key already exists,
            if (bucket[i].first == k) {
                bucket[i] = Pair(k, v) // replace value
                return // no new pair was added so size doesn't change
            }
        }

        // if it doesn't exist, then add a new key-value pair
        bucket.add(Pair(k, v))
        count++ // increase number of stored elements

        // rehash if the table becomes too full (resizing aspect)
        if (count.toDouble() / buckets.size > maxLoadFactor) {
            rehash() // increase capacity and redistribute the existing pairs
        }
    }

    // Check whether a key exists in the table
    override fun contains(k: K): Boolean {
        val index = hash(k) // calculate bucket index for key
        // search though every pair in bucket
        for (pair in buckets[index]) {
            // compare the stored key with the wanted key
            if (pair.first == k) {
                return true
            }
        }
        return false // key wasn't found in its bucket
    }

    // retrieve the value associated with key
    override fun get(k: K): V? {
        val index = hash(k)
        // search through the pairs in that bucket
        for (pair in buckets[index]) {
            // if key matches, return associated value
            if (pair.first == k) {
                return pair.second
            }
        }
        // if key's not found, return null
        return null
    }

    // remove a pair from the hash table
    override fun remove(k: K): Boolean {
        val index = hash(k)
        val bucket = buckets[index] // get list of pairs in that bucket
        // search through bucket using indices
        for (i in bucket.indices) {
            // if pair contains the key
            if (bucket[i].first == k) {
                bucket.removeAt(i) // then remove matching pair from bucket
                count-- // and decrease number of stored pairs
                return true // and indicate that it was removed
            }
        }
        // if key wasn't found reutrn false because nothing was removed
        return false
    }

    // return number of stored pairs
    override fun size(): Int {
        return count
    }

    // return all pairs in the table
    override fun keyValuePairs(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>() // create list to hold pairs
        // visit every bucket
        for (bucket in buckets) {
            result.addAll(bucket) // and add all pairs from bucket to result lsit
        }
        // return complete list of pairs
        return result
    }

    // increase the capacity of the hash table and redistribute all existing pairs
    private fun rehash() {

        // save old pairs before replacing buckets
        val oldPairs = keyValuePairs()

        // holding the new capacity
        val newCapacity: Int

        // check whether another prime is available
        if (primeIndex < primeSizes.lastIndex) {

            // move to the next prime number in the list
            primeIndex++

            // use that prime as the new capacity
            newCapacity = primeSizes[primeIndex]

        } else {

            // else find another prime larger than twice the current capacity
            newCapacity = nextPrime(buckets.size * 2)
        }

        // create new array with larger number of empty buckets
        buckets = Array(newCapacity) {
            mutableListOf<Pair<K, V>>()
        }

        // now reinsert every old pair into the new table
        for (pair in oldPairs) {

            // recalculate bucket index using new capacity
            val index = hash(pair.first)

            // add the pair to its new bucket
            buckets[index].add(pair)
        }
    }

    // find next prime number greater than or equal to the given starting value
    private fun nextPrime(start: Int): Int {
        var number = start // start checking at given number
        // continue searching until prime is found
        while (true) {
            var isPrime = true // assume current number is prime until a divisor is discovered
            var divisor = 2 // begin checking possible divisors at 2
            //
            while (divisor * divisor <= number) {
                // if number divides evenly it isn't prime
                if (number % divisor == 0) {
                    isPrime = false
                    break
                }
                divisor++ // try next possible divisor
            }
            // if no divisor is found, return the prime
            if (isPrime) {
                return number
            }
            number++ // otherwise check next integer
        }
    }
}

// run the tests
fun main() {

    // run both sets of tests
    testHashTable()
    testLempelZiv()

    // demonstrate LZ78 compression
    val compressor = LempelZiv()
    val original = "ABABABA"

    val compressed = compressor.encode(original)
    val decompressed = compressor.decode(compressed)

    println()
    println("Original: $original")
    println("Compressed: $compressed")
    println("Decompressed: $decompressed")

    check(original == decompressed)

    println("All tests passed! :DD")
}
