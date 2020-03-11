var refreshSeconds = 10;
var timeOut = refreshSeconds * 1000;

/*$(function(){
	completeBatchesTable();
	setInterval(function() {
		completeBatchesTable();
	}, timeOut);
	
});*/


function completeBatchesTable(){
		var servletURL = "./batchRequest";
		var index = location.href.indexOf("batchrequests.htm");
		if(index!= -1){
			 servletURL = "./batchRequest";
		}
		var countTotal = 0;
		var countSuccess = 0;
		var countFailed = 0;
		var countInProgress = 0;
		var countWaiting = 0;
		$.ajax({
			data:{
				userFilter: false
			},
			//todo : investigate if this shold be true or false
			async: false,
			
			url : servletURL,
			method : 'GET',
			success: function(data, textStatus, xhr){
				
				var $auxTbody = $("<tbody>");
				$.each(data, function(index, value){
					$.ajax({
						async: false,
						url :  servletURL,
						method : 'GET',
						data : {
							groupkey: value
						},
						success: function(response, textStatus, xhr){
							response = response.items;
							countTotal+= response.nb_batches;
							if(response.progress == 0){
								return;
							}else{
								$.each(response.batches, function(index, batch){
									if(batch.status == "SUCCESS"){
										countSuccess++;
									}else if(batch.status == "FAILED"){
										countFailed++;
									}else{
										countInProgress++;
									}
									countWaiting = countTotal - (countSuccess + countFailed + countInProgress);
									var $tr = $("<tr>");
									$tr.append($("<td>").append(batch.id));
									$tr.append($("<td>").append(batch.status));
									$progressBar = $("<div>").progressbar({
									      value: batch.progress 
								    });
									$tr.append($("<td>").append($progressBar));
									$tr.append($("<td>").append(response.username));
									$auxTbody.append($tr);
									$header = $(".accordion").find("#"+value).prev();
									if($header.length > 0){
										$header.find(".batchInfo").text(batch.status);
									}
									
									
									
								});
								
							}
						}
					});
					
				});
				console.log("start");
				$("#nbBatches").text(countTotal);
				$("#nbSuccess").text(countSuccess);
				$("#nbFailed").text(countFailed);
				$("#nbInProgress").text(countInProgress);
				$("#nbWaiting").text(countWaiting);
				$("#batches").find("tbody").remove();
				$("#batches").append($auxTbody);
				console.log("stop");
				
			},
			error: function (xhr, textStatus, error){
				switch (xhr.status) {
				case 403:
					alert("You don't have necessary authorities to perform this action.");
					break;
				case 406:
					var json = $.parseJSON(xhr.responseText);
					alert(json.message);
					break;
				default:
					break;
				}
			}
		});
	
}

String.prototype.startsWith = function(prefix) {
    return this.indexOf(prefix) === 0;
}

String.prototype.endsWith = function(suffix) {
    return this.match(suffix+"$") == suffix;
};