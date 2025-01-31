public class Test {

    public static final TestHandler testHandler = new TestHandler();
    public static void main(String[] args) {
        String[] args2 = {"MEMORY_SPACE_DEFRAG"};
        if (args2.length < 1) {
            System.out.println("Usage: java Test <Question Name/number>");
        } else {
            TesterQuestionEnum question = TesterQuestionEnum.valueOf(args2[0]);
            testHandler.questionDecider(question, args2);
        }
    }
}
