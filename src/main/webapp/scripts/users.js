

function toggleUserLine(tr, state) {
	//var tr = tdUsername.closest('tr');
	var trs = tr.closest('table').find('tr'); 
	
	trs.not(tr).removeClass("selected");
	trs.find('.viewMode').removeClass("invisible");
	trs.find('.editMode').toggleClass("invisible", true);
	
	tr.toggleClass("selected", state);
	
	//state==true -> edit mode
	if (state) {
		tr.find('.viewMode').toggleClass('invisible', true);
		tr.find('.editMode').toggleClass('invisible', false);
	}
	
	return tr.hasClass("selected");
}

//state = true -> edit mode
//state = false -> view mode
function selectLine(tr, username, state) {
	var selected = toggleUserLine(tr, state);
	
	handleSelectedUser(username, selected);
	
	//handle role checkboxes
	if (typeof state !== 'undefined')
		$('.checkRole').prop('disabled', !state);
}

function handleSelectedUser(username, selected) {
	//uncheck 
	$('.checkRole').prop('checked', false);
	
	//disable if not in edit mode
	
	$('.checkRole').prop('disabled', true);
	
	if (selected)
		getUserRoles(username);
}

function getUserRoles(username) {
	$.ajax({
		type: 'GET',
		url: 'users/' + username + '/roles',
		success: function(data) {
			var json = $.parseJSON(data);
			//select checkboxes by roleid
			$.each(json, function(i, item) {
				var action = item.action;
				var roleid = item.roleId;
				
			    var cb = $(".checkRole[value='" + action + "'][data-roleid='" + roleid + "']");
			    
			    if (cb.length) {
				    cb.prop('checked', true);
				    cb.change();
			    }
			});
		}
	});
}

$(function() {
	
//	$("#addUser").click(function(){
//		createDialog('./users/add-user.htm', $$actionCreate);
//	});

	$("#addRole").click(function(){
		createDialog('users/roles/add', $$actionCreate);
	});
	
	$('#sync').click(function() {
		var div = $('<div>');
		var form = $('<form>').attr({
			'action': 'users/sync',
			'method': 'post'
		});
		
		//selected server side when executing sync
		var importing = $('<select>').attr({
			'multiple': 'multiple',
			'disabled': 'disabled'
			});
		
		var removing = $('<select>').attr({
			'multiple': 'multiple',
			'disabled': 'disabled'
			});
		
		form.append(importingUsers);
		form.append(importing);
		form.append(removingUsers);
		form.append(removing);
		div.append(form);
		
		//get inserting users
		$.ajax({
			type: 'GET',
			url: 'users/sync/provider',
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			success: function(data) {
				var json = data;
				
				$.each(json, function(i, user) {
					importing.append($('<option>', {
						value: JSON.stringify({
							username: user.username,
							email: user.email
							}),
						text: user.username,
						selected: 'selected'
					}));
				});
			}
		});
		
		//get removing users
		$.ajax({
			type: 'GET',
			url: 'users/sync/deleted',
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			success: function(data) {
				var json = data;
				
				$.each(json, function(i, user) {
					removing.append($('<option>', {
						value: user.id,
						text: user.username,
						selected: 'selected'
					}));
				});
			}
		});
		
		openDialog(div, $$actionOk);
	});
	
	
	$("button.editUserRole").each(function(){
		$(this).append($('<span class="glyphicon glyphicon-tag">'));
		$(this).removeClass("editUserRole");
		
		$(this).click(function(){
			//get username
			var tdUsername = $(this).closest('tr');			
			var username = $(this).attr('data-username');
			
			selectLine(tdUsername, username, true);
		});
	});
	
	$("button.cancelUserRole").each(function(){
		$(this).append($('<span class="glyphicon glyphicon-remove">'));
		$(this).removeClass("cancelUserRole");
		
		$(this).click(function(){
			//get tr
			var tr = $(this).closest('tr');
			
			selectLine(tr, null, false);
		});
	});
	
	$("button.saveUserRole").each(function(){
		$(this).append($('<span class="glyphicon glyphicon-floppy-disk">'));
		$(this).removeClass("saveUserRole");
		$(this).click(function(){
			var user = $(this).data("username");
			var tr = $(this).closest('tr');
			
			var jsonRoles = [];
			
			$('.checkRole:checked').each(function(){
				var action = $(this).val();
				var roleid = $(this).data('roleid');
				
				jsonRoles.push({
					"roleId": roleid,
					"action": action
				});
			});
			
			//console.log(jsonRoles);
			
			$.ajax({
				type: 'POST',
				url: 'users/' + user + '/update-roles',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				data: JSON.stringify(jsonRoles),
				beforeSend: function(xhr){
					var csrfToken = $('#_csrf').attr("content");
					var csrfHeader = $('#_csrf_header').attr("content");
		            xhr.setRequestHeader(csrfHeader, csrfToken);
			    },
				success: function() {
					selectLine(tr, null, false);
				}
			});
		});
	});
	
	$("button.edit").each(function(){
		$(this).append($('<span class="glyphicon glyphicon-pencil">'));
		$(this).removeClass("edit");
	});
	
	$("button.editUser").each(function(){
		$(this).removeClass("editUser");
		$(this).click(function(){
			var user = $(this).data("username");
			
			createDialog('users/' + user + '/edit', $$actionUpdate);
		});
	});
	
	$("button.editRole").each(function(){
		$(this).removeClass("editRole");
		$(this).click(function(){
			var roleid = $(this).data("roleid");
			
			createDialog('users/roles/' + roleid + '/edit', $$actionUpdate);
		});
	});
	
	$("button.delete").each(function(){
		$(this).append($('<span class="glyphicon glyphicon-trash">'));
		$(this).removeClass("delete");
	});
	
	$("button.deleteUser").each(function(){
		$(this).removeClass("deleteUser");
		$(this).click(function(){
			var user = $(this).data("username");
			var userid = $(this).data("userid");
			
			var msg = "Are you sure you want to delete user: '"+ user +"' ?"
			var url = "users/" + userid + "/delete";
			
			confirmDelete(msg, url);
		});
		
	});
	
	$("button.deleteRole").each(function(){
		$(this).removeClass("deleteRole");
		$(this).click(function(){
			var roleid = $(this).data("roleid");
			
			var msg = "Are you sure you want to delete this role?"
			var url = "users/roles/" + roleid + "/delete";
			
			confirmDelete(msg, url);
		});
		
	});
	
	
	$("#userTable tr td:nth-child(1)").on("click", function() {
		var tr = $(this).parent()
		var username = $(this).html();

		selectLine(tr, username);
	});
	
	$(".toggle").each(function(){
		//apply for 2 or more
		if ($(this).find('li').length <= 1)
			return;
		
		var header = $(this).find('li').first();
		
		header.css('margin-left', '0px');
		
		$(this).find('li').not(header).hide();
		
		var collapse = 'glyphicon-menu-down';
		var expand = 'glyphicon-menu-right';
		
		var handle = $('<span class="glyphicon"></span>').addClass(expand);
		
		handle.click(function(){
			$(this).toggleClass(expand + ' ' + collapse);
		});

		 
		
		header.prepend(handle);
		header.click(function(){
			$(this).siblings().not($(this)).toggle();
			$(this).find('span').toggleClass(expand + ' ' + collapse);
		});
		
	});
	
	$('.checkRoleView').change(function() {
		var checked = $(this).prop('checked');
		
		var cbView = $(this).closest('tr').find("input[type='checkbox'][value='view']");
		
		//view is optional
		if (cbView.length) {
			if (checked) {
				cbView.prop('checked', checked);
				cbView.prop('disabled', checked);
			}
			else {
				var unchecked = $(this).closest('tr').find("input:checked[type='checkbox'][value!='view']").length;
				
				cbView.prop('checked', unchecked > 0);
				cbView.prop('disabled', unchecked > 0);
			}
		}
	});
	
});
