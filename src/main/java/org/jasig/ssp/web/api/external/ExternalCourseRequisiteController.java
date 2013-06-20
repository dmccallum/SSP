package org.jasig.ssp.web.api.external;

import java.util.List;

import org.jasig.ssp.factory.external.ExternalCourseRequisiteTOFactory;
import org.jasig.ssp.model.external.ExternalCourse;
import org.jasig.ssp.model.external.ExternalCourseRequisite;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.external.ExternalCourseRequisiteService;
import org.jasig.ssp.transferobject.external.ExternalCourseRequisiteTO;
import org.jasig.ssp.transferobject.external.ExternalCourseTO;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/1/reference/courserequisites")
public class ExternalCourseRequisiteController
		extends
		AbstractExternalController<ExternalCourseRequisiteTO, ExternalCourseRequisite> {


	@Autowired
	protected transient ExternalCourseRequisiteService service;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExternalCourseController.class);


	@Override
	protected ExternalCourseRequisiteService getService() {
		return service;
	}

	@Autowired
	protected transient ExternalCourseRequisiteTOFactory factory;

	@Override
	protected ExternalCourseRequisiteTOFactory getFactory() {
		return factory;
	}

	protected ExternalCourseRequisiteController() {
		super(ExternalCourseRequisiteTO.class, ExternalCourseRequisite.class);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@RequestMapping(value = "/{requiringCourseCode}", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REFERENCE_READ)
	public @ResponseBody
	List<ExternalCourseRequisiteTO> get(final @PathVariable String requiringCourseCode) throws ObjectNotFoundException,
			ValidationException {
		final List<ExternalCourseRequisite> models = getService().getRequisitesForCourse(requiringCourseCode);
		if (models == null ) {
			return null;
		}
		List<ExternalCourseRequisiteTO> externalCourseTOs = getFactory().asTOList(models);

		return externalCourseTOs;
	}

}
