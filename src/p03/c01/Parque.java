package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Fernando Pisot Serrano
 * @author Ángel Ortiz de Lejarazu Sánchez
 * @version 1.0
 * Programación Concurrente. Práctica 3. 10/03/23
 */
public class Parque implements IParque{

	/** Máxima capacidad de personas en el parque.*/
	private final int MAX_PERSONAS_PARQUE = 30;
	/** Personas en el parque total (independientemente de entrada).*/
	private int contadorPersonasTotales;
	/** Tabla con nombre de puerta y contador parcial de personas.*/
	private Hashtable<String, Integer> contadoresPersonasPuerta;

	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
	//public void entrarAlParque(String puerta){
	public synchronized void entrarAlParque(String puerta){

		comprobarAntesDeEntrar();

		// Aumentamos el contador total
		contadorPersonasTotales++;

		// Se incluye una espera entre ambos incrementos para comprobar exclusión con sincronización
		try {
			TimeUnit.MILLISECONDS.sleep(generadorAleatorios.nextInt(3000));
		}catch (InterruptedException e) {
			Logger.getGlobal().log(Level.INFO, "Interrupción del hilo que utiliza el objeto parque");
			return;
		}
		// Incrementamos contador por puerta
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");

		// Comprobamos que cumple invariante: personas totales = suma de personas por puerta
		checkInvariante();

		// Se notifica el cambio dede estado
		notifyAll();
		
	}
	
	// 
	// TODO Método salirDelParque
	//
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
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
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		// TODO 
		// TODO
		
		
		
	}

	protected void comprobarAntesDeEntrar(){
		// Hay que comprobar que no se ha llegado al máximo de gente permitida en el parque
		if (contadorPersonasTotales == MAX_PERSONAS_PARQUE){
			// No sé cómo tiene que indicar para que no meta más gente. No debería devolver un booleano?
		}
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}

		//
		// TODO
		//
	}

	protected void comprobarAntesDeSalir(){
		// Hay que comprobar que queda gente en el parque
		if (contadorPersonasTotales > 0){
			//return true?
		}
		// TODO
		//
	}


}
