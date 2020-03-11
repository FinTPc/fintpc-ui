$(function() {
	var accounts;
	$("#banks").on("change", function() {
		$('#accountsIntern').empty();
		$('#accountsExtern').empty();
		getLocalAccounts($(this).val());
	});
	
	checkboxClickEvent();
	$("#save").click(function() {
		$("input[name='intern']").each(function() {
			if ($(this).is(':checked')) {
				var iban = $(this)
						.parent()
						.next()
						.children()
						.text().trim();
				if (window.accounts.external[iban] != undefined) {
					window.accounts.internal[iban].resource = window.accounts.external[iban].resource;
				}
			}
		});
		save();
		return false;
		});
	})
	
	function checkboxClickEvent() {
		$(':checkbox[name="intern"]').click(function() {
			if ($(this).is(':checked')) {
				if (window.accounts.external[$(this).val().split(":")[0]] == undefined) {
					$(this).prop("checked", false);
				}
			}
		});
		$(':checkbox[name="extern"]').change(function() {
			if (this.checked) {
				$(this).prop("checked", false);
			} else {
				$(this).prop("checked", true);
			}
		});
	}
	
	function getLocalAccounts(bic) {
		var counts = '';
		$.ajax({
			type : "GET",
			url : "page/" + bic,
			beforeSend : function() {
				$("#loader").show();
			},
			success : function(data) {
				accounts = JSON.parse(data);
				console.log(accounts);
				addItems($("#accountsIntern"), accounts.internal,"intern");
				addItems($('#accountsExtern'), accounts.external,"extern");
			},
			error : function(err) {
				console.log(err);
			},
			complete : function() {
				$("#loader").hide();
				checkboxClickEvent();
				$("#save").show();
				$("#bic").val(bic);
			}
		});
	}
	
	function save(){
		var checkbox = $("#accountsIntern > li > :checkbox");
		var intern = new Array();
		var resource = new Array();
		$.each(checkbox, function(item, value){
			if (value.checked){
				intern.push(value.value);
				resource.push($("#" + value.value).val());
			}
		});
		$.ajax({
			type : "GET",
			url : "sync?intern=" + intern + "&resource=" + resource + "&bic=" + $("#bic").val(),
			beforeSend : function() {
				$("#loader").show();
				$("#save").hide();
			},
			success : function(data) {
				accounts = JSON.parse(data);
				console.log(accounts);
				addItems($("#accountsIntern"), accounts.internal, "intern");
				addItems($('#accountsExtern'), accounts.external, "extern");
			},
			error : function(err) {
				console.log(err);
			},
			complete : function() {
				$("#loader").hide();
				checkboxClickEvent();
				$("#save").show();
			}
		});
	}
	function addItems(addToElement, getValue, name) {
		var counts = "";
		addToElement.empty();
		$.each(getValue,function(item, value) {
			var clas = value.checked == "true" ? 'bg-light"'
					: 'lh-condensed"';
			counts += '<li class="list-group-item d-flex justify-content-between ' + clas + '>'
					+ '<input type="hidden" id="' + value.iban + '" value="' + value.resource +'" />'
					+ '<input type="checkbox" name="'
					+ name
					+ '" '
					+ (value.checked == 'true' ? "checked" : "")
					+ ' value="'
					+ value.iban
					+ '" />'
					+ '<div style="display:inline">&nbsp<h6 class="my-0" style="display:inline">'
					+ value.iban
					+ '&nbsp'
					+ '</h6></div></li>';
		});
		addToElement.append(counts);
	}
