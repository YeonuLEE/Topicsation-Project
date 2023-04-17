//캘린더 
$(document).ready(function(){

    $('.datepicker').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        startDate: '0d'
    });
    $('.cell').click(function(){
        // if($('#yymmdd').val() = null){
        //     return false;
        // } 
        if($(this).hasClass("select")) {
            $("#modal-default").modal();
            $(this).removeClass('select');
        }else{
            $("#modal-default").modal();
            $(this).addClass('select');
        }
        // alert($('.select').text());
        // alert($('#yymmdd').val());
    });
});
