//튜터 스케줄
$(document).ready(function () {

    $('.datepicker').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        startDate: '0d'
    });
    $('.datepicker').datepicker('setDate', 'today');
    $('.cell').click(function () {
        if ($(this).hasClass("select")) {
            // $("#modal-default").modal();
            $(this).removeClass('select');
        } else {
            $(this).addClass('select');
        }
    });
});