package mapreduce;

import java.io.IOException;
//import java.math.RoundingMode;
//import java.text.DecimalFormat;



import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MeanTempByMonthReducer extends Reducer<Text, Text, Text, Text>  {

	@Override
	  public void reduce(Text key, Iterable<Text> values,
	      Context context)
	      throws IOException, InterruptedException {

		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		
	    double total = 0;
	    int count = 0;
	    for (Text value : values) {
	      String v = value.toString().trim();
	      total += Double.parseDouble(v);
	      count++;
	    }
	    double meanTempC = (total/count)/10;
	    double meanTempF = (meanTempC * (9/5) + 32);
	    String s = df.format(meanTempF);
	    context.write(key, new Text(s));
	  }

}
