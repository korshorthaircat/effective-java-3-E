package ch11.item79.broken;

import ch11.item79.ForwardingSet;
import ch11.item79.observer.SetObserver1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 잘못된 코드 - 동기화 블록 안에서 외계인 메서드를 호출하는 예
 *
 * 집합(Set)을 감싼 래퍼 클래스로서, 이 클래스의 클라이언트는 집합에 원소가 추가되면 알림을 받을 수 있다. [관찰자 패턴]
 */
public class ObservableSet1<E> extends ForwardingSet<E> {
    public ObservableSet1(Set<E> set) { super(set); }

    private final List<SetObserver1<E>> observers = new ArrayList<>();

    public void addObserver(SetObserver1<E> observer) {
        synchronized(observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver1<E> observer) {
        synchronized(observers) {
            return observers.remove(observer);
        }
    }

    /**
     * 집합에 원소가 추가되면 알려준다.
     * @param element
     */
    private void notifyElementAdded(E element) {
        synchronized(observers) {
            for (SetObserver1<E> observer : observers)
                observer.added(this, element);
        }
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
