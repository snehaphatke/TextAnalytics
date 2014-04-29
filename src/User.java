import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.codehaus.jackson.annotate.JsonProperty;

public class User  implements org.apache.hadoop.io.WritableComparable{

	private String _id;
	private int commentAgg = -1;   //75% in aggregate
	@JsonProperty("points")
	private int points = -1;       //25% in aggregate
	@Override
	public String toString() {
		return "User [_id=" + _id + ", commentAgg=" + commentAgg
				+ ", points=" + points + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + points;
		result = prime * result + commentAgg;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		if (points != other.points)
			return false;
		if (commentAgg != other.commentAgg)
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getCommentAgg() {
		return commentAgg;
	}
	public void setCommentAgg(int commentAgg) {
		this.commentAgg = commentAgg;
	}
	@JsonProperty("points")
	public int getPoints() {
		return points;
	}
	@JsonProperty("points")
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		this.points = in.readInt();
		this.commentAgg = in.readInt();
		this._id = in.readLine();
		
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(points);
		out.writeInt(commentAgg);
		out.writeChars(_id);
		
	}
	@Override
	public int compareTo(Object o) {
		User other = (User)o;
		int idDiff = _id.compareTo(other._id);
        if(idDiff != 0){
            return idDiff;
        }
        int actsDiff = new Integer(points).compareTo(other.points);
        if(actsDiff != 0){
            return actsDiff;
        }
        
        return new Integer(commentAgg).compareTo(other.commentAgg);
	}

}
