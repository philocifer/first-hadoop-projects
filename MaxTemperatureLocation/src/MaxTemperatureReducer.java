import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTemperatureReducer
  extends Reducer<Text, Text, Text, Text> {
  
  @Override
  public void reduce(Text key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {
    
    double maxValue = Double.MIN_VALUE;
    String maxLoc = "";
    for (Text value : values) {
    	String [] s = value.toString().split(",");
    	Double airTemp = Double.parseDouble(s[0]);
    	String loc = s[1];
    	if (airTemp > maxValue) {
    		maxValue = airTemp;
    		maxLoc = loc;
    	}
    }
    context.write(key, new Text(Double.toString(maxValue) + " - " + maxLoc));
  }
}