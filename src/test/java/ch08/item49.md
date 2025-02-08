# 49. 매개변수가 유효한지 검사하라

> 메서드나 생성자를 작성할 때 그 매개변수들에 어떤 제약이 있을지 생각해야 한다.
> 제약들을 문서화하고 메서드 코드 시작 부분에서 명시적으로 검사해야 한다.
> 이러한 노력은 유효성 검사가 실제 오류를 처음 걸러낼 때 충분히 보상받을 것이다.

- - - 
## 일반 원칙: “오류는 가능한 한 빨리 (발생한 곳에서) 잡아야 한다”
* 이유
    * 오류를 발생 즉시 잡지 못하면 해당 오류를 감지하기 어려워지고, 감지하더라도 발생 지점을 찾기 어려워지기 때문
* 구체적 원칙
    * ==입력 매개변수에 대한 제약은 반드시 문서화해야 한다.==
    * ==입력 매개변수에 대한 제약은 메서드 몸체가 시작되기 전에 검사해야 한다.==
        * 메서드 몸체가 실행되기 전 매개변수를 확인한다면 잘못된 값이 넘어왔을 때 즉각적으로 예외를 던질 수 있다.

* 메서드와 생성자 대부분은 입력받은 매개변수의 값이 특정 조건을 만족하기를 바란다.
    * 예) 인덱스 값은 음수여서는 안 된다. 객체 참조는 null이어서는 안 된다… 등
* 매개변수 검사를 제대로 하지 않는 경우 발생 가능한 문제들
    * (1) 메서드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다.
    * (2) 메서드가 잘 수행되지만 잘못된 결과를 반환할 수 있다.
    * (3) 메서드는 잘 수행됐지만 어떤 객체를 이상한 상태로 만들어, 미래의 알 수 없는 시점에 이 메서드와 관련 없는 오류를 낼 수 있다.
    * → 매개변수 검사에 실패하면 실패 원자성(failure atomicity)를 어기게 될 수 있다.

### 원칙(1): 입력 매개변수에 대한 제약은 반드시 문서화해야 한다.
* public, protected 메서드는 매개변수 값 잘못되었을 때 던지는 예외를 반드시 문서화해야 한다.
    * 보통은 IllegalArgumentException, IndexOutOfBoundsException, NullPointerException 중 하나가 될 것
    * `@throws` 자바독 태그 사용하면 된다.
    * 클래스 수준 주석은 그 클래스의 모든 public 메서드에 적용되므로 각 메서드에 일일이 기술하는 것보다 깔끔하다.
    * 예)
```java
/**  
* Returns a BigInteger whose value is {@code (this mod m}). This method  
* differs from {@code remainder} in that it always returns a  
* <i>non-negative</i> BigInteger.  
*  
* @param m the modulus.  
* @return {@code this mod m}  
* @throws ArithmeticException {@code m} m ≤ 0
* @see #remainder  
*/  
public BigInteger mod(BigInteger m) {  
if (m.signum <= 0) // m이 0 이하이면 예외 발생. m.signum은 양수일 때 1 반환
	throw new ArithmeticException("BigInteger: modulus not positive");  
	  
	BigInteger result = this.remainder(m); // 기본 나머지 연산 수행
	return (result.signum >= 0 ? result : result.add(m)); // 음수면 m을 더해서 양수로 변환 } 
}
```

### 원칙(2): 입력 매개변수에 대한 제약은 메서드 몸체가 시작되기 전에 검사해야 한다.
#### java7의 null 검사 기능 사용하기: `java.util.Objects.requireNonNull`
* `java.util.Objects.requireNonNull`
    * 자바7에 추가됨
    * 유연하고 사용하기 편하며, 더이상 null 검사를 수동으로 하지 않아도 된다.
    * 원하는 예외 메세지도 지정할 수 있다.
    * 입력을 그대로 반환하므로, 값을 사용하는 동시에 null 검사를 수행할 수 있다.
    * 반환값을 무시하고 필요한 곳 어디서든 순수한 null 검사 목적으로 사용할 수도 있다.
```java
public class Main {
    public static void main(String[] args) {
        String name = null;
        String result = Objects.requireNonNull(name, "이름은 null일 수 없습니다."); 
        // NullPointerException: 이름은 null일 수 없습니다.

		String result2 = Objects.requireNonNull(name, () -> "이름이 필요합니다!"); // NullPointerException: 이름이 필요합니다!
    }
}
```
##### 활용 예시
- **메서드 인자의 유효성 검사**
```java
public void setName(String name) {
    this.name = Objects.requireNonNull(name, "이름은 null일 수 없습니다.");
}
```
- **객체 생성 시 null 방지**
```java
public class User {
    private final String username;

    public User(String username) {
        this.username = Objects.requireNonNull(username, "username은 필수입니다.");
    }
}
```
- **null-safe 반환값 처리**
```java
public static String getValue(String value) {
    return Objects.requireNonNull(value, "반환값이 null입니다.");
}
```

#### java9의 null 검사 기능 사용하기: `requireNonNullElse` 및 `requireNonNullElseGet`
* null일 경우의 기본값 지정
* **`requireNonNullElse(T obj, T defaultObj)`**
    - `obj`가 `null`이면 `defaultObj`를 반환
```java
String name = null;
String safeName = Objects.requireNonNullElse(name, "기본값");
System.out.println(safeName); // 기본값
```
* **`requireNonNullElseGet(T obj, Supplier<? extends T> supplier)`**
    - `obj`가 `null`이면 `supplier`에서 생성한 값을 반환
```java
String name = null;
String safeName = Objects.requireNonNullElseGet(name, () -> "기본값 생성");
System.out.println(safeName); // 기본값 생성
```

#### java9의 인덱스 유효성 검사 기능 사용하기: `checkFromIndexSize`, `checkFromToIndex`, `checkIndex`
* `checkFromIndexSize`, `checkFromToIndex`, `checkIndex`
* null 검사 메서드만큼 유연하지는 않다.
    * 예외 메세지 저장 불가
    * 리스트와 배열 전용으로 설계됨
    * 닫힌 범위(양 끝단 값을 포함하도록)는 다루지 못함
* 위의 제약과 무관한 상황에서는 아주 유용하고 편하다.

|메서드|조건|예외 발생 상황|
|---|---|---|
|`checkIndex(index, length)`|`0 ≤ index < length`|index가 범위를 초과한 경우|
|`checkFromToIndex(fromIndex, toIndex, length)`|`0 ≤ fromIndex ≤ toIndex ≤ length`|`toIndex`가 `length`를 초과하거나, `fromIndex > toIndex`일 때|
|`checkFromIndexSize(fromIndex, size, length)`|`0 ≤ fromIndex ≤ fromIndex + size ≤ length`|`fromIndex + size`가 `length`를 초과할 때|
#### public이 아닌 메서드는 단언문(assert)를 사용해 매개변수 유효성을 검사할 수 있다.
```java
private static void sort(long[] a, int offset, int length) {
	assert a != null;
	assert offset >= 0 && offset <= a.length;
	assert length >= 0 && length <= a.length - offset;
	// ...
}
```
* assert문은 단언한 조건이 무조건 참이라고 선언한다.
* assert문과 일반적인 유효성 검사의 차이
    * (1) assert는 실패하면 AssertionError를 던진다.
    * (2) assert는 런타임에 아무런 효과도, 아무런 성능 저하도 없다.
        * 단, java 실행시 명령줄에서 `-ea`, `--enableassertions` 플래그 설정시 런타임에 영향을 준다.
* `assert` 문은 **개발 및 디버깅 과정에서 코드의 논리적 오류를 빠르게 잡기 위한 도구** 즉, 프로그램이 예상대로 동작하는지 확인하기 위해 사용됨
* 왜 `assert`는 기본적으로 비활성화 되어 있는가?
    - **운영 환경(Production)에서는 성능 최적화가 중요**
    - `assert` 문은 실행 중에도 불필요한 검사를 수행할 수 있으므로 **배포(운영) 시에는 비활성화**
    - 하지만 **개발 및 테스트 환경에서는 활성화하여 버그를 미리 방지** 가능
* `assert` vs `if` 검증의 차이점
    - **개발 중 오류를 조기에 잡고 싶을 때 → `assert`**
    - **운영 환경에서도 검증이 필요할 때 → `if` 조건 + 예외 처리 (`throw new Exception`)**

| 비교 항목           | `assert`           | `if` 문                                              |
| --------------- | ------------------ | --------------------------------------------------- |
| **의도**          | 디버깅 & 테스트용         | 정상적인 실행 흐름 제어                                       |
| **기본 동작**       | 비활성화 (`-ea` 옵션 필요) | 항상 실행됨                                              |
| **운영 환경 사용 여부** | X (비활성화됨)          | O (실제 로직에서 사용)                                      |
| **예제 코드**       | `assert x > 0;`    | `if (x <= 0) throw new IllegalArgumentException();` |

#### 메서드 몸체 실행 전에 매개변수 유효성을 검사해야 한다. → 예외
* 예외
    * 유효성 검사 비용이 지나치게 높거나 실용적이지 않을 때
    * 검사 과정에서 암묵적으로 검사가 수행될 때
        * 예) Collections.sort(List): 상호비교될수 없는 타입의 객체가 List에 들어있다고 하더라도, 그 객체와 비교할 때 ClassCastException을 던질 것이므로 비교하기 전 미리 List검사를 하는 것이 실익이 없다.
        * 단, 암묵적 유효성 검사에 너무 의존했다가는 실패 원자성을 해칠 수 있으니 유의
        * 계산과정에 필요한 유효선 검사가 이뤄지더라도, 잘못된 예외를 던지는 경우도 있다. (api문서에서 던진다고 설명한 예외와 실제 발생한 예외가 다를 수도 있음)
            * 예외 번역(exception translate)관용구 사용해 api 문서에 기재된 예외로 번역해줘야 한다.

#### 나중에 쓰려고 저장하는 매개변수의 유효성 검사는 더 신경써서 검사해야 한다.
* “나중에 쓰려고…” → 예외 발생시 어디서 가져왔는지 추적하기 어려워 디버깅이 어려울 수 있기 때문
* 예외) 생성자
    * 생성자 매개변수의 유효성 검사는 클래스 불변식을 어기는 객체가 만들어지지 않게 하는데 꼭 필요하다.
