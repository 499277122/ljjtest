 $(document).ready(function () {
      $(".panel-color").bind("mouseenter", function() {
        $(this).css({"background-color": "#f5f5f5"});
			});
      $(".panel-color").bind("mouseleave", function() {
        $(this).css({"background-color": "white"});
			});
			$(".showTab2").bind("click", function() {
				$("#tab2").removeClass(); 
				$("#tab2-button").trigger("click");
			});
			$("#tab1-button").bind("click", function() {
				$("#tab2").addClass("hide"); 
			});
			$('.i-checks').iCheck({
          checkboxClass: 'icheckbox_square-green',
          radioClass: 'iradio_square-green',
      });
});