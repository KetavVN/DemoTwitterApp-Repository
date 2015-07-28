package com.redis.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.redis.test.domain.User;
import com.redis.test.service.UserService;

/**
 * Handles requests for the application home page.
 * 
 * @author ketav
 */

@Controller
@SessionAttributes(value="userName")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value={"/home", "/welcome"}, method=RequestMethod.GET)
	public String displayWelcomePage(Model model, HttpSession session){
		
		String userName = (String)session.getAttribute("userName");
		
		if(userName==null){
			model.addAttribute("user", new User());
			return "home";
		}
		
		User user = userService.findUser(userName);
		
		if(user == null || user.getUserName() == null){
			model.addAttribute("user", new User());
			return "home";
		}
		
		model.addAttribute("user", user);
		return "welcome";
	}
	
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String addUser(@Valid User user, BindingResult bindingResult, 
			Model model, HttpSession session){
		
		if(bindingResult.hasErrors()){
			model.addAttribute("user", user);
			return "home";
		}
		
		userService.saveUser(user);
		model.asMap().clear();
		session.setAttribute("userName", user.getUserName());
		return "redirect:/welcome";
	}
	
	@ResponseStatus(value=HttpStatus.OK)
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String removeUser(Model model, HttpSession session){
		
		String userName = (String) session.getAttribute("userName");
		if(userName!=null){
			User user = new User();
			user.setUserName(userName);
			userService.removeUser(user);
			session.removeAttribute("userName");
		}
		
		model.addAttribute("user", new User());
		return "redirect:/home";	//register
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView conflict(HttpServletRequest req, Exception e) {
		logger.error(e.toString(), e);
		ModelAndView mav = new ModelAndView();
	    mav.addObject("message", "Username already exists!");
	    mav.setViewName("error");
		return mav;
	}
	
	@ExceptionHandler(SecurityException.class)
	public ModelAndView encryption(HttpServletRequest req, Exception e) {
		logger.error(e.getMessage(), e);
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "Error processing the request.");
		mav.setViewName("error");
		return mav;
	}
}
