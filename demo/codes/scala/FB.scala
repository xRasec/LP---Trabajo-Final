package FB

import java.io.File
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object FuerzaBrutaMain {
  def buscarPatronFuerzaBruta(pat: String, txt: String): Int = {
    val m = pat.length
    val n = txt.length
    var count = 0

    def buscar(i: Int): Unit =
      if (i + m > n) {
        // Finalización de la búsqueda
      } else {
        val coincidencia = verificarCoincidencia(i)
        if (coincidencia) {
          count += 1
        }
        buscar(i + 1)
      }

    def verificarCoincidencia(i: Int): Boolean = {
      (0 until m).forall(j => txt(i + j) == pat(j))
    }

    buscar(0)
    count
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
            val count = buscarPatronFuerzaBruta(pat, txt)
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
                println(s"Seccion del texto:")
                println(s"$antes $palabraEncontrada $despues")
              } else {
                println("Opción inválida.")
              }
            } else {
              println("La palabra no se encontro en el texto.")
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