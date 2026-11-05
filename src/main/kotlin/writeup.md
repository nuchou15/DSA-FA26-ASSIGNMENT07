# Results from Applying Associative Arrays to Solve a Problem (Lempel-Ziv Compression)

I applied my associative array implementation to the LZ78 compression algorithm. The hash table is used as a dictionary that stores previously encountered phrases and their int indices. This allows the algorithm to quickly check whether a phrase already exists and reuse its index when encoding new text.

I tested the algorithm using several inputs, including `"ABABABA"`, `"AAAAAAAAAAAA"`, and `"belloooo bello"`. I also tested an empty string and a string with no repeating characters. For each input, I encoded the text and then decoded it to verify that the original text was recovered correctly. And all tests passed!

The results demonstrate how LZ78 takes advantage of repeated patterns. When a phrase appears multiple times, the algorithm can refer to its dictionary index instead of storing the entire phrase again. As a result, inputs with many repeated characters or patterns generally benefit more from compression than inputs with little repetition.

The associative array made dictionary lookups efficient, with O(1) insertion and retrieval. Overall, this project demonstrated how a hash table can be applied to a practical problem by supporting the dictionary operations needed for LZ78 compression.