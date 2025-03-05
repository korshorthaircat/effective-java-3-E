package ch11.item79.fixed;

import ch11.item79.ForwardingSet;
import ch11.item79.observer.SetObserver3;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 자바 동시성 컬렉션 라이브러리의 copyOnWriteArrayList 사용하기
 *
 * addObserver, removeObserver, notifyElementAdded 에서 명시적으로 동기화하는 코드가 사라졌다.
 */
public class ObservableSet3<E> extends ForwardingSet<E> {
    public ObservableSet3(Set<E> set) { super(set); }
    private final List<SetObserver3<E>> observers = new CopyOnWriteArrayList<>();
    // copyOnWriteArrayList: 자바 동시성 컬렉션 라이브러리에서 지원하는 클래스. 내부 변경 작업을 항상 깨끗한 복사본 만들어 수행하도록 구현됨
    // 내부 배열을 수정하지 않으므로, 순회시 락이 필요없어 매우 빠름
    // 수정할 일 드물고 순회만 빈번히 일어나는 관찰자 리스트 용도로 최적

    public void addObserver(SetObserver3<E> observer) {
        observers.add(observer);
    }

    public boolean removeObserver(SetObserver3<E> observer) {
        return observers.remove(observer);
    }

    private void notifyElementAdded(E element) {
        for (SetObserver3<E> observer : observers)
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
            result |= add(element);  // Calls notifyElementAdded
        return result;
    }
}
