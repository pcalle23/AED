package aed.individual2;

import es.upm.aedlib.indexedlist.*;

public class IndexedListCheckSumUtils {

	// a no es null, podria tener tamaño 0, n>0
	public static IndexedList<Integer> indexedListCheckSum(IndexedList<Integer> list, int n) {
		ArrayIndexedList<Integer> res = new ArrayIndexedList<>();
		if (list.size()==0) { 
			ArrayIndexedList<Integer> res1 = new ArrayIndexedList<>();
			return res1;
		}
		int i=0;
		int j=0;
		int contador=0;
		int checksum=0;
		while (i<list.size()) {
			if (contador==n) {
				res.add(j, checksum);
				checksum=0;
				contador=0;
			}else {
				res.add(j, list.get(i));
				checksum=checksum+list.get(i);
				i++;
				contador++;
			}
			j++;
		}
		res.add(res.size(),checksum);


		return res;
	}
	// list no es null, podria tener tamaño 0, n>0
	public static boolean checkIndexedListCheckSum(IndexedList<Integer> list, int n) {
		ArrayIndexedList<Integer> res = new ArrayIndexedList<>();
		boolean comprobar=true;
		int i=0;
		int j=0;
		int contador=0;
		int checksum=0;
		if(list.size()==n) {
			comprobar=false;
		}else if (list.size()<n) {
			comprobar=true;
		}else if(list.size()%(n+1)==1){
			comprobar=false;
		}else {
			while (i<list.size()) {
				if (contador==n) {
					res.add(j, checksum);
					if (checksum!=list.get(i)) {
						comprobar=false;
					}
					checksum=0;
					contador=0;
				}else {
					res.add(j, list.get(i));
					checksum=checksum+list.get(i);

					contador++;
				}
				j++;
				i++;

			}

		}
		return comprobar;
	}
}


