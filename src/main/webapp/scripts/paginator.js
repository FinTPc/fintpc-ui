var numberOfItemsPerPage = 100;
var total;
var currentPage;

$(function(){
	loadPaginator();
	$('#itemsPerPage').val($("#limit").val());
	$("#itemsPerPage").on("change", function(){
//		currentPage = $('.pui-paginator-element.ui-state-active').text();
//		computeOffset();
		$("#limit").val($(this).val());
		$("#offset").val(0);
		$("#mainForm").submit();
    });
	
});

function loadPaginator(){
	$('#paginator').puipaginator({  
		totalRecords: $("#total").text() * 1,  
		rows: $("#limit").val() * 1,
        page: ($("#offset").val() /$("#limit").val()),
        paginate: function(event, state) {  
        	currentPage = $('.pui-paginator-element.ui-state-active').text();
        	computeOffset();
        	$("#offset").val(offset);
    		$("#mainForm").submit();
        	//here load properly
        }  
    });
}

function computeOffset(){
	offset = (currentPage - 1) * $("#limit").val();
}