/**
 * 
 */
$(function(){
		var fields = [];
		
		var id = $('#id').val();
						
		$.each(template.txtemplatesdetaileds, function(item, detailed){
			var templateDetailed = detailed.txtemplatesconfigdetailed;
			if (null != templateDetailed){
				templateDetailed.value = detailed.value;
				templateDetailed.iddetailed = detailed.id;
				fields.push(templateDetailed);
			}
		})
		drawFields(fields);
		if(template.type==1){
			$("#xpathsArea").toggle();
			$('#multiselect').multiselect();
			getTemplates = "/groups";
		}else{
			$("#typeMultiple").toggle();
		}	
		
		$("input[type=radio]").change(function(){
			$("#typeMultiple").toggle();
			$("#xpathsArea").toggle();
			getTemplates = "/fields";
			if($(this).val()=="1"){
				getTemplates = "/groups";
				
				$('#multiselect').multiselect();
			}
			var id = ($("#messageType").val()==''?0:$("#messageType").val());
			loadTemplateData(id);
		});
		
		$("#messageType").change(function(){
			if($(this).val()!=""){
				loadTemplateData($(this).val())
			}
		})
		
		function loadTemplateData(id){
			$("#xpathsArea").empty();
			$("#multiselect").empty();
			$("#multiselect_to").empty();
			$.ajax({
                type: "GET",
                url: "templates/" + id + getTemplates, 
                dataType: "json",
                beforeSned: function(){
                	
                },
                success: function (data) {
                	if (getTemplates == '/groups'){
                		$.each(data, function(item, value){
                			$('#multiselect').append('<option value="'+value.id+'" >'+value.name+'</option>');	
                		});
                	}else{
	                	drawFields(data.fields);
	                	$$dropDowns[2] = [];
	                	$$dropDowns[2] = (data.internal);
	                	reWriteValue(fields);
                	}
                },
                complete: function(){
                	
                }
            });
		}
		
		function drawFields(data){
			$.each(data, function(item, configDetailed){
    			if(null != configDetailed.txtemplatesconfigoption){
        			var optionId = configDetailed.txtemplatesconfigoption.id;
        			var edit;
        			var dataSource = configDetailed.txtemplatesconfigoption.datasource;
        			configDetailed.value = configDetailed.value==undefined?"":configDetailed.value;
        			var attribute = {};
        			attribute.name = optionId;
        			attribute["data-id"] = configDetailed.id;
        			attribute.pattern = configDetailed.pattern;
        			attribute.value = configDetailed.value;
        			attribute.id = configDetailed.iddetailed==undefined?'null':configDetailed.iddetailed;
        			switch(dataSource){
        				case 'timestamp':edit = $("<input type='text' class='form-control dateTimePicker'></input>");break;
        				case 'date':edit = $("<input type='text' class='form-control datePicker'></input>");break;
        				case 'sequency': break;//edit = $("<input type='text'></input>");break;
        				case 'fixvalue':edit = $("<input type='text' class='form-control' ></input>");break;
        				case 'editvalue':edit = $("<input type='text' class='form-control' ></input>");break;
        				default: edit = $("<select style='width:auto;' class='form-control' ></select>");
        						 for(var i=0;i<$$dropDowns[optionId].length;i++){
        							 if ($$dropDowns[optionId][i] == attribute.value){
        								 edit.append("<option selected>"+$$dropDowns[optionId][i]+"</option>");
        							 }else{
        								 edit.append("<option >"+$$dropDowns[optionId][i]+"</option>");
        							 }
        						 } break;
        			}
        			if (edit != undefined){
	        			for(key in attribute){
	        				edit.attr(key, attribute[key]);
	        			}
	        			var div = $("<div style='margin-bottom: 5px;display:flex;'></div").append("<div style='float: left;width: 50%;'><span>"+configDetailed.fieldlabel+"</span></div>").append($('<div></div>').append(edit));
	        			if (dataSource != 'sequency'){
	        				$("#xpathsArea").append(div)
	        			}
	    			}
        		}
    		});
			registerDateTime();
		}
		
		function reWriteValue(data){
			$.each(data, function(item, value){
				$("input[data-id='"+ value.id +"']").val(value.value);
				$('select[data-id="'+ value.id +'"]').children().each(function(){
					if ($(this).val() == value.value){
						$(this).attr('selected');
					}
				});
			});
		}
	});