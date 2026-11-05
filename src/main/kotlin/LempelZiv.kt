package org.example

// prevIndex indentifies an existing phrase in the dict
// nextCharCode stores the character that comes after
// a nextCharCode of -1 indicates that the input ended at an existing dictionary phrase
class LZ78Pair(
    val prevIndex: Int,
    val nextCharCode: Int
)


class LempelZiv {
    // compresses a string using the LZ78 algorithm
    // dictionary stores phrases as keys and their int codes as values
    // returned string contains pairs like this: prevIndex, nextCharCode; prevIndex, nextCharCode
    fun encode(input: String): String {
        // create a dictionary using our own hash table
        val dictionary = HashTable<String, Int>()

        // empty string always has dict index 0
        dictionary[""] = 0

        // next dictionary code to assign
        var nextCode = 1

        // store all of the encoded pairs
        val encodedPairs = mutableListOf<LZ78Pair>()

        // track our current position
        var position = 0

        // continue until we have processed the entire input
        while (position < input.length) {
            // Start with an empty prev string
            var prev = ""
            // which has an index of 0
            var prevIndex = 0

            // track whether we created a new phrase to preven tan extra pair from being added
            // when a new phrase uses the final char
            var phraseAdded = false

            // search for the longest existing prev
            while (position < input.length) {

                // add the next character to the current prev
                val candidate = prev + input[position]

                // check whether already in the dictionary
                val existingIndex = dictionary[candidate]

                if (existingIndex != null) {
                    // the candidate already exists that we can move onto next char
                    prev = candidate
                    prevIndex = existingIndex
                    position++

                } else {

                    // candidate is new and so we create a pair containing:
                    // index of existing prev and code of new character
                    encodedPairs.add(
                        LZ78Pair(prevIndex, input[position].code)
                    )
                    // add to dict
                    dictionary[candidate] = nextCode
                    // increase the code for the next
                    nextCode++
                    // move past the character we just encoded
                    position++
                    // new phrase was successfully created
                    phraseAdded = true
                    // start searching for the next phrase
                    break
                }
            }


            /*
             * If we reached the end of the input without creating
             * a new phrase, the remaining prefix is already in
             * the dictionary.
             *
             * Store it using -1 to indicate that there is no
             * additional character.
             */
            if (!phraseAdded && prev.isNotEmpty()) {

                encodedPairs.add(
                    LZ78Pair(prevIndex, -1)
                )
            }
        }

        // convert the encoded pairs into one string (prevIndex, nextCharCode)
        val result = StringBuilder()
        for (i in encodedPairs.indices) {
            // Add a semicolon between pairs
            if (i > 0) {
                result.append(";")
            }
            // Add the prev index
            result.append(encodedPairs[i].prevIndex)

            // separate the two values with a comma
            result.append(",")

            // add the next character code
            result.append(encodedPairs[i].nextCharCode)
        }
        return result.toString()
    }

    // Decompress a string
    // It will rebuild the dictionary and use the pairs to reconstruct the OG inpute
    fun decode(encoded: String): String {

        // an empty encoded string represents an empty input
        if (encoded.isEmpty()) {
            return ""
        }

        // the dictionary stores phrases in order of their codes
        val dictionary = mutableListOf<String>()

        // dictionary index 0 represents the empty string
        dictionary.add("")

        // stores the reconstructed original text
        val result = StringBuilder()


        // separate the encoded string into individual pairs
        val pairs = encoded.split(";")

        // process each encoded pair
        for (pair in pairs) {

            // find the comma separating the two numbers
            val commaPosition = pair.indexOf(",")

            // read the prev index
            val prevIndex =
                pair.substring(0, commaPosition).toInt()

            // read the character code
            val nextCharCode =
                pair.substring(commaPosition + 1).toInt()


            // make sure the prev index is valid
            check(prevIndex >= 0 && prevIndex < dictionary.size)

            // retrieve the existing prev from the dictionary
            val prev = dictionary[prevIndex]


            if (nextCharCode == -1) {

                // add phrase to the output
                result.append(prev)

            } else {

                // convert char code back into char
                val nextChar = nextCharCode.toChar()

                // reconstruct the new phrase
                val newPhrase = prev + nextChar

                // add the phrase to the output
                result.append(newPhrase)

                // add the new phrase to the dictionary
                dictionary.add(newPhrase)
            }
        }
        return result.toString()
    }
}
