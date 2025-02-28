package com.stream.app.spring_stream_backend.Services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.spring_stream_backend.Repositories.VideoRepositories;
import com.stream.app.spring_stream_backend.Services.Videoservice;
import com.stream.app.spring_stream_backend.entities.Video;

import jakarta.annotation.PostConstruct;


@Service
public class VideoServiceImpl implements Videoservice{
	
	@Value("${files.video}")
    String DIR;

    @Value("${file.video.hsl}")
    String HSL_DIR;
    
    private VideoRepositories videoRepositories;
    
    public VideoServiceImpl(VideoRepositories videoRepositories) {
		this.videoRepositories = videoRepositories;
	}

	@PostConstruct
    public void init() {
    	File file=new File(DIR);
    	
//    	File file1=new File(HSL_DIR);	
//    	if(!file1.exists()) {
//    		file.mkdir();
//    	}
    	
    	//Alternate way to create a directory
    	try {
            Files.createDirectories(Paths.get(HSL_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    	
    	if(!file.exists()) {
    		file.mkdir();
    		System.out.println("Folder created");
    	}
    	else {
    		System.out.println("folder already created");
    	}
    }
    
//  @Override
//	public Video save(Video video, MultipartFile file) {
//		try {
//			String filename = file.getOriginalFilename();
//            String contentType = file.getContentType();
//            InputStream inputStream = file.getInputStream();
//            
//            String cleanFileName = StringUtils.cleanPath(filename);
//            String cleanFolder = StringUtils.cleanPath(DIR);
//
//            Path path = Paths.get(cleanFolder, cleanFileName);
//            
//            System.out.println(contentType);
//            System.out.println(path);
//            
//            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
//            
//            video.setContentType(contentType);
//            video.setFilePath(path.toString());
//            
//            Video savedVideo = videoRepositories.save(video);
//            //processing video
//            processVideo(savedVideo.getVideoId());
//
//
//            // metadata save
//            return savedVideo;
//            
//			}
//		catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	@Override
	public Video save(Video video, MultipartFile file) {
        // original file name

        try {


            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();


            // file path
            String cleanFileName = StringUtils.cleanPath(filename);


            //folder path : create

            String cleanFolder = StringUtils.cleanPath(DIR);


            // folder path with  filename
            Path path = Paths.get(cleanFolder, cleanFileName);

            System.out.println(contentType);
            System.out.println(path);

            // copy file to the folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);


            // video meta data

            video.setContentType(contentType);
            video.setFilePath(path.toString());
            Video savedVideo = videoRepositories.save(video);
            //processing video
            processVideo(savedVideo.getVideoId());

            //delete actual video file and database entry  if exception

            // metadata save
            return savedVideo;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in processing video ");
        }


    }

	@Override
	public Video getById(String videoId) {
		Video video = videoRepositories.findById(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        return video;
	}

	@Override
	public List<Video> getAllVideo() {
		return videoRepositories.findAll();
	}

	@Override
	public Video getByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public String processVideo(String videoId) {
//
//        Video video = this.getById(videoId);
//        String filePath = video.getFilePath();
//
//        //path where to store data:
//        Path videoPath = Paths.get(filePath);
//
//
////        String output360p = HSL_DIR + videoId + "/360p/";
////        String output720p = HSL_DIR + videoId + "/720p/";
////        String output1080p = HSL_DIR + videoId + "/1080p/";
//
//        try {
////            Files.createDirectories(Paths.get(output360p));
////            Files.createDirectories(Paths.get(output720p));
////            Files.createDirectories(Paths.get(output1080p));
//
//            // ffmpeg command
//            Path outputPath = Paths.get(HSL_DIR, videoId);
//
//            Files.createDirectories(outputPath);
//
//
//            String ffmpegCmd = String.format(
//                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
//                    videoPath, outputPath, outputPath
//            );
//
////            StringBuilder ffmpegCmd = new StringBuilder();
////            ffmpegCmd.append("ffmpeg  -i ")
////                    .append(videoPath.toString())
////                    .append(" -c:v libx264 -c:a aac")
////                    .append(" ")
////                    .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
////                    .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
////                    .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
////                    .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
////                    .append("-master_pl_name ").append(HSL_DIR).append(videoId).append("/master.m3u8 ")
////                    .append("-f hls -hls_time 10 -hls_list_size 0 ")
////                    .append("-hls_segment_filename \"").append(HSL_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
////                    .append("\"").append(HSL_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");
//
//
//            System.out.println(ffmpegCmd);
//            //file this command
//            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
//            processBuilder.inheritIO();
//            Process process = processBuilder.start();
//            int exit = process.waitFor();
//            if (exit != 0) {
//                throw new RuntimeException("video processing failed!!");
//            }
//
//            return videoId;
//
//
//        } catch (IOException ex) {
//            throw new RuntimeException("Video processing fail!!");
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
	
	@Override
    public String processVideo(String videoId) {

        Video video = this.getById(videoId);
        String filePath = video.getFilePath();

        //path where to store data:
        Path videoPath = Paths.get(filePath);


//        String output360p = HSL_DIR + videoId + "/360p/";
//        String output720p = HSL_DIR + videoId + "/720p/";
//        String output1080p = HSL_DIR + videoId + "/1080p/";

        try {
//            Files.createDirectories(Paths.get(output360p));
//            Files.createDirectories(Paths.get(output720p));
//            Files.createDirectories(Paths.get(output1080p));

            // ffmpeg command
            Path outputPath = Paths.get(HSL_DIR, videoId);

            Files.createDirectories(outputPath);


//            String ffmpegCmd = String.format(
//                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
//                    videoPath, outputPath, outputPath
//            );

//            StringBuilder ffmpegCmd = new StringBuilder();
//            ffmpegCmd.append("ffmpeg  -i ")
//                    .append(videoPath.toString())
//                    .append(" -c:v libx264 -c:a aac")
//                    .append(" ")
//                    .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
//                    .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
//                    .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
//                    .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
//                    .append("-master_pl_name ").append(HSL_DIR).append(videoId).append("/master.m3u8 ")
//                    .append("-f hls -hls_time 10 -hls_list_size 0 ")
//                    .append("-hls_segment_filename \"").append(HSL_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
//                    .append("\"").append(HSL_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");


//            System.out.println(ffmpegCmd);
//            //file this command
//            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
//            processBuilder.inheritIO();
//            Process process = processBuilder.start();
//            int exit = process.waitFor();
//            if (exit != 0) {
//                throw new RuntimeException("video processing failed!!");
//            }
            
            String ffmpegCmd = String.format(
            	    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s\\segment_%%3d.ts\"  \"%s\\master.m3u8\" ",
            	    videoPath, outputPath.toString(), outputPath.toString()
            	);

            
//            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "-c", ffmpegCmd);
//            processBuilder.inheritIO();
//            Process process = processBuilder.start();
//            int exit = process.waitFor();
//            if (exit != 0) {
//                // Capture and print FFmpeg error output
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//                throw new RuntimeException("Video processing failed with exit code: " + exit);
//            }
            
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCmd);
            processBuilder.redirectErrorStream(true); // Redirects error output to standard output
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print the output for debugging
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Video processing failed, exit code: " + exitCode);
            }


            return videoId;


        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

	
	
}
