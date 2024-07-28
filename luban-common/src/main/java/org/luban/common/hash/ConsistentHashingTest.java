package org.luban.common.hash;

/**
 * @author kris
 * @date 2024/7/26
 */
public class ConsistentHashingTest {
    public static void main(String[] args) {
        ConsistentHashing consistentHashing = new ConsistentHashing(3);

        Node node1 = new Node("Node1");
        Node node2 = new Node("Node2");
        Node node3 = new Node("Node3");

        consistentHashing.addNode(node1);
        consistentHashing.addNode(node2);
        consistentHashing.addNode(node3);

        System.out.println("Get node for key 'A': " + consistentHashing.getNode("A"));
        System.out.println("Get node for key 'B': " + consistentHashing.getNode("B"));
        System.out.println("Get node for key 'C': " + consistentHashing.getNode("C"));

        consistentHashing.removeNode(node2);

        System.out.println("After removing Node2:");
        System.out.println("Get node for key 'A': " + consistentHashing.getNode("A"));
        System.out.println("Get node for key 'B': " + consistentHashing.getNode("B"));
        System.out.println("Get node for key 'C': " + consistentHashing.getNode("C"));
        System.out.println("Get node for key 'D': " + consistentHashing.getNode("C"));
    }
}
