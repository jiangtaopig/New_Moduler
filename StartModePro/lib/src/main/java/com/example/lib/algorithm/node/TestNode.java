package com.example.lib.algorithm.node;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TestNode {
    public static void main(String[] args) {

        int [] nums = {1, 3, 8};
        ListNode l1 = createListNode(nums);
        ListNode l2 = createListNode(new int[]{2, 5});
        ListNode listNode = merge2List(l1, l2);

        ListNode l3 = createListNode(new int[] {1, 2, 3, 4, 5});
        ListNode l4 = deleteLastKNode(l3, 3);
        getIntersectionNode(createListNode(new int[]{1, 2}), createListNode(new int[]{3, 4}));
        ListNode l5 = reverseListNode(createListNode(new int[]{1, 2, 3, 4}));
        ListNode l6 =  reverseBySpecifyNum(createListNode(new int[] {1, 2, 3, 4, 5, 6}), 3);


        ListNode listNode1 = createListNode(new int[]{1, 2, 3});
        ListNode listNode2 = createListNode(new int[]{2, 3, 3, 4});
        ListNode listNode3 = createListNode(new int[]{4, 5});
//        ListNode res = mergeKListNode1(new ListNode[]{listNode1, listNode2, listNode3});
        mergeKListNode2(new ListNode[]{listNode3, listNode2, listNode1});

        ListNode part = reversePartListNode(createListNode(new int[] {1, 2, 3, 4, 5, 6}), 1, 4);
        int a = 1;

        List<String> list = new ArrayList<>();
        list.add("1");
        list.remove("1");

        A aw = new A();
    }

    /**
     * 翻转单链表
     * @param listNode
     * @return
     */
    private static ListNode reverseListNode(ListNode listNode) {
        if (listNode == null) return null;
        ListNode L = new ListNode(-1);
        ListNode p = listNode;
        while (p != null) {
            p = p.next;
            listNode.next = L.next;
            L.next = listNode;
            listNode = p;
        }
        return L.next;
    }

    /**
     * 翻转从 m 到 n 之前的链表，包括 m  和 n (1 << m << n << 链表的长度)
     */
    private static ListNode reversePartListNode(ListNode listNode, int m, int n) {
        if (listNode == null || listNode.next == null) return listNode;

        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = listNode;
        ListNode r = dummyNode;

        int i = 1;
        while (i < m) {
            i++;
            r = r.next;
        }

        ListNode pre = r.next;
        ListNode cur = r.next.next;

        // 因为 m 对应的结点不动，m 到 n 之间的结点包含 n 要插入到 r 和 m 之间
        for (int j = 0; j < n - m; j++) {
            pre.next = cur.next;
            cur.next = r.next;
            r.next = cur;
            cur = pre.next;
        }


        return dummyNode.next;
}

    /**
     * 每k个结点翻转一次翻转链表
     * 如： 输入 1->2->3->4->5->6 , k = 3 输出： 3->2->1->6->5->4
     *     输入 1->2->3->4->5->6->7->8 , k = 3 输出： 3->2->1->6->5->4->7->8
     * @param listNode
     * @param k
     * @return
     */
    private static ListNode reverseBySpecifyNum(ListNode listNode, int k) {
        ListNode p = listNode, q = listNode, t ;
        ListNode dummy = new ListNode(-1);
        ListNode r = dummy;
        ListNode pre = listNode;
        int i = 0;
        while (q != null && i < k) {
            q = q.next;
            i++;
            if (i == k) {
                while (p != q) {
                    t = p.next;
                    p.next = r.next;
                    r.next = p;
                    p = t;
                }
                i = 0;
                r = pre;
                pre = q;
                p = q;
            }
        }
        r.next = p;
        return dummy.next;
    }

    /**
     * 获取2个链表的交点，没有就返回 null （当 p1 和 p2 都指向 null 的时候 说明 p1 = p2 ，那么返回 null）
     */
    private static ListNode getIntersectionNode(ListNode l1, ListNode l2) {
        ListNode p1 = l1;
        ListNode p2 = l2;
        while (p1 != p2) {
            p1 = p1 == null ? l2 : p1.next;
            p2 = p2 == null ? l1 : p2.next;
        }
        // 因为 while 循环终止的条件是 p1 = p2 ，所以返回 p1 或者 p2 都行
        return p1;
    }

    /**
     * 合并 k 个升序链表
     *
     */
    private static ListNode mergeKListNode1(ListNode[] listNodes) {
        ListNode res = listNodes[0];
        for (int i = 1; i < listNodes.length; i++) {
            res = merge2List(res, listNodes[i]);
        }
       return res;
    }

    /**
     * 合并 k 个有序链表的方法2：
     * 利用 小顶堆 来找到每个链表的最小的结点
     *
     */
    private static ListNode mergeKListNode2(ListNode[] listNodes) {

        // 小顶堆就是根节点的值最小，左右子节点的值都大于根节点的值。 PriorityQueue 默认就是小顶堆
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(listNodes.length, new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                // 大顶堆是 o2.value - o1.value
                return o1.value - o2.value;
            }
        });

        // 所有链表的放入 priorityQueue
        for (ListNode listNode : listNodes) {
            priorityQueue.add(listNode);
        }

        ListNode dummyNode = new ListNode(-1);
        ListNode r = dummyNode;
        while (!priorityQueue.isEmpty()) {
            // 把结点最小的那个链表从 priorityQueue 中取出来，然后取第一个结点就是最小的（因为是升序的链表）
            ListNode node = priorityQueue.poll();
            r.next = node;
            if (node.next != null) {
                // 之前把整个链表取出来了，所以现在要 add 进 priorityQueue
                priorityQueue.add(node.next);
            }
            r = r.next;
        }
        return dummyNode.next;
    }

    /**
     * 合并2个有序链表
     */
    private static ListNode merge2List(ListNode l1, ListNode l2) {
        ListNode L = new ListNode(-1);
        ListNode p = L;

        while (l1 != null && l2 != null) {
            if (l1.value > l2.value) {
                p.next = l2;
//                p = l2;
                l2 = l2.next;
            } else {
                p.next = l1;
//                p = l1;
                l1 = l1.next;
            }
            // 每次比较完之后 p 要指向下一个结点
            p = p.next;
        }
        // 因为跳出循环的条件是 l1 或者 l2 等于 null，
        if (l1 != null) {
            p.next = l1;
        }
        if (l2 != null) {
            p.next = l2;
        }
        return L.next;
    }

    /**
     * 找到环形链表的入口
     * @param listNode
     * @return
     */
    private static ListNode findCircleNodeEnter(ListNode listNode) {
        ListNode fast = listNode;
        ListNode slow = listNode;
        // 1. 根据快慢指针判断是否有环
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) // 相等则表示有环
                break;
        }

        // 快慢指针中任意一个指向链表的起始结点，这里让慢指针指向起始结点
        slow = listNode;
        // 快慢指针每次走一步，直到相遇
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    /**
     * 找到链表的倒数第K个结点
     * @param listNode
     * @return
     */
    private static ListNode findLastKNode(ListNode listNode, int K) {

        ListNode fast = listNode;
        ListNode p = listNode;

        // fast 先走 K 步
        for (int i = 0; i < K; i++) {
            fast = fast.next;
        }

        //
        while (fast != null) {
            fast = fast.next;
            p = p.next;
        }
        return p;
    }

    /**
     * 删除倒数第K个结点
     * @param listNode
     * @param K
     * @return
     */
    private static ListNode deleteLastKNode(ListNode listNode, int K) {
        ListNode fast = listNode;
        // 先让 fast 走 K 步
        for (int i = 0; i < K; i++) {
            fast = fast.next;
        }
        ListNode p = listNode;

        if (fast == null) { // 表示倒数第K个结点就是 listnode 的第一个结点
            return listNode.next;
        }

        // 这样 p 对应的是待删除结点的前一个结点
        while (fast.next != null) {
            fast = fast.next;
            p = p.next;
        }
        ListNode delete = p.next;
        p.next = delete.next;
        delete.next = null;

        return listNode;
    }

    private static ListNode createListNode(int[] nums) {
        ListNode L = new ListNode(-1);
        ListNode p = L;
        for (int a : nums) {
            ListNode node = new ListNode(a);
            node.next = p.next;
            p.next = node;
            p = p.next;
        }
        return L.next;
    }
 }

 class A {

 }
