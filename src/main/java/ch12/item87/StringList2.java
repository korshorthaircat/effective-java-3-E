package ch12.item87;

import java.io.*;

/**
 * 합리적인 커스텀 직렬화 방식
 *
 * 내부 구조(노드 기반 이중 연결 리스트)를 직렬화에 포함시키지 않고, 필요한 정보만 직렬화하여 성능과 안정성을 높임
 */
public final class StringList2 implements Serializable {
    private transient int size   = 0;
    private transient Entry head = null;
    // transient 필드는 직렬화 대상이 아님 (내부 구현이므로 외부 노출X)
    // 대신에 writeObject, readObject로 필요한 정보만 직렬화/역직렬화
    // 단, 해당 객체의 논리적 상태와 무관한 필드라고 확신할 때만 transient 한정자를 생략해야 한다.

    private static class Entry { // 내부에서만 사용하는 노드 클래스. 굳이 직렬화할 필요 없음
        String data;
        Entry  next;
        Entry  previous;
    }

    private static final long serialVersionUID = 0;
    // 어떤 직렬화 형태를 택하든, 직렬화 가능 클래스에는 모두 직렬 버전 UID를 명시적으로 부여하자.
    // 그래야 직렬버전 UID가 일으키는 잠재적 호환성 문제가 사라진다.

    /**
     * Serialize this {@code StringList} instance.
     *
     * @serialData The size of the list (the number of strings it contains) is emitted ({@code int}), followed by all of
     * its elements (each a {@code String}), in the proper
     * sequence.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        // 모든 원소를 올바른 순서로 기록한다. size와 각 string만 저장
        for (Entry e = head; e != null; e = e.next)
            s.writeObject(e.data);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();

        // 모든 원소를 읽어 이 리스트에 삽입한다. 순서대로 string을 읽어 리스트 재구성
        for (int i = 0; i < numElements; i++)
            add((String) s.readObject());
    }

    /**
     *  Entry를 생성하고 head에 추가
     * @param str
     */
    public final void add(String str) {
        Entry newEntry = new Entry();
        newEntry.data = str;
        newEntry.next = head;
        if (head != null) {
            head.previous = newEntry;
        }
        head = newEntry;
        size++;
    }

    public void print() {
        for (Entry e = head; e != null; e = e.next) {
            System.out.print(e.data + ", ");
        }
    }

}
