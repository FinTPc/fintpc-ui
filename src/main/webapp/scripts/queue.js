//var refreshSeconds = 10;
//var timeOut = refreshSeconds * 1000;
$(function() {

	$('button.messageAction').click(function(e) {	
		_this = this;
		if($(this).attr("data-hasDetails")==1){
			createConfirmDialog("<span>Details:</span><textarea id='det'/>", function(){
				actionSubmit(_this, $("#det").val());
			});
		}else{
			actionSubmit(_this, "");
		}
	});
	
	function actionSubmit(_this, details){
		item = {};

		item["queueName"] = $("#queueName").text(); 

		item["messageId"]= [];

		var currentTab = $(_this).closest('div.tab-pane');
		var currentGroup = $(_this).closest('table');

		item["action"] = $(_this).val();
		item["reason"] = currentGroup.find("div#"+ item["action"]+" option").filter(":selected").val();		
		item["messageType"] =  currentTab.find('input:hidden[name="messageType"]').val();

		
		item["details"] = details;
			
		currentGroup.find("input.routeCheckbox:checked[type=checkbox]").each(function(){
			item["messageId"].push($(this).val());
		});

		if (item["messageId"].length >0) {

			$.ajax({
                type: "POST",
                url: "message-routing-jobs",
                data: {item: JSON.stringify(item)},  
                //dataType: "json",
                beforeSend: function(xhr){
    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
    	            					 $('#_csrf').attr("content"));
                },
                success: function (data) {
                	if(data=="Edit"){
                		document.location.href = "./Edit"
                	}else{
                		location.reload();
                	}
            		console.log('messasge routing jobs');
                	console.log(data);
                },
                error: function (xhr, textStatus, error){
                	console.log(xhr, textStatus, error)
                }
            });
        }  
	}

	$('button.groupAction').click(function(e) {		
		item = {};
		item["queueName"] = $("#queueName").text();		
		item["groupKeys"]= [];
		item["timeKeys"]=[];
		item["fieldValues"]=[];

		item["action"] = $(this).val();	

		var currentTab = $(this).closest('div.tab-pane');

		item["messageType"] =  currentTab.find('input:hidden[name="messageType"]').val();

		currentTab.find("input.batchCheckbox:checked[type=checkbox]").each(function(){
			 var groupKey = $(this).val();
			 item["groupKeys"].push(groupKey);
			 item["timeKeys"].push(groups[groupKey].timekey);
			 item["fieldValues"].push(groups[groupKey].values);
		});

		if (item["groupKeys"].length >0) {
            $.ajax({
                type: "POST",
                url: "group-routing-jobs",
                data: {item: JSON.stringify(item)},  
                dataType: "json",
                beforeSend: function(xhr){
    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
    	            					 $('#_csrf').attr("content"));
                },
                success: function (data) {
                    console.log('group routing jobs');
                	console.log(data);
                }
            });
        }  
	});
});
