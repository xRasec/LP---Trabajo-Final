package KMP

import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object KMPMain {
  val startTime = System.currentTimeMillis()
  var count = 0
  def KMPSearch(pat: String, txt: String): Unit = {
    val m = pat.length
    val n = txt.length
    val lps = computeLPSArray(pat, m, Array.fill(m)(0))
    search(0, 0, lps)

    def search(i: Int, j: Int, lps: Array[Int]): Unit = {
      if ((n - i) >= (m - j)) {
        if (pat(j) == txt(i)) {
          if (j == m - 1) {
            println("Patron en la posicion = " + (i - j))
            count += 1 // Incrementar el contador
            search(i - j + 1 + lps(j), lps(j), lps)
          } else {
            search(i + 1, j + 1, lps)
          }
        } else {
          if (j != 0) {
            search(i, lps(j - 1), lps)
          } else {
            search(i + 1, 0, lps)
          }
        }
      }
    }

    println("Cantidad total de apariciones: " + count)
  }

  def computeLPSArray(pat: String, M: Int, lps: Array[Int]): Array[Int] = {
    val (_, result) = (1 until M).foldLeft((0, lps)) { case ((len, acc), i) =>
      if (pat(i) == pat(len)) {
        val updatedLps = acc.updated(i, len + 1)
        (len + 1, updatedLps)
        } else if (len != 0) {
        computeLPS(len, acc)
        } else {
        (0, acc.updated(i, 0))
        }
      }
    result
  }
  
  def computeLPS(len: Int, lps: Array[Int]): (Int, Array[Int]) = {
    val newLen = lps(len - 1)
    if (newLen != 0) {
      computeLPS(newLen, lps.updated(len, newLen))
      } else {
      (0, lps.updated(len, 0))
      }
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
      var orden = 0
      while (orden != 3) {
        println("Seleccione una orden:")
        println("1. Mostrar cantidad de apariciones del patron")
        println("2. Mostrar seccion del texto donde figura la palabra ingresada")
        println("3. Salir")

        orden = scala.io.StdIn.readInt()

        orden match {
          case 1 =>
            println("Ingrese el patron a buscar:")
            val pat = scala.io.StdIn.readLine()
            val count = KMPSearch(pat, txt)
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
            println("orden invalida. Por favor, seleccione una opción valida.")
        }
        println()
      }
    } finally {
      // Cerrar el documento PDF
      document.close()
    }
  }
}
