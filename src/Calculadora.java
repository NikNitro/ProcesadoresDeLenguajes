//package calculadora;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 2+2
3*5
12*40
0
21 + 12
  11 *22
 5 -  1
  123
  10 + 20 * 30
10* 20+ 30
30 * 20  +10
1*2+3*4+5*6+7*8
1+-2--3*-4
-11+22-0+4*-1
2+32/2/2/2
----3
64/-2/2/2/-2*-2*-2
 */

public class Calculadora {
	private BufferedReader br = null;	//Para ficheros
	private Scanner sc = null;			//Para System.in
	private String nombreSalida = null;

	private List<Integer> listaInt;
	private List<Character> listaChar;
	
/*	public Calculadora() {
		sc = new Scanner(System.in);
		imprime(this.calcular());
		sc.close();
	}*/
	
	public Calculadora(String txt) {
		try {
			File f = new File(txt);
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
			imprime(this.calcular());
			br.close();
			
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Calculadora(String in, String out) {
		nombreSalida = out;
		new Calculadora(in);
	}
	
	private int operar(int a, char s, int b) {
		switch(s) {
			case '+' : return a+b;
			case '*' : return a*b;
			case '-' : return a-b;
			case '/' : return a/b;
			default : return a;
		}
	}

	public String calcular(){
		
		String std = "";
		String linea;
		try {
	//		if(br!=null)
				linea = br.readLine();
	//		else
	//			linea = sc.next();
			
		
			while(linea != null) {
	//			if(bienFormada(linea)) {
					separador(linea);
					if(listaInt.size()!=0) 
						std = std + opera(listaChar, listaInt) + "\n";
					else
						std = std + "\n";
	//				if(br!=null)
						linea = br.readLine();
						
						
						
						
	/*				else
						linea = sc.next();
					
				} else {
					std = std + "0 \n";
					if(br!=null)
						linea = br.readLine();
					else
						linea = sc.next();
				}
	*/		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return std;
	}
	
	public void imprime(String std) {
		if(nombreSalida == null)
			System.out.println(std);
		else {
			File f = new File(nombreSalida);
			try {
				PrintWriter pw = new PrintWriter(f);
				Scanner aux = new Scanner(std);
				while(aux.hasNext())
					pw.println(aux.next());
				pw.close();
			//	System.out.println("Imprito:\n"+std);
			} catch (FileNotFoundException e) { e.printStackTrace(); }
			}
	}
	
	/**
	 * Se encarga de ver si una string está bien formada (numero+signo+numero...) que se ve si no tiene dos signos seguidos
	 * y si no empieza ni acaba por caracter
	 * @param std String
	 */
	public boolean bienFormada(String std) {
		boolean bien = true;
		char[] array = std.toCharArray();
		for(int i = 0; i < array.length-1; i++) {
			if(esOperador(array[i]) && esOperador(array[i+1]) && (array[i]!='-' && array[i+1]!='-'))
				bien = false;
		}		
		if((esOperador(array[0]) && array[0]!='-') || esOperador(array[array.length-1]))
			bien = false;
		return bien;
	}
	
	public boolean esOperador(char c){
		if(c== '+' || c== '-' || c== '*' || c== '/')
			return true;
		else
			return false;
	}

	/**
	 * Separa en dos listas numeros y operadores de forma ordenada.
	 * @param args
	 */
	public void separador(String std) {
		int negativo = 0;		//Cuando es true, el - trabaja como signo
		char anterior = 'w';  //Usado para los signos
		listaInt = new ArrayList<Integer>();
		listaChar = new ArrayList<Character>();
		char[] cadena = std.toCharArray();
		boolean entra = false;				//Por si es una linea vacia no entra
		int num = 0;
		for(int i = 0; i < cadena.length; i++) {
			if(cadena[i]>='0' && cadena[i]<='9') {
					entra = true;
					num = num*10 + Integer.parseInt(""+cadena[i]);
					anterior = '6'; //Por poner algun numero
				
			} else if (cadena[i] == ' ') {}
			//Para los signos negativos
			//Lo comentado se puede quitar ya que nada más empezar, anterior = w y luego cambia.
			else if(/*cadena[i]=='-' && i==0 || cadena[i]=='-' && */(anterior<'0' || anterior>'9')) { 
				entra = true;
				negativo++;
				
			}
			else {
				
				listaInt.add((int)(num*java.lang.Math.pow(-1, negativo)));
				negativo = 0;
				
				num = 0;
				
				listaChar.add(cadena[i]);
				anterior = cadena[i];
			}
		}
		if(entra)
			listaInt.add((int)(num*java.lang.Math.pow(-1, negativo)));
	}

	/**
	 * Busca el operador más importante. Devuelve su indice en la lista.
	 * @return
	 */
	public int buscaMayor(List<Character> lista) {
		int primeraAparicion = -1;
		int aux = -1;
		primeraAparicion = lista.indexOf('*');
		aux = lista.indexOf('/');
		if(aux!=-1 && primeraAparicion!=-1 && aux<primeraAparicion || aux!=-1 && primeraAparicion==-1)
			primeraAparicion = aux;
		if(primeraAparicion==-1) {
			primeraAparicion = lista.indexOf('+');
			aux = lista.indexOf('-');
			if(aux!=-1 && primeraAparicion!=-1 && aux<primeraAparicion || aux!=-1 && primeraAparicion==-1)
				primeraAparicion = aux;
		}
		return primeraAparicion;
	}
	
	public int opera(List<Character> operador, List<Integer> valores) {
		while(operador.size()>0) {
			int index = buscaMayor(operador);
			char op = operador.remove(index);
			int primero = valores.remove(index);
			int segundo = valores.remove(index);//Sería +1 pero como el anterior lo eliminamos, éste pasa a ser index
			int cuenta = operar(primero, op, segundo);
			valores.add(index, cuenta);
		}
		
		return valores.get(0);
	}
	public static void main(String[] args) {
		Calculadora c = new Calculadora(args[0]);
		//System.out.println(c.calcular());
	}
}
