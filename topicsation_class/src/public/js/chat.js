$(document).ready(function () {
  const socket = io();

  let roomName;
  var classId = window.location.pathname.split("/").pop(); // /class/456

  function addMessage(message) {
    const chatArea = document.getElementById("chat-area");
    chatArea.value += message + "\n";
    chatArea.scrollTop = chatArea.scrollHeight;
  }

  function handleMessageSubmit(event) {
    event.preventDefault();
    const input = document.getElementById("msgInput");
    const value = input.value;
    socket.emit("new_message", input.value, roomName, () => {
      addMessage(`You : ${value}`);
    });
    input.value = ""; // 여기 실행되고 콜백함수 실행됨
  }

  function handleNicknameSubmit() {
    socket.emit("nickname", "Chat partner");
  }

  function showRoom() {
    const msgForm = document.getElementById("msg");
    msgForm.addEventListener("submit", handleMessageSubmit);
  }

  handleNicknameSubmit();

  function handleRoomSubmit() {
    socket.emit("enter_room", classId, showRoom);
    roomName = classId;
  }

  handleRoomSubmit();

  socket.on("welcomeChat", (user) => {
    addMessage(`${user} arrived!`);
  });

  socket.on("bye", (left) => {
    if (left != null) {
      addMessage(`${left} left T_T`);
    }
  });

  socket.on("new_message", addMessage);
});
