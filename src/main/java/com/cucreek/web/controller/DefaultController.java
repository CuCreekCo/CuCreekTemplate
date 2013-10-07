package com.cucreek.web.controller;

import com.cucreek.common.SystemConstants;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;

/**
 * Handles all root "/" web requests
 *
 * @author jljdavidson
 */
@Controller
@RequestMapping("/")
public class DefaultController {

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("date",
            DateFormatUtils.format(Calendar.getInstance().getTime(), SystemConstants.DATE_FORMAT_DISPLAY));
		return "index";
	}
}