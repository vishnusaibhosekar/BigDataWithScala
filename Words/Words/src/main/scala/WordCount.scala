import org.apache.spark._
import org.apache.log4j._

// GOAL: Count up how many of each word appears in a book as simply as possible.

// Our main function (where the action happens)
@main def run(): Unit =

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")

    // Read each line of my book into an RDD
    val input = sc.textFile("data/book.txt")

    //input.foreach(println)

    //////////////////////////////////////////////////////////////////////////////////
    // First attempt ----------------------------------------------------------------

    // Split into words separated by a space character
    //val words = input.flatMap(x => x.split(" "))

    // Count up the occurrences of each word

    //val wordCounts = words.countByValue()

    // Print the results.
    //wordCounts.foreach(println)

    // ...not exactly what we had hoped for  :'(


    //////////////////////////////////////////////////////////////////////////////////
    // Second attempt ----------------------------------------------------------------
    
    // Split into words using a regex that extracts WORDS, removing punctuation.
    val words = input.flatMap(x => x.split("\\W+"))

    // Also account for capitalization by normalizing everything to lower case.
    val lowerCaseWords = words.map(x => x.toLowerCase())
    
    // Count up the occurrences of each word
    // val wordCounts = lowerCaseWords.countByValue()

    // Print the results.
    // wordCounts.foreach(println)
    

    // ...better, but this (unsorted) list of counts doesn't give us much insight.

    /* Maybe we should sort the results and print them in a nicer format.
       We could sort once we're done... but that's not the 'big data' way of doing things.
       */

    //////////////////////////////////////////////////////////////////////////////////
    // Third attempt -----------------------------------------------------------------
    // compute `words` and `lowerCaseWords` as above, and then...
    
    // Count up the occurrences of each word
    val wordCounts = lowerCaseWords.map(x => (x, 1)).reduceByKey(_+_)

    // Flip (word, count) tuples to (count, word) and then sort by key (the counts)
    val wordCountsSorted = wordCounts.map( x => (x._2, x._1) ).sortByKey()
    
    // Print the results, flipping the (count, word) results to word: count as we go.
    for (result <- wordCountsSorted) {
      val count = result._1
      val word = result._2
      println(s"$word: $count")
    }




