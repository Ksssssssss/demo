package com.hoolai.hdfs.analyse;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 *
 *
 *@description: 框架默认把key相同的聚集在一起
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2019-09-02 20:44
 * 
 */
 
public class WordCountReduce extends Reducer<Text, IntWritable,Text,IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int outValue = 0;
        for (IntWritable inValue : values) {
            outValue += inValue.get();
        }

        context.write(key,new IntWritable(outValue));
    }
}
