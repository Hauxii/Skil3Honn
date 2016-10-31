package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Lenny on 25.10.2016.
 */
public class VideoServiceTest {
    VideoService _videoService;
    ObjectNode _video;
    @Before
    public void setUp() throws Exception {
        _videoService = new VideoService();
        _video = new JsonNodeFactory(false).objectNode();
        _video.put("title", "PPAP");
        _video.put("url", "www.youtube.com");
        _videoService.addVideo(_video);
    }

    @After
    public void tearDown() throws Exception {
        _videoService.RemoveVideo(_video.get("title").toString());
    }

    @Test
    public void CheckVideoInAllVideos() throws Exception {
        //Check if video added is in all videos list
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("title", "PPAP");
        video.put("url", "www.youtube.com");
        ArrayNode list =_videoService.getAllVideos();
        boolean exists = false;
        for(int i = 0; i < list.size();i++) {
            JsonNode tmp = list.get(i);
            if(video.get("url").equals(tmp.get("url"))){
                exists = true;
            }
        }

        assertEquals(true,exists);
    }

    @Test
    public void addVideoToChannel() throws Exception {
        //Check if cideo added is in channel video list
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("title", "PPAP");
        video.put("url", "www.youtube.com");
        _videoService.addVideoToChannel(video,"CNN");
        ArrayNode list =_videoService.listAllVideosInChannel("CNN");
        /*System.out.println(list);
        boolean exists = false;
        for(int i = 0; i < list.size();i++) {
            JsonNode tmp = list.get(i);
            System.out.println(tmp);
            if(video.get("url").equals(tmp.get("url"))){
                exists = true;
            }
        }

        assertEquals(true,exists);*/
    }

    @Test
    public void removeVideo() throws Exception {
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("title", "All by myself");
        video.put("url", "www.youtube.com");

        _videoService.addVideoToChannel(video, "BBC");
        _videoService.RemoveVideo(video.get("title").toString());
        //TODO: assert that video was removed
    }

}