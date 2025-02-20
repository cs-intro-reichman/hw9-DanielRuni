public class TestMemorySpace {

    public static void main(String[] args) {
        testAll();
    }

    private static void testAll() {
        testInitialization();
        testSimpleAllocation();
        testMultipleAllocations();
        testAllocationFailure();
        testFree();
        testDefrag();
        testComplexScenario();

        System.out.println("All tests completed successfully!");
    }

    private static void testInitialization() {
        MemorySpace memory = new MemorySpace(100);
        String expected = "(0 , 100)\n";
        assertString(expected, memory.toString(), "Initialization");
    }

    private static void testSimpleAllocation() {
        MemorySpace memory = new MemorySpace(100);
        int address = memory.malloc(20);
        assertEqual(0, address, "Simple allocation address");

        String expected = "(20 , 80)\n(0 , 20)\n";
        assertString(expected, memory.toString(), "Simple allocation state");
    }

    private static void testMultipleAllocations() {
        MemorySpace memory = new MemorySpace(100);
        int addr1 = memory.malloc(20);
        int addr2 = memory.malloc(30);
        int addr3 = memory.malloc(40);

        assertEqual(0, addr1, "First allocation");
        assertEqual(20, addr2, "Second allocation");
        assertEqual(50, addr3, "Third allocation");

        String expected = "(90 , 10)\n(0 , 20) (20 , 30) (50 , 40)\n";
        assertString(expected, memory.toString(), "Multiple allocations state");
    }

    private static void testAllocationFailure() {
        MemorySpace memory = new MemorySpace(100);
        memory.malloc(60);
        int address = memory.malloc(50);
        assertEqual(-1, address, "Allocation failure");

        String expected = "(60 , 40)\n(0 , 60)\n";
        assertString(expected, memory.toString(), "State after allocation failure");
    }

    private static void testFree() {
        MemorySpace memory = new MemorySpace(100);
        int addr1 = memory.malloc(20);
        memory.malloc(30);
        memory.free(addr1);

        String expected = "(0 , 20)(50 , 50)\n(20 , 30)\n";
        assertString(expected, memory.toString(), "Free memory state");
    }

    private static void testDefrag() {
        MemorySpace memory = new MemorySpace(100);
        memory.malloc(20);
        memory.malloc(20);
        int addr3 = memory.malloc(20);

        memory.free(0);
        memory.free(addr3);

        String beforeDefrag = "(0 , 20) (40 , 20) (60 , 40)\n(20 , 20)\n";
        assertString(beforeDefrag, memory.toString(), "Before defrag");

        memory.defrag();

        String afterDefrag = "(0 , 20) (40 , 60)\n(20 , 20)\n";
        assertString(afterDefrag, memory.toString(), "After defrag");
    }

    private static void testComplexScenario() {
        MemorySpace memory = new MemorySpace(100);
        memory.malloc(20); // Allocates at address 0
        int addr2 = memory.malloc(20); // Allocates at address 20
        memory.malloc(20); // Allocates at address 40
    
        memory.free(addr2); // Frees the block at address 20
        int addr4 = memory.malloc(10); // Allocates at address 60
    
        assertEqual(60, addr4, "Reuse freed space");
    
        String expected = "(20 , 20) (70 , 30)\n(0 , 20) (40 , 20) (60 , 10)\n";
        assertString(expected, memory.toString(), "Complex scenario state");
    }

    private static void assertEqual(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ": Expected " + expected + " but got " + actual);
        }
    }

    private static void assertString(String expected, String actual, String message) {
        // Normalize and split the strings into sets of blocks
        String[] expectedBlocks = normalizeString(expected).split("\\)\\(");
        String[] actualBlocks = normalizeString(actual).split("\\)\\(");
    
        // Handle edge cases for leading/trailing parentheses
        if (expectedBlocks.length > 0) {
            expectedBlocks[0] = expectedBlocks[0].replace("(", "");
            expectedBlocks[expectedBlocks.length - 1] = expectedBlocks[expectedBlocks.length - 1].replace(")", "");
        }
        if (actualBlocks.length > 0) {
            actualBlocks[0] = actualBlocks[0].replace("(", "");
            actualBlocks[actualBlocks.length - 1] = actualBlocks[actualBlocks.length - 1].replace(")", "");
        }
    
        // Compare the sets of blocks
        java.util.Arrays.sort(expectedBlocks);
        java.util.Arrays.sort(actualBlocks);
    
        if (!java.util.Arrays.equals(expectedBlocks, actualBlocks)) {
            throw new AssertionError(message + ":\nExpected: " + java.util.Arrays.toString(expectedBlocks) +
                    "\nActual: " + java.util.Arrays.toString(actualBlocks));
        }
    }

    private static String normalizeString(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}