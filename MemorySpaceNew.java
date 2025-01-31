/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpaceNew {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpaceNew(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {		
		Node node = freeList.getNode(0);
		MemoryBlock freeBlock; 
		while (node != null) {
			freeBlock = node.block;
			if (freeBlock.length > length) {
				MemoryBlock newBlock = new MemoryBlock(freeBlock.baseAddress, length);
				allocatedList.addLast(newBlock);
				freeBlock.length = freeBlock.length - length;
				freeBlock.baseAddress = freeBlock.baseAddress + length;
				return newBlock.baseAddress;
			}
			if (freeBlock.length == length) {
				allocatedList.addLast(freeBlock);
				freeList.remove(freeBlock);
				return freeBlock.baseAddress;
			}
			node = node.next;
		}
		// No block was allocated:
		return -1;
	}

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		MemoryBlock allocatedBlock;
		Node node = allocatedList.getNode(0);
		while (node != null) {
		    allocatedBlock = node.block;
			if (allocatedBlock.baseAddress == address) {
				freeList.addLast(allocatedBlock);
				allocatedList.remove(allocatedBlock);
				return;
			}
			node = node.next;
		}
	}
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		LinkedList remove = new LinkedList();
		ListIterator it = freeList.iterator();
		while(it.hasNext()){
			MemoryBlock curr = it.next();
			boolean flag = true;
			while(flag){
				flag = false;
				ListIterator it2 = freeList.iterator();
				while(it2.hasNext()){
					MemoryBlock next = it2.next();
					if(curr.baseAddress + curr.length == next.baseAddress){
						if (remove.indexOf(next) == -1){
							remove.addLast(next);
						}
						curr.length = curr.length + next.length;
						flag = true;
						break;
					}
				}
			}
		}
		it = remove.iterator();
		while(it.hasNext()) {
			freeList.remove(it.next());
		}
	}

	public static void main(String[] args) {
		MemorySpaceNew memory = new MemorySpaceNew(100);
		// System.out.println(memory.malloc(40)); // 0
		// System.out.println(memory.malloc(40)); // 10
		// System.out.println(memory.malloc(30)); // 10
		int address = memory.malloc(5);
		int address1 = memory.malloc(20);
		int address2 = memory.malloc(20);
		int address3 = memory.malloc(55);
		System.out.println(address);
		System.out.println(address1);
		System.out.println(address2);
		System.out.println(address3);
		System.out.println(memory);
		System.out.println();
		memory.free(address);
		memory.free(address1);
		memory.free(address2);
		System.out.println(memory);
		System.out.println();
		memory.defrag();
		System.out.println(memory);
		// int address4 = memory.malloc(15);
		// System.out.println(address4);
		// System.out.println(memory);
		// int address3 = memory.malloc(10);
		
		// System.out.println(memory);
	}
}
