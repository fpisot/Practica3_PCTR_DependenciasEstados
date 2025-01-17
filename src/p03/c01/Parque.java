package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

/**
 * Simula la entrada y salida de personas en un parque.
 *
 * @author Fernando Pisot Serrano
 * @author Ángel Ortiz de Lejarazu Sánchez
 * @version 1.0
 * Programación Concurrente. Práctica 3. 10/03/23
 */
public class Parque implements IParque{

	/** Máxima capacidad de personas en el parque.*/
	private final int MAX_PERSONAS_PARQUE = 50;
	/** Personas en el parque total (independientemente de entrada).*/
	private int contadorPersonasTotales;
	/** Tabla con nombre de puerta y contador parcial de personas.*/
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	/** Generador aleatorio para esperas */
	private static Random generadorAleatorios = new Random ();
	/** Para calcular los tiempos medios.*/
	private long tinicial;
	private double tmedio;
	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}

	/**
	 * Método sincronizado de acceso al parque. Aumenta una persona por puerta
	 * @param puerta de acceso a parque.
	 */
	@Override
	//public void entrarAlParque(String puerta){
	public synchronized void entrarAlParque(String puerta){

		// Aumentamos el contador total
		// si valor de puerta no está iniciado asignamos 0
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		
		comprobarAntesDeEntrar(); //wait
		
		//Aumentamos el contador total.
		contadorPersonasTotales++;
		// Incrementamos contador por puerta
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		//Cálculo de tiempos
		long tActual = System.currentTimeMillis();
		tmedio = (tmedio + (tActual - tinicial))/2.0;
		
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		// Comprobamos que cumple invariante: personas totales = suma de personas por puerta
		checkInvariante();
		// Se notifica el cambio de estado
		notifyAll();
		
	}

	@Override
	public synchronized void salirDelParque(String puerta) {
		// Si no hay entradas, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		
		// Comprobamos que queda gente en el parque
		comprobarAntesDeSalir();
		
		/**
		Bucles comentados por duplicidad.
		// Comprobación necesaria por si no hubiera mismo número de entrada y salida por puerta.
		if (contadoresPersonasPuerta.get(puerta) == null || contadoresPersonasPuerta.get(puerta) == 0) {
			return;
		}
		//VALORAR EL CASO DE SALIDA ANTES DE ENTRADA PUERTA == NULL
		if (contadoresPersonasPuerta.get(puerta) == null || contadoresPersonasPuerta.get(puerta) == 0) {
			return;
		}
		*/
		
		// Decrementamos el contador total
		contadorPersonasTotales--;
		// Decrementamos contador por puerta
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);

		//Cálculo de tiempos
		long tActual = System.currentTimeMillis();
		tmedio = (tmedio + (tActual - tinicial))/2.0;
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");

		// Comprobamos que cumple invariante: personas totales = suma de personas por puerta
		checkInvariante();

		// Se notifica el cambio de estado
		notifyAll();

	}

	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		//System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales + " tiempo medio de estancia: " + obtenerTmedio());

		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	/**
	 * Se va a comprobar que:
	 * Suma total personas en parque = suma parcial de gente por puertas.
	 * Personas entre 0 y MAX_PERSONAS_PARQUE.
	 * */
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales
				: "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales <= MAX_PERSONAS_PARQUE : "Aforo máximo de parque sobrepasado";
		assert contadorPersonasTotales > 0 : "Ha salido gente del parque que no existía!!!";
	}

	protected void comprobarAntesDeEntrar(){
	// Hay que comprobar que no se ha llegado al máximo de gente permitida en el parque
		while (contadorPersonasTotales >= MAX_PERSONAS_PARQUE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Comprueba que haya gente en el parque sino quedará en estado de espera.
	 */
	protected void comprobarAntesDeSalir(){
	// Hay que comprobar que queda gente en el parque para que puedan salir alguien.
		while (contadorPersonasTotales <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Funcion que calcula el tiempo medio de espera de cada persona en el parque.
	 */
	
	private synchronized double obtenerTmedio() {
	    long tActual = System.currentTimeMillis();
	    return (tActual - tinicial) / (contadorPersonasTotales * 1.0);
	}
}
