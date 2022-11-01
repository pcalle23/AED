package aed.hotel;

import java.util.Comparator;
import es.upm.aedlib.indexedlist.*;

public class MiHotel implements Hotel {
	private IndexedList<Habitacion> habitaciones;

	public MiHotel() {
		this.habitaciones = new ArrayIndexedList<>();
	}
	/*Realizamos una busqueda binaria para clases que extiendad de la clase padre Comparable. Por tanto es 
	 * requisito indispensable que tengan como metodo CompareTo(); Para ello le pasamos como parableto el 
	 * objeto a buscar y la lista donde debe estar contenido. */

	static public <E extends Comparable<E>> int buscar(E e, IndexedList<E> l) {
		int inicio = 0;
		int fin = l.size();
		while (inicio <= fin) {
			int medio = (fin - inicio) / 2 + inicio;
			if (l.get(medio).compareTo(e) < 0) {
				inicio = medio + 1;
			} else if (l.get(medio).compareTo(e) > 0) {
				fin = medio - 1;
			} else {
				return medio;
			}
		}
		throw new IllegalArgumentException();
	}
	/*Simplemente buscamos la habitacion que se quiere reservar con la busqueda binaria y luego 
	 * se termina insertando en la lista de habitaciones. Haciendo asi que siga un orden establecido
	 * por el compareTo() del objeto con coste asi de 2*log(n). De esta forma solo necesitamos dar como
	 * parametro de entrada la habitacion y de salida metera esa habitacion o lanzara una excepcion. */

	@Override
	public void anadirHabitacion(Habitacion habitacion) {
		MiHotel.insertar(habitacion, habitaciones, new ComparaHabitaciones());
	}

	/*Introducimos como parametro la reserva, buscamos la habitacion por el numero de habitacion que tenga 
	 * la reserva. Luego, intentamos insertar el valor en la lista de reservas asociado y si no lanzara una excepcion. 
	 * tiene coste 2*log (n). */
	@Override
	public boolean reservaHabitacion(Reserva reserva) {
		if(habitaciones.size()==0) {
			throw new IllegalArgumentException();
		}
		int h = MiHotel.buscar(new Habitacion(reserva.getHabitacion(),-1),habitaciones);
		try {
			return MiHotel.insertar(reserva,habitaciones.get(h).getReservas(),new ComparaReservas());
		}catch(IllegalArgumentException err) {
			return false;
		}

	}
	/* Buscamos la avitacion por el atributo habitacion del parametro. Y luego buscamos a ver si esta la reserva si esta se eliminara si no saltara Error
	 *  no se si se puede hacer que al no encontrar tampoco la reserva salga un IllegalArgumentExeption*/
	@Override
	public boolean cancelarReserva(Reserva reserva) {
		int encontrado = MiHotel.buscar(new Habitacion(reserva.getHabitacion(),-1), habitaciones); 
		Habitacion habAux = habitaciones.get(encontrado);
		habAux.getReservas().removeElementAt(MiHotel.buscar(reserva, habAux.getReservas()));
		return true;
	}

	/*con este metodo podriamos haber metido un segundo bucle for he ir iterando uno a uno. Pero consideramos que es mejor como lo solucionamos
	 * ya que se va de un coste 0(N^2) a 0(n*2log(n)). Sabemos que de parametros de entrada tenemos un dia de entrada y otro de salida y que
	 * de output hay que sacar una lista con las habitaciones disponibles */
	@Override
	public IndexedList<Habitacion> disponibilidadHabitaciones(String diaEntrada, String diaSalida) {
		Reserva intervalo = new Reserva(null,null,diaEntrada,diaSalida); 
		IndexedList<Habitacion> solucion = new ArrayIndexedList<>();
		for(int i = 0; i < habitaciones.size(); i++) {
			Habitacion habAux= habitaciones.get(i);
			try {
				if( MiHotel.insertar(intervalo,habAux.getReservas(),new ComparaReservas())) {
					MiHotel.insertar(habAux,solucion, new ComparaPrecios());
					habAux.getReservas().removeElementAt(MiHotel.buscar(intervalo,habAux.getReservas()));
				}
			}catch(IllegalArgumentException err){}
		}
		return solucion;
	}
	/* Entra como parametro el dni/pasaporte de un cliente y devolvera aquellas reservas que tenga realizadas
	 * este cliente. */
	@Override
	public IndexedList<Reserva> reservasPorCliente(String dniPasaporte) {
		ComparaClientes c = new ComparaClientes();
		Reserva cliente = new Reserva(null,dniPasaporte,null,null); 
		IndexedList<Reserva> solucion = new ArrayIndexedList<>();
		for(int i = 0, k = 0; i < habitaciones.size() && habitaciones.size() != 0; i++) {
			Habitacion habAux= habitaciones.get(i);
			for(int j = 0; j < habAux.getReservas().size(); j++) {
				if(c.compare(habAux.getReservas().get(j),cliente) == 0) {
					solucion.add(k, habAux.getReservas().get(j));
					k++;
				}
			}
		}
		return solucion;
	}
	/* Entra como parametro un dia y comprueba que habitaciones estan ocupadas en ese tiempo y devolvera unicamente
	 * aquellas que esten libres que seran las que se puedan limpiar. */
	public IndexedList<Habitacion> habitacionesParaLimpiar(String hoyDia) {
		IndexedList<Habitacion> solucion = new ArrayIndexedList<>();
		for(int i = 0; i < habitaciones.size(); i++) {
			boolean encontrado = false;
			int inicio = 0;
			Habitacion habAux= habitaciones.get(i);
			int fin = habAux.getReservas().size() -1;
			while (inicio <= fin && !encontrado && habAux.getReservas().size() > 0) {
				int medio = (fin - inicio) / 2 + inicio;
				if (habAux.getReservas().get(medio).getDiaSalida().compareTo(hoyDia) < 0) {
					inicio = medio + 1;
				} else if (habAux.getReservas().get(medio).getDiaEntrada().compareTo(hoyDia) >= 0) {
					fin = medio - 1;
				}else {
					encontrado = true;
				}
			}
			if(encontrado) {
				MiHotel.insertar(habAux, solucion, new ComparaHabitaciones());
			}
		}
		return solucion;
	}


	/* Recibe como parametros el nombre de una habitacion y un dia, y el metodo comprueba que para estos hay una
 reserva, siendo este el caso retornara la resera y si no coinciden retornara null. */
	@Override
	public Reserva reservaDeHabitacion(String nombreHabitacion, String dia) {
		Habitacion h = new Habitacion(nombreHabitacion,0);
		h = habitaciones.get(MiHotel.buscar(h, habitaciones));
		int inicio = 0;
		int fin = h.getReservas().size() -1;
		while (inicio <= fin && h.getReservas().size() > 0) {
			int medio = (fin - inicio) / 2 + inicio;
			if (h.getReservas().get(medio).getDiaSalida().compareTo(dia) <= 0) {
				System.out.println("iteracion i: "+medio +" /"+inicio+" /"+fin);
				inicio = medio + 1;
			} else if (h.getReservas().get(medio).getDiaEntrada().compareTo(dia) > 0) {
				fin = medio - 1;
			}else {
				return h.getReservas().get(medio);
			}
		}
		return null;
	}


	static <E extends Comparable<E>> boolean insertar(E e, IndexedList<E> l, Comparator<E> c)  {
		int inicio = 0;
		int fin = l.size();
		if(l.size() == 0 || c.compare(l.get(0),e) > 0) {
			l.add(0, e);
			return true;
		}
		if(c.compare(l.get(fin-1),e) < 0) {
			l.add(fin, e);
			return true;
		}
		while (inicio <= fin) {
			int medio = (fin - inicio) / 2 + inicio;
			if (c.compare(l.get(medio), e) < 0) { 
				if(c.compare(l.get(medio+1), e) > 0) { 
					l.add(medio+1, e);
					return true;
				}
				inicio = medio + 1;
			} else if (c.compare(l.get(medio), e) > 0) {
				if(c.compare(l.get(medio-1), e) < 0) {
					l.add(medio, e);
					return true;
				}
				fin = medio - 1;
			} else {
				throw new IllegalArgumentException();
			}
		}
		return false;
	}

	/* Realiza una comparacion de dos habitaciones devolviendo esta misma. */
	public static class ComparaHabitaciones implements Comparator<Habitacion>{

		@Override
		public int compare(Habitacion h1, Habitacion h2) {
			return h1.compareTo(h2);
		}

	}
	/* Buscamos una comparacion de los dias de las habitaciones de que recibe como parametro las dos habitaciones 
	a comparar retornandonos la diferencia de los dias de entrada y salida de ambas habitaciones. */
	public static class ComparaReservas implements Comparator<Reserva>{

		@Override
		public int compare(Reserva o1, Reserva o2) {
			if(!conflicto(o1.getDiaEntrada(), o1.getDiaSalida(), o2.getDiaEntrada(), o2.getDiaSalida())){
				return o1.compareTo(o2);   
			}
			return 0;  
		}
	}
	/* Buscamos una comparacion de los clientes de que recibe como parametro los dos clientes a comparar
 retornandonos la comparacion de los dni/pasaporte de estos. */
	public static class ComparaClientes implements Comparator<Reserva>{

		@Override
		public int compare(Reserva o1, Reserva o2) {
			return o1.getDniPasaporte().compareTo(o2.getDniPasaporte()); 
		}
	}

	static boolean conflicto (String diaEntrada1, String diaSalida1, String diaEntrada2, String diaSalida2 ) {
		return !(diaSalida1.compareTo(diaEntrada2) <= 0 || diaSalida2.compareTo(diaEntrada1) <=  0) ; 
	}

	/* Buscamos una comparacion de los precios de que recibe como parametro las dos habitaciones a comparar
 retornandonos la diferencia de los dos precios. */
	public static class ComparaPrecios implements Comparator<Habitacion>{
		@Override
		public int compare(Habitacion h1, Habitacion h2) {
			return h1.getPrecio() - h2.getPrecio(); 
		}
	}
	public static void main(String[] args) {
	}
}
