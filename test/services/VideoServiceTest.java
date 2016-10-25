package services;

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
        _video.put("videoname", "PPAP");
        _video.put("url", "www.youtube.com");
        _videoService.addVideoToChannel(_video);
    }

    @After
    public void tearDown() throws Exception {
        _videoService.RemoveVideo(_video.get("video_name").toString());
    }

    @Test
    public void addVideoToChannelAllVideos() throws Exception {
        //Check if video added is in all videos list
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("videoname", "PPAP");
        video.put("url", "www.youtube.com");
        //TODO: add and check
    }

    @Test
    public void addVideoToChannel() throws Exception {
        //Check if cideo added is in channel video list
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("videoname", "PPAP");
        video.put("url", "www.youtube.com");
        //TODO: add and check
    }

    @Test
    public void removeVideo() throws Exception {
        ObjectNode video = new JsonNodeFactory(false).objectNode();
        video.put("videoname", "PPAP");
        video.put("url", "www.youtube.com");
        _videoService.addVideoToChannel(video);
        _videoService.RemoveVideo(video.get("videoname").toString());
        //TODO: assert that video was removed
    }

}