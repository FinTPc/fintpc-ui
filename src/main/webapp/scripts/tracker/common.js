function hideResult(container) {
	container.hide();
	container.find('span').text("");
}

function showResultList(template, list) {
	$.each(list, function(i, item){
		var container = template.clone();
		
		//console.log(container);
		//console.log(template);
		
		container.removeClass('dynamicTemplate').addClass('dynamicResult');
		container.insertAfter(template.last());
		
		//console.log(container);
		//console.log(item);
		
		showResult(container, item);
	});
}

function showResult(container, values) {
	$.each(values, function(key, value){
		//console.log(key + "=" + value);
		if (typeof value == 'object' && !Array.isArray(value)) {
			//console.log(key);
			$.each(value, function(inner, innerValue) {
				setReportValue(container, 'val' + toFirstUpperCase(key) + toFirstUpperCase(inner), innerValue);
			});
		}
		else
			setReportValue(container, 'val' + toFirstUpperCase(key), value);
	});
	
	container.show();
}

function setReportValue(container, id, value) {
	container.find('span[data-id="' + id + '"]').text(value);
	container.find('input[data-id="' + id + '"]').val(value);
}

//display intervals in a new dialog
function registerShowIntervals() {
	$('.showIntervals').click(function(e) {
		e.preventDefault();
		
		var intervals = $(this).parent().find('input:hidden').val();
		
		var listIntervals = $('<ul class="list-group" style="max-height: 300px; overflow-y: scroll;">');
		
		$.each(intervals.split(','), function(i, item){
			listIntervals.append($('<li class="list-group-item">' + item + '</li>'));
		});
		
		createSimpleDialog(listIntervals);
	});
}

//util
function toFirstUpperCase(str) {
	//console.log(str);
	return str.charAt(0).toUpperCase() + str.substr(1);
}