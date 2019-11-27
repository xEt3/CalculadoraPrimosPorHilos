package examen1Eval;

import java.io.PipedWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Procesador extends Thread {
	private int nproceso;
	private CyclicBarrier iniciaProceso;
	private CountDownLatch esperaFinProceso;
	private PipedWriter emisor;
	private PrintWriter flujoSalida;
	private Semaphore accesoPepeCounter;
	private Semaphore concurrencia;
	private Random rand = new Random();

	public Procesador(int nproceso, CyclicBarrier iniciaProceso, CountDownLatch esperaFinProceso, PipedWriter emsior,
			Semaphore accesoPipeCounter, Semaphore concurrencia) {
		super();
		this.nproceso = nproceso;
		this.iniciaProceso = iniciaProceso;
		this.esperaFinProceso = esperaFinProceso;
		this.emisor = emsior;
		this.flujoSalida = new PrintWriter(emsior);
		this.accesoPepeCounter = accesoPipeCounter;
		this.concurrencia = concurrencia;
	}

	@Override
	public void run() {
		try {
			iniciaProceso.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		while (concurrencia.tryAcquire()) {
			try {
				long aleatorio = rand.nextInt(300);
				Thread.sleep(aleatorio);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (esprimo(nproceso)) {
			flujoSalida.println(nproceso);
			try {
				accesoPepeCounter.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CalculaPrimos.datosPipe++;
			accesoPepeCounter.release();
		}
		concurrencia.release();
		esperaFinProceso.countDown();
	}

	private boolean esprimo(int valor) {
		boolean primo = false;
		switch (valor) {
		case 0:
			primo = false;
			break;
		case 1:
			primo = false;
			break;
		case 2:
			primo = true;
			break;
		default:
			int contador = 0;
			for (int i = 1; i <= valor; i++) {
				if ((valor % i) == 0) {
					contador++;
				}
			} // end-for
			if (contador <= 2) {
				// es primo
				primo = true;
			} else {
				primo = false;
			} // end-if-else
		}// end-case
		return primo;
	}
}
