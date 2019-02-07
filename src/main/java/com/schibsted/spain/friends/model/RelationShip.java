package com.schibsted.spain.friends.model;

import java.util.Objects;

public class RelationShip {
	private User x;
	private User y;

	public RelationShip(User x, User y) {
		if (x.equals(y)) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RelationShip that = (RelationShip) o;
		return (x.equals(that.x) && y.equals(that.y))
				|| (x.equals(that.y) && y.equals(that.x));
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
