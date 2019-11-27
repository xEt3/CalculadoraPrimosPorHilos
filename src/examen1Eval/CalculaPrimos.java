package examen1Eval;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class CalculaPrimos {
	public static int datosPipe;
	public static int[] valores;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Programa iniciado");
		final int maxProcesos = 1000;
		final int maxProcesosCPU = 10;
		Semaphore accesoPipeCounter = new Semaphore(1);
		Semaphore concurrencia = new Semaphore(maxProcesosCPU);
		Procesador[] procesador = new Procesador[maxProcesos];
		PipedWriter emisor = new PipedWriter();
		PipedReader receptor = new PipedReader(emisor);
		CyclicBarrier iniciaProceso = new CyclicBarrier(maxProcesos);
		CountDownLatch esperaFinProceso= new CountDownLatch(maxProcesos);
		CountDownLatch esperaFinOrdena = new CountDownLatch(1);
		for (int i = 0; i < procesador.length; i++) {
			procesador[i]=new Procesador(i, iniciaProceso, esperaFinProceso, emisor, accesoPipeCounter, concurrencia);
			procesador[i].start();
		}
		OrdenaDatos ordenador = new OrdenaDatos(esperaFinProceso, esperaFinOrdena, receptor);
		ordenador.start();
		esperaFinOrdena.await();
		emisor.close();
		receptor.close();
	}

}
