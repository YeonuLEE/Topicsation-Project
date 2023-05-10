import http from "http";
import https from "https";
import { Server } from "socket.io";
import { instrument } from "@socket.io/admin-ui";
import { filtering, AhoCorasick, wordsToCensor} from './censoredWords.js';
const crypto = require('crypto');
const cors = require('cors');

require('dotenv').config();

const app = require("./app");
const fs = require('fs');

//인증서 파일 경로
const privateKey = fs.readFileSync('/etc/letsencrypt/live/www.topicsation.online/privkey.pem', 'utf8');
const certificate = fs.readFileSync('/etc/letsencrypt/live/www.topicsation.online/cert.pem', 'utf8');
const ca = fs.readFileSync('/etc/letsencrypt/live/www.topicsation.online/chain.pem', 'utf8');

const credentials = {
  key: privateKey,
  cert: certificate,
  ca: ca
};

// TURN server 아이디, 비밀번호 전달
app.get('/getTurnCredentials', (req, res) => {
  let staticAuthSecret = process.env.STATIC_AUTH_SECRET; // .env 파일의 STATIC_AUTH_SECRET 값
  let username = Math.floor(Date.now() / 1000) + 3600; // 유닉스 타임스탬프 + 유효시간(여기서는 1시간)

  let password = crypto.createHmac('sha1', staticAuthSecret)
      .update(username.toString())
      .digest('base64');

  res.json({username, password});
});

let username = Math.floor(Date.now() / 1000) + 3600;
let password = crypto.createHmac('sha1', "wrc0516!@")
    .update(username.toString())
    .digest('base64');
console.log("username ", username)
console.log("password ", password)

// CORS 설정
var corsOptions = {
  origin: 'https://www.topicsation.online',
  methods: ["GET", "POST", "PUT", "DELETE"],
  credentials: true,
  allowedHeaders: "*"
};

app.use(cors(corsOptions));

// 금지어 필터링 설정
const ac  = new AhoCorasick();
wordsToCensor.forEach((pattern) => ac.insert(pattern));
ac.buildFailureLinks();

// connect
const httpsServer = https.createServer(credentials, app);
const httpServer = http.createServer(app); // http server
const wsServer = new Server(httpsServer, {
  cors: {
    origin: ["https://admin.socket.io"],
    credentials: true,
  },
});

instrument(wsServer, {
  auth: false,
  mode: "development",
});

// wsServer logic
wsServer.on("connection", (socket) => {

  // 채팅 기능 관련
  socket.on("enter_room", (roomName, done) => {
    socket.join(roomName);
    done();
    socket.to(roomName).emit("welcomeChat", socket.nickname);
  });
  socket.on("new_message", (msg, room, done) => {
    // 메시지 금지어 필터링
    let filteredMsg = filtering(msg, ac)
    socket.to(room).emit("new_message", `${socket.nickname}: ${filteredMsg}`);
    done();
  });
  socket.on("nickname", (nickname) => {
    socket["nickname"] = nickname;
  });
  socket.on("disconnecting", () => {
    socket.rooms.forEach((room) =>
      socket.to(room).emit("bye", socket.nickname)
    );
  });

  // 화상 기능 관련
  socket.on("join_room", (roomName) => {
    socket.join(roomName);
    socket.to(roomName).emit("welcome");
  });
  socket.on("offer" , (offer, roomName) => {
    socket.to(roomName).emit("offer", offer);
  });
  socket.on("answer", (answer, roomName) => {
    socket.to(roomName).emit("answer", answer);
  });
  socket.on("ice", (ice, roomName) => {
    socket.to(roomName).emit("ice", ice);
  });
});

// listen
const handleListen = () => console.log("Listening on https://www.topicsation.online");
httpsServer.listen(443, handleListen);
