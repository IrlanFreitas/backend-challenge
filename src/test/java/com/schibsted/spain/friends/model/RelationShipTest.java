package com.schibsted.spain.friends.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RelationShipTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUsersAreSame() {
		new RelationShip(new User("Pepito"), new User("Pepito"));
	}

	@Test
	public void checkBidirectionalRelation() {
		RelationShip juanAndPepe = new RelationShip(
				new User("Juanito"),
				new User("Pepito")
		);

		RelationShip pepeAndJuaj = new RelationShip(
				new User("Juanito"),
				new User("Pepito")
		);

		assertEquals(juanAndPepe, pepeAndJuaj);
	}
}
