# 44. 표준 함수형 인터페이스를 사용하라

> 이제 자바도 람다를 지원한다. 지금부터는 API를 설계할 때 람다도 염두에 두어야 한다는 뜻이다.
> 입력값과 반환값에 함수형 인터페이스 타입을 활용하라. 보통은 java.util.function 패키지의 표준 함수형 인터페이스를 사용하는 것이 가장 좋은 선택이다.
> 흔치는 않지만 새로운 함수형 인터페이스를 만들어 쓰는 편이 나을 수도 있다.

- - -

## 함수형 인터페이스를 직접 선언해 사용해보는 예시
###  예) `LinkedHashMap`의 `removeEldestEntry`
```java
protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	return size() > 100;
}
```
* `LinkedHashMap`에서 가장 오래된 항목을 자동으로 삭제할지 여부를 결정하는 protected 메서드
* 기본적으로 `false`를 반환하여 자동 삭제가 일어나지 않지만, 오버라이딩하면 특정 개수를 초과할 때 자동 삭제하도록 설정 가능
* 캐시 기능 구현시 유용하게 사용할 수 있음
  * [MyCache1.java](..%2F..%2F..%2Fmain%2Fjava%2Fch07%2Fitem44%2FMyCache1.java)
  * [MyCache2.java](..%2F..%2F..%2Fmain%2Fjava%2Fch07%2Fitem44%2FMyCache2.java)
* 직접 선언한 함수형 인터페이스 사용하는 경우
  * [MyCache3.java](..%2F..%2F..%2Fmain%2Fjava%2Fch07%2Fitem44%2FMyCache3.java)
* 표준 함수형 인터페이스 사용하는 경우
  * [MyCache4.java](..%2F..%2F..%2Fmain%2Fjava%2Fch07%2Fitem44%2FMyCache4.java)


## 표준 함수형 인터페이스 사용의 장점
* 자바 표준 라이브러리에 이미 같은 모양의 인터페이스가 존재하면 굳이 재작성할 필요가 없음
* api가 다루는 개념의 수가 줄어들어 익히기 더 쉬움
* 표준 함수형 인터페이스가 유용한 디폴트 메서드를 많이 제공함
  * 다른 코드와의 상호운용성이 좋아짐
  * 예) Predicate인터페이스는 predicate를 조합하는 메서드를 제공함

## 기본 함수형 인터페이스

| 인터페이스               | 함수 시그니처               | 예시                    |
| ------------------- | --------------------- | --------------------- |
| `UnaryOperator<T>`  | `T apply (T t)`       | `String::toLowerCase` |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | `BigInteger::add`     |
| `Predicate`         | `boolean test(T t)`   | `Collection::isEmpty` |
| `Function<T, R>`    | `R apply(T t)`        | `Arrays::asList`      |
| `Supplier<T>`       | `T ge()`              | `Instant::now`        |
| `Consumer<T>`       | `void accept(T t)`    | `System.out::println` |

## 커스텀 함수형 인터페이스 선언이 필요한 상황
* 커스텀 함수형 인터페이스가 반드시 필요한 상황
  * 구조적으로 필요한 용도의 표준 함수형 인터페이스가 없을 때
    * 예) 매개변수를 3개 받는 Predicate, 검사 예외를 던지는 경우...
* 커스텀 함수형 인터페이스 선언을 고려해볼만한 상황
  * (1) 자주 쓰이며 이름 자체가 용도를 명확히 설명해줄 때
  * (2) 반드시 따라야하는 규약이 있을 때
  * (3) 유용한 디폴트 메서드를 제공할 수 있을 때
* 예) `Comparator<T>`는 구조적으로는 `ToIntBiFunction<T, U>`와 구조적으로 동일하지만 독자적 인터페이스로 존재함
    * 이유?
        * (1) api에서 자주 사용되는데 이름이 그 용도를 훌륭히 설명함
        * (2) 구현하는 쪽에서 반드시 지켜야할 규약을 담고 있음
        * (3) 비교자들을 변환하고 조합해주는 디폴트메서드를 갖고 있음

## 커스텀 함수형 인터페이스 작성시 유의할 점 
* '인터페이스' 설계이므로 아주 주의해서 설계!
* `@FunctionInterface` 애너테이션 달기
  * 프로그래머의 의도 명시. 이 인터페이스가 람다용으로 설계되었음을 알리기. 
  * 컴파일시 오류 예방, 유지보수과정에서 누가 실수로 메서드 추가 못하게...

## 함수형 인터페이스를 api에서 사용시 유의할 점
* 서로 다른 함수형 인터페이스를 같은 위치의 인수로 받는 메서드들을 다중정의해서는 안 됨(다중 정의는 주의해서 사용하라)
  * 클라이언트에게 모호함을 야기하기 때문

- - -
* 참고3) [[Map과 Entry]]
* 참고1) [[HashMap의 내부 구조 및 저장 과정]]
* 참고2) [[HashMap과 LinkedHashMap 비교]]