package ch11.item79.observer;

import ch11.item79.broken.ObservableSet1;

/**
 * 이 인터페이스는 구조적으로 BiConsumer<ObservableSet<E>, E>와 동일하다.
 * 그럼에도 커스텀 함수형 인터페이스를 정의한 이유는
 *  이름이 더 직관적이고,
 *  다중 콜백을 지원하도록 확장할 수 있어서이다.
 * @param <E>
 */
public interface SetObserver1<E> {
    void added(ObservableSet1<E> set, E element); // ObservableSet에 원소가 더해지면 호출된다.
}
