package ch11.item79.fixed;

import ch11.item79.ForwardingSet;
import ch11.item79.observer.SetObserver2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 열린 호출(open call) 방식으로 문제 해소
 * - 외계인 메서드 호출을 동기화블록 바깥으로 옮기기
 *
 * 기본 규칙! 동기화 영역에서는 가능한 일을 적게 하자.
 */
public class ObservableSet2<E> extends ForwardingSet<E> {
    public ObservableSet2(Set<E> set) { super(set); }

    private final List<SetObserver2<E>> observers = new ArrayList<>();

    public void addObserver(SetObserver2<E> observer) {
        synchronized(observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver2<E> observer) {
        synchronized(observers) {
            return observers.remove(observer);
        }
    }

    private void notifyElementAdded(E element) {
        List<SetObserver2<E>> snapshot = null;
        synchronized(observers) {
            snapshot = new ArrayList<>(observers);
        }
        // 외계인 메서드 호출을 동기화블록 바깥으로 옮기기
        for (SetObserver2<E> observer : snapshot)
            observer.added(this, element);
    }

    @Override public boolean add(E element) {
        boolean added = super.add(element);
        if (added)
            notifyElementAdded(element);
        return added;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c)
            result |= add(element);  // notifyElementAdded를 호출한다.
        return result;
    }
}
