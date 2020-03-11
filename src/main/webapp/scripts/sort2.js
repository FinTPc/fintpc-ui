$(function(){
	
	//init sorted header, in conformation with request received
	$(".sortable").each(function(){
		var currentField = 
			$(this).parent().parent().parent().parent().find("input[name='sortField']");
		var currentOrder = 
			$(this).parent().parent().parent().parent().find("input[name='sortOrder']");
		var selectedField = $(this).find("input[name='field']");
	
		if(currentField == selectedField){
			$(this).find("input[name='sortOrder']").val(currentOrder.val());
			$(this).addClass("selectedSort");
		}
	});
	
	$(".sortable").click(function(){
		var selectedOrder = $(this).find("input[name='sortOrder']").val();
		var selectedField = $(this).find("input[name='field']").val();
		var newOrder;
		
		$(".sortable").each(function(){
			$(this).attr("class", "sortable");
			$(this).find("span.sortable-icon").attr("class", "sortable-icon");
		});
		
		
		
		//if current header already selected, change order
		if(selectedOrder == "" || selectedOrder == "descending"){
			newOrder = "ascending";
			$(this).addClass("ascending");
			$(this).find("span[class='sortable-icon']").addClass("sortable-icon-ascending");
		}else{
			newOrder = "descending";
			$(this).addClass("descending");
			
			$(this).find("span[class='sortable-icon']").addClass("sortable-icon-descending");
		}
		
		$(this).parent().parent().parent().parent().find("input[name='sortField']").val(selectedField);
		$(this).parent().parent().parent().parent().find("input[name='sortOrder']").val(newOrder);
		loadTable($(this).parent().parent().parent(), false);
	});
});