package com.epam.rd.autocode.hashtableopen816;

import java.util.Objects;

public class HashTable<K, V> implements HashtableOpen8to16 {
    private Node<K, V>[] hashTable;
    private int size;
    private int temp = 8;

    public HashTable() {
        this.hashTable = new Node[8];
    }

    private int hash(Node<K, V> node) {
        return Math.abs(node.hashCode() % hashTable.length);
    }

    @Override
    public void insert(int key, Object value) {
        for (Node<K, V> kvNode : hashTable) {
            if (kvNode != null && kvNode.getKey().equals(key)) {
                return;
            }
        }
        if (size == 16) {
            throw new IllegalStateException();
        }
        changeSize();
        Node<K, V> newNode = new Node(key, value);
        int hash = hash(newNode);
        if (hashTable[hash] == null) {
            simpleAdd(hash, newNode);
            return;
        }
        if (!keyExistButValueNew(newNode)) {
            collision(newNode);
        }
    }

    private void changeSize() {
        if (size == temp && !checkArrayHaveEmpty() && temp != 16) {
            temp *= 2;
            copyArray();
            if (temp >= 17) {
                throw new IllegalStateException();
            }
        }
    }

    private boolean checkArrayHaveEmpty() {
        if (size == temp) {
            return false;
        }
        for (Node<K, V> kvNode : hashTable) {
            if (Objects.nonNull(kvNode)) {
            } else return true;
        }
        return false;
    }

    private void collision(Node<K, V> newNode) {
        if (hashTable[hash(newNode)] != null && !hashTable[hash(newNode)].getKey().equals(newNode.getKey())) {
            for (Node<K, V> kvNode : hashTable) {
                if (kvNode != null && kvNode.getKey().equals(newNode.getKey())) {
                    size++;
                    return;
                }
            }
            for (int j = hash(newNode); checkArrayHaveEmpty(); j++) {
                if (j == temp) {
                    j = 0;
                }
                if (hashTable[j] == null) {
                    hashTable[j] = newNode;
                    size++;
                    return;
                }
            }
        }
    }

    private boolean keyExistButValueNew(Node<K, V> newNode) {
        if (hashTable[hash(newNode)] != null && !hashTable[hash(newNode)].getValue().equals(newNode.getValue()) && hashTable[hash(newNode)].getKey().equals(newNode.getKey())) {
            hashTable[hash(newNode)].setValue(newNode.getValue());
            return true;
        }
        return false;
    }

    private void simpleAdd(int hash, Node<K, V> newNode) {
        hashTable[hash] = newNode;
        size++;
    }

    private void copyArray() {
        Node<K, V>[] oldHashtable = hashTable;
        hashTable = new Node[temp];
        size = 0;
        for (Node<K, V> kvNode : oldHashtable) {
            if (kvNode != null) {
                insert((Integer) kvNode.getKey(), kvNode.getValue());
            }
        }
    }

    @Override
    public Object search(int key) {
        int index = hash(new Node(key, null));
        if (index < hashTable.length) {
            for (Node<K, V> kvNode : hashTable) {
                if (kvNode != null && kvNode.getKey().equals(key)) {
                    return kvNode.getKey();
                }
            }
        }
        return null;
    }

    @Override
    public void remove(int key) {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && hashTable[i].getKey().equals(key)) {
                size--;
                hashTable[i] = null;
            }
        }
        if (size != 0 && temp % 4 == 0 && (temp / size) % 4 == 0) {
            temp = temp / 2;
            Node<K, V>[] oldHashTable = hashTable;
            hashTable = new Node[temp];
            size = 0;
            for (Node<K, V> kvNode : oldHashTable) {
                if (kvNode != null) {
                    insert((Integer) kvNode.getKey(), kvNode.getValue());
                }
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int[] keys() {
        int[] result = new int[temp];
        for (int i = 0; i < temp; i++) {
            if (Objects.nonNull(hashTable[i])) {
                result[i] = (int) hashTable[i].getKey();
            }
        }
        return result;
    }

    public class Node<K, V> {
        private final K key;
        Node<K, V> next;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value) || Objects.equals(this.hashCode(), o.hashCode());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode((Integer) key);
        }
    }
}