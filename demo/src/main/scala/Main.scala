import KMP.KMPMain
import FB.FuerzaBrutaMain
import BM.BoyerMooreMain
import scala.io.StdIn.readLine
import java.io.{File, PrintWriter}
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object TrabajoFinal {
  // Clase para representar a un usuario
  case class Usuario(nombre: String, contrasena: String)

  // Lista de usuarios (solo para propósitos del ejemplo)
  val usuarios: List[Usuario] = List(
    Usuario("Gian", "contra1"),
    Usuario("Nic", "contra2"),
    Usuario("Meche", "haru"),
    Usuario("Raa", "contra3")
  )

  // Función para realizar el login del usuario
  def login(): Boolean = {
    println("Bienvenido al sistema de login en Scala")
    println("Ingrese su nombre de usuario: ")
    val nombreUsuario = readLine()
    println("Ingrese su password: ")
    val contrasena = readLine()

    usuarios.exists(usuario => usuario.nombre == nombreUsuario && usuario.contrasena == contrasena)
  }

  def escribirEnHistorial(texto: String): Unit = {
    val historialFile = new File("historial.txt")
    val writer = new PrintWriter(historialFile, "UTF-8")
    try {
      // Leer contenido previo del archivo, si existe
      val prevContent = Option(historialFile)
        .filter(_.exists())
        .map { file =>
          val source = scala.io.Source.fromFile(file)
          try source.getLines().mkString("\n") finally source.close()
        }
        .getOrElse("")

      // Escribir el contenido previo más el nuevo texto al archivo
      writer.append(prevContent)
      writer.append(texto)
    } finally {
      writer.close()
    }
  }

   def main(args: Array[String]): Unit = {
    // Iniciar sesión
    if (login()) {
      // Solicitar al usuario que ingrese la ubicación del archivo PDF
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
        var pat = ""
        var lps: Array[Int] = Array()
        var len = 0

        while (opcion != 4) {
          println("Seleccione una opcion:")
          println("1. Usar el algoritmo KMP")
          println("2. Usar el algoritmo Fuerza Bruta")
          println("3. Usar el algoritmo BoyerMoore")
          println("4. Salir")

          opcion = scala.io.StdIn.readInt()

          opcion match {
            case 1 =>
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
                    val count = KMPMain.KMPSearch(pat, txt)
                    escribirEnHistorial(s"El patron '$pat' aparece $count veces.\n")
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
                        println("Opcion invalida.")
                      }
                    } else {
                      println("La palabra no se encontro en el texto.")
                    }
                    escribirEnHistorial(s"La palabra '$palabra' aparece ${apariciones.length} veces.\n")
                  case 3 =>
                    println("Saliendo del programa...")
                  case _ =>
                    println("Orden invalida. Por favor, seleccione una opcion valida.")
                }
                println()
              }
            case 2 =>
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
                    val count = FuerzaBrutaMain.buscarPatronFuerzaBruta(pat, txt)
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
                        println("Opcion invalida.")
                      }
                    } else {
                      println("La palabra no se encontro en el texto.")
                    }
                  case 3 =>
                    println("Saliendo del programa...")
                  case _ =>
                    println("Orden invalida. Por favor, seleccione una opcion valida.")
                }
                println()
              }
            case 3 =>
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
                    val count = BoyerMooreMain.BMsearch(txt, pat)
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
                        println("Opcion invalida.")
                      }
                    } else {
                      println("La palabra no se encontro en el texto.")
                    }
                  case 3 =>
                    println("Saliendo del programa...")
                  case _ =>
                    println("Orden invalida. Por favor, seleccione una opcion valida.")
                }
                println()
              }
            case 4 =>
              println("Saliendo del programa...")
            case _ =>
              println("Opcion invalida. Por favor, seleccione una opcion valida.")
          }
          println()
        }
      } finally {
        // Cerrar el documento PDF
        document.close()
      }
    } else {
      println("Credenciales incorrectas. Saliendo del programa...")
    }
}

}
