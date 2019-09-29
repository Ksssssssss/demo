package com.hoolai.hdfs.analyse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 *
 *
 *@description: 分配job，处理集群的调度
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2019-09-02 21:06
 * 
 */
 
public class WordCountYarn {

    public static void main(String[] args) throws IOException ,InterruptedException,ClassNotFoundException {
        Configuration configuration = new Configuration();
        configuration.set("yarn.resourcemanager.hostname","rm01");
        Job job = Job.getInstance(configuration);

        job.setJarByClass(WordCountYarn.class);

        job.setMapperClass(WordCountMap.class);

        job.setReducerClass(WordCountReduce.class);

        job.setMapOutputKeyClass(Text.class);

        job.setMapOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));

        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}
