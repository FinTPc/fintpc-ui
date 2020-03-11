$(function() {

	$(".submitButton").click(function() {
		var schemaname = $("#name").val();
		$("#name").val(schemaname.replace(/ /g,''));
	});

});