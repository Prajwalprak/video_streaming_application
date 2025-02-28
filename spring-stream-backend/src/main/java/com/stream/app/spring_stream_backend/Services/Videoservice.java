package com.stream.app.spring_stream_backend.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stream.app.spring_stream_backend.entities.Video;

public interface Videoservice {
	
	Video save(Video video, MultipartFile file);
	
	Video getById(String videoId);
	
	List<Video> getAllVideo();
	
	Video getByTitle(String title);
	
	String processVideo(String videoId);

}
