package aed.filter;

import java.util.Iterator;
import es.upm.aedlib.positionlist.*;
import java.util.function.Predicate;


public class Utils {

  public static <E> Iterable<E> filter(Iterable<E> d, Predicate<E> pred) {
	  PositionList <E> res = new NodePositionList <E>();
	  Iterator <E> iter = d.iterator();
	  if (d != null) {
	  while (iter.hasNext()) {
		  E x = iter.next();
		  if ( x != null && pred.test(x))
			  res.addLast(x);}
	  }else
		  throw new IllegalArgumentException();
    return res;
  }
}

