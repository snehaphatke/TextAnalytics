import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;

public class ExpertIdentifier {
	
	public static class Map extends Mapper<LongWritable, Text, Text, Comment> {
		private ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			Comment  comment = mapper.readValue(value.toString(), Comment.class);
			//System.out.println("SNEHA "+ value);
			context.write(new Text(comment.getUserId()), comment);
			System.out.println("SNEHA" + comment.getUserId() + " " + comment);
					
		}
	}

	public static class Reduce extends Reducer<Text, Comment, Text, Text> {
		private ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		public void reduce(Text userId, Iterable<Comment> values, Context context) throws IOException, InterruptedException {
			int likesCount =0;
			int commentsCount =0;
			int nonZeroCount = 0;
			int noOfExpSolns = 0;
			for (Comment val : values) {
				
				int likes = val.getLikes();
				if(likes > 0) {
					nonZeroCount++;
					likesCount+=likes;
				}
				commentsCount++;
				if(val.isExpertSoln()) {
					noOfExpSolns++;
				}
			}
			//actual avg : 50% weightage
			//comments count weightage : 25% of 1000 count weightage
			//nonero rating : 25% of 1000 count weightage
			double avg = likesCount / nonZeroCount;
			double avgAgg = Math.min(1, avg/10.0);
			double countAgg = Math.min(1, commentsCount/50.0);
			double nonZeroAgg = Math.min(1, nonZeroCount/25.0);
			double expAgg = Math.min(1, noOfExpSolns/20.0);
			double agg = avgAgg*40 + 10 * countAgg + 10 * nonZeroAgg + expAgg*40;

            System.out.println("SNEHA" + userId + " " + avg + " " + commentsCount + " " + nonZeroCount + " " + agg);
			User user = new User();
			user.set_id(userId.toString());
			user.setCommentAgg((int)(agg + 0.5));

			context.write(new Text(""), new Text(mapper.writeValueAsString(user)));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//conf.set("fs.default.name", "hdfs://127.0.0.1:9000");
		//conf.set("hbase.master", "localhost:60000");
		//conf.set("hbase.zookeeper.quorum", "localhost");
		//conf.set("hbase.zookeeper.property.clientPort", "2181");

		String intOutputDir = "tmpOut";
		File file = new File(intOutputDir);
        if(file.exists()) {
            for (String temp : file.list()) {
                File fileDelete = new File(file, temp);
                fileDelete.delete();
            }
            file.delete();
        }
		file = new File(args[2]);
        if (file.exists()) {
            for (String temp : file.list()) {
                File fileDelete = new File(file, temp);
                fileDelete.delete();
            }
            file.delete();
        }
		
		Job job = new Job(conf, "ExpertIdentifier");
		job.setJarByClass(ExpertIdentifier.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Comment.class);

		job.setMapperClass(ExpertIdentifier.Map.class);
		job.setReducerClass(ExpertIdentifier.Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
        FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(intOutputDir));

		job.waitForCompletion(true);
		
		job = new Job(conf, "FinalUserAggregateFinder");
		job.setJarByClass(FinalUserAggregateFinder.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(User.class);

		job.setMapperClass(FinalUserAggregateFinder.Map.class);
		job.setReducerClass(FinalUserAggregateFinder.Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		Path userPath = new Path(args[1]);
		MultipleInputs.addInputPath(job, new Path(intOutputDir + "/part*"), TextInputFormat.class);
		MultipleInputs.addInputPath(job, userPath, TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(intOutputDir + "/part*"));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.waitForCompletion(true);
	}       
}


