$(function() {
	var deleteMessage = undefined;

	$('#DeleteConsent').click(
			function() {
				deleteMessage = $$messages["consentEntity.confirm.delete"];
				if (deleteMessage !== undefined && confirm(deleteMessage)) {
					$
							.ajax(
									{
										type : 'POST',
										url : "./consent/" + $('#id').val()
												+ "/delete",
										beforeSend : function(xhr) {
											xhr.setRequestHeader($(
													'#_csrf_header').attr(
													"content"), $('#_csrf')
													.attr("content"));
										},
									}).success(function(body) {
								if (body == '200') {
									$('#consentId').val('');
									$('#consentExpir').val('');
								}
							});
				}
			})
	$('#UpdateConsent').click(function() {
		$.ajax({
			type : 'GET',
			url : "./consent/" + $('#id').val() + "/update",
		// beforeSend: function(xhr){
		// xhr.setRequestHeader($('#_csrf_header').attr("content"),
		// $('#_csrf').attr("content"));
		// },
		}).success(function(body) {
			var response = $.parseJSON(body);
			$('#consentId').val(response.consentEntity.consentId);
			$('#consentExpir').val(response.consentEntity.validUntil);
			console.log(response);
		});
	})

	/* delete token */

	$('#DeleteToken').click(function() {
		deleteMessage = $$messages["token.confirm.delete"];
		if (deleteMessage !== undefined && confirm(deleteMessage)) {
			$.ajax({
				type : 'GET',
				url : "./token/" + $('#id').val() + "/delete",
			}).success(function(body) {
				if (body == "200") {
					$('#token').val('');
					$('#tokenExpir').val('');
				}
			});
		}
	})
	$('#UpdateToken').click(function() {
		$.ajax({
			type : 'GET',
			url : "./get_redirect?id=" +  $('#id').val(),
		}).success(function(body) {
			console.log(body);
			window.open(body);
		});
	});
})
