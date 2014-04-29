import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.codehaus.jackson.annotate.JsonProperty;


public class User  implements org.apache.hadoop.io.WritableComparable{
	/*{ "city" : "San Jose", 
		"state" : "CA", 
		"points" : 0, 
		"no_of_exp_solutions" : 0, 
		"expertise_in_tags" : "", 
		"first_name" : "Paras", 
		"last_name" : "Agrawal", 
		"email" : "paras@gmail.com", 
		"role" : "Learner", 
		"password_digest" : "$2a$10$DHC964FnLBYzTRz1ODcJ/OGHPJEq1RFjeoNj2gw8qdV5MhA8UHROS", 
		"area_of_interest" : "Database", 
		"gender" : "Male", 
		"birthdate(2i)" : "5", 
		"birthdate(3i)" : "12", 
		"birthdate(1i)" : "1990", 
		"zip_code" : 95112, 
		"_id" : "paras@gmail.com",
		"updated_at" : ISODate("2014-04-12T07:15:27.129Z"), 
		"created_at" : ISODate("2014-04-12T07:15:27.129Z"), 
		"auth_token" : "d54d3d98f032c338b06b9a2680dbd422", 
		"roles_mask" : 2 }*/
	private String email;
	private int commentAgg = -1;//75% in aggregate
	@JsonProperty("points")
	private int activities = -1;//25% in aggregate
	@Override
	public String toString() {
		return "User [email=" + email + ", commentAgg=" + commentAgg
				+ ", activities=" + activities + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activities;
		result = prime * result + commentAgg;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (activities != other.activities)
			return false;
		if (commentAgg != other.commentAgg)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getCommentAgg() {
		return commentAgg;
	}
	public void setCommentAgg(int commentAgg) {
		this.commentAgg = commentAgg;
	}
	@JsonProperty("points")
	public int getActivities() {
		return activities;
	}
	@JsonProperty("points")
	public void setActivities(int activities) {
		this.activities = activities;
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		this.activities = in.readInt();
		this.commentAgg = in.readInt();
		this.email = in.readLine();
		
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(activities);
		out.writeInt(commentAgg);
		out.writeChars(email);
		
	}
	@Override
	public int compareTo(Object o) {
		User other = (User)o;
		int idDiff = email.compareTo(other.email);
        if(idDiff != 0){
            return idDiff;
        }
        int actsDiff = new Integer(activities).compareTo(other.activities);
        if(actsDiff != 0){
            return actsDiff;
        }
        
        return new Integer(commentAgg).compareTo(other.commentAgg);
	}

}
