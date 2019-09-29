package com.hoolai.hdfs.analyse;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @description:MapTask解析时调用map方法
 * @author: Ksssss(chenlin@hoolai.com)
 * @time: 2019-09-02 20:17
 */

public class WordCountMap extends Mapper<LongWritable, Text,Text, IntWritable> {


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String inValue = value.toString();
        String[] wordKeys = inValue.split("\n");

        for (String wordKey : wordKeys) {
            context.write(new Text(wordKey),new IntWritable(1));
        }
    }
}
