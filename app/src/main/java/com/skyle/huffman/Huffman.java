package com.skyle.huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class Huffman {

    private int currentCreation = 0;
    private int totalOutput = 0;
    private int totalInput = 0;
    private int totalCharacters = 0;
    private Node root;
    private PriorityQueue<Node> minHeap;
    private HashMap<Character, Integer> frequencies;
    private HashMap<Character, String> encode;
    private HashMap<String, Character> decode;

    public Huffman(String seed) {

        if (seed == null || seed.isEmpty())
            throw new IllegalArgumentException();

        frequencies = new HashMap<Character, Integer>();

        for (int i=0; i<seed.length(); ++i) {
            char s = seed.charAt(i);
            if (frequencies.containsKey(s))
                frequencies.put(s, frequencies.get(s) + 1);
            else
                frequencies.put(s, 1);
        }

        if (frequencies.size() == 1)
            throw new IllegalArgumentException();

        minHeap = new PriorityQueue<Node>();

        for (Entry<Character, Integer> e : frequencies.entrySet()) {
            totalCharacters += e.getValue();
            minHeap.add(new Node(e.getValue(), e.getKey()));
        }

        if (totalCharacters == 1)
            throw new IllegalArgumentException();

        while (minHeap.size() > 1) {
            Node n1 = minHeap.poll();
            Node n2 = minHeap.poll();
            Node n3 = new Node(n1.freq + n2.freq);
            n3.left = n1;
            n3.right = n2;
            minHeap.add(n3);
        }

        encode = new HashMap<Character, String>();
        decode = new HashMap<String, Character>();

        root = minHeap.peek();

        findLeaf(root, "");

    }

    void findLeaf(Node n, String path) {
        if (n.isLeaf || (n.left == null && n.right == null)) {
            encode.put(n.character, path);
            decode.put(path, n.character);
        } else {
            if (n.left != null)
                findLeaf(n.left, path+"0");
            if (n.right != null)
                findLeaf(n.right, path+"1");
        }
    }

    public String compress(String input) {
        if (input == null)
            throw new IllegalArgumentException();


        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<input.length(); ++i) {
            if (encode.containsKey(input.charAt(i)))
                sb.append(encode.get(input.charAt(i)));
            else
                throw new IllegalArgumentException();
        }
        totalInput += input.length() * 16;
        totalOutput += sb.length();
        return sb.toString();
    }

    public String decompress(String input) {

        if (input == null || input.length() == 0 || !input.matches("[01]+"))
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder();

        int start = 0;
        int end = 1;

        while (end <= input.length()) {
            if (decode.containsKey(input.substring(start, end))) {
                sb.append(decode.get(input.substring(start, end)));
                start = end;
            }
            end++;
        }

        if (sb.length() == 0)
            throw new IllegalArgumentException();

        return sb.toString();

    }

    public double expectedEncodingLength() {
        double total = 0;
        for (Entry<Character, Integer> e : frequencies.entrySet()) {
            total += encode.get(e.getKey()).length() * ((double) e.getValue() / (double) totalCharacters);
        }
        return total;
    }

    public double compressionRatio() {
        if (totalOutput == 0)
            throw new IllegalStateException();
        return (double) totalOutput / (double) totalInput;
    }

    public String[] encoding() {
        String[] encoding = new String[encode.size()];
        int i=0;
        for (Entry<Character, String> e : encode.entrySet()) {
            encoding[i++] = e.getKey().toString()+"$"+e.getValue().toString();
        }
        return encoding;
    }

    public int getTotalCharactes() {
        return totalCharacters;
    }

    public int getCharacters() {
        return encode.size();
    }

    class Node implements Comparable<Node> {

        public Node left = null;
        public Node right = null;
        public int freq;
        public Character character;
        public int creation;
        public boolean isLeaf;

        public Node(Integer f, Character c) {
            freq = f;
            character = c;
            creation = currentCreation;
            currentCreation++;
            isLeaf = false;
        }

        public Node(Integer f) {
            freq = f;
            creation = currentCreation;
            currentCreation++;
            isLeaf = false;
            character = null;
        }

        @Override
        public int compareTo(Node n) {
            if (n.freq > freq)
                return -1;
            else if (n.freq < freq)
                return 1;
            else if (n.character != null && character != null && n.character > character)
                return -1;
            else if (n.character != null && character != null && n.character < character)
                return 1;
            else if (n.creation > creation)
                return -1;
            return 1;

        }

    }
}