package aed.individual5;

import java.util.Iterator;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.map.*;

public class Utils {
	public static <E> PositionList<E> deleteRepeated(PositionList<E> l) {
		PositionList<E> list = new NodePositionList<E>();
		Position <E> cursor;
		int j = 0;
	    for (int i = 0; i < l.size() - 1; i++) {
	        if (cursor != list.next(cursor)) {
	            list.addAfter(cursor, list.prev(cursor));
	            j++;
	        }
	    }
	    list[j] = l[list.size() - 1];
	 return list;
		return null;
	}

	public static <E> PositionList<E> compactar (Iterable<E> lista) {

		return null;
	}

	public static Map<String,Integer> maxTemperatures(TempData[] tempData) {
		
}