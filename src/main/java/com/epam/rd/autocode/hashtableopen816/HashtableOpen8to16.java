package com.epam.rd.autocode.hashtableopen816;

public interface HashtableOpen8to16 {
    static HashtableOpen8to16 getInstance() {
        return new HashTable();
    }

    void insert(int key, Object value);

    Object search(int key);

    void remove(int key);

    int size();

    int[] keys();
}

