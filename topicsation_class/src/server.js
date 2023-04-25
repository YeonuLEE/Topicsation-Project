import http from "http";
import { Server } from "socket.io";
import { instrument } from "@socket.io/admin-ui";
import nodemon from "nodemon";

const app = require("./app");

// connect
const httpSever = http.createServer(app); // http server
//const wss = new WebSocket.Server({ server }); // WebSocket server on http server
// 두개의 서버가 하나의 port를 공유, 필수 아님 wss만 만들어도됨
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
  // Chat
  socket.on("enter_room", (roomName, done) => {
    console.log("chat room : ", roomName);
    socket.join(roomName);
    done();
    socket.to(roomName).emit("welcomeChat", socket.nickname);
    // wsServer.sockets.emit("room_change", publicRooms());
  });

  socket.on("new_message", (msg, room, done) => {
    console.log("새로운 메세지 : ", msg);
    socket.to(room).emit("new_message", `${socket.nickname}: ${msg}`);
    done();
  });
  socket.on("nickname", (nickname) => {
    console.log("nickname : ", nickname);
    socket["nickname"] = nickname;
  });

  socket.on("disconnecting", () => {
    socket.rooms.forEach((room) =>
      socket.to(room).emit("bye", socket.nickname)
    );
  });

  // Zoom
  socket.on("join_room", (roomName) => {
    console.log("수업 번호 : ", roomName);
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
