// 수업 페이지 컨트롤러
const path = require("path");

exports.showClass = (req, res) => {
  //res.render("classroom");
  const classId = req.params.id;
  const token = req.query.token;

  res.render("classroom", { $classId : classId, $token : token});
};
