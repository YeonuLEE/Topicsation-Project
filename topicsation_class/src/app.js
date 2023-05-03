import express from "express";
const io = require("socket.io");
const mustacheExpress = require("mustache-express");

const app = express();
const socket = io();

// Mustache 템플릿 엔진 설정
app.engine("mustache", mustacheExpress());
app.set("view engine", "mustache");
app.set("views", __dirname + "/public/views");

// 정적 파일 미들웨어
app.use(express.static("public"));
app.use(express.static("public"));
app.use(express.static("public"));
app.use("/img", express.static(__dirname + "/public/img"));
app.use("/js", express.static(__dirname + "/public/js"));

// 라우터 모듈 등록
const router = require("./routes/classRouter");
app.use(router);

// export
module.exports = app;
