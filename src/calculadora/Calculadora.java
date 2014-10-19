package calculadora;

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
 */

public class Calculadora {
	private BufferedReader br;
	private String nombreSalida = "out.txt";

	private List<Integer> listaInt = new ArrayList<>();
	private List<Character> listaChar = new ArrayList<>();
	
	public Calculadora(String txt) {
		try {
			File f = new File(txt);
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
			imprime(this.calcular());
			
			
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	/*
	public String linea(String cuenta) {
		String std = "";
	//	Scanner sc = new Scanner(cuenta);
		char[] cadena = cuenta.toCharArray();
		int primero = 0;
		int segundo = 0;
		char operando = '?';
		for(int i = 0; i < cadena.length; i++) {
			if(cadena[i]>='0' && cadena[i]<='9') {
				if(operando == '?') {
					primero = primero*10 + Integer.parseInt(""+cadena[i]);
				} else {
					segundo = segundo*10 + Integer.parseInt(""+cadena[i]);
				}
			} else if (cadena[i] == ' ') {
			} else {
				operando = cadena[i];
			}
		}
		std = std + operar(primero, operando, segundo);
		
		
		return std;
	}
*/	
	public String calcular(){

		String std = "";
		String linea;
		try {
			linea = br.readLine();
			
		
			while(linea != null) {
				if(bienFormada(linea)) {
					separador(linea);
					std = std + opera(listaChar, listaInt) + "\n";
					linea = br.readLine();
					
				} else {
					std = std + "0 \n";
					linea = br.readLine();
				}
			}
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
				System.out.println("Imprito:\n"+std);
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
			if(esOperador(array[i]) && esOperador(array[i+1]))
				bien = false;
		}		
		if(esOperador(array[0]) || esOperador(array[array.length-1]))
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
		listaInt = new ArrayList<>();
		listaChar = new ArrayList<>();
		char[] cadena = std.toCharArray();
		int num = 0;
		for(int i = 0; i < cadena.length; i++) {
			if(cadena[i]>='0' && cadena[i]<='9') {
					num = num*10 + Integer.parseInt(""+cadena[i]);
				
			} else if (cadena[i] == ' ') {
			} else {
				listaInt.add(num);
				num = 0;
				
				listaChar.add(cadena[i]);
			}
		}
		listaInt.add(num);
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
		if(primeraAparicion==-1)
			primeraAparicion = lista.indexOf('+');
			aux = lista.indexOf('-');
			if(aux!=-1 && primeraAparicion!=-1 && aux<primeraAparicion || aux!=-1 && primeraAparicion==-1)
				primeraAparicion = aux;
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
		Calculadora c = new Calculadora("in.txt");
		//System.out.println(c.calcular());
	}
}
