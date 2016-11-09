package com.github.philocifer.mapreduce;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StockReducer extends Reducer<Text, Text, Text, Text> {
	  @Override
	  public void reduce(Text key, Iterable<Text> values, Context context)
	      throws IOException, InterruptedException {

		  Set<String> stockList = new TreeSet<String>();
		  for (Text value : values) {
			  stockList.add(value.toString());
		  }
		  
		  boolean first = true;
		  String stockText = "";
		  for (String s : stockList) {
			  if (first) {
				  stockText = s;
				  first = false;
			  } else {
				  stockText += ", " + s;
			  }
		  }
		  context.write(key, new Text(stockText));
	  }
}
