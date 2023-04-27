
$(document).ready(function () {
  const socket = io();

  // const welcome = document.getElementById("welcome");
  // const form = welcome.querySelector("form");
  const room = document.getElementById("room");

  let roomName;
  var classId = window.location.pathname.split("/").pop(); // /class/456

  function addMessage(message) {
    // const ul = room.querySelector("ul");
    // const li = document.createElement("li");
    // li.innerText = message;
    // ul.appendChild(li);
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
    // event.preventDefault();
    // const input = room.querySelector("#name input");
    // const value = input.value;
    socket.emit("nickname", "Chat partner");
  }

  function showRoom() {
    //   welcome.hidden = true;
    // room.hidden = false;
    // const h3 = room.querySelector("h3");
    // h3.innerText = `Room ${roomName}`;
    const msgForm = document.getElementById("msg");
    // const nameForm = room.querySelector("#name");
    msgForm.addEventListener("submit", handleMessageSubmit);
    // nameForm.addEventListener("submit", handleNicknameSubmit);
  }

  handleNicknameSubmit();

  function handleRoomSubmit() {
    //   event.preventDefault();
    // const input = form.querySelector("input");
    socket.emit("enter_room", classId, showRoom);
    roomName = classId;
    // input.value = "";
  }

  handleRoomSubmit();
  // form.addEventListener("submit", handleRoomSubmit);

  socket.on("welcomeChat", (user) => {
    // const h3 = room.querySelector("h3");
    // h3.innerText = `Room ${roomName} (${newCount})`;
    addMessage(`${user} arrived!`);
  });

  socket.on("bye", (left) => {
    // const h3 = room.querySelector("h3");
    // h3.innerText = `Room ${roomName} (${newCount})`;
    if (left != null) {
      addMessage(`${left} left ㅠㅠ`);
    }
  });

  socket.on("new_message", addMessage);

  // socket.on("room_change", (rooms) => {
  //   const roomList = welcome.querySelector("ul");
  //   roomList.innerHTML = "";
  //   if (rooms.length === 0) {
  //     roomList.innerHTML = "";
  //     return;
  //   }
  //   rooms.forEach((room) => {
  //     const li = document.createElement("li");
  //     li.innerText = room;
  //     roomList.append(li);
  //   });
  // });
});
