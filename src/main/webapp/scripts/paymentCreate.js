/**
 * 
 */
	var savePoint = 0;
	function diminution(el, count){
		for(var i=0;i<count;i++){
			el.children().last().remove();
		}
	}
	
	function multipliction(el, count){
		var mult = el.children().last().children();
		var title = (mult.text()).replace(" ", "");
		for(var i=0;i<count;i++){
			var newElement = mult.clone();
			var newTitle = title + (i+1);
			newElement.text(newTitle);
			var href = '#'+newTitle;
			newElement.attr('href', href);
			var li = $('<li></li>').append(newElement);
			el.append(li);
			var div = $('#'+title).clone().attr('id',newTitle);
			div.removeClass('active in');
			$('#tabContent').append(div);
		}
	}
	
	function hideAll(){
		$('#div_sample').hide();
		$('#div_template').hide();
		$('#container').hide();
		$('#no').hide();
	}
	
	function getSimple(id){
		$.ajax({
            type: "GET",
            url: "payment-create/" + id + '/config', 
             dataType: "json",
            beforeSend: function(){
            	$('#loader').show();
            },
            success: function (data) {
            	$('#container').show();
            	$('#ulTab').empty();
            	$('#tabContent').empty();
            	$('#ulTab').append('<li class="active"><a data-toggle="pill" href="#'+data.messagetype+'">'+data.messagetype+'</a></li>');
            	var divContent = $('#tabContent');
            	var bloc = $('<div id="'+data.messagetype+'" class="tab-pane fade in active"></div>');
            	var width = screen.width / 3;
            	$.each(data.txtemplatesconfigdetaileds, function(item, txtemplatesconfigdetaileds){
        			var element = createElement(txtemplatesconfigdetaileds, "", txtemplatesconfigdetaileds.txtemplatesconfigoption);
        			if (element != undefined){
	        			div = $('<div style="margin-bottom:5px;width:'+width+'px;"></div>').append('<div style="display:inline-block;width:25%">'+txtemplatesconfigdetaileds.fieldlabel+'</div>').append(element);
	        			bloc.append(div);
	        			divContent.append(bloc);
        			}
            	})
            	registerDateTime();
            },
            complete: function(){
            	$('#loader').hide();
            }
        })
	}
	
	function createElement(element, value, options){
			var input;
			var attribute = {};
			attribute.name = element.fieldxpath ;
			attribute.data_id = element.id ;
			attribute.pattern = element.pattern ;
			if(''==value){
				value = element.fieldvalue;
			}
			attribute.value = value;
			var optionId = -1;
			var dataSource = element.fieldtype; 
				if (null != options){
					dataSource = options.datasource;
					optionId = options.id;
				}
			
			switch(dataSource){
			case 'timestamp':break;//input = $("<input type='text' class='dateTimePicker'></input>");break;
			case 'date':break;//input = $("<input type='text' class='datePicker'></input>");break;
			case 'sequency': break;//input = $("<input type='text'></input>");break;
			case 'fixvalue':input = $("<input type='text'></input>");break;
			case 'editvalue':input = $("<input type='text'></input>");break;
			case 'text':input = $("<input type='text'></input>");break;
			default: input = $("<select style='width:auto;'></select>");
 					 for(var i=0;i<$$dropDowns[optionId].length;i++){
 						if ($$dropDowns[optionId][i] == value){
 							input.append("<option selected>"+$$dropDowns[optionId][i]+"</option>");
						 }else{
							 input.append("<option >"+$$dropDowns[optionId][i]+"</option>");
						 }
 					 } break;
			}
			if (input != undefined){
				for(key in attribute){
					input.attr(key, attribute[key]);
				}
				input.attr('disabled', true);
				if (null != options && options.datasource == 'editvalue'){
					input.removeAttr('disabled');
				}
			}
			return input;
	}
	
	function getTemplate(id){
		$.ajax({
            type: "GET",
            url: "payment-create/" + id + '/template', 
             dataType: "json",
            beforeSend: function(){
            	$('#loader').show();
            },
            success: function (data) {
            	$('#container').show();
            	$('#ulTab').empty();
            	$('#tabContent').empty();
            	var width = screen.width / 3;
            	$.each(data, function(item, detail){
            		var activ = ""
            		if(item == 0){
	            		activ = "in active";
	            	}
            		$('#ulTab').append('<li class="'+activ+'"><a data-toggle="pill" href="#'+detail.name.replace(" ", "")+'">'+detail.name+'</a></li>');
	            	var divContent = $('#tabContent');
	            	var bloc = $('<div id="'+detail.name.replace(" ", "")+'" class="tab-pane fade '+ activ+'"></div>');
	            	$.each(detail.txtemplatesdetaileds, function(item, obj){            		
	            		var element = createElement(obj.txtemplatesconfigdetailed, obj.value, obj.txtemplatesconfigdetailed.txtemplatesconfigoption);
	            		if (element != undefined){
		            		div = $('<div style="margin-bottom:5px;width:'+width+'px;"></div>').append('<div style="display:inline-block;width:25%">'+obj.txtemplatesconfigdetailed.fieldlabel+'</div>').append(element);
		            		bloc.append(div);
	            		}
	            	})
	            	divContent.append(bloc);	
            	});
            	
            	
            },
            complete: function(){
            	$('#loader').hide();
            }
        })
	}
	
	function paymentSimple(){
		$.ajax({
            type: "GET",
            url: "payment-create/templates", 
            dataType: "json",
            beforeSend: function(){
            	$('#loader').show();
            },
            success: function (data) {
            	$('#templates').empty();
            	$('#templates').append('<option></option>');
            	$.each(data, function(item, value){
            		$('#templates').append('<option value="'+value.id+'">' + value.messagetype + '</option>');	
            	})
            	$('#div_template').show();
            },
            complete: function(){
            	$('#loader').hide();
            }
        });
	}
	
	function paymentComplex(id){
		$.ajax({
            type: "GET",
            url: "payment-create/" + id + "/sample", 
            dataType: "json",
            beforeSend: function(){
            	$('#loader').show();
            },
            success: function (data) {
            	$('#templates').empty();
            	$('#templates').append('<option></option>');
            	$.each(data, function(item, value){
            		$('#templates').append('<option value="'+value.id+'">'+value.name+'</option>');	
            	})
            	$('#div_template').show();
            },
            complete: function(){
            	$('#loader').hide();
            }
        });
	}
	
	function savePayload(xmlObject){
		$.ajax({
            type: "GET",
            data: {payload: JSON.stringify(xmlObject)},
            url: "payment-create/create-payload", 
            dataType: "json",
            beforeSend: function(){
            	$('#loader').show();
            },
            success: function (data) {
            	window.savePoint--;
            	if(window.savePoint == 0){
            		$("payments").val(0);
            	}
            },
            complete: function(){
            	window.savePoint--;
            	if(window.savePoint == 0){
            		location.reload();
            	}
            	$('#loader').hide();
            }
        });
	}
	
	$(function(){
		
		$('#createPayment').click(function(){
			
			$('#tabContent > div').each(function(){
				$(this).each(function(){
					var xml = [];
					var id = $(this).attr('id');
					$('#' + id + ' :input[type=text]').each(function(){
						var info = {};
						info.fieldxpath = $(this).attr('name');
						info.fieldvalue = $(this).attr('value');
						xml.push(info);
					})
					$('#' + id).find('select').each(function(){
						var info = {};
						info.fieldxpath = $(this).attr('name');
						info.fieldvalue = $(this).val();
						xml.push(info);
					})
					savePayload(xml);
					window.savePoint++;
				})
			})
			
		});
	})
	