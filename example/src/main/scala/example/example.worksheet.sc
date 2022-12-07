1 + 2
val x = 42
x * x

/**
 * Let's define a function to compute the factorial of an integer.
 * Recall, the factorial of n is denoted by n! and is defined as
 * n! = n * (n-1) * ... * 2 * 1.  For example, 3! = 3 * 2 * 1.
 */

def factorial(n: Int): Int = if (n == 0 || n ==1) 1 else n * factorial(n-1)

// Let's try it out with input `n=3`.
factorial(3)

/**
 * The above definition seems to work fine, at least for some 
 * example input values (e.g., n=3). However, it has a couple of
 * flaws.
 *
 * 1. It could fail to terminate if the caller of `factorial`
 *    passes a negative integer argument. (Why?)
 * 
 * 2. It is not "tail-recursive" (a.k.a. "stack-safe").
 * 
 * A recursive function is called "tail-recursive" if it returns 
 * either a value or a recursive call to itself.  In contrast, 
 * a recursive function is not tail-recursive if it returns a value
 * consisting of some extra operation performed on the value returned 
 * by a recursive call.  The "extra operation" in the factorial 
 * function above is the multiplication of `factorial(n-1)` by `n`.
 * 
 * We will address the first problem later.  First let's see how
 * to rewrite a recursive function so that it is tail-recursive.
 * 
 * The key is to perform the recursion inside a helper function that
 * keeps track of an accumulator variable that will build the desired
 * result that will ultimately be returned.
 * 
 * Here is how this could be done for the factorial function.
 */

def better_factorial(n: Int): Int =

    // The helper function.  Here `acc` keeps track of (and accumulates) 
    // the desired result that is returned in the end.
    def better_factorial_aux(n: Int, acc: Int): Int = 
        if (n == 0 || n ==1) acc
        else better_factorial_aux(n-1, n*acc)
    
    // To complete the definition, we must call the helper function, 
    // starting with an "empty" accumulator (in this case, "empty" 
    // means `acc = 1`).
    better_factorial_aux(n, 1)

// Let's try it!
better_factorial(3)

/**
 * Now that our factorial function is stack-safe, let's improve it further
 * by addressing the first issue raised above---the possibility of negative
 * input values.
 * 
 * A simple way to handle this is to change the return type of 
 * the function so that, instead of `Int`, it returns `Option[Int]`.
 * 
 * For any type `T`, the type `Option[T]` returns either `None` in case of
 * failure, and `Some(ans)` in case of success, where `ans` is a value of 
 * type `T`.
 * 
 * For the factorial function, we will check if the input is negative.
 * If it is, then we simply return None.  Otherwise, we define a helper
 * function that is similar to the one above, except that it returns
 * `Some(acc)` instead of just `acc`.
 */

def even_better_factorial(n: Int): Option[Int] = {
    
    if(n < 0) None
    def better_factorial_aux(n: Int, acc: Int): Option[Int] = 
        if (n == 0 || n ==1) Some(acc)
        else better_factorial_aux(n-1, n*acc)
    
    better_factorial_aux(n, 1)
}

even_better_factorial(3)

/** LISTS AND MAPS
 * 
 * Let's start off simply with mappings of lists of integers.
 * 
 * Scala has a `List[_]` type constructor for constructing lists
 * containing elements of a given type.  For example, here is how
 * I could form a list `t` containing the integers 1, 2, 3.
 */

val t: List[Int] = List(1,2,3)

// The type declaration is a bit redundant here and Scala doesn't 
// require it, so we could have written `val t = List(1,2,3)` instead.

/**
 * A list of Boolean (true/false) values will have type `List[Boolean]`. 
 * For example,
 */
val b = List(true, false, true, true)

/**
 * Let's write a map function that takes a list of integers and a function 
 * `f: Int => Int` that maps integers to integers, and applies `f` to each 
 * element of the list, returning the results in a new list.
 * 
 * To define such a mapping function, we will use an extremely useful feature 
 * of Scala (which is absent from most procedural languages). It's called 
 * "pattern matching" and it allows us to direct our program to do different
 * things depending on the *structure* of the input argument.
 * 
 * The list type `List[Int]` has two constructors, `Nil` and `::`.
 * `Nil` denotes the empty list and `::` is used to construct a list 
 * out of an integer combined with another list.  That is, a non-empty 
 * list has the form `x :: xs` where `x` is an integer, called the "head" 
 * of the list, and `xs` is another (possibly empty) list of integers.
 *
 * For example, the list containing just one element, say (3), is constructed 
 * as `3 :: Nil`; the list (2, 3) is constructed as `2 :: (3 :: Nil)`; the 
 * list (1, 2, 3) is constructed as `1 :: (2 :: (3 :: Nil))`.
 * 
 * To operate recursively on a list, we check what form the list has.
 * If it's empty, then we do nothing and return the empty list. If it's
 * non-empty, then we operate on the head of the list and then make a 
 * recursive call involving the tail of the list.
 * 
 * Here is how this is done for the mapping function described above.
 */

def my_map(l: List[Int], f: Int => Int): List[Int] = l match
    case Nil => Nil
    case x :: xs => f(x) :: my_map(xs, f)

/**
 * This is not bad as a first attempt, but again we created a recursive 
 * function that's not stack-safe.  Do you see why?  Notice the operation,
 * `f(x) :: _` that occurs *after* the recursive call to `my_map`. We
 * must avoid letting operations leak out of our recursive calls like this.
 * Again, the standard solution is to employ a helper function that keeps
 * track of the answer in an accumulator variable, which we call `acc`.
 */    

def better_map(l: List[Int], f: Int => Int): List[Int] = {

    def better_map_aux(l: List[Int], acc: List[Int]):List[Int] = l match
        case Nil => acc
        case x :: xs => better_map_aux(xs, acc.appended(f(x)))

    better_map_aux(l, Nil)
}

/**
 * Another flaw you might have noticed in our map function is that it's not
 * very general.  It only works for lists of integers.  What if we want it
 * to work for lists containing elements of an arbitrary type `T` and for
 * functions `f` that map values of type `T` to values of some other arbitrary 
 * type `S`?  Scala has a simple syntax for making functions "generic" in this
 * sense.  Here's how it works for our map example.
 */

def even_better_map[T, S](l: List[T], f: T => S): List[S] = {

    def even_better_map_aux(l: List[T], acc: List[S]):List[S] = l match
        case Nil => acc
        case x :: xs => even_better_map_aux(xs, acc.appended(f(x)))

    even_better_map_aux(l, Nil)
}

/**
 * That is, when we want to use arbitrary types, we simply include type variables
 * inside square brackets just after the function name.
 */

val l1 = List(1,2,3)

def square(x: Int) = x * x

even_better_map(l1, square)
even_better_map(l1, x => x * x)

// Let's confirm that our map function works lists of other types as well.

even_better_map(List(1.0, 2.0, 3.0), x => x < 2.5)

// Here we passed in the anonymous function `x => x < 2.5`, which has
// type `Double => Boolean` and returns `true` if its input argument
// is less than 2.5 and returns false otherwise.