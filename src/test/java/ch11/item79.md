# 79. 과도한 동기화는 피하라
> 교착상태와 데이터 훼손을 피하려면
> 	동기화 영역 안에서 외계인 메서드를 절대 호출하지 말자. (→ 동기화 영역 안에서의 작업은 최소한으로 줄이자.)
> 가변 클래스를 설계할때는 스스로 동기화해야 할지 말지 고민하자.
> 	멀티코어 세상인 지금은 과도한 동기화를 피하는게 중요하다. 합당한 이유가 있을 때만 동기화하고, 동기화 여부를 문서에 밝히자.

- - - 
* 과도한 동기화가 발생시키는 문제
    * 정확성 측면: 교착 상태, 예측할 수 없는 동작 등
    * 성능 측면: 성능 저하
        * “과도한 동기화가 초래하는 진짜 비용은 락을 얻는데 드는 CPU 시간이 아니다. 바로 경쟁하느라 낭비한 시간, 즉 병렬로 실행될 기회를 잃고 모든 코어가 메모리를 일관되게 보기 위한 지연시간이 진짜 비용이다.”
        * “가상머신의 코드 최적화를 제한한다는 점도 과도한 동기화의 또다른 측면이다.”

## 동기화 메서드, 동기화 블록 안에서는 제어를 클라이언트에게 양도하면 안 된다.
* 응답 불가, 안전 실패를 피하기 위해 지켜야 할 원칙
* 예)
    * 동기화된 영역 안에서 재정의할 수 있는 메서드를 호출하면 안 된다.
        * 재정의 가능한 메서드: 상속 가능한 클래스에 있는 `private`, `static`, `final`이 아닌 메서드(private은 서브클래스에서 접근 불가, static 메서드는 클래스 단위로 관리되며 오버라이딩이 아니라 메서드 hiding이 일어남. final 메서드는 변경할 수 있으므로 재정의 불가)
    * 동기화된 영역 안에서는 클라이언트가 넘겨준 함수 객체를 호출하면 안 된다.
* 클래스 관점에서는 외계인 메서드(alien method)가 무슨 일을 할지 알지도 못하고, 통제할 수도 없다. 외계인 메서드가 하는 일에 따라 동기화된 영역은 예외를 일으키거나 교착상태에 빠지거나 데이터를 훼손할 수도 있다.

### 잘못된 코드 - 동기화 블록 안에서 외계인 메서드 호출하는 예
[ObservableSet1.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2Fbroken%2FObservableSet1.java)
* 예외 발생
[Test2.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2FTest2.java)
* 교착상태 발생
[Test3.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2FTest3.java)
### 수정한 코드 - 열린 호출(open call) 방식
[ObservableSet2.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2Ffixed%2FObservableSet2.java)
[Test4.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2FTest4.java)
### 수정한 코드 - 자바 동시성 컬렉션 라이브러리의 copyOnWriteArrayList 사용
[ObservableSet3.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2Ffixed%2FObservableSet3.java)
[Test5.java](..%2F..%2F..%2Fmain%2Fjava%2Fch11%2Fitem79%2FTest5.java)