import http from "http";
import { Server } from "socket.io";
import { instrument } from "@socket.io/admin-ui";
import { filtering, AhoCorasick, wordsToCensor} from './censoredWords.js';

const app = require("./app");

// 금지어 필터링 설정
const ac  = new AhoCorasick();
wordsToCensor.forEach((pattern) => ac.insert(pattern));
ac.buildFailureLinks();

// connect
const httpSever = http.createServer(app); // http server
const wsServer = new Server(httpSever, {
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
  socket.on("offer", (offer, roomName) => {
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
const handleListen = () => console.log("Listening on http://localhost:3000");
httpSever.listen(3000, handleListen);
