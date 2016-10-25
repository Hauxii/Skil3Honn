package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Berglind on 24.10.2016.
 */
public class VideoService extends AppDataContext{

    public ArrayNode getAllVideos() throws ServiceException{
        JsonNodeFactory factory = new JsonNodeFactory(false);

        ArrayNode listOfVideos = factory.arrayNode();

        try {
            Statement st = conn.createStatement();
            String query = " SELECT * "
                    + " FROM videos";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                ObjectNode node = factory.objectNode();
                node.put("title", rs.getString("video_name"));
                node.put("url", rs.getString("video_url"));

                listOfVideos.add(node);
            }
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
        return listOfVideos;
    }

    public ArrayNode listAllVideosInChannel(JsonNode channel) throws ServiceException{
        String channelname = channel.get("channel").toString();
        JsonNodeFactory factory = new JsonNodeFactory(false);

        ArrayNode listOfChannelVideos = factory.arrayNode();
        try {
            Statement st = conn.createStatement();
            String getallvideoid = "SELECT * FROM channels WHERE channel_name = " + channelname;
            ResultSet rs = st.executeQuery(getallvideoid);

            while(rs.next()){
                String getallvideos = "SELECT * FROM videos WHERE video_id = " + rs.getInt("video_id");
                ResultSet vids = st.executeQuery(getallvideos);
                while(vids.next()){
                    ObjectNode node = factory.objectNode();
                    node.put("title", rs.getString("video_name"));
                    node.put("url", rs.getString("video_url"));
                    listOfChannelVideos.add(node);
                }
            }
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
        return listOfChannelVideos;
    }

    public void addVideoToChannel(JsonNode videoToChannel)throws ServiceException{
        try {
            Statement st = conn.createStatement();
            String statement = "SELECT video_id FROM videos WHERE video_name = " + videoToChannel.get("videoname");
            ResultSet rs = st.executeQuery(statement);
            while(rs.next()){
                String insert = "INSERT INTO channels (channel_name,video_id) VALUES ("
                        + videoToChannel.get("channelname")
                        +","
                        + rs.getInt("video_id")
                        + ")";
                st.executeUpdate(insert);
            }
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
    }

    public void RemoveVideo(String videoname) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM videos WHERE video_name = " + videoname);
            ResultSet rs = st.executeQuery("SELECT video_id FROM videos WHERE video_name = " + videoname);
            int videoid = 0;
            while(rs.next()){
                videoid = rs.getInt("video_id");
            }
            st.executeUpdate("DELETE FROM channels WHERE video_id = " + videoid);
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
    }
}
