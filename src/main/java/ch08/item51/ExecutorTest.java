package ch08.item51;

public class ExecutorTest {
    public static void main(String[] args) {
        // 필수값만 설정, 선택값은 기본값으로 처리됨
        ExecutionRequest request1 = new ExecutionRequest.Builder("필수값")
                .build();

        // 일부 선택값만 설정
        ExecutionRequest request2 = new ExecutionRequest.Builder("필수값")
                .setParamB(100) // 기본값 0 → 100으로 변경
                .build();

        // 모든 값을 설정
        ExecutionRequest request3 = new ExecutionRequest.Builder("필수값")
                .setParamB(200)
                .setParamC(true)
                .setParamD(99.9)
                .build();

        ExecutorService service = new ExecutorService();
        service.execute(request1);
        service.execute(request2);
        service.execute(request3);
    }
}

