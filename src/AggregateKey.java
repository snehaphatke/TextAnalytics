import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;


public class AggregateKey  implements org.apache.hadoop.io.WritableComparable{
	private static ObjectMapper mapper = new ObjectMapper();
	private long userid;
	private String tag;
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + (int) (userid ^ (userid >>> 32));
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
		AggregateKey other = (AggregateKey) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AggregateKey [userid=" + userid + ", tag=" + tag + "]";
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		this.userid = in.readLong();
		this.tag = in.readLine();
		
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(userid);
		out.writeChars(tag);
		
	}
	@Override
	public int compareTo(Object arg0) {
		AggregateKey other = (AggregateKey)arg0;
		int idDiff = new Long(userid).compareTo(other.userid);
        if(idDiff != 0){
            return idDiff;
        }
        
        return tag.compareTo(other.tag);
		
	}

}
