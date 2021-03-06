/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api; // NOPMD

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonProgramStatus;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonProgramStatusService;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.impl.SecurityServiceInTestEnvironment;
import org.jasig.ssp.service.reference.ProgramStatusChangeReasonService;
import org.jasig.ssp.transferobject.PersonProgramStatusTO;
import org.jasig.ssp.transferobject.ServiceResponse;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link PersonProgramStatusController} tests
 * 
 * @author jon.adams
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../ControllerIntegrationTests-context.xml")
@TransactionConfiguration()
@Transactional
public class PersonProgramStatusControllerIntegrationTest { // NOPMD by jon

	@Autowired
	private transient PersonProgramStatusController controller;

	@Autowired
	protected transient SessionFactory sessionFactory;

	@Autowired
	protected transient PersonProgramStatusService personProgramStatusService;

	@Autowired
	protected transient ProgramStatusChangeReasonService programStatusChangeReasonService;

	@Autowired
	protected transient PersonService personService;

	private static final UUID PERSON_ID = UUID
			.fromString("1010e4a0-1001-0110-1011-4ffc02fe81ff");

	private static final UUID PROGRAM_STATUS_ID = UUID
			.fromString("b2d12527-5056-a51a-8054-113116baab88");

	private static final UUID PROGRAM_STATUS_CHANGE_REASON_ID = UUID
			.fromString("b2d127d7-5056-a51a-802d-3717c4316d29");

	@Autowired
	private transient SecurityServiceInTestEnvironment securityService;

	/**
	 * Setup the security service with the administrator user.
	 */
	@Before
	public void setUp() {
		securityService.setCurrent(new Person(Person.SYSTEM_ADMINISTRATOR_ID),
				"ROLE_PERSON_PROGRAM_STATUS_READ",
				"ROLE_PERSON_PROGRAM_STATUS_WRITE",
				"ROLE_PERSON_PROGRAM_STATUS_DELETE");
	}

	/**
	 * Test that the {@link PersonProgramStatusController#get(UUID, UUID)}
	 * action returns the correct validation errors when an invalid ID is sent.
	 * 
	 * @throws ValidationException
	 *             If validation error occurred.
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test(expected = ObjectNotFoundException.class)
	public void testControllerGetOfInvalidId() throws ObjectNotFoundException,
			ValidationException {
		assertNotNull(
				"Controller under test was not initialized by the container correctly.",
				controller);

		final PersonProgramStatusTO obj = controller.get(PERSON_ID,
				UUID.randomUUID());

		assertNull(
				"Returned PersonProgramStatusTO from the controller should have been null.",
				obj);
	}

	/**
	 * Test the
	 * {@link PersonProgramStatusController#getAll(UUID, ObjectStatus, Integer, Integer, String, String)}
	 * action.
	 * 
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test
	public void testControllerAll() throws ObjectNotFoundException {
		final Collection<PersonProgramStatusTO> list = controller.getAll(
				PERSON_ID, ObjectStatus.ACTIVE, null, null, null, null)
				.getRows();

		assertNotNull("List should not have been null.", list);
	}

	/**
	 * Test the
	 * {@link PersonProgramStatusController#create(UUID, PersonProgramStatusTO)}
	 * and {@link PersonProgramStatusController#delete(UUID, UUID)} actions.
	 * 
	 * @throws ValidationException
	 *             If validation error occurred.
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test(expected = ObjectNotFoundException.class)
	public void testControllerDelete() throws ValidationException,
			ObjectNotFoundException {
		final Date now = new Date();
		final PersonProgramStatusTO obj = createProgramStatus();
		obj.setEffectiveDate(now);
		final PersonProgramStatusTO saved = controller.create(PERSON_ID,
				obj);
		assertNotNull("Saved instance should not have been null.", saved);

		final UUID savedId = saved.getId();
		assertNotNull(
				"Saved instance identifier should not have been null.",
				savedId);

		assertEquals("Saved instance values did not match.", now,
				saved.getEffectiveDate());
		assertEquals("Saved instance sets did not match.",
				PROGRAM_STATUS_CHANGE_REASON_ID,
				saved.getProgramStatusChangeReasonId());

		final ServiceResponse response = controller.delete(savedId,
				PERSON_ID);

		assertNotNull("Deletion response should not have been null.",
				response);
		assertTrue("Deletion response did not return success.",
				response.isSuccess());

		final PersonProgramStatusTO afterDeletion = controller.get(savedId,
				PERSON_ID);
		// ObjectNotFoundException expected at this point
		assertNull(
				"Instance should not be able to get loaded after it has been deleted.",
				afterDeletion);
	}

	/**
	 * Test that the
	 * {@link PersonProgramStatusController#create(UUID, PersonProgramStatusTO)}
	 * action auto-expires old instances correctly.
	 * 
	 * @throws ValidationException
	 *             If validation error occurred.
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test
	public void testControllerCreateWithAutoExpiration()
			throws ValidationException, ObjectNotFoundException {
		final Date now = new Date();

		final PersonProgramStatusTO obj = createProgramStatus();
		obj.setEffectiveDate(now);
		final PersonProgramStatusTO saved = controller.create(PERSON_ID,
				obj);

		final UUID savedId = saved.getId();

		assertEquals("Saved effective date does not match.", now,
				saved.getEffectiveDate());
		assertEquals("Saved instance sets does not match.",
				PROGRAM_STATUS_CHANGE_REASON_ID,
				saved.getProgramStatusChangeReasonId());

		final PersonProgramStatusTO obj2 = createProgramStatus();
		obj2.setEffectiveDate(now);

		final PersonProgramStatusTO saved2 = controller.create(PERSON_ID,
				obj2);
		assertNotNull("Saved instance should not have been null.", saved2);

		final PersonProgramStatusTO autoExpired = controller.get(savedId,
				PERSON_ID);
		assertNotNull(
				"Saved instance identifier should not have been null.",
				autoExpired);

		assertNotNull(
				"Original instance should have been auto-expired when new one added.",
				autoExpired.getExpirationDate());
		assertNotNull(
				"Original instance should have had an effective date.",
				autoExpired.getEffectiveDate());
	}

	/**
	 * Test that the
	 * {@link PersonProgramStatusController#create(UUID, PersonProgramStatusTO)}
	 * action prohibits duplicates correctly.
	 * 
	 * @throws ValidationException
	 *             If validation error occurred.
	 * @throws ObjectNotFoundException
	 *             If object could not be found.
	 */
	@Test(expected = ValidationException.class)
	public void testControllerCreateDuplicateProhibition()
			throws ValidationException, ObjectNotFoundException {
		final Date now = new Date();
		final PersonProgramStatusTO obj = createProgramStatus();
		obj.setEffectiveDate(now);
		final PersonProgramStatusTO saved = controller.create(PERSON_ID,
				obj);

		final UUID savedId = saved.getId();

		assertEquals("Saved instance values did not match.", now,
				saved.getEffectiveDate());
		assertEquals("Saved instance sets did not match.",
				PROGRAM_STATUS_CHANGE_REASON_ID,
				saved.getProgramStatusChangeReasonId());

		final PersonProgramStatusTO obj2 = createProgramStatus();

		final PersonProgramStatusTO saved2 = controller.create(PERSON_ID,
				obj2);
		assertNotNull("Saved instance should not have been null.", saved2);

		final PersonProgramStatusTO autoExpired = controller.get(savedId,
				PERSON_ID);
		assertNotNull(
				"Saved instance identifier should not have been null.",
				autoExpired);

		assertNotNull(
				"Original instance should have been auto-expired when new one added.",
				autoExpired.getExpirationDate());

		saved2.setExpirationDate(null);
		controller.save(saved2.getId(), PERSON_ID, saved2);
	}

	@Test(expected = ValidationException.class)
	public void testControllerCreateWithInvalidDataGetId()
			throws ValidationException, ObjectNotFoundException {
		final PersonProgramStatusTO obj = new PersonProgramStatusTO();
		obj.setId(UUID.randomUUID());
		obj.setEffectiveDate(null);
		controller.create(UUID.randomUUID(), obj);
		fail("Create with invalid Person UUID should have thrown exception.");
	}

	public static PersonProgramStatusTO createProgramStatus() {
		final PersonProgramStatusTO obj = new PersonProgramStatusTO();
		obj.setPersonId(PERSON_ID);
		obj.setObjectStatus(ObjectStatus.ACTIVE);
		obj.setProgramStatusId(PROGRAM_STATUS_ID);
		obj.setProgramStatusChangeReasonId(PROGRAM_STATUS_CHANGE_REASON_ID);
		return obj;
	}

	@Test
	public void testGetCurrent() throws ObjectNotFoundException,
			ValidationException {

		final PersonProgramStatus status = personProgramStatusService
				.getCurrent(PERSON_ID);

		final Date yesterday = new Date(new Date().getTime()
				- (24 * 60 * 60 * 1000));
		status.setExpirationDate(yesterday);

		// arrange, act
		final PersonProgramStatusTO programStatus = controller
				.getCurrent(PERSON_ID);

		// assert
		assertNull(
				"No PersonProgramStatus should have been returned as they are all currently expired.",
				programStatus);
	}

	/**
	 * Test that getLogger() returns the matching log class name for the current
	 * class under test.
	 */
	@Test
	public void testLogger() {
		final Logger logger = controller.getLogger();
		assertEquals("Log class name did not match.", controller.getClass()
				.getName(), logger.getName());
	}
}