package com.github.philocifer.mapreduce;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StockReducer extends Reducer<Text, Text, Text, Text> {
	  @Override
	  public void reduce(Text key, Iterable<Text> values, Context context)
	      throws IOException, InterruptedException {

		  Set<String> stockSet = new TreeSet<String>();
		  for (Text value : values) {
			  stockSet.add(value.toString());
		  }
		  
		  boolean first = true;
		  String stockText = "";
		  for (String s : stockSet) {
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
