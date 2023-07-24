import KMP.KMPMain
import FB.FuerzaBrutaMain
import BM.BoyerMooreMain
import scala.io.StdIn.readLine
import java.io.File
import java.io.{FileWriter, PrintWriter}
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object TrabajoFinal {
  // Clase para representar a un usuario
  case class Usuario(nombre: String, contrasena: String)

  // Lista de usuarios (solo para propósitos del ejemplo)
  val usuarios: List[Usuario] = List(
    Usuario("Giancarlo", "contra1"),
    Usuario("Nicole", "contra2"),
    Usuario("Meche", "haru"),
    Usuario("Cesar", "teribol")
  )

  // Función para realizar el login del usuario
  def login(): (Boolean, String) = {
    println("Bienvenido al sistema de login en Scala")
    println("Ingrese su nombre de usuario: ")
    val nombreUsuario = readLine()
    println("Ingrese su password: ")
    val contrasena = readLine()

    usuarios.find(usuario => usuario.nombre == nombreUsuario && usuario.contrasena == contrasena) match {
      case Some(usuarioEncontrado) => (true, usuarioEncontrado.nombre)
      case None => (false, "")
    }
  }

  def guardarRegistro(usuario: String, nombreArchivo: String, textoBuscado: String, algoritmo: String, resultado: String, tiempoEjecucion: Long): Unit = {
    val registro = s"$usuario,$nombreArchivo,$textoBuscado,$algoritmo,$resultado,$tiempoEjecucion"
    val pw = new PrintWriter(new FileWriter("historial.txt", true))
    try {
      pw.println(registro)
    } finally {
      pw.close()
    }
  }
  
  def main(args: Array[String]): Unit = {
    // Iniciar sesión
    val (inicioSesionExitoso, nombreUsuario) = login()
    if (inicioSesionExitoso) {
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
                println("Seleccione una opcion:")
                println("1. Mostrar cantidad de apariciones del patron")
                println("2. Mostrar seccion del texto donde figura la palabra ingresada")
                println("3. Salir")

                orden = scala.io.StdIn.readInt()

                orden match {
                  case 1 =>
                    println("Ingrese el patron a buscar:")
                    val pat = scala.io.StdIn.readLine()
                    val startTime = System.currentTimeMillis()
                    val count = KMPMain.KMPSearch(pat, txt)
                    val endTime = System.currentTimeMillis()
                    val tiempoEjecucion = endTime - startTime
                    println(s"La cantidad de apariciones de '$pat' es: $count")
                    guardarRegistro(nombreUsuario, filePath, pat, "KMP", count.toString, tiempoEjecucion)
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
            case 2 =>
              var orden = 0
              while (orden != 3) {
                println("Seleccione una opcion:")
                println("1. Mostrar cantidad de apariciones del patron")
                println("2. Mostrar seccion del texto donde figura la palabra ingresada")
                println("3. Salir")

                orden = scala.io.StdIn.readInt()

                orden match {
                  case 1 =>
                    println("Ingrese el patron a buscar:")
                    val pat = scala.io.StdIn.readLine()
                    val startTime = System.currentTimeMillis()
                    val count = FuerzaBrutaMain.buscarPatronFuerzaBruta(pat, txt)
                    val endTime = System.currentTimeMillis()
                    val tiempoEjecucion = endTime - startTime
                    println(s"La cantidad de apariciones de '$pat' es: $count")
                    guardarRegistro(nombreUsuario, filePath, pat, "Fuerza Bruta", count.toString, tiempoEjecucion)
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
                println("Seleccione una opcion:")
                println("1. Mostrar cantidad de apariciones del patron")
                println("2. Mostrar seccion del texto donde figura la palabra ingresada")
                println("3. Salir")

                orden = scala.io.StdIn.readInt()

                orden match {
                  case 1 =>
                    println("Ingrese el patron a buscar:")
                    val pat = scala.io.StdIn.readLine()
                    val startTime = System.currentTimeMillis()
                    val count = BoyerMooreMain.BMsearch(txt, pat)
                    val endTime = System.currentTimeMillis()
                    val tiempoEjecucion = endTime - startTime
                    println(s"La cantidad de apariciones de '$pat' es: $count")
                    guardarRegistro(nombreUsuario, filePath, pat, "Boyer Moore", count.toString, tiempoEjecucion)
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
