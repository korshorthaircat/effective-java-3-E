# 47. 반환 타입으로는 스트림보다 컬렉션이 낫다

> 원소 시퀀스를 반환하는 메서드 작성시에는, 이를 스트림으로 처리하기 원하는 사용자와 반복으로 처리하길 원하는 사용자가 모두 있을 수 있음을 떠올리고 양쪽을 다 만족시키려고 노력하자.
> 	컬렉션을 반환할 수 있다면 그렇게 하라.
> 		반환 전부터 이미 원소들을 컬렉션에 담아 관리하고 있거나, 컬렉션을 하나 더 만들어도 될 정도로 원소 개수가 적다면 ArrayList같은 표준 컬렉션에 담아 반환하라.
> 		그렇지 않으면 앞서의 멱집합 예처럼 전용 컬렉션을 구현할지 고민하라.
> 	컬렉션을 반환할 수 없다면 스트림과 Iterable 중 더 자연스러운 것을 반환하라.
> 		나중에 Stream인터페이스가 Iterable을 지원하도록 자바가 수정된다면, 그떄는 안심하고 스트림을 반환하면 될 것이다.(스트림 처리와 반복 모두에 사용할 수 있으므로)

- - - 
## 주요 자료 구조의 for-each 및 stream 사용 가능 여부

| 개념                                     | `Iterable` 구현 여부  | `for-each` 사용 가능 여부 | `Stream` 사용 가능 여부                    | 비고                                                                                                              |
| -------------------------------------- | ----------------- | ------------------- | ------------------------------------ | --------------------------------------------------------------------------------------------------------------- |
| **`Iterable<T>`**                      | ✅ (최상위 인터페이스)     | ✅                   | ❌ (`Iterable` 자체는 `Stream` 변환 메서드 없음 | `for-each` 문에서 직접 사용 가능                                                                                         |
| **`Collection<E>` (List, Set, Queue)** | ✅ (`Iterable` 상속) | ✅                   | ✅ (`collection.stream()`)            | `List`, `Set`, `Queue` 등 대부분의 컬렉션 포함                                                                            |
| **`T[]` (배열)**                         | ❌                 | ✅ (JVM이 자동 변환)      | ✅ (`Arrays.stream(array)`)           | `Iterable`을 구현하지 않지만, for-each 사용 가능(원래 for-each 사용시 iterator가 필요하지만, 배열의 경우 JVM이 for-each를 인덱스 기반 루프로 변환해주기 때문 |
| **`Stream<T>`**                        | ❌                 | ❌                   | ✅ (자기 자신)                            | `for (T t : stream)`사용 불가 <br>하지만 `forEach()` 메서드를 제공함 (`stream.forEach(...)`)                                  |

* 참고) [[Iterable과 Collection]]
* 참고) [[Stream이 Iterable 구현하지 않은 이유]]
## 원소 시퀀스를 반환하는 public API의 반환타입으로는 Collection이 최선이다.
* 원소 시퀀스를 반환하는 public API의 반환타입으로는 Collection(또는 그의 하위타입)이 최선이다.
    * 이유: Collection 인터페이스는 반복과 스트림을 동시에 지원하기 때문이다.
        * Collection은 Iterable의 하위타입이므로 반복을 지원하고,
        * Collection은 stream 메서드를 제공하므로 스트림을 지원한다.
* 주의!) 컬렉션을 반환한다는 이유로, 덩치 큰 원소 시퀀스를 메모리에 올려서는 안 된다.
    * 반환할 시퀀스 크기가 메모리에 올려도 안전할만큼 적당하면, 표준 컬렉션 구현체(ArrayList, HashSet 등)을 반환하는게 최선이다.
    * 반환할 시퀀스 크기가 크지만, 표현을 간결하게 할 수 있다면 → 전용 컬렉션 구현 방안을 검토하라.
        * 예) 주어진 집합의 멱집합을 반환해야 하는 경우

### 예) 전용 컬렉션 구현하는 코드
* 예) 주어진 집합의 멱집합을 반환해야 하는 상황
    * 멱집합:
        * 한 집합의 모든 부분집합을 원소로 하는 집합
            * 예) {a, b, c}의 멱집합: {{}, {a}, {b}, {c}. {a, b}, {b, c}, {a, c}, {a, b, c}}
        * 집합의 원소 개수가 n개라면 → 멱집합의 원소 개수는 2^n 개 이다.
    * 급수적으로 늘어나므로, 멱집합을 표준 컬렉션 구현체에 저장해 반환하려는 생각은 위험하다. → 전용 컬렉션 구현 고려 필요 (`AbstarctList`이용)
    * 멱집합 표현의 아이디어
        * 멱집합을 구성하는 각 원소의 인덱스를 비트 벡터로 사용
        * 인덱스의 n번째 비트 값은 멱집합의 해당 원소가 원래 집합의 n번째 원소를 포함하는지 여부를 알려줌
        * 0부터 $2^n-1$까지의 이진수와 원소 n개인 집합의 멱집합이 자연스럽게 매핑됨
            * 예)
                * {a, b, c}의 멱집합은 원소가 8개이므로 유효한 인덱스는 0~7이며, 이진수로는 000~111이다.
                * 이 때 인덱스를 이진수로 나타내면, 각n번째 자리의 값이 각각 원소 a,b,c를 포함하는지 여부를 알려준다.
                * {a, b, c}의 멱집합의 인덱스를 이진수로 나타내rh → 각 원소 포함 여부 확인
                    * 000 → {}
                    * 001 → {a}
                    * 010 → {b}
                    * 011 → {b, a}
                    * 100 → {c}
                    * 101 → {c, a}
                    * 110 → {c, b}
                    * 111 → {c, b, a}
* 참고: [[비트마스크를 이용한 집합 구현]]

```JAVA
import java.util.*;  
  
/**  
 * 멱집합  
 * 멱집합은 어떤 집합의 모든 부분집합을 포함하는 집합이다.  
 * 원소가n개인 집합의 멱집합은 2^n개의 원소를 갖는다.  
 */public class PowerSet {  
  
    /**  
     * 전달받은 집합의 멱집합을 커스텀 컬렉션으로 반환한다.  
     */    public static final <E> Collection<Set<E>> of(Set<E> s) {  
        List<E> src = new ArrayList<>(s); // 리스트 변환을 통해 인덱스 사용 가능하게 함  
        if (src.size() > 30)  
            throw new IllegalArgumentException("집합에 원소가 너무 많습니다. " + s);  
        // 2^30개의 부분집합 생성시 메모리 사용량 고려  
  
        return new AbstractList<Set<E>>() {  
            // AbstractList 사용함으로써, 모든 부부집합을 미리 생성하지 않고 요청이 있을 때 계산하여 변환하는 Lazy Evaluation 기법을 적용  
            // 실제로 데이터가 저장되어 있는 것이 아니라, 필요할 때마다(get 호출 시) 부분집합을 계산해서 반환됨  
            @Override  
            public int size() {  
                return 1 << src.size(); // 멱집합의 크기는 2를 원래 집합의 원소 수만큼 거듭제곱한 것과 같다.  
            }  
  
            @Override  
            public boolean contains(Object o) {  
                return o instanceof Set && src.containsAll((Set)o);  
            }  
  
            @Override  
            public Set<E> get(int index) {  
                Set<E> result = new HashSet<>();  
                for (int i = 0; index != 0; i++, index >>= 1)  
                    // index의 각 비트는 집합 내 원소의 포함 여부를 나타냄 (1이면 포함, 0이면 제외)  
                    // for문에서 index를 오른쪽으로 한칸씩 이동하면서(2로 나누면서) 한 비트씩 검사  
                    if ((index & 1) == 1) // index의 이진수 첫째자리 값이 1인지 확인  
                        result.add(src.get(i)); // 일치하면 해당 원소를 결과 집합에 추가  
                return result;  
            }  
        };  
    }  
  
    public static void main(String[] args) {  
        Set<String> s = Set.of("a", "b", "c");  
        System.out.println(PowerSet.of(s));  
        // 출력 결과:  
//        [[], [a], [b], [a, b], [c], [a, c], [b, c], [a, b, c]]  
  
        // 참고  
// get(int index)는 System.out.println()이 리스트를 출력할 때 자동으로 호출된다.  
// AbstractList는 부분집합을 미리 계산해서 저장하지 않고 get(int index)가 호출될 때마다 비트 연산을 사용하여 부분집합을 생성한다.  
// toString()이 Iterator를 사용하여 리스트를 순회하면서 get(0), get(1), ..., get(size()-1)이 자동으로 여러 번 호출된 것이다.  
    }  
}
```
