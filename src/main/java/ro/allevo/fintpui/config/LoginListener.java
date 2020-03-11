package ro.allevo.fintpui.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.QueueServiceImpl;
import ro.allevo.fintpui.utils.PagedCollection;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private QueueServiceImpl queueService;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		request.getSession().setAttribute("isAuthentific", event.getAuthentication().isAuthenticated());
		request.getSession().setAttribute("userName", event.getAuthentication().getName());
		request.getSession().setAttribute("listReports", messageTypeService.getBusinessAreas());
		PagedCollection<Queue> transaction = queueService.getTransactions();
		if (null != transaction) {
			request.getSession().setAttribute("listTransactions", transaction.getItems());
		}
		if (request.getSession().getAttribute("banner") == null) {
			request.getSession().setAttribute("banner", "45");
		} else {
			request.getSession().setAttribute("banner", request.getSession().getAttribute("banner"));
		}
	}
	
}
