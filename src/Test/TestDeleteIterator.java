package Test;

import java.util.LinkedList;
import java.util.ListIterator;

public class TestDeleteIterator 
{
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add (3);
        System.out.println(list);
        ListIterator<Integer> it = list.listIterator(0);
        System.out.println(it.next());
        it.add(10);
        System.out.println(it.next());
        System.out.println(list);
        it.previous();
        it.remove();
        System.out.println(list);
        System.out.println(it.next());


    }   
}
