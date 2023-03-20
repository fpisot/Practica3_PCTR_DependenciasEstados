package src.p03.c01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simula la salida de personas del parque. Cada puerta será un hilo de ejecución.
 *
 * @author Fernando Pisot Serrano
 * @author Ángel Ortiz de Lejarazu Sánchez
 * @version 1.0
 * Programación Concurrente. Práctica 3. 19/03/23
 */
public class ActividadSalidaPuerta implements Runnable{

    /**  Se realizarán 20 entradas por puerta */
    private static final int NUMSALIDAS = 20;
    /** Identificador de puerta */
    private String puerta;
    /** Referencia al parque */
    private IParque parque;
    /** Segundos dormido entre interacción */
    private static final int SEGUNDOS_ESPERA = 5;

    public ActividadSalidaPuerta(String puerta, IParque parque) {
        this.puerta = puerta;
        this.parque = parque;
    }

    @Override
    public void run() {
        for (int i = 0; i < NUMSALIDAS; i ++) {
            try {
                parque.salirDelParque(puerta);
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(SEGUNDOS_ESPERA)*1000);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.INFO, "Salida interrumpida en puerta: " + puerta);
                Logger.getGlobal().log(Level.INFO, e.toString());
                return;
            }
        }
    }
}
