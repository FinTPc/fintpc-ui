//number

$(function(){
	registerValidations();
});

function registerValidations() {
	registerAmountValidation();
	registerIBANValidation();
}

function registerAmountValidation() {
	$('.form-amount').numeric({
		decimalPlaces: 2,
		negative: false,
		decimal: '.'
		});
}


//iban
function registerIBANValidation() {
	$("input#iban").on(//should be on class
		'keyup change',
		function() {
			var iban = $('#iban').val();			
			$("#bic").empty();
			control = $('#bic').append('<option></option>');
			
			if (iban.length > 7) {				
				if (iban.substr(0, 2) == 'RO') {
					$.each(bics, function(key, value) {
						if (value.bic.substr(0, 4) == iban.substr(4, 4)) {
							control.append('<option value="' + value.bic + '">'
									+ value.name + '</option>');													
						}
					});
				}
			 else
				$.each(bics, function(key, value) {
					control.append('<option value="' + value.bic + '">' + value.name
							+ '</option>');
				})
			} else
				$.each(bics, function(key, value) {
					control.append('<option value="' + value.bic + '">' + value.name
							+ '</option>');
				});

			$("#bic").append(control);
		});
}

function accountValidation(bics) {
	control = $('#bic').append('<option></option>');
	$.each(bics, function(key, value) {
		// array
		control.append('<option value="' + value + '">' + value + '</option>');
	});

	$("#bic").append(control);
}

function selectBic(bic){
	var iban = $('#iban').val();		
	control = $('#bic').append('<option></option>');
	$.each(bics, function(key, value) {
		if (value.substr(0, 4) == iban.substr(4, 4)) 
			if (value == bic) {
				control.append('<option value="' + value + '" selected>'
						+ value + '</option>');								
			}
			else
				control.append('<option value="' + value + '">'
						+ value + '</option>');
	
	});
}