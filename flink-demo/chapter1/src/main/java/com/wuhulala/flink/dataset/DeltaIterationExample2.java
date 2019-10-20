package com.wuhulala.flink.dataset;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatJoinFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.operators.DeltaIteration;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在一个图中传递最小值。
 * 例子中，每个顶点都有一个ID和一种颜色。每个顶点都会将其ID传递给相邻的顶点，目标是将最小值传遍整个图。
 * 如果一个顶点接收到的ID比自己当前的小，就将颜色替换成接收到ID的顶点颜色。在社区分析和联通性计算中常用。
 * https://blog.csdn.net/rlnLo2pNEfx9c/article/details/86522101
 * <p>
 * 第一次迭代
 * <p>
 * 对于第一个子图，ID 1 会和ID2进行比较，并且ID 2变成ID 1的颜色。ID 3 和ID 4会接收到ID 2 并与其进行比较，使得ID 3 ID4变成ID 2的颜色。此时就可以next worker里就会减去未改变的顶点1.
 * <p>
 * 对于第二个子图，第一次遍历ID 6 ID7就会变成ID 5的颜色，结束遍历。Next work里会减去未改变的顶点5.
 * <p>
 * 第二次迭代
 * <p>
 * 此时next work里的顶点由于已经减去顶点 1 和顶点5，所以只剩顶点(2,3,4,6,7)。在第二次迭代之后，第二个子图就不会在变化了，next workset里不会有其顶点，然而第一个子图，由于顶点3和4又一次变化，所以还需要第三次迭代。此时，第一个子图就是热数据，第二个子图就是冷数据。计算就变成了针对第一个子图的局部计算，针对第一个子图的顶点3和顶点4进行计算。
 * <p>
 * 第三次迭代
 * <p>
 * 由于顶点3和4都不会变化，next workset就为空了，然后就会终止迭代。
 * ————————————————
 * 版权声明：本文为CSDN博主「Spark高级玩法」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/rlnLo2pNEfx9c/article/details/86522101
 *
 * @author wuhulala<br>
 * @date 2019/9/16<br>
 * @since v1.0<br>
 */
public class DeltaIterationExample2 {

    private static final Logger logger = LoggerFactory.getLogger(DeltaIterationExample2.class);

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        System.out.println("------------------- started -------------------------");
        // read the initial data sets
        DataSet<Tuple2<Integer, Integer>> initialSolutionSet = env.fromElements(Tuple2.of(1, 1), Tuple2.of(2, 2), Tuple2.of(3, 3), Tuple2.of(4, 4), Tuple2.of(5, 5), Tuple2.of(6, 6), Tuple2.of(7, 7));// [...]

        DataSet<Tuple2<Integer, Integer>> edges = env.fromElements(Tuple2.of(1, 2), Tuple2.of(2, 3), Tuple2.of(3, 4), Tuple2.of(2, 4), Tuple2.of(5, 6), Tuple2.of(6, 7), Tuple2.of(5, 7))
                .flatMap(new FlatMapFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
                    @Override
                    public void flatMap(Tuple2<Integer, Integer> value, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                        out.collect(Tuple2.of(value.f0, value.f1));
                        out.collect(Tuple2.of(value.f1, value.f0));
                    }
                });
        DataSet<Tuple2<Integer, Integer>> tmp = env.fromElements(Tuple2.of(1, 2));

        // System.out.println(edges.collect());


        int maxIterations = 3;
        int keyPosition = 0;


        DeltaIteration<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> iteration = initialSolutionSet
                .iterateDelta(initialSolutionSet, maxIterations, keyPosition);
        // 构造本点之间的连线 默认 1和1 也要有连线

        DataSet<Tuple2<Integer, Integer>> deltas = iteration.getWorkset()
                .join(edges)
                .where(0)
                .equalTo(0)
                .with(new FlatJoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
                    @Override
                    public void join(Tuple2<Integer, Integer> first, Tuple2<Integer, Integer> second, Collector<Tuple2<Integer, Integer>> out) throws Exception {

                        out.collect(Tuple2.of(second.f1, first.f1));
                        System.out.println("(" + first.f0 + "," + second.f1 + "):::" + first.f1);
                    }
                })
                // 取所有的邻接点最小值
                .groupBy(0).aggregate(Aggregations.MIN, 1)
                // 将邻接点的最小值和原本的值 最后比较一波
                .join(iteration.getSolutionSet())
                .where(0)
                .equalTo(0)
                .with(new FlatJoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
                    @Override
                    public void join(Tuple2<Integer, Integer> first, Tuple2<Integer, Integer> second, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                        if (first.f1 < second.f1) {
                            out.collect(first);
                        }
                    }
                });

        System.out.println("--------------------------------");
        iteration.closeWith(deltas, deltas).print();
    }


    private static class CompareChangesToCurrent implements JoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> {

        @Override
        public Tuple2<Integer, Integer> join(Tuple2<Integer, Integer> first, Tuple2<Integer, Integer> second) throws Exception {
            if (first.f1.equals(second.f1)) {
                return first;
            }
            return null;
        }
    }

    private static class FilterByThreshold implements FilterFunction<Tuple2<Integer, Integer>> {
        @Override
        public boolean filter(Tuple2<Integer, Integer> value) throws Exception {
            return !value.f0.equals(value.f1);
        }
    }
}
