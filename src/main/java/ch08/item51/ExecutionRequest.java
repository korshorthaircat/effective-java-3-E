package ch08.item51;
public class ExecutionRequest {
    private String paramA;  // 필수값
    private int paramB;     // 선택값 (기본값: 0)
    private boolean paramC; // 선택값 (기본값: false)
    private double paramD;  // 선택값 (기본값: 1.0)

    // private 생성자 (빌더 패턴 사용)
    private ExecutionRequest(Builder builder) {
        this.paramA = builder.paramA;
        this.paramB = builder.paramB;
        this.paramC = builder.paramC;
        this.paramD = builder.paramD;
    }

    // 유효성 검사 메서드
    public void validate() {
        if (paramA == null || paramA.isEmpty()) {
            throw new IllegalArgumentException("paramA는 필수입니다.");
        }
        if (paramB < 0) {
            throw new IllegalArgumentException("paramB는 0 이상이어야 합니다.");
        }
    }

    // 빌더 클래스 (기본값 포함)
    public static class Builder {
        private String paramA;
        private int paramB = 0;
        private boolean paramC = false;
        private double paramD = 1.0;

        // 필수 매개변수만 생성자에서 설정
        public Builder(String paramA) {
            this.paramA = paramA;
        }

        public Builder setParamB(int paramB) {
            this.paramB = paramB;
            return this;
        }

        public Builder setParamC(boolean paramC) {
            this.paramC = paramC;
            return this;
        }

        public Builder setParamD(double paramD) {
            this.paramD = paramD;
            return this;
        }

        public ExecutionRequest build() {
            return new ExecutionRequest(this);
        }
    }

    public String getParamA() { return paramA; }
    public int getParamB() { return paramB; }
    public boolean isParamC() { return paramC; }
    public double getParamD() { return paramD; }
}
