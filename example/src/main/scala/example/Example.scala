package example

object Example {

  def add(n: Int, m: Int): Int = m + n

  def divide(n:Double, m: Double): Option[Double] = 
    if (m == 0) None
    else Some(n/m)

}