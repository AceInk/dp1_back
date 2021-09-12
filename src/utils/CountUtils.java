/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import model.algorithm.Vertex;

/**
 *
 * @author jorge
 */
public class CountUtils {
    public static int countOcurrences(Vertex vertex, List<Vertex> list){
        int count =0;
        count = list.stream().filter(v -> (vertex.equals(v))).map(_item -> 1).reduce(count, Integer::sum);
        return count;
    }
}
