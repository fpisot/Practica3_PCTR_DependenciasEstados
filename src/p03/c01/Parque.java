package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

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
	private final int MAX_PERSONAS_PARQUE = 30;
	/** Personas en el parque total (independientemente de entrada).*/
	private int contadorPersonasTotales;
	/** Tabla con nombre de puerta y contador parcial de personas.*/
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	/** Generador aleatorio para esperas */
	private static Random generadorAleatorios = new Random ();
	
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

		// Se notifica el cambio de estado
		notifyAll();
		
	}

	@Override
	public synchronized void salirDelParque(String puerta) {
		// Comprobamos que queda gente en el parque
		comprobarAntesDeSalir();
		comprobarantesDeSalirPuerta(puerta);

		// Decrementamos el contador total
		contadorPersonasTotales--;

		// Se incluye una espera entre ambos decrementos para comprobar exclusión con sincronización
		try {
			TimeUnit.MILLISECONDS.sleep(generadorAleatorios.nextInt(3000));
		}catch (InterruptedException e) {
			Logger.getGlobal().log(Level.INFO, "Interrupción del hilo que utiliza el objeto parque");
			return;
		}
		// Incrementamos contador por puerta
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");

		// Comprobamos que cumple invariante: personas totales = suma de personas por puerta
		checkInvariante();

		// Se notifica el cambio de estado
		notifyAll();

	}

	
	
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
	/**
	 * Se va a comprobar que:
	 * Suma total personas en parque = suma parcial de gente por puertas.
	 * Personas entre 0 y MAX_PERSONAS_PARQUE.
	 * */
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales <= MAX_PERSONAS_PARQUE : "Aforo máximo de parque sobrepasado";
		assert contadorPersonasTotales > 0 : "Ha salido gente del parque que no existía!!!";
	}

	protected void comprobarAntesDeEntrar(){
		if (contadorPersonasTotales == MAX_PERSONAS_PARQUE){
			wait(); //Si parque está lleno deberá esperar.
		}
		// Si no hay entradas por esa puerta, inicializamos //NO ESTOY SEGURO DE SI ESTO SE PUEDE HACER AQUÍ
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}

	}

	/**
	 * Comprueba que haya gente en el parque sino quedará en estado de espera.
	 */
	protected void comprobarAntesDeSalir(){

		if (contadorPersonasTotales == 0){
			wait(); 	// Si no queda gente en el parque no puede salir.
		}
		// QUIZA TENGA QUE IR ENTRE TRY-CATCH!!*************************************************************

	}
	/**
	 * Creo esta función porque puede haber gente en el parque pero por otras puertas.
	 * Como no sé si se puede cambiar la programación dada lo creo en otra función
	 */
	protected void comprobarantesDeSalirPuerta(String puerta){
		Integer personasPuerta = contadoresPersonasPuerta.get(puerta);
		if (personasPuerta == 0){
			wait(); 	// Si no personas en la puerta indicada espera.
		}
		// QUIZA TENGA QUE IR ENTRE TRY-CATCH!!*************************************************************

	}

}
