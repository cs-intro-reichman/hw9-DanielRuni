/* This program runs a test of the MMS system.
 * It allocates and frees memory blocks, and checks the impact on the free list
 * and the allocated list. If the MMS is implemented correctly, it should produce
 * the outputs documented below.
 * 
 * This class provides a *FINAL TEST* of the memory management system.
 * Before using it, you must unit-test each step of your development, separately and
 * gradually, as you develop the various methods of the LinkedList and MemorySpace classes.
 */
public class LinkedListTest {
	public static void main (String[] args) {
		MemorySpace m = new MemorySpace(1000);
		System.out.println(m + "\n");
		//// Expected output:
		//// (0 , 1000) 
		
		// Makes a few memory allocations, and keeps the addresses
		// of the allocated blocks in an array.
		int[] addresses = new int[20];
		for (int i = 0; i < addresses.length; i++) {
			addresses[i] = m.malloc((int) Math.pow(i + 2, 5) % 123);
		}
		System.out.println(m + "\n");
		//// Expected output:
		//// (959 , 41) 
		//// (0 , 32) (32 , 120) (152 , 40) (192 , 50) (242 , 27) (269 , 79) (348 , 50) (398 , 9) (407 , 1) (408 , 44) (452 , 3) (455 , 79) (534 , 68) (602 , 96) (698 , 1) (699 , 68) (767 , 42) (809 , 109) (918 , 32) (950 , 9) 
		
		// Frees a few memory blocks:
		for (int i = 0; i < addresses.length; i += 2) {
			m.free(addresses[i]);
		}
		System.out.println(m + "\n");
		//// Expected output:
		//// (959 , 41) (0 , 32) (152 , 40) (242 , 27) (348 , 50) (407 , 1) (452 , 3) (534 , 68) (698 , 1) (767 , 42) (918 , 32) 
		//// (32 , 120) (192 , 50) (269 , 79) (398 , 9) (408 , 44) (455 , 79) (602 , 96) (699 , 68) (809 , 109) (950 , 9) 

		// Frees some more memory and defrags (IGNORE THIS TESTING SECTION FOR NOW)
		m.free(addresses[1]);
		m.free(addresses[3]);
		m.defrag();
		System.out.println(m);
		//// expected output:
		//// (959 , 41) (0 , 269) (348 , 50) (407 , 1) (452 , 3) (534 , 68) (698 , 1) (767 , 42) (918 , 32) 
		//// (269 , 79) (398 , 9) (408 , 44) (455 , 79) (602 , 96) (699 , 68) (809 , 109) (950 , 9) 
	}
}