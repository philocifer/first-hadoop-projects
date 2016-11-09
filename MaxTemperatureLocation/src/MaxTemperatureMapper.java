import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapper
  extends Mapper<LongWritable, Text, Text, Text> {
  
	enum Temperature {
		  FAILED_QUALITY_CHECK
	}

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

	  int offset = 8;
	  String tmax = "TMAX";

	  DecimalFormat df = new DecimalFormat("#.#");
	  df.setRoundingMode(RoundingMode.CEILING);
	  
	  String line = value.toString();
	  String rectype = line.substring(17, 21);
	  if (rectype.equals(tmax)) {
		  String year = line.substring(11, 15);
		  String location = line.substring(0, 12);
		  for (int i=0; i<=30; i++) {
			  String qFlag = line.substring(27 + offset*i, 28 + offset*i);
			  if (qFlag.equals(" ")) {
				  String t = line.substring(21 + offset*i, 26 + offset*i);
				  int airTempC = Integer.parseInt(t.trim());
				  double airTempF = (airTempC / 10) * (1.8) + 32;
				  context.write(new Text(year), new Text(df.format(airTempF) + "," + location));
			  } else {
				  System.err.println("Temperature failed quality check for input: " + value);
				  context.setStatus("Detected corrupt record: see logs.");
				  context.getCounter(Temperature.FAILED_QUALITY_CHECK).increment(1);
			  }
		  }
	  }
	  	  
  }
}