package aed.polinomios;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;


/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

  // Una lista de monomios
  PositionList<Monomio> terms;

  /**
   * Crea el polinomio "0".
   */
  public Polinomio() {
    terms = new NodePositionList<>();
  }

  /**
   * Crea un polinomio definado por una lista con monomios.
   * @param terms una lista de monomios
   */
  public Polinomio(PositionList<Monomio> terms) {
    this.terms = terms;
  }

  /**
   * Crea un polinomio definado por un String.
   * La representaci√≥n del polinomio es una secuencia de monomios separados
   * por '+' (y posiblemente con caracteres blancos).
   * Un monomio esta compuesto por tres partes,
   * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
   * compuesto por un un caracter '^' seguido por un entero.
   * Se puede omitir multiples partes de un monomio, 
   * ejemplos:
   * <pre>
   * {@code
   * new Polinomio("2x^3 + 9");
   * new Polinomio("2x^3 + -9");
   * new Polinomio("x");   // == 1x^1
   * new Polinomio("5");   // == 5x^0
   * new Polinomio("8x");  // == 8x^1
   * new Polinomio("0");   // == 0x^0
   * }
   * </pre>
   * @throws IllegalArgumentException si el argumento es malformado
   * @param polinomio - una secuencia de monomios separados por '+'
   */
  public Polinomio(String polinomio) {
	  terms = new NodePositionList<>();
	  boolean coef = true;
	  if(polinomio.equals("0")) {
		  return;
	  }
	  int exponente = 0;
	  int coeficiente = 1;
	  for(int i = 0; i < polinomio.length(); i++) {
		  int valor  = Character.getNumericValue(polinomio.charAt(i));
			  if(polinomio.charAt(i) == 'x') {
				  coef = false;
				  exponente = 1;
			  }else if(polinomio.charAt(i) == '+'){
				  coef = true;
				  terms.addLast(new Monomio(coeficiente, exponente));
				  exponente = 0;
				  coeficiente = 1;
			  }else if(coef && valor != -1) {
				  coeficiente = Character.getNumericValue(polinomio.charAt(i));
			  }else if(valor != -1) {
				  exponente = Character.getNumericValue(polinomio.charAt(i));
			  }else if (polinomio.equals("0")) {
				  coef = true;
			  }
	  }
	  terms.addLast(new Monomio(coeficiente, exponente));
  }

  
  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
	  return  Polinomio.SumaResta(p1, p2, 1);
  }
    
  /**
   * Substraccion de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la resta de los polinomios.
   */
  public static Polinomio resta(Polinomio p1, Polinomio p2) {
	  return  Polinomio.SumaResta(p1, p2, -1);
  }
    
  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
	 PositionList<Polinomio> list = new NodePositionList<>();
	 Polinomio solucion = new Polinomio();
	 Position<Monomio> cursorm = p2.terms.first();
	 while(cursorm != null) {
		 list.addFirst(multiplica(cursorm.element(), p1));
		 cursorm = p2.terms.next(cursorm);
	 }
	 Position<Polinomio> cursorp = list.first();
	 while(cursorp != null) {
		 solucion = suma(solucion, cursorp.element());
		 cursorp = list.next(cursorp);
	 }
	 return solucion;
  }

  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Monomio t, Polinomio p) {
    Polinomio solucion = new Polinomio();
    Position<Monomio> cursor = p.terms.first();
    while(cursor != null) {
    	int coeficiente = t.getCoeficiente() * cursor.element().getCoeficiente();
    	int exponente = t.getExponente() + cursor.element().getExponente();
    	solucion.terms.addLast(new Monomio(coeficiente, exponente));
    	cursor = p.terms.next(cursor);
    }
    return solucion;
  }
    
  /**
   * Devuelve el valor del polinomio cuando su variable es sustiuida por un valor concreto.
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser -1.
   * @param valor el valor asignado a la variable del polinomio
   * @return el valor del polinomio para ese valor de la variable.
   */
  public long evaluar(int valor) {
	  Position<Monomio> cursor = terms.first();
	  int value = 0;
	  while(cursor != null) {
		  value += Math.pow(valor, cursor.element().getExponente()) * cursor.element().getCoeficiente();
		  cursor = terms.next(cursor);
	  }
    return value;
  }

  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * @return el grado del polinomio
   */
  public int grado() {
	  if(terms.size() > 0) {
		  return terms.first().element().getExponente();
	  }
	  return -1;
  }

  @Override
  public String toString() {
    if (terms.isEmpty()) return "0";
    else {
      StringBuffer buf = new StringBuffer();
      Position<Monomio> cursor = terms.first();
      while (cursor != null) {
        Monomio p = cursor.element();
        int coef = p.getCoeficiente();
        int exp = p.getExponente();
        buf.append(new Integer(coef).toString());
        if (exp > 0) {
          buf.append("x");
          buf.append("^");
          buf.append(new Integer(exp)).toString();
        }
        cursor = terms.next(cursor);
        if (cursor != null) buf.append(" + ");
      }
      return buf.toString();
    }
  }
/* comprueba si dos polinomios son iguales
*/
 
  @Override
 public boolean equals(Object obj) {
	if (this == obj)
		return true;
	else if (obj instanceof Polinomio) {
		Polinomio pol = (Polinomio) obj;
		if (terms.size() != pol.terms.size()) {
			return false;
		}else return terms.equals(pol.terms);
	}else 
		return false;
}
  /* 
   * El valor de variante puede ser 1 o -1 dependiendo de si se trata de suma o de resta y asi proceder· a realizar la operacion.
   */
  private static <E> Polinomio SumaResta(Polinomio p1, Polinomio p2, int variante) {
	  Polinomio solucion = new Polinomio();
	  Position<Monomio> cursorp1 = p1.terms.first();
	  Position<Monomio> cursorp2 = p2.terms.first();
	  while(cursorp1 != null || cursorp2 != null) {	  
		  if(cursorp1 != null && cursorp2 != null && cursorp1.element().getExponente() == cursorp2.element().getExponente()) {
			  int suma = cursorp1.element().getCoeficiente() + (variante)*cursorp2.element().getCoeficiente();
			  if(suma != 0) {
			  solucion.terms.addLast(new Monomio(suma, cursorp1.element().getExponente()));
			  }
			  cursorp1 = p1.terms.next(cursorp1);
			  cursorp2 = p2.terms.next(cursorp2);
		  }else if(cursorp2 != null && cursorp1 == null || cursorp2 != null && cursorp1 != null && cursorp1.element().getExponente() < cursorp2.element().getExponente()) {
			  solucion.terms.addLast((new Monomio (cursorp2.element().getCoeficiente()*(variante), cursorp2.element().getExponente())));
			  cursorp2 = p2.terms.next(cursorp2);
		  }else {
			  solucion.terms.addLast((cursorp1.element()));
			  cursorp1 = p1.terms.next(cursorp1);
		  }
	  }
    return solucion;
  }
}
