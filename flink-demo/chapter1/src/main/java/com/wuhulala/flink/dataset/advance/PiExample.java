package com.wuhulala.flink.dataset.advance;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;

/**
 * 蒙特卡罗法
 *
 * 这种方法是一种利用计算机随机数的功能基于“随机数”的算法，通过计算落在单位圆内的点与落在正方形内的
 * 点的比值求PI。
 *
 * 假定一点能够均匀地扔到一个正方形中，计算落入其中的点个数。通过计数其中落入内切圆的点的个数；
 * 如果一共投入N个点，其中有M个落入圆中，则只要点均匀，假定圆周的半径为R，则：
 * M/N=πR^2/(2R)^2,即π=4∗M/N
 *
 * @author wuhulala<br>
 * @date 2019/9/16<br>
 * @since v1.0<br>
 */
public class PiExample {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(12);
        // Create initial IterativeDataSet
        IterativeDataSet<Integer> initial = env.fromElements(0).iterate(10000);

        DataSet<Integer> iteration = initial.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer i) throws Exception {
                double x = Math.random();
                double y = Math.random();

                return i + ((x * x + y * y < 1) ? 1 : 0);
            }
        });

        // Iteratively transform the IterativeDataSet
        DataSet<Integer> count = initial.closeWith(iteration);

        count.map(new MapFunction<Integer, Double>() {
            @Override
            public Double map(Integer count) throws Exception {
                return count / (double) 10000 * 4;
            }
        }).print();

        //env.execute("Iterative Pi Example");
    }


}
