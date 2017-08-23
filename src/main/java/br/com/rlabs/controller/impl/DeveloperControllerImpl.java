package br.com.rlabs.controller.impl;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.rlabs.config.ResourceMessage;
import br.com.rlabs.controller.DeveloperController;
import br.com.rlabs.entity.model.Developer;
import br.com.rlabs.exceptions.UniqueConstraintException;
import br.com.rlabs.service.DeveloperService;
import br.com.rlabs.service.OrganizationService;

/**
 * The Developer WEB-MVC Controller.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
@Controller
@RequestMapping(value = "/configuration/developer")
public class DeveloperControllerImpl implements DeveloperController {

	@Autowired
	private DeveloperService service;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ResourceMessage message;

	private static final String FORM = "modules/configuration/developer-form";
	private static final String FORM_LIST = "modules/configuration/developer-list";
	private static final String REDIRECT = "redirect:/configuration/developer";

	@Override
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form() {
		final ModelAndView modelAndView = new ModelAndView(FORM);
		modelAndView.addObject(new Developer());
		modelAndView.addObject("organizations", organizationService.list());
		return modelAndView;
	}

	@Override
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView = new ModelAndView(FORM_LIST);
		modelAndView.addObject("developers", service.list());
		return modelAndView;
	}

	@Override
	@RequestMapping(value = "/form/{internal}", method = RequestMethod.GET)
	public ModelAndView getByInternal(@PathVariable("internal") UUID internal) {
		final ModelAndView modelAndView = new ModelAndView(FORM);
		modelAndView.addObject("developer", service.getByInternal(internal));
		modelAndView.addObject("organizations", organizationService.list());
		return modelAndView;
	}

	@Override
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public ModelAndView persist(@Valid Developer developer, BindingResult result) {
		final ModelAndView modelAndView = new ModelAndView(FORM);
		modelAndView.addObject("organizations", organizationService.list());

		if (!result.hasErrors()) {
			try {
				service.persist(developer);
				modelAndView.setViewName(REDIRECT);
			} catch (UniqueConstraintException e) {
				modelAndView.addObject("error", e.getMessage());
			}
		}

		return modelAndView;
	}

	@Override
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@ModelAttribute("internal") UUID internal) {
		final ModelAndView modelAndView = new ModelAndView(FORM_LIST);

		try {
			service.delete(internal);
		} catch (DataIntegrityViolationException e) {
			modelAndView.addObject("error", message.getString("message.constraint.delete"));
		}

		modelAndView.addObject("developers", service.list());
		return modelAndView;
	}

}
