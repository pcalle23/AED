package aed.individual1;
public class ArrayCheckSumUtils {
	// a no es null, podria tener tamaño 0, n>0
	public static int[] arrayCheckSum(int[] arr, int n) {
		int [] res;
		int checksum=0;
		int contador=0;
		int k=0;
		int i=0;
		if (arr.length==0) {
			return res= new int[0];
		}else {
			if (arr.length%n==0) {
				res= new int[arr.length+ (arr.length/n)];
			}else
				res= new int[arr.length+ (arr.length/n)+1];

			while (i<arr.length) {
				if	(contador==n) {
					res[k]=checksum;
					checksum=0;
					contador=0;
				}else {
					res[k]= arr[i];
					checksum=checksum+arr[i];
					i++;
					contador++;
				}
				k++;
			}

			
			res[res.length-1]=checksum;

			return res;
		}
	}
}