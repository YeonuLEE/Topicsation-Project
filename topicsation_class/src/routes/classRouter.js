// routes/classRouter.js
const express = require("express");
const router = express.Router();
const controller = require("../controllers/classController");

// 수업 페이지 라우팅
router.get("/lesson/:id", controller.showClass);

// 다른 페이지 라우팅
router.get("/about", (req, res) => {
  // 다른 페이지에 대한 로직 수행
  // 생성된 HTML 페이지 반환
});

module.exports = router;
