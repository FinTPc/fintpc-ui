$(function(){
	
	//init sorted row, in conformation with request received
	$(".sortable").each(function(){
		var currentField = $("#orderField").val();
		var selectedField = $(this).find("input").attr("name");
		if(currentField == selectedField){
			$(this).find("input").val($("#order").val());
			$(this).addClass("selectedSort");
		}
	});
	
	$(".sortable").click(function(){
		var selectedOrder = $(this).find("input").val();
		var selectedField = $(this).find("input").attr("name");
		var newOrder;
		
		//if current header already selected, change order
		if(selectedOrder == "" || selectedOrder == "desc"){
			newOrder = "asc";
		}else{
			newOrder = "desc";
		}
		$("#order").val(newOrder);
		$("#orderField").val(selectedField);
		$("#mainForm").submit();
	});
});