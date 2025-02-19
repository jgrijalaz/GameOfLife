import java.util.Arrays;
import java.util.Random;

public class Gol
{
    /**
     *  altura (número de filas)
     *  */
    private int h;
    /**
     * ancho (número de columnas) */
    private int w;
    /**
     *  matriz principal */
    private int[][] a;
    /**
     * matriz secundaria */
    private int[][] b;

    /**
     * generador */
    Random generador;

    /**
     * Inicializa los atributos h y w e instancia las matrices "a" y "b".
     * @param h es la altura (número de filas)
     * @param w es el ancho (número de columnas)
     */
    public Gol(int h, int w)
    {
        //NOTESTED: Gol. Inicializa e instancia.
        this.h = h;
        this.w = w;
        a = new int[h][w];
        b = new int[h][w];
        generador = new Random();
    }

    /**
     * Devuelve la situación actual.
     * @return la matriz principal
     */
    public int[][] getSituacion()
    {
        //NOTESTED: getSituacion. Devuelve.
        return a;
    }

    /**
     * Asigna el valor 1 a la celda de la matriz principal "a".
     * @param f fila
     * @param c columna
     */
    public void ponerVivo(int f, int c)
    {
        //NOTESTED: ponerVivo. Asigna.
        a[f][c] = 1;
    }

    /**
     * Pone un número de posiciones aleatorias con valor 1.
     * @param n número de posiciones aleatorias
     */
    public void crearAleatorios(int n)
    {
        //NOTESTED: crearAleatorios. Utiliza ponerVivo.
        int x = 0;
        int y = 0;

        for (int i = 0; i < n; i++)
        {
            x = generador.nextInt(h);
            y = generador.nextInt(w);
            ponerVivo(x, y);
        }
    }

    /**
     * Calcula el número de celdas vecinas vivas (no se considera así misma).
     * @param f fila
     * @param f columna
     * @return número de celdas vecinas vivas
     */
    private int vecinos(int f, int c)
    {
        //NOTESTED: vecinos. Cuidado con los límites.
        int vecinos = 0;
        boolean valida = false;
        boolean misma = false;

        for (int x = f - 1; x <= f + 1; x++)
        {
            for (int y = c - 1; y <= c + 1; y++)
            {
                valida = posicionValida(x,y);
                misma = (x==f && y==c);
                if(  valida  &&  !misma  &&  (a[x][y] == 1)  )
                {
                    vecinos++;
                }
            }
        }

        return vecinos;
    }

    private boolean posicionValida(int f, int c)
    {
        return (f >= 0 && f < h)  &&  (c >= 0 && c < w);
    }

    /**
     * Cuenta todas las celdas vivas del tablero.
     * @return número de celdas totales vivas
     */
    public int quedanVivos()
    {
        //NOTESTED: quedanVivos. Utiliza si puedes Arrays.stream sino como quieras.
        int vivas = 0;

        for (int f = 0; f < h; f++)
        {
            vivas += Arrays.stream(a[f]).sum();
        }

        return vivas;
    }

    /**
     * Limpia la matriz principal "a".
     */
    public void limpiar()
    {
        limpiar(a);
    }

    /**
     * Establece todos los valores de las celdas de la matriz a 0.
     * @param m matriz
     */
    private void limpiar(int[][] m)
    {
        //NOTESTED: limpiar. Utiliza Arrays.fill.
        for (int f = 0; f < h; f++)
        {
            Arrays.fill(m[f], 0);
        }
    }

    /**
     * Copia en la matriz principal "a" a partir de la posición dada por la fila f y la columna c,
     * los valores de la matriz "m" pasada como parámetro.
     * @param f fila
     * @param c columna
     * @param m matriz
     */

    public void copiar(int f, int c, int[][] m)
    {
        int m_max_filas = m.length;
        int m_max_cols = m[0].length;
        int fila_en_m = 0;
        int col_en_m = 0;

        for (int fila_en_a = f; fila_en_a < m_max_filas + f; fila_en_a++, fila_en_m++)
        {
            col_en_m = 0;
            for (int col_en_a = c; col_en_a < m_max_cols + c; col_en_a++, col_en_m++)
            {
                if(posicionValida(fila_en_a, col_en_a))
                {
                    a[fila_en_a][col_en_a] = m[fila_en_m][col_en_m];
                }
            }
        }

    }

    /**
     * Devuelve una nueva instancia con las dimensiones y los valores de la matriz pasada como parámetro.
     * @param m matriz origen
     * @return copia de la matriz origen
     */
    private int[][] copiaDe(int[][] m)
    {
        //NOTESTED: copiaDe. Utiliza Arrays.copyOf.
        int[][] copia = new int[h][w];

        for (int i = 0; i < h; i++)
        {
            copia[i] = Arrays.copyOf(m[i], w);
        }

        return copia;
    }

    /**
     * Comprueba si dos matrices tienen las mismas dimensiones y valores.
     * @param m1 matriz 1
     * @param m2 matriz 2
     * @return si son iguales
     */
    private boolean sonIguales(int[][] m1, int[][] m2)
    {
        //NOTESTED: sonIguales. Utiliza Arrays.equals.

        // comprueba largo de dimension 1
        if(m1.length != m2.length)
        {
            return false;
        }

        // comprueba largo dimension 2
        if(m1[0].length != m2[0].length)
        {
            return false;
        }

        // comprueba que todos los arrays de
        // dimension 2 son iguales
        for (int f = 0; f < m1.length; f++)
        {
            if ( ! Arrays.equals(m1[f], m2[f]) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Recorre todas las celdas de la matriz principal a y calcula el número de vecinos que tiene.
     * En base a las reglas del juego de la vida en la matriz secundaria b se establecerá el valor
     * que tendrá la celda en el siguiente instante de tiempo. Tras haber recorrido todas las celdas
     * la matriz principal debe pasar a ser la secundaria y al revés, la secundaria la principal.
     * Para ello se debe utilizar una referencia local a una matriz sin instanciar, por ejemplo "x",
     * para que cuando "a" apunte a "b" la referencia de "a" no se pierda y apuntando "b" a "x" se
     * hace el cambio. Es el mismo mecanismo para intercambiar el valor de dos variables primitivas
     * utilizando una tercera variable local. Para que en la matriz secundaria "b" se vayan haciendo
     * los cálculos es preciso limpiar la matriz "b" antes de empezar a recorrer las celdas.
     */
    public void avanza()
    {
        //NOTESTED: avanza. Utiliza limpiar y vecinos.
        int[][] x;
        int vecinos = 0;

        limpiar(b);

        for (int f = 0; f < h; f++)
        {
            for (int c = 0; c < w; c++)
            {
                vecinos = vecinos(f,c);

                if(a[f][c] == 0)    // SI ESTA MUERTA
                {
                    if(vecinos == 3)
                    {
                        b[f][c] = 1;
                    }
                }
                else if(  vecinos == 2  ||  vecinos == 3  )  // SI ESTA VIVA
                {
                    b[f][c] = 1;
                }
            }
        }

        x = a;
        a = b;
        b = x; // conservamos el estado anterior por eficiencia
    }

    /**
     * Comprueba si el tablero es el mismo que en la situación anterior después de avanzar.
     * @return si la matriz principal es igual a la secundaria
     */
    public boolean haAvanzado()
    {
        return sonIguales(a,b);
    }

    /**
     * Crea una copia local de la matriz principal "a" y avanza el estado hasta un número límite
     * de veces comprobando cada vez si la copia local es igual a la matriz principal "a".
     * En caso de ser iguales el método devuelve el número de pasos transcurridos. Si se finaliza
     * el bucle sin encontrar el periodo se devolverá Integer.MAX_VALUE y la matriz principal
     * recuperará los valores de antes de empezar.
     * @param limite valor máximo para el periodo
     * @return número de estados de los que consta la secuencia o
     * Integer.MAX_VALUE si no es menor que el limite
     */
    public int detectaPeriodo(int limite)
    {
        //NOTESTED: detectaPeriodo. Utiliza avanza y sonIguales.
        //int p = Integer.MAX_VALUE;
        int[][] copia = copiaDe(a);


        for (int i = 1; i <= limite; i++)
        {
            avanza();
            if(sonIguales(copia, a))
            {
                return i;
            }
        }

        return limite;
    }

}
