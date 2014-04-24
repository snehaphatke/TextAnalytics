import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;




public class SampleInputCreator {


	public static void main(String[] args) throws Exception {
		//10 posts each of 10 different users each commenting to 10 other posts => total 100 comments
		//1-5 user will randomly get ratings from 1-8 for half of their post
		//6-8 users will randomly get ratings from 4-10 for half of their post
		//9-10 users will randomly get ratings from 8-10 for 3/4th of their post

		
//		List<Post> posts = new ArrayList<Post>();
//		List<User> users = new ArrayList<User>();
//		for(int i =1 ;i <=10;i++) {
//			User user = new User();
//			user.setId(i);
//			users.add(user);
//			for (int k=1;k<=10;k++) {
//				Post post = new Post();
//				post.setPostid(k);
//				post.setUserid(user.getId());
//				post.setPost("TEST");
//				posts.add(post);
//			}
//		}
//		Random random = new Random(System.currentTimeMillis());
//		int commentid = 0;
//		//List<Comment> comments = new ArrayList<Comment>();
//		ObjectMapper mapper = new ObjectMapper();
//		File file = new File("KBEMinput/test.json");
//		for(Post post:posts) {
//			for(User user:users) {
//				boolean toCreate = random.nextBoolean();
//				int rating  =0;
//				if(toCreate) {
//					if(user.getId() < 6) {
//						rating = Math.abs(random.nextInt()) %8;
//					} else if(user.getId() < 9) {
//						rating = Math.abs(random.nextInt()) %6 + 4;
//					} else {
//						rating = Math.abs(random.nextInt()) %2 + 8;
//					}
//				}
//				Comment comment = new Comment();
//				comment.setComment("test");
//				comment.setCommentid(++commentid);
//				comment.setPostid(post.getPostid());
//				comment.setRating(rating);
//				comment.setUserid(user.getId());
//				//comments.add(comment);
//				FileUtils.writeStringToFile(file, mapper.writeValueAsString(comment) + "\n", true);
//			}
//		}




	}

}   


