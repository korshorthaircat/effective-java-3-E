# 50. 적시에 방어적 복사본을 만들라

> 방어적으로 프로그래밍해야 한다!
>
> 클래스가 클라이언트로부터 받는, 혹은 클라이언트로 반환하는 구성요소가 가변이라면
> 그 요소는 반드시 방어적으로 복사해야 한다.
> 복사 비용이 너무 크거나 클라이언트가 그 요소를 잘못 수정할 일이 없음을 신뢰한다면
> 방어적 복사를 수행하는 대신 해당 구성요소를 수정했을 때의 책임이 클라이언트에 있음을 문서에 명시하도록 하자.

- - - 
* 클라이언트가 불변식을 깨뜨리려 혈안이 되어 있다고 가정하고 방어적으로 프로그래밍해야 한다.
    * 악의적인 의도 or 평범한 프로그래머의 실수
    * 적절치 않은 클라이언트로부터 클래스를 보호하는데 충분한 시간을 투자하는 게 좋다.
### 불변식이 깨진 클래스 예) Preiod
* 기간을 표현하는 클래스(Period)를 정의하여, 한번 값이 정해지면 변하지 않도록 할 생각이었다.
    * 하지만 자기도 모르게 내부를 수정하도록 허락하는 경우가 생긴다
```java
package ch08.item50;  
  
import java.util.Date;  
  
/**  
* Period - 불변식이 꺠진 클래스  
*/  
public final class Period1 {  
	private final Date start;  
	private final Date end;  
	  
	/**  
	* @param start 시작 시각  
	* @param end 종료 시각; 시작 시각보다 뒤여야 한다.  
	* @throws IllegalArgumentException 시작 시각이 종료 시각보다 늦을 때 발생한다.  
	* @throws NullPointerException start나 end가 null이면 발생한다.  
	*/  
	public Period1(Date start, Date end) {  
		if (start.compareTo(end) > 0)  
			throw new IllegalArgumentException(start + " after " + end);  
		this.start = start;  
		this.end = end;  
	}  
	  
	public Date start() {  
		return start;  
	}  
	public Date end() {  
		return end;  
	}  
	  
	public String toString() {  
		return start + " - " + end;  
	}  
}
```
#### Period의 내부를 변경하려는 공격(1)
```java
public class Attacks {  
	public static void main(String[] args) {  
		//----------- 첫 번째 공격  
		Date start = new Date();  
		Date end = new Date();  
		Period1 p = new Period1(start, end);  
		end.setYear(78); // Period의 내부를 수정할 수 있다!  
		System.out.println(p);    
	}
}
```
* 방어 방법
    * `Date` 대신 ==불변인 `Instant`(혹은 `LocalDateTime`, `ZonedDateItem`)를 사용==하면 해소된다.
        * 참고) [[Date, Instant, LocalDateTime, ZonedDateTime 비교]]
        * `Date`는 낡은 api이므로 새로운 코드 작성시 더이상 사용하면 안 된다.
        * 하지만 `Date`처럼 가변인 낡은 값 타입을 사용하던 시절이 길었으므로, 여전히 많은 api의 내부 구현에 그 잔재가 남아있다.
    * 생성자에서 받은 가변 ==매개변수 각각에 방어적 복사(defensive copy) 시행==
        * Period 인스턴스 안에서 원본이 아닌 복사본 사용
```java
// 수정한 생성자  
public Period2(Date start, Date end) {  
	// 매개변수의 방어적 복사본을 만들어 사용한다.  
	this.start = new Date(start.getTime());  
	this.end = new Date(end.getTime());  
	
	// 순서에 유의!
	// 방어적 복사 수행 이후에, 방아적 복사본을 가지고 매개변수 유효성 검사를 수행한다.  
	if (this.start.compareTo(this.end) > 0)  
		throw new IllegalArgumentException(this.start + " after " + this.end);  
}
```
* 주의) ==방어적 복사를 매개변수 유효성 검사 이전에 수행하기==
    * 방어적 복사 수행 이후에, 방어적 복사본을 가지고 매개변수 유효성 검사를 수행해야 한다.
    * 순서가 부자연스러워 보일지라도 반드시 이렇게 작성해야 한다.
        * 이유: 멀티스레딩 환경에서 원본 객체의 유효성 검사 후 복사본을 만드는 찰나의 순간, 다른 스레드가 원본 객체를 수정할 위험이 있기 때문이다. (검사시점/사용시점(time-of-check/time-of-use, TOCTOU) 공격)
* 주의) ==생성자에서 방어적 복사시 clone() 메서드 사용하지 않음==
    * 매개변수가 제 3자에 의해 extends될 수 있는 타입이라면 방어적 복사본 만들 때 clone 사용해서는 안 됨
    * 이유: Date는 final이 아니어서 extends 가능하므로, Date의 하위 타입을 만들어서 악의적으로 clone 재정의할 수도 있기 때문
#### Period의 내부를 변경하려는 공격(2)
```java
public class Attacks {  
	public static void main(String[] args) {  
		//----------- 두 번째 공격  
		Date start3 = new Date();  
		Date end3 = new Date();  
		Period1 p3 = new Period1(start3, end3);  
		p3.end().setYear(78); // p의 내부를 변경했다!  
		System.out.println("p3 = " + p3);
	}
}
```
* 방어 방법
    * ==접근자가 가변 필드의 방어적 복사본을 반환==하도록 한다.
```java
// 수정한 접근자 - 필드의 방어적 복사본을 반환한다.  
public Date start() {  
	return new Date(start.getTime());  
}  
  
public Date end() {  
	return new Date(end.getTime());  
}
```
* 접근자 메서드에서는 방어적 복사에 clone() 사용 가능
    * 이유: Period가 가진 Date객체가 jav.util.Date임이 확실하기 때문(신뢰할 수 없는 하위 타입이 아님)
    * 그럼에도 불구하고 인스턴스 복사시에는 일반적으로 생성자나 정적 팩터리 메서드를 쓰는게 좋다. 참고) [[13. clone 재정의는 주의해서 진행하라]]

### 매개변수 방어적 복사가 필요한 이유
* (1) 불변 객체를 만들기 위해서
* (2) 클라이언트가 제공한 객체의 참조를 내부의 자료구조에 보관해야 할 때 변경 가능성 고려
    * 클라이언트가 제공한 객체가
        * 변경될 수 있는 객체라면 → 그 객체가 클래스에 넘겨진 뒤 임의로 변경되어도 클래스가 문제없이 동작할지 고려 필요
        * 변경될 가능성이 있는지 확신할 수 없다면 → 복사본 만들어 저장할 것
    * 예) 클라이언트가 건네준 객체를 내부의 Set 인스턴스에 저장하거나 Map 인스턴스의 키로 사용하는 경우, 추후 그 객체가 변경될 때 객체를 담고있는 Set이나 Map의 불변식이 깨진다.
### 내부 객체를 만드는 제공하기 전 방어적 복사가 필요한 이유
* 참고) [[15. 클래스와 멤버의 접근 권한을 최소화하라]]

### 방어적 복사할 일을 줄이려면 → 되도록 불변 객체를 조합해 객체를 구성해야 한다.
* Period 예제의 경우, Instant, LocalDateTime, ZonedDateTime을 사용했다면(불변 객체를 조합해 객체 구성) 방어적 복사를 하지 않아도 좋다.
* 방어적 복사의 단점
    * 성능 저하
### 방어적 복사를 생략할 수 있는 경우
* (같은 패키지에서 속하는 등의 이유로) 호출자가 컴포넌트 내부를 수정하지 않으리라 확신하는 경우
    * 이 경우에도 호출자가 매개변수나 반환값을 수정하지 않아야 한다고 명확히 문서화해야 함
* 다른 패키지에서 사용하지만, 메서드나 생성자의 매개변수로 넘기는 행위가 객체의 통제권을 이전함을 뜻하는 경우
    * 통제권을 이전하는 메서드를 호출하는 클라이언트는 해당 객체를 더이상 직접 수정할 일이 없다고 약속해야 함
    * 클라이언트가 건네주는 가변 객체의 통제권을 넘겨받는다고 기대하는 메서드나 생성자에서도 이 사실을 문서화해야한다.
* 클래스와 해당 클라이언트가 상호 신뢰가 있을 때
    * 통제권을 넘겨받기로 한 메서드나 생성자를 가진 클래스는 악의적 클라이언트의 공격에 취약할 수 있기 때문
* 불변식이 깨지더라도 그 영향이 오직 호출한 클라이언트로 국한될 때
    * 에) 래퍼 클래스 패턴
        * 참고: [[18. 상속보다는 컴포지션을 사용하라]]
        * 래퍼 클래스의 특성상 클라이언트는 래퍼에 넘긴 객체에 여전히 직접 접근 가능함. 래퍼의 불변식을 파괴할 수 있으나 오직 그 영향을 클라이언트 자신만 받게 됨