package com.github.philocifer.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class StockMapReduce {
	
	public static class DescendingKeyComparator extends WritableComparator {
	    protected DescendingKeyComparator() {
	        super(Text.class, true);
	    }

	    @SuppressWarnings("rawtypes")
	    @Override
	    public int compare(WritableComparable w1, WritableComparable w2) {
	        Text key1 = (Text) w1;
	        Text key2 = (Text) w2;
	        Double key1Dbl = Double.parseDouble(key1.toString());
	        Double key2Dbl = Double.parseDouble(key2.toString());
	        return -1 * key1Dbl.compareTo(key2Dbl);
	    }
	}
	
	public static void main(String[] args) throws Exception {
	    if (args.length != 2) {
	        System.err.println("Usage: StockHighestDailyGainByYear <input path> <output path>");
	        System.exit(-1);
	      }
	    
	    Job job = new Job();
	    job.setJobName("Stock Highest Daily Gain By Year");
	    job.setJarByClass(StockMapReduce.class);
	    job.setSortComparatorClass(DescendingKeyComparator.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	    job.setMapperClass(StockMapper.class);
	    job.setReducerClass(StockReducer.class);
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	    
	}
}
