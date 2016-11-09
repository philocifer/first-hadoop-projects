package mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MeanTempByMonthMapper extends Mapper<LongWritable, Text, Text, Text>  {

	private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	@Override
	public void map(LongWritable key, Text value, Context context)
	    throws IOException, InterruptedException {
		
		String[] s = value.toString().split("\t");
		String yearmonth = s[0];
//		String station = s[1];

		String airTemp = s[1].trim();
//		double airTempC = (double) Integer.parseInt(airTemp);

		String monthNum = yearmonth.substring(4, 6);
		int monthInt = Integer.parseInt(monthNum.trim());
		String month = months[monthInt - 1];
		int year = Integer.parseInt(yearmonth.substring(0, 5));

		if (year >= 1950) {
			context.write(new Text(monthNum + "-" + month), new Text(airTemp));
		}
	}

}
