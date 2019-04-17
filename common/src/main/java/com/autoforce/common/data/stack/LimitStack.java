package com.autoforce.common.data.stack;

import java.util.*;

class LimitStack<T> {

    private final int mLimitSize;
    private final Deque<T> mDeque = new LinkedList<>();

    LimitStack(int limitSize) {
        this.mLimitSize = limitSize;
    }

    void add(T t) {

        if (t == null) {
            return;
        }

        // check duplicate
        checkDuplicate(t);
        mDeque.addFirst(t);
        resize();

    }

    void addAll(List<T> data, boolean isReverse) {

        if (data != null) {

            for (T t : data) {
                checkDuplicate(t);

                if (isReverse) {
                    mDeque.addFirst(t);
                } else {
                    mDeque.addLast(t);
                }

                resize();
            }
        }
    }


    @SuppressWarnings("unchecked")
    List<T> getAll() {
        if (mDeque.toArray() == null) {
            return Collections.EMPTY_LIST;
        }

        return (List<T>) Arrays.asList(mDeque.toArray());
    }

    private void resize() {
        if (mDeque.size() > mLimitSize) {
            mDeque.removeLast();
        }
    }

    private void checkDuplicate(T t) {

        for (Iterator<T> iter = mDeque.iterator(); iter.hasNext(); ) {

            T data = iter.next();

            if (t.equals(data)) {
                iter.remove();
                break;
            }

        }

    }

}
