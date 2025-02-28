import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import VideoUploadd from "./components/VideoUploadd";

import { TextInput, Button } from "flowbite-react";
import "./App.css";
import { Toaster } from "react-hot-toast";
import VideoPlayer from "./components/VideoPlayer";

function App() {
  const [fieldValue, setFieldValue] = useState(null);
  const [videoId, setVideoId] = useState(
    "165d50a3-c719-47f4-b817-d5436d0fbfae"
  );

  function playVideo(videoId) {
    setVideoId(videoId);
  }

  return (
    <>
      <Toaster />
      <div className="flex flex-col items-center space-y-9 justify-center py-7">
        <h1 className="text-3xl font-extrabold text-gray-600 dark:text-gray-100">
          Video Streaming App
        </h1>
        <div className="flex mt-14 w-full space-x-2  justify-between">
          <div className="w-full">
            <h1 className="text-white text-center mt-0">Playing video</h1>
            <div>
              <VideoPlayer
                src={`http://localhost:8080/api/v1/videos/${videoId}/master.m3u8`}
              ></VideoPlayer>
            </div>

            {/* <video 
            style={{
              width:"100%",
              height:500
            }}
            // src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} 
            src="http://localhost:8080/api/v1/videos/165d50a3-c719-47f4-b817-d5436d0fbfae/master.m3u8"
            controls></video> */}

            {/* <video
              id="my-video"
              class="video-js"
              controls
              preload="auto"
              width="640"
              data-setup="{}"
            >
              <source src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`}  type="video/mp4" />
              <p class="vjs-no-js">
                To view this video please enable JavaScript, and consider
                upgrading to a web browser that
                <a
                  href="https://videojs.com/html5-video-support/"
                  target="_blank"
                >
                  supports HTML5 video
                </a>
              </p>
            </video> */}
          </div>
          <div className="w-full">
            <VideoUploadd />
          </div>
        </div>
        <div className="my-4 flex  space-x-4">
          <TextInput
            onClick={(event) => {
              setFieldValue(event.target.value);
            }}
            placeholder="Enter video id here"
            name="video_id_field"
          />
          <Button
            onClick={() => {
              setVideoId(fieldValue);
            }}
          >
            Play
          </Button>
        </div>
      </div>
    </>
  );
}

export default App;
