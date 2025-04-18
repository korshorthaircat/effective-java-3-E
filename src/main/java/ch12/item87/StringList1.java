package ch12.item87;

import java.io.Serializable;

/**
 * 기본 직렬화 형태에 적합하지 않은 클래스
 *
 * 객체의 물리적 표현과 논리적 표현의 차이가 크다.
 *  - 논리적으로 이 클래스는 일련의 문자열을 표현하지만,
 *  - 물리적으로는 문자열들을 이중연결로 연결하고 있다.
 * 기본 직렬화가 연결 구조 전체를 깊이 우선 탐색으로 저장함 ⇒ 스택 오버플로, 순환 참조, 불필요한 복잡성 위험 있음
 */
public class StringList1 implements Serializable {
    private int size = 0;
    private Entry head = null;

    private static class Entry implements Serializable {
        String data;
        Entry next;
        Entry previous;
    }

    private static final long serialVersionUID = 0;
    // 어떤 직렬화 형태를 택하든, 직렬화 가능 클래스에는 모두 직렬 버전 UID를 명시적으로 부여하자.
    // 그래야 직렬버전 UID가 일으키는 잠재적 호환성 문제가 사라진다.

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
