$(function(){
	$(".batch").click(function(){
		var $accordionElement = $(this).parent().find("div");
		$accordionElement.find("h3").each(function(){
			//find out group key and div correspondent
			var groupKey = $(this).attr("aria-controls");
			var $groupDiv = $("#"+groupKey);
			
			var $checkbox = $(this).find("input[class='batchCheckbox']");
			var timekey = $(this).find("input[name='timekey']").val();
			var queue = $("#queueName").text();
			var fields = new Array();
			var $listValues = $groupDiv.find("ul[class='groupValues']");
			$listValues.find("li").each(function(){
				fields.push($(this).text());
			});
			var msgtype = $groupDiv.find("input[name='messageType']").val();

			if($checkbox.prop("checked")){
				$.ajax({
					data:{
						queuename : queue,
						groupkey : groupKey,
						timekey : timekey,
						fields : fields,
						msgtype : msgtype
					},
					method : 'POST',
					url : "./batchRequest",
					dataType : 'json',
					async : false,
					success: function(data, textStatus, xhr){
						console.log("SUCCESS STATUS " + xhr.status);
						completeBatchesTable(true);
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
			
			
		});
	});
});