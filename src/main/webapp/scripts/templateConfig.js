/**
 * 
 */

$(function() {
	
	if (null != template && null != template.validationxsd){
		$('#loader').show();
		setInterval(function(){
			$('#loader').hide();
			$('#validationxsd').show();
			$('#options').show();
		}, 5000);
	}
		
	createEntity();

	$.each(fields, function(item, field){
		var element = $('form').find(`[data-xsd2html2xml-xpath='${field.fieldxpath}']`);
		if (field.txtemplatesconfigoption == null)
		{
			var input = element.find('input');
			input.val(field.fieldvalue);
			input.attr('pattern', field.pattern);
			input.attr('required', field.mandatory);
			input.attr("config-id", field.id);
		}else{
			createDiv(element, field);
		}
		showField(field.fieldxpath, 0);
	});
	
	$("select").change(function() {
		var id = $(this).val();
		if (id > 0) {
			window.location = "./templates-config/" + id;
		}
	});

	$(".draggable").draggable({
		helper : 'clone',
	});

	$(".draggable1").draggable({
		helper : 'clone',
	});

	$("label, section").css({
		border : "1px solid pink"
	});
	
	$(".add").click(function(){
		addDroppable();
	});
	
	addDroppable();
		
	$("object,div").droppable({
		greedy : true,
		drop : function(event, ui) {
			var id = $(ui.draggable).attr("id");
			if (id == "remove") {
				ui.draggable.detach()
				ui = null;
			} else {
				ui.draggable.detach().css({
					left : 0,
					top : 0,
					width : "80px",
					display: "block",
					float: "none"
				}).appendTo($(this));
			}
		}
	});
		
	function showField(path, index){
		var element = $("form").find(`[data-xsd2html2xml-xpath='${path}']`);
		var show = element.attr('display') || element.attr('hidden');
		if (show == 'hidden' || show == undefined){
			if (0 != index)
				element.removeAttr('hidden');
			path = path.slice(0, path.lastIndexOf('/'));
			if (path.length>0)
				showField(path, 1);
		}
	}
		
	function createEntity(){
		if (template != null && template.type != null){
			var element = $('form').find(`[data-xsd2html2xml-xpath='${template.type}']`);
			var div = $('#options').find("[data-id=-1]");
			div.css({
				left : 0,
				top : 0,
				width : "80px",
				display: "block",
				float: "none"
			})
			div.appendTo($(element).parent()[0]);
			showField(template.type, 0);
		}
	};
		
	function createDiv(el, field){

		var div = $('#options').find(`[data-id='${field.txtemplatesconfigoption.id}']`).clone();
		div.attr("config-id", field.id);
		el = el.parent()[0];
		setAttributes(el, div);
		if (field.editable){
			var p = $('<p data-id="optional" style="border: 1px solid green;margin-right: 5px;cursor: grab;margin-bottom: 10px;margin-top: 10px;" id="optional" data-attr="edit" class="draggable"data-caption="edit"> Edit </p>');
			p.appendTo(el).css({left:0,top:0,width:"80px"});
		}
		var label = field.fieldlabel;
		var name = field.txtemplatesconfigoption.name;
		if (null != label && label.localeCompare(name) == 0){
			label = "";
		}
		div.attr("data-label", label);
		div.html(name + " (" + label + ")");
	}

	function setAttributes(parent, el){
		el.attr("id", "remove");
		//var data = el.attr('data-attr').split('.');
		//el.attr('data-attr', data[data.length-1]);
		el.css({
			left : 0,
			top : 0,
			width : "80px",
			display: "block",
			float: "none"
		}).appendTo(parent).dblclick(function() {
			var name =el.attr("data-caption");
			var label =el.attr("data-label")!=undefined?el.attr("data-label"):"";
			label = prompt("Label", label);
			label = (label == null?'':label);
			el.attr("data-label", label);
			el.html(name + " (" + label + ")");
		});
	}

	function addDroppable(){
		$("label, section").droppable({
			greedy : true,
			drop : function(event, ui) {
				var id = $(ui.draggable).attr("id");
				var data_id = $(ui.draggable).attr("data-id");
				if (data_id == -1){
					ui.draggable.detach().css({
						left : 0,
						top : 0,
						width : "80px",
						display: "block",
						float: "none"
					}).appendTo($(this));
				}else if (data_id > 0 || (id == "optional" || id == "mandatory")) {
					var el = $(ui.draggable.clone()).draggable();
					setAttributes($(this), el);
				}
			}
		});
	}

	function fieldEdit(element){
		var edit = $(element).children('p');
		if (edit.attr('data-attr') == 'edit'){
			return true;
		}
		return false;
	}

	$("#save").click(function(){
		var listObject = [];
		var txtemplatesconfig = template;
		txtemplatesconfig.validationxsd = null;
		$("form").each(function(){
			var elements = $(this).find("div");
			var path;
			
			$.each(elements, function(item, element){
				var  object = {};
				var id = element.attributes["data-id"].value;
				if ($(element).parent().attr("data-xsd2html2xml-xpath")!= undefined){
					path = $(element).parent().attr("data-xsd2html2xml-xpath");
					object.editable = fieldEdit($(element).parent());
				}else{
					path = $(element).parent().children().attr("data-xsd2html2xml-xpath");
					object.editable = fieldEdit($(element).parent());
				}
				if (id > -1){
					object['fieldxpath'] = path;
					object['mandatory'] = false;
					object['fieldvisibility'] = 1;
					object['txtemplatesconfigoption']={'id': element.attributes["data-id"].value};
					object['fieldlabel'] = (element.attributes['data-label']==undefined || element.attributes['data-label'].value=='')?element.attributes["data-caption"].value:element.attributes['data-label'].value;
					object['txtemplatesconfig'] = txtemplatesconfig;
					//object['fieldtype'] = element.attributes["data-attr"].value;
					object['id'] = element.attributes['config-id']!=undefined?element.attributes['config-id'].value:null;
					//if (id >= 13){
						object['pattern'] = $(element).parent().find('input').attr('pattern');
					//}
					listObject.push(object);
				}else{
					txtemplatesconfig['type'] = path;
				}
			});
			elements = $(this).find("input");
			$.each(elements, function(item, element){
				var  object = {};
				var path = $(element).parent().attr("data-xsd2html2xml-xpath");
				if (element.value != "" && path != undefined){
					object['fieldxpath'] = path;
					object['mandatory'] = (element.required?true:false);
					object['fieldvalue'] = element.value;
					object['pattern'] = (element.pattern?element.pattern:null);
					object['fieldvisibility'] = 1;
					object['fieldtype'] = element.type;// == 'text'?"input":'other');
					object['txtemplatesconfig'] = txtemplatesconfig;
					object['id'] = element.attributes['config-id']!=undefined?element.attributes['config-id'].value:null;
					if (element.type=="text" || (element.type=="radio" && element.checked) || (element.type=="checkbox" && element.selected == "selected")){
						listObject.push(object);
					}
				}
			});
		});
		$.ajax({
			method: 'POST',
			dataType: 'json',
			url: "save",
			data: {detailed: JSON.stringify(listObject), config: JSON.stringify(txtemplatesconfig)},
			beforeSend: function(xhr){
				xhr.setRequestHeader($('#_csrf_header').attr("content"),
				$('#_csrf').attr("content"));
				$('#loader').css("display", "block");
			},
			success: function(data){
				window.location = "/fintp_ui/templates-config";
			},
			complete: function(){
				$('#loader').css("display", "none");
			}
		});
	});
});