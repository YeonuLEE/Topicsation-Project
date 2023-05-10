$(document).ready(function () {

  const socket = io();

  // html 요소 불러오기
  const myFace = document.getElementById("myFace");
  const muteBtn = document.getElementById("mute");
  const cameraBtn = document.getElementById("camera");
  const camerasSelect = document.getElementById("cameras");
  const peersFace = document.getElementById("peersFace");

  // 변수 초기화
  var classId = window.location.pathname.split("/").pop(); // /class/456

  let myStream;
  let muted = false;
  let cameraOff = false;
  let roomName;
  let myPeerConnection;
  let myDataChannel;

  // 화상 관련 함수 선언
  async function getCameras() {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      const cameras = devices.filter((device) => device.kind === "videoinput");
      const currentCameras = myStream.getVideoTracks()[0];
      cameras.forEach((camera) => {
        const option = document.createElement("option");
        option.value = camera.deviceId;
        option.innerText = camera.label;
        if (currentCameras.label === camera.label) {
          option.selected = true;
        }
        camerasSelect.appendChild(option);
      });
    } catch (e) {
      console.log(e);
    }
  }

  async function getMedia(deviceId) {
    const initialContraints = {
      audio: true,
      video: { facingMode: "user" },
    };
    const cameraContraints = {
      audio: true,
      video: {
        deviceId: { exact: deviceId },
      },
    };
    try {
      myStream = await navigator.mediaDevices.getUserMedia(
        deviceId ? cameraContraints : initialContraints
      );
      myFace.srcObject = myStream;
      if (!deviceId) {
        await getCameras();
      }
    } catch (e) {
      console.log(e);
    }
  }

  function handleMuteClick() {
    myStream.getAudioTracks().forEach((track) => {
      track.enabled = !track.enabled;
    });
    if (!muted) {
      muteBtn.innerText = "Unmute";
      muted = true;
    } else {
      muteBtn.innerText = "Mute";
      muted = false;
    }
  }
  function handleCameraClick() {
    muted = false;
    myStream.getVideoTracks().forEach((track) => {
      track.enabled = !track.enabled;
    });
    if (cameraOff) {
      cameraBtn.innerText = "Turn Camera Off";
      cameraOff = false;
    } else {
      cameraBtn.innerText = "Turn Camera On";
      cameraOff = true;
    }
  }

  async function handleCameraChange() {
    await getMedia(camerasSelect.value);
    if (myPeerConnection) {
      const videoTrack = myStream.getVideoTracks()[0];
      const videoSender = myPeerConnection
        .getSenders()
        .find((sender) => sender.track.kind === "video");
      videoSender.replaceTrack(videoTrack);
    }

    if (cameraOff) {
      cameraBtn.innerText = "Turn Camera Off";
      cameraOff = false;
    } else {
      cameraBtn.innerText = "Turn Camera On";
      cameraOff = true;
    }
    if (!muted) {
      muteBtn.innerText = "Unmute";
      muted = true;
    } else {
      muteBtn.innerText = "Mute";
      muted = false;
    }
  }

  // 화상 관련 이벤트 등록
  muteBtn.addEventListener("click", handleMuteClick);
  cameraBtn.addEventListener("click", handleCameraClick);
  camerasSelect.addEventListener("input", handleCameraChange);

  async function initCall() {
    await getMedia();
    makeConnection();
  }

  async function handleWelcomeSubmit() {
    await initCall();
    socket.emit("join_room", classId);
    roomName = classId;
  }

  // 자동 room 연결
  handleWelcomeSubmit();

  // Socket Code
  // WebRTC 전에 signing 과정에서 WebSocket을 잠시 써야함
  socket.on("welcome", async () => {
    const offer = await myPeerConnection.createOffer();
    myPeerConnection.setLocalDescription(offer);
    socket.emit("offer", offer, roomName);
  });

  socket.on("offer", async (offer) => {

    myPeerConnection.setRemoteDescription(offer);
    const answer = await myPeerConnection.createAnswer();
    myPeerConnection.setLocalDescription(answer);
    socket.emit("answer", answer, roomName);
  });

  socket.on("answer", (answer) => {
    myPeerConnection.setRemoteDescription(answer);
  });

  socket.on("ice", (ice) => {
    myPeerConnection.addIceCandidate(ice);
  });

  // RTC Code
  function makeConnection() {
    myPeerConnection = new RTCPeerConnection({
      iceServers: [
        {
          urls: "stun:49.50.167.18:3478"
        },
        {
          urls: "turn:49.50.167.18:3478",
          username:username,
          credential:password
        }
      ],
    });
    myPeerConnection.addEventListener("icecandidate", handleIce);
    myPeerConnection.addEventListener("track", handleTrack);
    myStream
      .getTracks()
      .forEach((track) => myPeerConnection.addTrack(track, myStream));
  }

  function handleIce(data) {
    socket.emit("ice", data.candidate, roomName);
  }

  function handleTrack(data) {
    peersFace.srcObject = data.streams[0];
  }
});
