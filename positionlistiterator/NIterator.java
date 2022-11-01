package aed.positionlistiterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;


public class NIterator<E> implements Iterator<E> {
	int pos;
	int limit;
	Position <E> cursor;
	boolean comprobante;
	PositionList <E> list;
	int cont;
	int vecesPasado;
	boolean hayMas=false;
	private boolean firstEl;
	public NIterator(PositionList<E> list, int n) {
		this.list=list;
		this.pos=n;
		this.cursor=list.first();
		comprobante=false;
		this.firstEl=false;

	}
	private static boolean eqNull(Object o1, Object o2) {
		return o1 == o2 || o1 != null && o1.equals(o2);
	}
	@Override
	public boolean hasNext() {
//		System.out.println("VecesContado: " + vecesPasado);
		if(!firstEl && cursor != null) {
			return true;
		} else if(cursor==null) {
			return false;
		} else {
			boolean result = true;
			int counter2 = 0;
			Position<E> cursor2 = cursor;
			
			while(result && counter2<pos) {
				cursor2=list.next(cursor2);
				result= cursor2!=null;
				counter2++;
			}
			
			
			return result;
			
		}
//		if(list.isEmpty())										
//			return false;
//		if (list.last().equals(cursor))
//			return false;
//		else
//			return true;		
		
	}
	@Override
	public E next() {
		
		System.out.println(list.toString());
		
		if (hasNext() && !cursor.equals(list.last())) {
			E res = null;
			
			System.out.println("el cursor  es: " + cursor.toString());
			if(comprobante){System.out.println("el valor  es: " + cursor.element().toString());}
			System.out.println("es nulo : " + comprobante);
			
			if (eqNull(cursor, list.first()) && !comprobante) {
				
				boolean hecho=false;
				
				for (int i=0;i<list.size() && !hecho;i++) {
					
					if (cursor != null && cursor.element() != null) {
						
						hecho=true;
						this.comprobante = true;
						res = cursor.element();
						
					}else {
						cursor=list.next(cursor);
						vecesPasado++;
					}
					
				}
				
				return res;
				
			}
			
			
			System.out.println(".....................\n");
			System.out.println("el cursor  es: " + cursor.toString());
			if(comprobante){System.out.println("el valor  es: " + cursor.element().toString());}
			System.out.println("elemento siguiete : " + res );
			
			
			boolean listo = false;
			
			for (int i=0; i<pos && !hayMas; i++) {
				cursor = list.next(cursor);
				vecesPasado++;
				System.out.println("vecesPasado: " + vecesPasado);
				if (vecesPasado+pos > list.size()) {
					this.hayMas=true;
				}
			}
			
			System.out.println(".....................\n");
			System.out.println("el cursor  es: " + cursor.toString());
			//if(comprobante){System.out.println("el valor  es: " + cursor.element().toString());}
			
			while (hasNext() && !listo && !hayMas) {
				
				if (cursor != null && cursor.element()!=null) {
					
					res = cursor.element();
					listo=true;
					
				}else 
					
					cursor = list.next(cursor);	
				
			}
			
			System.out.println(".....................\n");
			System.out.println("el cursor  es: " + cursor.toString());
			if(comprobante){System.out.println("el valor  es: " + cursor.element().toString());}
			System.out.println("elemento siguiete : " + res);
			
//			if (eqNull(cursor, list.last()) && cursor != null && cursor.element() != null) {
//				
//				res = cursor.element();
//				
//			}
			
			return res;
			
		}else {
			
			throw new NoSuchElementException();
		}
	}

	//	public static void main(String[] args) {
	//		NodePositionList <Integer> r = new NodePositionList <>();
	//		r.addFirst(null);
	//		r.addAfter(null, 1);
	////		r.addAfter( 1, 2);
	//		
	//	}

}