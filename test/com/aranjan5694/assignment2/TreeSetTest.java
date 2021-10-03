package com.aranjan5694.assignment2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TreeSetTest {

    private static final int ORDER = 3;

    // Tree will keep elements in naturally sorted order
    TreeSet<Integer>  bTree = new TreeSet<>(ORDER);

    int[] testData;

    @BeforeEach
    void setUp() {
        initializeBtree();
    }

    @Test
    void getElement() {
        Arrays.sort(testData);
        int expectedValueAtTenthIndex = testData[10];
        int actualValueAtTenthIndex = bTree.getElement(10);
        assertEquals(expectedValueAtTenthIndex, actualValueAtTenthIndex);
    }

    //Add Unique Items to BTree and test size of Tree, Order of Elements and Iterator
    @Test
    void addElementAndTestSizeAndOrder() {

        Arrays.sort(testData);
        int[] expectedOrder = testData;

        int[] actualOrder = bTree.stream()
                .mapToInt(x -> x)
                .toArray();

        assertEquals(testData.length, bTree.size());
        Assertions.assertArrayEquals(expectedOrder, actualOrder);
    }


    //Duplicate entry shouldn't be allowed as it's a set
    @Test
    void testDuplicateItems() {
        Assertions.assertTrue(bTree.add(10000));
        Assertions.assertFalse(bTree.add(10000));
    }

    //For Each should return element in reverse
    @Test
    void testForEach() {
        List<Integer> actualOrder = new ArrayList<>();
        bTree.forEach(actualOrder::add);

        List<Integer> expectedOrder =
                reverse(Arrays.stream(testData)
                .boxed()
                .sorted())
                .collect(Collectors.toList());

        assertEquals(expectedOrder, actualOrder);
    }


    @Test
    void toArray() {
        Object[] bTreeArray = bTree.toArray();
        assertEquals(bTreeArray.length, bTree.size());
    }

    void initializeBtree(){
        int size = 1000;
        testData = new int[size];
        for(int i = 0; i < size; i++) {
            testData[i] = 1000 - i;
            assertTrue(bTree.add(testData[i]));
        }

    }

    public static <T> Stream<T> reverse(Stream<T> stream)
    {
        LinkedList<T> stack = new LinkedList<>();
        stream.forEach(stack::push);
        return stack.stream();
    }
}