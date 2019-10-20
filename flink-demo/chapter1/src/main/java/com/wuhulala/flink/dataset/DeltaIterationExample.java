package com.wuhulala.flink.dataset;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DeltaIteration;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuhulala<br>
 * @date 2019/9/16<br>
 * @since v1.0<br>
 */
public class DeltaIterationExample {

    private static final Logger logger = LoggerFactory.getLogger(DeltaIterationExample.class);

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        System.out.println("------------------- started -------------------------");
        // read the initial data sets
        DataSet<Tuple2<Long, Double>> initialSolutionSet = env.fromElements(Tuple2.of(1L, 2.0));// [...]

        DataSet<Tuple2<Long, Double>> initialDeltaSet = env.fromElements(Tuple2.of(1L, 2.0), Tuple2.of(2L, 3.9), Tuple2.of(2L, 2.0), Tuple2.of(3L, 2.0), Tuple2.of(1L, 2.0));// [...]


        int maxIterations = 100;
        int keyPosition = 0;

        System.out.println(initialDeltaSet);
        System.out.println("------------------- generate iteration ended -------------------------");

        DeltaIteration<Tuple2<Long, Double>, Tuple2<Long, Double>> iteration = initialSolutionSet
                .iterateDelta(initialDeltaSet, maxIterations, keyPosition);

        DataSet<Tuple2<Long, Double>> candidateUpdates = iteration.getWorkset()
                .groupBy(0)
                .reduceGroup(new ComputeCandidateChanges());
        try {

            System.out.println("------------------- candidateUpdates -------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataSet<Tuple2<Long, Double>> deltas = candidateUpdates
                .join(iteration.getSolutionSet())
                .where(0)
                .equalTo(0)
                .with(new CompareChangesToCurrent());

        DataSet<Tuple2<Long, Double>> nextWorkset = deltas
                .filter(new FilterByThreshold());

        String outputPath = "file:///D:\\01code\\personal\\flink\\flink-demo\\chapter1\\src\\main\\resources\\test\\a.csv";
        iteration.closeWith(deltas, nextWorkset).print();
//                .writeAsCsv(outputPath).setParallelism(1);
        System.out.println("------------------- ended -------------------------");

    }


    private static class ComputeCandidateChanges implements GroupReduceFunction<Tuple2<Long, Double>, Tuple2<Long, Double>> {
        @Override
        public void reduce(Iterable<Tuple2<Long, Double>> values, Collector<Tuple2<Long, Double>> out) throws Exception {
            logger.error("ComputeCandidateChanges：：：：start");
            Tuple2<Long, Double> result = Tuple2.of(0L, 0.0);
            values.forEach(tuple -> {
                result.f0 = tuple.f0;
                result.f1 = result.f1 + tuple.f1;
                logger.error("ComputeCandidateChanges：：：：running [" + tuple + "]");
            });

            logger.error("ComputeCandidateChanges：：：：ended" + result);
            out.collect(result);
        }
    }

    private static class CompareChangesToCurrent implements JoinFunction<Tuple2<Long, Double>, Tuple2<Long, Double>, Tuple2<Long, Double>> {

        @Override
        public Tuple2<Long, Double> join(Tuple2<Long, Double> first, Tuple2<Long, Double> second) throws Exception {
            System.out.println("CompareChangesToCurrent：：：：" + first + "--" + second);
            if (second != null) {
                return Tuple2.of(first.f0, second.f1 + first.f1);
            } else {
                return first;
            }
        }
    }

    private static class FilterByThreshold implements org.apache.flink.api.common.functions.FilterFunction<Tuple2<Long, Double>> {
        @Override
        public boolean filter(Tuple2<Long, Double> value) throws Exception {
            System.out.println(value);
            return false;
        }
    }
}
