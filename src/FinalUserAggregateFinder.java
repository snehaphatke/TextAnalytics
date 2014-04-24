

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;

public class FinalUserAggregateFinder {
	
	public static class Map extends Mapper<LongWritable, Text, Text, User> {
		private ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			try {
				User  user = mapper.readValue(value.toString(), User.class);
				System.out.println("SNEHA "+ value);
				context.write(new Text(user.getEmail()), user);
			} catch(EOFException e) {

			}
					
		}
	}
	
	

	public static class Reduce extends Reducer<Text, User, Text, IntWritable> {
		private ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		public void reduce(Text userId, Iterable<User> values, Context context) throws IOException, InterruptedException {
			double pointsAgg = 0;
			int commentAgg = 0;
			for (User val : values) {
				
				int points = val.getActivities();
				if(points > 0) {
					pointsAgg = Math.min(1, points/1000.0);
				}
				if (val.getCommentAgg() > 0) {
					commentAgg = val.getCommentAgg();
				}
			}
			
			double agg = pointsAgg*25 + commentAgg *3.0/4;
			
			context.write(new Text(userId), new IntWritable((int)(agg + 0.5)));
		}
	}


}


