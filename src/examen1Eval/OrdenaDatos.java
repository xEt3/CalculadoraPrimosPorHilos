package examen1Eval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;



public class OrdenaDatos extends Thread{
	private CountDownLatch esperaFinProceso;
	private CountDownLatch esperaFinOrdena;
	private PipedReader receptor;
	private BufferedReader flujoEntrada;
	private int[] valores;
	
	public OrdenaDatos(CountDownLatch esperaFinProceso, CountDownLatch esperaFinOrdena, PipedReader receptor) {
		this.esperaFinProceso = esperaFinProceso;
		this.esperaFinOrdena = esperaFinOrdena;
		this.receptor = receptor;
		flujoEntrada=new BufferedReader(receptor);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("esperando a que terminen los procesos");
			esperaFinProceso.await();
			System.out.println("procesos terminados");
			System.out.println("Numero de primos encontrados "+CalculaPrimos.datosPipe);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		valores=new int[CalculaPrimos.datosPipe];
		for (int i = 0; i < CalculaPrimos.datosPipe; i++) {
			try {
				String primo = flujoEntrada.readLine();
				valores[i]=Integer.parseInt(primo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Arrays.sort(valores);
		mostrarPrimos();
		esperaFinOrdena.countDown();
		
	}
	
	private void mostrarPrimos() {
		for (int i = 0; i < valores.length; i++) {
			System.out.println("Primo "+(i+1)+"-ésimo > "+valores[i]);
		}
	}
	
	

}
