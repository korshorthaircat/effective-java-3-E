package ch02.item01;

public class Settings {
    private boolean useAutoSteering;
    private boolean useABS;
    private Difficulty difficulty;

    private Settings() {} //외부에서 생성자 호출이 불가능하게 접근제어자를 private으로 설정함
    private static final Settings SETTINGS = new Settings();

    //정적 팩터리 메서드로 미리 만들어놓은 인스턴스를 반환하면 불필요한 객체 생성을 막을 수 있다. (플라이웨이트Flyweight 패턴과 비슷한 기법)
    //(특히 생성 비용이 큰)같은 객체가 자주 요청되는 상황이라면 성능을 상당히 끌어올려준다.
    public static Settings getInstance() {
        return SETTINGS;
    }
}
