package com.github.philocifer.mapreduce;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class StockMapper extends Mapper<LongWritable, Text, Text, Text>{
	  @Override
	  public void map(LongWritable key, Text value, Context context)
	      throws IOException, InterruptedException {
		  
		  DecimalFormat df = new DecimalFormat("#.##");
		  df.setRoundingMode(RoundingMode.HALF_UP);
		  
		  String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
		  String stockSymbol = fileName.substring(6, fileName.length() - 4);
		  
		  String line = value.toString();
		  String [] s = line.split(",");
		  String year = s[0].substring(0, 4);
		  double quoteHi = Double.parseDouble(s[2]);
		  double quoteLow = Double.parseDouble(s[5]);
		  double dailyGain = quoteHi - quoteLow;
		  context.write(new Text(df.format(dailyGain)), new Text(stockSymbol));
		  
	  }
}
