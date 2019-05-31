package com.zhaohg.spark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * WordCountTask class, we will call this class with the demo3 WordCountTest.
 */
public class WordCountTask {
    /**
     * We use a logger to print the output. Sl4j is a common library which works with log4j, the
     * logging system used by Apache Spark.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

    /**
     * This is the entry point function when the task is called with spark-submit.sh from command
     * line. In our example we will call the task from a WordCountTest instead.
     * See {@see http://spark.apache.org/docs/latest/submitting-applications.html}
     */
    public static void main(String[] args) {
        checkArgument(args.length > 1, "Please provide the path of input file as first parameter.");
        new WordCountTask().run(args[1]);
    }

    /**
     * The task body
     */
    public void run(String inputFilePath) {
        /*
         * This is the address of the Spark cluster. We will call the task from WordCountTest and we
         * use a local standalone cluster. [*] means use all the cores available.
         * See {@see http://spark.apache.org/docs/latest/submitting-applications.html#master-urls}.
         */
        String master = "local[*]";

        /*
         * Initialises a Spark context.
         */
        SparkConf conf = new SparkConf().setAppName(WordCountTask.class.getName()).setMaster(master);
        JavaSparkContext context = new JavaSparkContext(conf);

        /*
         * Performs a work count sequence of tasks and prints the output with a logger.
         */
        context.textFile(inputFilePath).flatMap(text -> Arrays.asList(text.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b)
                .foreach(result -> LOGGER.info(String.format("Word [%s] count [%d].", result._1(), result._2)));


        JavaRDD<String> lines = context.textFile("G:\\runtime\\spark-1.6.0\\README.md");


        JavaRDD<String> words = lines.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> wordsCount = pairs.reduceByKey((Integer a, Integer b) -> a + b);

        //排序
        wordsCount = wordsCount.mapToPair(tuple -> new Tuple2(tuple._2, tuple._1));
        wordsCount = wordsCount.sortByKey(false);
        wordsCount = wordsCount.mapToPair(tuple -> new Tuple2(tuple._2, tuple._1));

        wordsCount.foreach(tuple -> {
            System.out.println(tuple._1 + ":" + tuple._2);
        });

    }
}
