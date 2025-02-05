# 48. 스트림 병렬화는 주의해서 적용하라

>  계산도 정확하고 성능도 빨라질 거라는 확신 없이는 스트림 파이프라인 병렬화를 시도하지 마라.
>  스트림을 잘못 병렬화하면 프로그램을 오작동하게 하거나 성능을 급격히 떨어뜨린다.
>  병렬화하는 편이 낫다고 믿어라도, 수정 후 코드가 여전히 정확한지 확인하고 운영 환경과 유사한 조건에서 수행해보며 성능지표를 유심히 관찰하라.

- - - 
## 자바 주요 릴리즈에서의 동시성 프로그래밍 지원을 위한 개선
* 자바1.0 (1996)
    * Thread 클래스, Runnable 인터페이스 도입
    * synchronized 키워드를 통한 동기화 지원
    * wait/notify 메서드를 사용한 스레드간 통신
* 자바5 (2004)
    * **Java Concurrency Framework (`java.util.concurrent` 패키지) 추가**
    - **`Executor` 프레임워크**: 스레드 풀(Thread Pool) 관리
* 자바7 (2011)
    * **Fork/Join 프레임워크 (`java.util.concurrent.forkjoin`) 도입**. 고성능 병렬 분해(parallel decom-position)프레임워크
        - `ForkJoinPool`: 작업을 자동으로 분할하여 병렬 실행하는 기능
* 자바8 (2014)
    * 함수형 프로그래밍 & 병렬 스트림
        * parallel 메서드 호출시 파이프라인 병렬 실행 가능한 스트림 지원

## 동시성 프로그램 작성시 유의할 점
* 안전성(safety)
* 응답 가능(liveness)

## 스트림 파이프라인을 (마구잡이로)병렬화하여 문제가 되는 예)
* 예) [[메르센 소수(Mersenne prime)]] 출력 프로그램
```java
public class ParallelMersennePrimes {  
	static Stream<BigInteger> primes() {  
		return Stream.iterate(TWO, BigInteger::nextProbablePrime);  
	}  
	  
	public static void main(String[] args) {  
		long startTime = System.nanoTime();  
		System.out.println("startTime = " + startTime);  
		  
		// 스트림을 사용해 처음 20개의 메르센 소수 생성  
		primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))  
			// .parallel() // 병렬 스트림 사용시 문제 발생! CPU를 90%나 사용하며 출력되지 않는다.  
			.filter(mersenne -> mersenne.isProbablePrime(50))  
			.limit(20)  
			.forEach(System.out::println);  
		  
		long endTime = System.nanoTime();  
		System.out.println("endTime = " + endTime);  
		System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000 + " ms"); // 실행 시간 출력  
	}  
}
```

### 스트림 병렬화가 적합한지 아닌지 판단이 필요하다!
#### 스트림 파이프라인 병렬화가 성능 개선에 도움을 주지 못하는 경우
* 데이터 소스가 `Stream.iterate`이거나, 중간 연산으로 limit을 쓰면 파이프라인 병렬화로 성능개선을 기대할 수 없다.
    * **`Stream.iterate()`는 병렬 처리에 적합하지 않은 이유**
        - `Stream.iterate()`는 내부적으로 순차적으로 값을 생성합니다. 따라서 `parallel()`을 사용해도 각 스레드가 독립적으로 데이터를 가져오는 것이 아니라, 공유된 단일 스트림에서 순차적으로 데이터를 생성하는 구조입니다. 이는 병렬 처리의 장점을 살리지 못하게 합니다.
    * limit 사용시 파이프라인 병렬화로 성능개선을 기대할 수 없는 이유
        * 파이프라인 병렬화는 limit을 다룰 때, CPU 코어가 남는다면 원소를 몇 개 더 처리한 후 제한된 개수 이후의 결과를 버려도 아무 해가 없다고 가정한다.
        * 그런데 이 코드의 경우 새롭게 메르센 소수를 찾을 때마다 그 전 소수를 찾을 때 보다 두 배 정도 더 오래 걸린다. (원소 하나를 계산하는 비용이 대략 그 이전까지의 원소 전부를 계산한 비용을 합친 것만큼 든다는 의미)
* 종단 연산에서 수행하는 작업량이 파이프라인 전체 작업량 중 상당비중을 차지하며, 순차적인 연산일 때
    * 종단 연산에서 병렬화에 적합한 방식
        * 축소(reduction)
            * 파이프라인에서 만들어진 모든 원소를 하나로 합치는 작업
            * Stream의 reduce 메서드 중 하나, 혹은 min, max, count, sum 중 하나를 선택해 수행함
        * anyMatch, allMatch, noneMatch처럼 조건에 맞으면 바로 반환되는 메서드
    * 종단 연산에서 병렬화에 적합하지 않은 방식
        * collect
            * 가변 축소(mutable reduction)을 수행하는 Stream의 collect는 병렬화에 적합하지 않음
            * 컬렉션을 합치는 부담이 크기 때문

#### 병렬화의 효과가 가장 좋은 스트림의 소스 타입
* ArrrayList, HashMap, HashSet, ConcurrentHashMap의 인스턴스
* 배열, int, long
* 특징
    * 데이터를 원하는 크기로 정확하고 손쉽게 나눌수 있어, 일을 다수의 스레드에 분배하기 좋음
        * 나누는 작업은 Spliterator가 담당하며, Spliterator객체는 Stream이나 Iterable의 spliterator 메서드로 얻어낼 수 있음
    * 원소들을 순차적으로 실행할 때 참조 지역성(locality of reference)가 뛰어남
        * 이웃한 원소의 참조가 메모리에 연속적으로 저장되어 있음
        * 하지만 참조가 가리키는 실제 객체는 메모리에 서로 떨어져 있을 수 있으며, 그러면 참조 지역성이 나빠짐
        * 참조 지역성이 낮은 경우, 스레드는 데이터가 주메모리에서 캐시메모리로 오기를 기다리며 일을 하지 않음
        * 따라서 참조 지역성은 다량 데이터 처리하는 벌크 연산 병렬시 매우 중요한 요소임
        * 참조 지역성이 가장 뛰어난 자료구조: 기본 타입 배열
            * 기본 타입 배열에서는 (참조가 아닌)데이터 자체가 메모리에 연속적으로 저장되기 때문

### 스트림을 잘못 병렬화할 경우의 문제점
* (응답 불가를 포함해)성능이 나빠질 뿐만 아니라, 결과 자체가 잘못되거나 예상 못한 동작이 발생할 수 있다.
* 안전 실패(safety failure)
    * 병렬화한 파이프라인이 사용하는 mappers, filters 혹은 프로그래머가 제공한 다른 함수 객체가 명세대로 작동하지 않을 때 발생 가능
* 스트림 병렬화는 오직 성능 최적화 수단임을 기억하라.
* 스트림 병렬화가 효과를 보는 경우는 많지 않다. → 단, 조건이 잘 갖춰지면 parallel 메서드 호출 하나로 엄청난 성능 향상을 만끽할 수 있다.

## 스트림 파이프라인을 병렬화로 성능 개선되는 예)
```java
public class ParallelPrimeCounting {  
	/**  
	* n보다 작거나 같은 소수의 개수 반환  
	*  
	* @param n  
	* @return n보다 작거나 같은 소수의 개수  
	*/  
	static long pi(long n) {  
		return LongStream.rangeClosed(2, n)  
			.parallel()  
			.mapToObj(BigInteger::valueOf)  
			.filter(i -> i.isProbablePrime(50))  
			.count();  
	}  
	  
	public static void main(String[] args) {  
		long startTime = System.nanoTime();  
		System.out.println("startTime = " + startTime);  
	  
		System.out.println(pi(10_000_000));  
	  
		long endTime = System.nanoTime();  
		System.out.println("endTime = " + endTime);  
		System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");  
	}  
}
// 출력 결과:  
	// (1) 병렬화하지 않을 때  
	// startTime = 105715665733916  
	// 664579  
	// endTime = 105731267078666  
	// Execution Time: 15601 ms  
	//  
	// (2) 병렬화했을 때 - parallel()// startTime = 105779176134458  
	// 664579  
	// endTime = 105783155160291  
	// Execution Time: 3979 ms  
```
* 위 코드의 스트림이 병렬연산에 적합한 이유
    * **데이터가 독립적이고 연산이 개별적으로 수행됨**
        - 각 숫자(`long` 값)는 다른 숫자와 **독립적으로** 소수 판별 연산을 수행합니다.
        - 따라서 각 요소를 개별적으로 처리하는 데 **경합 조건(Race Condition)이 발생하지 않음** → 병렬 처리에 적합.
    - **CPU 집중 연산(CPU-Bound Operation)**
        - `isProbablePrime(50)`은 **계산량이 많은 연산**(CPU를 많이 사용)입니다.
        - 병렬 처리의 이점이 큼 → 여러 개의 CPU 코어를 활용하면 전체 실행 시간이 줄어듦.
    - **순서가 중요하지 않음 (Order Independence)**
        - `count()` 연산은 스트림의 **순서를 유지할 필요 없음**(비순차적 연산).
        - 병렬 처리 시, 개별 요소를 처리한 후 개수를 합산하면 되므로 병렬화 효율이 높음.
    - **병렬 처리에 적합한 데이터 구조 사용**
        - `LongStream.rangeClosed(2, n)`은 **프리미티브 타입 스트림**으로, 내부적으로 **효율적인 분할(split)**이 가능.
        - `Spliterator`가 범위를 균등하게 분할하여 여러 코어에서 처리 가능.

##### 추가 학습 필요
* `SplittableRandom`
    * 무작위 수로 이뤄진 스트림 병렬화할 때는 ThreadLocalRandom보다 SplittableRandom 사용 권장
* `ThreadLocalRandom`
    * 단일 쓰레드에서 사용하고자 설계됨
    * 병렬 스트림용 데이터 소스로 사용 가능하지만 SplittableRandom만큼 빠르지 않음
* `Random`
    * 모든 연산을 동기화하므로, 병렬처리시 성능이 최악임