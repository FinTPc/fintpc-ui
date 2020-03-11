$(function() {
	$("#tableHeaders").hide();
	$("#transactionType").hide();

	var businessArea = new URL(window.location.href).searchParams
			.get("businessArea");
	/*
	 * $("#tableHeaders").show(); $("#closeHeadersButton").show();
	 * $("#transactionType").hide();
	 */

	// ?????
	if (businessArea != "Statements") {
		$("#transactionType").show();
		var messageTypes = $('#messageType');

		$.ajax({
			data : {
				businessArea : businessArea
			},
			method : 'GET',
			url : '../message-types',
			dataType : 'json',
			success : function(data) {
				messageTypes.children('option:not(:first)').remove();

				$.each(data, function(key, value) {
					messageTypes.append('<option value="' + value.friendlyName
							+ '">' + value.friendlyName + '</option>');
				});
			}
		});
	}

	$.ajax({
				data : {
					businessArea : businessArea
				},
				method : 'GET',
				url : '../message-criteria',
				dataType : 'json',
				success : function(data) {

					// var row = $('.filters-row');
					//				
					// var column1 = row.children('div :first');
					// var column2 = row.children('div :nth-child(2)');
					// column1.children().remove();
					// column2.children().remove();
					// var column = column1;
					//				
					// var length = data.length;
					// var i=0;

					$.each(
									data,
									function(key, value) {
										// if (i == Math.ceil(length/2))
										// column = column2;
										var masterDiv;
										var label = value.masterlabel.replace(
												" ", "-").toLowerCase();
										if ($('#' + label).length) {
											// div exist
											masterDiv = $('#' + label);
										} else {
											// div is not exist
											masterDiv = $('#masterlabel')
													.clone().prop('id', label)
													.removeAttr('style');
											masterDiv
													.children('div :first')
													.children()
													.children(':first')
													.prop('href',
															'#filter-' + label)
													.html(
															'<span style ="position: absolute;margin-left: 400px;" class="arrow down"></span>'
																	+ value.masterlabel);
											$('.panel-group').append(masterDiv);
										}
										masterDiv.children('div :last').prop(
												'id', 'filter-' + label)
												.children().append(
														renderFilter(value));
										// i++;
									});

					registerDateTime();
					$('form input[id="filter_insertdate_idate"]').prop('autocomplete', 'off');
					var params = JSON.parse($('input[name=params]').val());
					$('#filter_insertdate_idate').val(
							params['filter_insertdate_idate'][0]);
					registerValidations();
					registerUIControls();
				}
			});

	$.ajax({
		data : {
			businessArea : businessArea
		},
		method : 'GET',
		url : '../message-results',
		dataType : 'json',
		success : function(data) {
			var table = $('.results-row > table');

			var firstRow = table.find('tr:first');
			var secondRow = table.find('tr:nth-child(2)');

			firstRow.children().remove();
			secondRow.children().remove();

			$.each(data, function(key, value) {
				var id = "column_" + value.field;
				firstRow.append($('<td></td>').append(
								$('<label></label>').attr('for', id).text(
										value.label)));
				secondRow.append($('<td></td>').append(
						$('<input type="checkbox"></input>').attr('id', id)
								.attr('name', 'columns').addClass(
										'reportsHeader').val(value.field).prop(
										'checked', true)));
			});
		}
	});
	
	$.ajax({
		data: {
			'report': businessArea
		},
		method: 'GET', 
		url: '../getfilter',
		dataType: 'json',
		success: function(data){
			console.log(data);
			$.each(data, function(item, value){
				$("#"+item).val(value);
				console.log(value);
			});
		},
		
		complete: {
			
		}
	});

	$("#viewHeadersButton").click(function(e) {
		$("#toggle").toggle({
			direction : "down"
		});
		$(this).hide();
	});

	$("#closeHeadersButton").click(function(e) {
		$("#toggle").toggle({
			direction : "down"
		});
		$("#viewHeadersButton").show();
	});

	$("#saveFilter").click(function() {
		var map = {};
		map['report'] = businessArea;
		$("#messagesForm").each(function() {
			map = $('form').serializeArray();
//			var elements = $(this).find(':input');
//			getElement(elements);
//			$.each(elements, function(item, element) {
//				//if (element.id != undefined && element.id != "") {
//					map[element.id] = element.value;
//				//}
//			})
		});
		$.ajax({
			data: {
				'filters': map
			},
			method: 'GET',
			url: '../savefilter',
			dataType: 'json',
			success: function(data){
				
			},
			complete: {
				
			}
		});
		console.log(map);
	});

	$("#messagesForm").submit(
			function(e) {
				e.preventDefault();
				if ($('form').attr('action').includes('general/to', 0)) {
					exit();
				}
				var formParams = {};

				var tempParam = $('form').serializeArray();
				$.each(tempParam, function(key, value) {
					if (value.name in formParams) {
						formParams[value.name].push(value.value);
					} else {
						formParams[value.name] = [];
						formParams[value.name].push(value.value);
					}
				});

				$.ajax({
					data : {
						businessArea : businessArea
					},
					method : 'GET',
					url : '../message-criteria',
					dataType : 'json',
					success : function(data) {
						var searchCriteria = $('#dynamicSearch');
						var fields = {};

						// set report search criteria
						for ( var k in formParams) {
							var prop = "";
							if (k.startsWith("filter_"))
								prop = k.replace("filter_", "");
							if (prop != "") {
								prop = prop.split("_")[0];

								if (!fields.hasOwnProperty(prop))
									fields[prop] = [];
								if (formParams[k][0] != "")
									fields[prop].push(formParams[k]);
							}
						}
						searchCriteria.html('');
						var table = $('.datatable').DataTable();
						var columns = table.settings().init().columns;

						$.each(data, function(key, value) {
							if (fields.hasOwnProperty(value.field)
									&& fields[value.field] != "") {
								searchCriteria.append(' ' + value.label
										+ ': <b>' + fields[value.field]
										+ '</b>');
							}
						});
						delete formParams.params;
						formParams['businessArea'] = [ businessArea ];
						formParams['columns'] = [];
						$.each(columns, function(key, value) {
							if (key > 0) {
								formParams['columns'].push(value.name);
							}
						});
						initDatatable(columns, formParams, businessArea);
					}
				});
				closeNav();
				;
			});
});

function getElement(elements){
	$.each(elements, function(i, value){
		console.log(value);
	});
}

function renderFilter(json) {
	var div = $('<div></div>').addClass('form-group');
	var label = $('<label></label>').addClass('col-md-4 control-label').text(
			json.label);
	var controlDiv = $('<div></div>').addClass('col-md-8');
	div.append(label);
	div.append(controlDiv);

	var field = "";

	var control = renderControl(json);

	if (control != null) {

		// compute filter field
		switch (json.type) {
		case "string":
		case "date":
			field = "filter_" + json.field;
			break;
		case "dropdown":
		case "multiselect":
			field = "filter_" + json.field + "_exact";
			break;
		case "datetime,datetime":
		case "time":
		case "date,date":
			field = "filter_" + json.field + "_idate";
			break;
		}

		// extra
		if (json.type == "number,number") {
			var minfield = "filter_" + json.field + "_lnum";
			control.first().attr('id', minfield).attr('name', minfield);
			control.eq(1).attr('name', 'filter_' + json.field + '_unum');
		}

		label.attr("for", field);

		if (field != "")
			control.attr({
				'id' : field,
				'name' : field
			});

		control.addClass('form-control');

		controlDiv.append(control);
	}

	return div;
}

function initDatatable(columns, params, businessArea) {
	// datatable init
	if ($.fn.dataTable.isDataTable('.datatable')) {
		var table = $('.datatable').DataTable();
		table.destroy();
	}

	var t = $('.datatable')
			.DataTable(
					{
						processing : true,
						'serverSide' : true,
						'filter' : false,
						'columns' : columns,
						'order' : [],
						'ajax' : {
							url : 'page',
							type : 'POST',
							data : function(d) {
								$.each(d.columns,
												function(i, value) {
													d.columns[i]['type'] = (value.name == 'amountchar') ? 'amount': value.name;
													d.columns[i]['className'] = (value.data == 'amount') ? 'dt-right': '';
													d.columns[i]['name'] = "";
												});

								d["params"] = JSON.stringify(params);
							},
							beforeSend : function(xhr) {
								$('#loader').css("display", "block");
								xhr
										.setRequestHeader($('#_csrf_header')
												.attr("content"), $('#_csrf')
												.attr("content"));
							},
							complete : function() {
								$('#loader').css("display", "none");
							},
							error : function() {
								$('#loader').css("display", "none");
							},
						},
						'createdRow' : function(row, data, index) {
							$('th').removeClass('dt-right');
							var td = $('td', row).eq(0);
							td.html(t.page.len() * t.page.info().page + index + 1);
							td.append($('<span title="View"></span>').addClass("glyphicon glyphicon-chevron-right view").css('cursor', 'pointer')
											.click(function() {
														var id = data.correlationid;
														$('#loader').css("display","block");
														createDialog("../view-message?id="+ id + "&businessArea="
																		+ params.businessArea[0],"NOBUTTON", null,'Message details');
														

													}));
							if (businessArea == "Payments"){
								td.append($('<span title="Status"></span>').addClass("glyphicon glyphicon-info-sign").css('cursor', 'pointer')
										.click(function() {
														var id = data.correlationid;
														$('#loader').css("display","block");
														createDialog("../message-status?id="+ id + "&businessArea="
																		+ params.businessArea[0], "NOBUTTON",null, 'Status');
												}));
							}
							var tr = $('td', row);
						},

					});
}
