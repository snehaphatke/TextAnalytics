

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

public class ExpertIdentifier {
	
	public static class Map extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
		private ObjectMapper mapper = new ObjectMapper();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			Comment  comment = mapper.readValue(value.toString(), Comment.class);
			//System.out.println("SNEHA "+ value);
			context.write(new LongWritable(comment.getUserid()), new IntWritable(comment.getRating()));
		}
	}
	
	

	public static class Reduce extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
		
		public void reduce(LongWritable user, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int ratingSum =0;
			int commentsCount =0;
			int nonZeroCount = 0;
			for (IntWritable val : values) {
				
				int rating = val.get();
				if(rating > 0) {
					nonZeroCount++;
					ratingSum+=rating;
				}
				commentsCount++;
			}
			//actual avg : 50% weightage
			//comments count weightage : 25% of 1000 count weightage
			//nonero rating : 25% of 1000 count weightage
			int avg = ratingSum / nonZeroCount;
			double countAgg = 1;
			final int COUNTS = 50;
			if(commentsCount < COUNTS) {
				countAgg = commentsCount*1.0/COUNTS;
			}
			double nonZeroAgg = 1;
			if(nonZeroCount < COUNTS) {
				nonZeroAgg = nonZeroCount*1.0/COUNTS;
			}
			double agg = avg*0.5 + 2.5 * countAgg + 2.5 * nonZeroAgg;
			System.out.println("HARISH" + user + " " + avg + " " + commentsCount + " " + nonZeroCount + " " + agg);
			
			context.write(new LongWritable(user.get()), new IntWritable((int)(agg + 0.5)));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//	        conf.set("fs.default.name", "hdfs://127.0.0.1:9000");
		//			conf.set("hbase.master", "localhost:60000");
		//			conf.set("hbase.zookeeper.quorum", "localhost");
		//			conf.set("hbase.zookeeper.property.clientPort", "2181");

		File file = new File("output");
     	   for (String temp : file.list()) {
     	      File fileDelete = new File(file, temp);
     	      fileDelete.delete();
     	   }
		file.delete();
		
		Job job = new Job(conf, "ExpertIdentifier");
		job.setJarByClass(ExpertIdentifier.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		//job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}       
}


