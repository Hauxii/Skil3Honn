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

    public ArrayNode listAllVideosInChannel(String channel) throws ServiceException{
        JsonNodeFactory factory = new JsonNodeFactory(false);

        ArrayNode listOfChannelVideos = factory.arrayNode();
        try {
            Statement st = conn.createStatement();
            String getallvideoid = "SELECT * FROM channels WHERE channel_name = " + "'" + channel + "'";
            ResultSet rs = st.executeQuery(getallvideoid);
            int videos[] = new int[100];
            int counter = 0;
            while(rs.next()){
                videos[counter++] = rs.getInt("video_id");

            }

            for(int i = 0; i < counter; i++){
                String getallvideos = "SELECT * FROM videos WHERE video_id = " + videos[i];
                ResultSet vids = st.executeQuery(getallvideos);
                while(vids.next()){
                    ObjectNode node = factory.objectNode();
                    node.put("title", rs.getString("video_name"));
                    node.put("url", rs.getString("video_url"));
                    listOfChannelVideos.add(node);
                }
            }
            return listOfChannelVideos;
        }
        catch(Exception ex){

            //System.out.println(ex.getMessage());
        }
        throw new ServiceException("video not found");
    }

    public void addVideoToChannel(JsonNode video,String channel)throws ServiceException{
        try {
            Statement st = conn.createStatement();
            String statement = "SELECT video_id FROM videos WHERE video_name = " + video.get("title");
            ResultSet rs = st.executeQuery(statement);
            if(rs.next()){
                String insert = "INSERT INTO channels (channel_name,video_id) VALUES ("
                        + "'" + channel + "'"
                        +","
                        + rs.getInt("video_id")
                        + ")";
                st.executeUpdate(insert);
            }
            else{
                throw new ServiceException("Video Doesnt Exists");
            }
        }
        catch(Exception ex){
            //throw new ServiceException(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    public void RemoveVideo(String videoname) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM videos WHERE video_name = " + videoname);
            ResultSet rs = st.executeQuery("SELECT video_id FROM videos WHERE video_name = " + "'" + videoname + "'");
            int videoid = 0;
            if(rs.next()){
                videoid = rs.getInt("video_id");
            }
            st.executeUpdate("DELETE FROM channels WHERE video_id = " + videoid);
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
    }

    public void addVideo(JsonNode video) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            String checkifvideoexists = "SELECT video_id FROM videos WHERE video_url = " + video.get("url");
            ResultSet rs = st.executeQuery(checkifvideoexists);

            if(!rs.next()){
                String statement = "VALUES ( "
                        + video.get("title")
                        + ", "
                        + video.get("url")
                        + ")";


                st.executeUpdate("INSERT INTO videos (video_name,video_url)" + statement);
            }
            else{
                throw new ServiceException ("Video already exists");
            }
        }
        catch(Exception ex){
            throw new ServiceException(ex.getMessage());
        }
    }
}
