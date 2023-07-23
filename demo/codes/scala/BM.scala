package BM

import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object BoyerMooreMain {
  val NO_OF_CHARS = 65536
  val startTime = System.currentTimeMillis()

  def badCharHeuristic(string: String, size: Int): Array[Int] = {
    val badChar = Array.fill(NO_OF_CHARS)(-1)
    for (i <- 0 until size) {
      badChar(string(i).toInt) = i
    }
    badChar
  }

  def BMsearch(txt: String, pat: String): Int = {
    val m = pat.length
    val n = txt.length
    val badChar = badCharHeuristic(pat, m)
    var s = 0
    var count = 0 // Variable para contar las apariciones del patrón

    while (s <= n - m) {
      var j = m - 1
      while (j >= 0 && pat(j) == txt(s + j)) {
        j -= 1
      }
      if (j < 0) {
        println(s"Patron en la posicion = $s")
        count += 1 // Incrementar la cantidad total de apariciones
        s += (if (s + m < n) m - badChar(txt(s + m).toInt) else 1)
      } else {
        s += Math.max(1, j - badChar(txt(s + j).toInt))
      }
    }
    count // Devolver la cantidad total de apariciones
  }

  def main(args: Array[String]): Unit = {
    // Solicitar al usuario que ingrese la ubicación del archivo
    println("Ingrese la ubicacion del archivo PDF:")
    val filePath = scala.io.StdIn.readLine()

    // Cargar el documento PDF
    val document = PDDocument.load(new File(filePath))

    try {
      // Crear un objeto PDFTextStripper para extraer el texto del PDF
      val pdfStripper = new PDFTextStripper()

      // Obtener el texto del PDF
      val txt = pdfStripper.getText(document)

      // Menú
      var opcion = 0
      while (opcion != 3) {
        println("Seleccione una opcion:")
        println("1. Mostrar cantidad de apariciones del patron")
        println("2. Mostrar seccion del texto donde figura la palabra ingresada")
        println("3. Salir")

        opcion = scala.io.StdIn.readInt()

        opcion match {
          case 1 =>
            println("Ingrese el patron a buscar:")
            val pat = scala.io.StdIn.readLine()
            val count = BMsearch(txt, pat)
            println(s"La cantidad de apariciones de '$pat' es: $count")
          case 2 =>
            println("Ingrese la palabra a buscar:")
            val palabra = scala.io.StdIn.readLine().toLowerCase()
            val words = txt.toLowerCase().split("\\s+")
            var apariciones = List[(String, String, String)]()
            for (i <- 1 until words.length - 1) {
              if (words(i) == palabra) {
                val seccion = (words(i - 1), words(i), words(i + 1))
                apariciones = apariciones :+ seccion
              }
            }
            if (apariciones.nonEmpty) {
              println(s"La palabra '$palabra' aparece ${apariciones.length} veces.")
              println("Seleccione a cual aparicion desea ir (ingrese un numero entre 1 y el total de apariciones):")
              val seleccion = scala.io.StdIn.readInt()
              if (seleccion >= 1 && seleccion <= apariciones.length) {
                val (antes, palabraEncontrada, despues) = apariciones(seleccion - 1)
                println(s"Sección del texto:")
                println(s"$antes $palabraEncontrada $despues")
              } else {
                println("Opción inválida.")
              }
            } else {
              println("La palabra no se encontró en el texto.")
            }
          case 3 =>
            println("Saliendo del programa...")
          case _ =>
            println("Opcion invalida. Por favor, seleccione una opción valida.")
        }
        println()
      }
    } finally {
      // Cerrar el documento PDF
      document.close()
    }
  }
}
