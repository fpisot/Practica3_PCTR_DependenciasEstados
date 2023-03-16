package src.p03.c01;

public class ActividadSalidaPuerta{

    private static final int NUMSALIDAS = 20; // TIENE QUE SER IGUAL A NUMENTRADAS ¿UNA SOLA VARIABLE***********?
    private String puerta;
    private IParque parque;

    public ActividadEntradaPuerta(String puerta, IParque parque) {
        this.puerta = puerta;
        this.parque = parque;
    }

    @Override
    public void run() {
        for (int i = 0; i < NUMSALIDAS; i ++) {
            try {
                parque.salirDelParque(puerta);
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5)*1000);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.INFO, "Salida interrumpida en puerta: " + puerta);
                Logger.getGlobal().log(Level.INFO, e.toString());
                return;
            }
        }
    }
}
