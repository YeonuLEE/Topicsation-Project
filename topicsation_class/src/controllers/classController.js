// 수업 페이지 컨트롤러
exports.showClass = (req, res) => {
  const classId = req.params.id;
  const token = req.query.token;

  res.render("classroom", { $classId : classId, $token : token});
};
