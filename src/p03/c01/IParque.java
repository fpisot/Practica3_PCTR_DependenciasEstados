package src.p03.c01;
/**
 * Interfaz del parque. Cada puerta será un hilo de ejecución.
 *
 * @author Fernando Pisot Serrano
 * @author Ángel Ortiz de Lejarazu Sánchez
 * @version 1.0
 * Programación Concurrente. Práctica 3. 19/03/23
 */
public interface IParque {
	/**
	 * Aumenta en 1 las personas en el parque
	 * @param puerta por la que acceden
	 * */
	public abstract void entrarAlParque(String puerta);
	/**
	 * Disminuye en 1 las personas en el parque
	 * @param puerta por la que acceden
	 * */
	public abstract void salirDelParque(String puerta);



}
