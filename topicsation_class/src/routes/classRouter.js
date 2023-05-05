// routes/classRouter.js
const express = require("express");
const router = express.Router();
const controller = require("../controllers/classController");

// 수업 페이지 라우팅
router.get("/lesson/:id", controller.showClass);

module.exports = router;
