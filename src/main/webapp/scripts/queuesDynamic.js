var refreshSeconds = 30;
var timeOut = refreshSeconds * 1000;

$(function() {
	setInterval(function() {
		ajaxCountMessagesInQueues();
	}, timeOut);
});



function ajaxCountMessagesInQueues(){
	$.ajax({
		method : 'GET',
		url : "queues/count",
		async : true,
		beforeSend: function(xhr){
	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
	            					 $('#_csrf').attr("content"));
	    },
		success : function(response) {
			var queues = JSON.parse(response);
			$(".fintpTable > tbody  > tr > td:nth-child(3)").each(function() {
				$(this).find("img").remove();
				$(this).find("span").text(queues[$(this).find("span").attr("data-id")]);
			})
		},
		error : function(jqXHR, text, errorThrown) {
			console.log(jqXHR, text, errorThrown)
		}
	});
}

