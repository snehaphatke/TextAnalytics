import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Comment implements org.apache.hadoop.io.WritableComparable{
	
//	@JsonProperty("post_id")
//	private String postId;
//	@JsonProperty("_id")
//	private String id;

	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("expert_solution")
	private boolean isExpertSoln;
	private int likes;
	
	@Override
	public String toString() {
		return "Comment [userId=" + userId + ", isExpertSoln=" + isExpertSoln
				+ ", likes=" + likes + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isExpertSoln ? 1231 : 1237);
		result = prime * result + likes;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (isExpertSoln != other.isExpertSoln)
			return false;
		if (likes != other.likes)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}
	
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonProperty("expert_solution")
	public boolean isExpertSoln() {
		return isExpertSoln;
	}
	
	@JsonProperty("expert_solution")
	public void setExpertSoln(boolean isExpertSoln) {
		this.isExpertSoln = isExpertSoln;
	}
	public int getLikes() {
		return likes;
	}
	
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		this.likes = in.readInt();
		this.isExpertSoln = in.readBoolean();
		this.userId = in.readLine();
		
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(likes);
		out.writeBoolean(isExpertSoln);
		out.writeChars(userId);
	}
	
	@Override
	public int compareTo(Object arg0) {
		Comment other = (Comment)arg0;
		int idDiff = userId.compareTo(other.userId);
        if(idDiff != 0){
            return idDiff;
        }
        int likesDiff = new Integer(likes).compareTo(other.likes);
        if(likesDiff != 0){
            return likesDiff;
        }
        
        return new Boolean(isExpertSoln).compareTo(other.isExpertSoln);
	}
	


}
