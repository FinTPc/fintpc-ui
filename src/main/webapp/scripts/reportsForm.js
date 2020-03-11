$(function() {

	var businessAreaRadio = $("input:radio[name=businessArea]");
	$("#tableHeaders").hide();
	$("#transactionType").hide();
	businessAreaRadio.prop('checked', false);
	
	businessAreaRadio.change(function() {
		var businessArea = $('input[name=businessArea]:checked').val();
		$("#tableHeaders").show();
		$("#closeHeadersButton").show();
		$("#transactionType").hide();
		
		//?????
		if (businessArea != "Statements") {
			$("#transactionType").show();
			var messageTypes = $('#messageType');
			
			$.ajax({
				data : {
					businessArea : businessArea
				},
				method : 'GET',
				url : 'message-types',
				dataType : 'json',
				success : function(data) {
					messageTypes.children('option:not(:first)').remove();
					
					$.each(data, function(key, value) {
						messageTypes.append('<option value="' + value.friendlyName + '">' + value.friendlyName + '</option>');
					});
				}
			});
		}
		
		$.ajax({
			data : {
				businessArea : businessArea
			},
			method : 'GET',
			url : 'message-criteria',
			dataType : 'json',
			success : function(data) {
				var row = $('.filters-row');
				
				var column1 = row.children('div :first');
				var column2 = row.children('div :nth-child(2)');
				column1.children().remove();
				column2.children().remove();
				var column = column1;
				
				var length = data.length;
				var i=0;
				
				$.each(data, function(key, value){
					if (i == Math.ceil(length/2))
						column = column2;
					column.append(renderFilter(value));
					i++;
				});
				
				registerDateTime();
				registerValidations();
				registerUIControls();
			}
		});
		
		$.ajax({
			data : {
				businessArea : businessArea
			},
			method : 'GET',
			url : 'message-results',
			dataType : 'json',
			success : function(data) {
				var table = $('.results-row > table');
				
				var firstRow = table.find('tr:first');
				var secondRow = table.find('tr:nth-child(2)');
				
				firstRow.children().remove();
				secondRow.children().remove();
				
				$.each(data, function(key, value){
					//console.log(value);
					var id = "column_" + value.field;
					firstRow.append($('<td></td>')
								.append($('<label></label>')
										.attr('for', id)
										.text(value.label)
										)
								);
					secondRow.append($('<td></td>').append(
							$('<input type="checkbox"></input>')
								.attr('id', id)
								.attr('name', 'columns')
								.addClass('reportsHeader')
								.val(value.field)
								.prop('checked', true)
							)
					);
				});
			}
		});
	});
	
	$("#viewHeadersButton").click(function(e){
		$("#toggle").toggle( { direction: "down" });
		$(this).hide();
	});
	
	$("#closeHeadersButton").click(function(e){
		$("#toggle").toggle( { direction: "down" });
		$("#viewHeadersButton").show();
	});
	
	$("form").submit(function(e){
		if (!businessAreaRadio.is(':checked'))
			{
				e.preventDefault();
				alert($$messages["reports.search"]);
			}
	});
	
	$("form").on("reset", function() {
		//remove filters
		var row = $('.filters-row');
		$("#tableHeaders").hide();
		$("#closeHeadersButton").hide();
		$("#transactionType").hide();
		var column1 = row.children('div :first');
		var column2 = row.children('div :nth-child(2)');
		column1.children().remove();
		column2.children().remove();
		
		//remove result columns
		var table = $('.results-row > table');
		
		var firstRow = table.find('tr:first');
		var secondRow = table.find('tr:nth-child(2)');
		
		firstRow.children().remove();
		secondRow.children().remove();
	});
});



function renderFilter(json) {
	//console.log(json);
	var div = $('<div></div>').addClass('form-group');
	var label = $('<label></label>')
					.addClass('col-md-4 control-label')
					.text(json.label);
	var controlDiv = $('<div></div>').addClass('col-md-8');
	div.append(label);
	div.append(controlDiv);
	
	var field = "";
	
	var control = renderControl(json);
	
	if (control != null) {
		
		//compute filter field
		switch (json.type) {
		case "string" :
		case "date" :
			field = "filter_" + json.field;
			break;
		case "dropdown" :
		case "multiselect" :
			field = "filter_" + json.field + "_exact";
			break;
		case "datetime,datetime" :
		case "time":
		case "date,date" :
			field = "filter_" + json.field + "_idate";
			break;
		}
		
		//extra
		if (json.type == "number,number") {
			var minfield = "filter_" + json.field + "_lnum";
			control.first().attr('id', minfield)
					.attr('name', minfield);
			control.eq(1)
					.attr('name', 'filter_' + json.field + '_unum');
		}
		
		label.attr("for", field);
		
		if (field != "")
			control.attr({
				'id': field,
				'name': field
				});
		
		control.addClass('form-control');
		
		controlDiv.append(control);
	}
	
	return div;
}
