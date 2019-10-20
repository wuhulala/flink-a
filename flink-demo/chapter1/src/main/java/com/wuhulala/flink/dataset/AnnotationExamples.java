package com.wuhulala.flink.dataset;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.FunctionAnnotation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * @author wuhulala<br>
 * @date 2019/9/19<br>
 * @since v1.0<br>
 */
public class AnnotationExamples {

    public static void main(String[] args) throws Exception {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        System.out.println("------------------- started -------------------------");
        // read the initial data sets
        DataSet<Tuple2<Integer, Integer>> test = env.fromElements(Tuple2.of(1, 2));// [...]

        test.map(new MyMap()).print();
    }

    @FunctionAnnotation.ForwardedFields("f0->f2")
    public static class MyMap implements
            MapFunction<Tuple2<Integer, Integer>, Tuple3<String, Integer, Integer>> {
        @Override
        public Tuple3<String, Integer, Integer> map(Tuple2<Integer, Integer> val) {
            return new Tuple3<String, Integer, Integer>("foo", val.f1 / 2, val.f0);
        }
    }

}
