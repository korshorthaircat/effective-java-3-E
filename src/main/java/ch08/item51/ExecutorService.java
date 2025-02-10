package ch08.item51;

public class ExecutorService {
    public void execute(ExecutionRequest request) {
        request.validate();

        System.out.println("Executing with:");
        System.out.println("paramA: " + request.getParamA());
        System.out.println("paramB: " + request.getParamB());
        System.out.println("paramC: " + request.isParamC());
        System.out.println("paramD: " + request.getParamD());

        // 추가적인 계산 로직 수행...
    }
}
