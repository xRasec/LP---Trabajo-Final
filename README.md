# LP---Trabajo-Final

Este proyecto contiene tres ejemplos de algoritmos para realizar búsqueda de patrones de texto. Los códigos están ubicados en la carpeta main y se describen a continuación.

## Código 1: KMP

### Descripción
Es un algoritmo eficiente para la búsqueda de patrones en una cadena de texto. El KMP evita comparaciones innecesarias al aprovechar información previa sobre el patrón que se busca, en este proyecto será utilizado para comparación de algoritmos.

### Instrucciones de Uso
1. Cambiar de directorio con cd demo.
2. Escribir sbt compile.
3. Escribir sbt run.
4. Seleccionar el algoritmo KMP.
5. Y seleccionar la opción a utilizar.

## Código 2: BoyerMoore

### Descripción
Es un algoritmo eficiente para la búsqueda de patrones en una cadena de texto. El Boyer-Moore utiliza dos heurísticas: la regla del desplazamiento de caracteres y la regla del desplazamiento del patrón, esto para minimizar el número de comparaciones necesarias durante la búsqueda, en este proyecto será utilizado para comparación de algoritmos.

### Instrucciones de Uso
1. Cambiar de directorio con cd demo.
2. Escribir sbt compile.
3. Escribir sbt run.
4. Seleccionar el algoritmo BoyerMoore.
5. Y seleccionar la opción a utilizar.

## Código 3: FuerzaBruta

### Descripción
Es el método más sencillo para buscar patrones en un texto, ya que no requiere ningún procesamiento previo del patrón o el texto. La idea principal es comparar carácter por carácter el patrón y el texto, si se encuentra alguna discrepancia, el patrón se desplaza una posición a la derecha y se repite la comparación hasta encontrar una coincidencia o llegar al final del texto, en este proyecto será utilizado para comparación de algoritmos, en este proyecto será utilizado para comparación de algoritmos.


### Instrucciones de Uso
1. Cambiar de directorio con cd demo.
2. Escribir sbt compile.
3. Escribir sbt run.
4. Seleccionar el algoritmo Fuerza Bruta.
5. Y seleccionar la opción a utilizar.

## Requisitos

- Tener el JavaSDK descargado, el Scala descargado.
- En VSCode se descargaron las extensiones: Scala(Metals), Snippets y Syntax
- Instalar las depedencias en el build.sbt.