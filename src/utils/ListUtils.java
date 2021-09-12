/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jorge
 */
public class ListUtils<T> {
    // A Function to generate a random permutation of arr[]
    public static <T> List<T> shuffle(List<T> arr) {
        // Creating a object for Random class
        List<T> copy = new ArrayList<>();
        copy.addAll(arr);
        List<T> shuffled = new ArrayList<>();
        Random r = new Random();
        int n= arr.size();
        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = 0; i < n; i++) {
            // Pick a random index from 0 to i
            int j = r.nextInt(copy.size());

            // Swap arr[i] with the element at random index
            T temp = copy.get(j);
            shuffled.add(temp);
            copy.remove(j);
        }
        return shuffled;
    }
}