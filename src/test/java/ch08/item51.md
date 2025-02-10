# 51. 메서드 시그니처를 신중히 설계하라

> (개별 아이템으로 두기 애매한 API 설계 요령들)
>
> 메서드 이름을 신중히 짓자.
> 편의 메서드를 너무 많이 만들지 말자.
> 매개변수 목록은 짧게 유지하자.
> 	→ 과하게 긴 매개변수 목록을 짧게 줄여주는 기술
> 매개변수의 타입으로는 클래스보다 인터페이스가 낫다.
> 매개변수의 타입으로는 boolean보다 원소 2개짜리 열거 타입이 낫다.

- - - 
## 메서드 이름을 신중히 짓자.
* 항상 표준 명명규칙을 따라야 한다.
* 같은 패키지에 속한 다른 이름들과 일관되게 짓는게 최우선이다.
* 개발자 커뮤니티에 널리 받아들여지는 이름을 사용하자.
* 긴 이름은 피하자.

## 편의 메서드를 너무 많이 만들지 말자.
* 메서드가 너무 많은 클래스는 익히고, 사용하고, 문서화하고, 테스트하고, 유지보수하기 어렵다.

## 매개변수 목록은 짧게 유지하자.
* 매개변수는 4개 이하가 좋다.
    * 같은 타입의 매개변수 여러 개가 연달아 나오는 경우 특히 해롭다. (순서 기억 어렵고, 실수로 순서 바꿔 입력해도 컴파일됨)
### 과하게 긴 매개변수 목록을 짧게 줄여주는 기술
#### 과하게 긴 매개변수 목록을 짧게 줄여주는 기술 (1) 여러 메서드로 쪼갠다.
* 여러 메서드로 쪼갤 때, 쪼개진 메서드 각각은 원래 매개변수 목록의 부분집합을 받는다.
* 잘못하면 메서드가 너무 많아질 수 있지만, [[직교성(orthogonality)]]이 높아짐에 따라 오히려 메서드 수를 줄여주는 효과가 있다.
    * “직교성이 높아짐” = “공통점이 없는 기능들이 잘 분리되어 있음”
    * 예) `java.util.List`인터페이스
        * 상황: 리스트에서 주어진 원소의 인덱스를 찾아야 하는데, 전체 리스트가 아니라 지정된 범위의 부분 리스트에서 인덱스를 찾아야 함
            * 이 기능을 별개의 메서드로 제공하려면 매개변수가 3개 필요하다. (부분리스트의 시작, 부분리스트의 끝, 찾을 원소)
            * 그런데 List는 대신에 부분리스트를 반환하는 subList와, 주어진 원소의 인덱스를 찾는 inedxOf 메서드를 별개로 제공함으로써, 두 메서드를 조합하여 원하는 목적을 이루게끔 한다.
    * 편의성을 높인다는 생각에 고수준의 복잡한 기능을 하나씩 메서드로 추가하다보면, 부지불식간에 눈덩이처럼 커진 api가 만들어질 수 있다. 하지만 잘 갖춰진 기본 기능을 제공하면 아무리 복잡한 기능도 조합해낼 수 있다.
    * 일반적으로 직교성이 높은 설계는 가볍고 구현하기 쉬우며 유연하고 강력하다.
    * 그렇다고 무한정 메서드를 작게 나누는 것이 능사는 아니다.
        * → api가 다루는 개념의 추상화 수준에 맞게 조절해야 한다.
        * → 특정 조합의 패턴이 상당이 자주 사용되거나 최적화하여 성능을 크게 개선할 수 있다면, 직교성이 낮아지더라도 편의 기능으로 제공하는 편이 나을 수 있다.
    * 직교성은 절대적인 가치라기보다는, 철학과 원칙을 가지고 일관되게 적용해야 하는 설계 특성이다.

#### 과하게 긴 매개변수 목록을 짧게 줄여주는 기술 (2) 매개변수 여러개를 묶어주는 도우미 클래스를 만든다.
* 일반적으로 도우미 클래스는 정적 멤버 클래스로 둔다. 참고) [[24. 멤버 클래스는 되도록 static으로 만들라]]
* 잇따른 매개변수 몇 개를 독립된 하나의 개념으로 볼 수 있을 때 추천하는 기법이다.
* 예) 카드 게임을 클래스로 만들때, 메서드 호출시 카드의 숫자(rank), 무늬(suit)를 항상 전달해야 한다. 이 둘을 묶는 도우미 클래스를 만들어 하나의 매개변수로 전달하면 api는 물론 클래스 내부 구현도 깔끔해진다.
#### 과하게 긴 매개변수 목록을 짧게 줄여주는 기술 (3) 모든 매개변수를 하나로 추상화한 객체를 정의해, 클라이언트에서 이 객체의 setter를 호출해 필요한 값을 설정하게 한다.
* 빌더 패턴을 메서드 호출에 응용하는 것
* 매개변수가 많을 때, 모든 매개변수를 하나로 추상화한 객체를 정의하고, 클라이언트에서 이 객체의 setter를 호출해 필요한 값을 설정하게 한다.
* 이 때 각 setter 메서드는 매개변수 하나 혹은 서로 연관된 몇 개만 설정하게 한다.
* 클라이언트는 먼저 필요한 매개변수를 다 설정한 다음, execute를 호출해 앞서 설정한 매개변수들의 유효성을 검사한다.
* 마지막으로 설정이 완료된 객체를 넘겨 원하는 계산을 수행한다.
* 예) 빌더 패턴을 적용해 과도하게 긴 매개변수를 하나의 객체로 추상화

## 매개변수의 타입으로는 클래스보다 인터페이스가 낫다.
* 예) 메서드에서 HashMap이 아닌 Map을 받는다.

## 매개변수의 타입으로는 boolean보다 원소 2개짜리 열거 타입이 낫다.
* 메서드 이름상 boolean을 받아야 의미가 명확할 때는 예외이다.
* 열거 타입 사용시
    * 코드를 읽고 쓰기 더 쉬워진다.
    * 나중에 선택지 추가하기도 쉽다.
* 예)
    * `Thermometer.newInstance(TemperatureScale.CELSIUS)`가 `Thermometer.newInstance(true)`보다 이해가 쉽다.
```java
public enum TemperatureScale {FAHRENHEIT, CELSIUS}

public class Thermometer {
    private final TemperatureScale scale;

    private Thermometer(TemperatureScale scale) {
        this.scale = scale;
    }

    public static Thermometer newInstance(TemperatureScale scale) {
        if (scale == null) {
            throw new IllegalArgumentException("Temperature scale cannot be null");
        }
        return new Thermometer(scale);
    }

    public TemperatureScale getScale() {
        return scale;
    }

    public static void main(String[] args) {
        Thermometer celsiusThermometer = Thermometer.newInstance(TemperatureScale.CELSIUS);
        Thermometer fahrenheitThermometer = Thermometer.newInstance(TemperatureScale.FAHRENHEIT);

        System.out.println(celsiusThermometer);
        System.out.println(fahrenheitThermometer);
    }
}
```
