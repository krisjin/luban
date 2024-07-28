package org.luban.common.hash;

/**
 * @author kris
 * @date 2024/7/26
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {
    private final SortedMap<Integer, Node> circle = new TreeMap<>();
    private final int numberOfReplicas;

    public ConsistentHashing(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    private int hash(String key) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(key.getBytes());
        byte[] digest = md.digest();
        int h = ((digest[3] & 0xFF) << 24) | ((digest[2] & 0xFF) << 16) |
                ((digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
        return h;
    }

    public void addNode(Node node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hash(node.getName() + i), node);
        }
    }

    public void removeNode(Node node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hash(node.getName() + i));
        }
    }

    public Node getNode(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hash(key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, Node> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
}
