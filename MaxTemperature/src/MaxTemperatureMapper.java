import java.io.IOException;
import java.text.DecimalFormatSymbols;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapper
  extends Mapper<LongWritable, Text, Text, IntWritable> {

  private static final int MISSING = 9999;
  
  public static boolean isStringNumeric( String str )
  {
      DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
      char localeMinusSign = currentLocaleSymbols.getMinusSign();

      if ( !Character.isDigit( str.charAt( 0 ) )) return false;

      boolean isDecimalSeparatorFound = false;
      char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

      for ( char c : str.substring( 1 ).toCharArray() )
      {
          if ( !Character.isDigit( c ) )
          {
              if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
              {
                  isDecimalSeparatorFound = true;
                  continue;
              }
              return false;
          }
      }
      return true;
  }
  
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

	  int offset = 8;
	  String tmax = "TMAX";
    
	  String line = value.toString();
	  String rectype = line.substring(17, 21);
	  if (rectype.equals(tmax)) {
		  String year = line.substring(11, 15);
		  for (int i=0; i<=30; i++) {
			  String t = line.substring(21 + offset*i, 26 + offset*i);
			  int airTemp = Integer.parseInt(t.trim());
			  context.write(new Text(year), new IntWritable(airTemp));
		  }
	  }
	  	  
  }
}