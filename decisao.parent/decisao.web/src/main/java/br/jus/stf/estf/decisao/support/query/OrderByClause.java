package br.jus.stf.estf.decisao.support.query;

import java.util.ArrayList;
import java.util.List;

public class OrderByClause {

	private List<Item> clauseItems = new ArrayList<Item>();

	private OrderByClause() {

	}

	public List<Item> getClauseItems() {
		return clauseItems;
	}

	public void setClauseItems(List<Item> clauseItems) {
		this.clauseItems = clauseItems;
	}

	public static OrderByClause buildOrderBy() {
		return new OrderByClause();
	}

	public OrderByClause addItem(String column, String extras) {
		clauseItems.add(new Item(column, extras));
		return this;
	}

	public String getProjection() {
		String projection = "";
		for (Item i : clauseItems) {
			projection += i.getColumn() + ", ";
		}
		return projection;
	}

	public String getFullClause() {
		String fullClause = "";
		for (Item i : clauseItems) {
			fullClause += i.getColumn() + " " + i.getExtras() + ", ";
		}
		return fullClause;
	}

	public static class Item {

		private String column;
		private String extras;

		public Item(String column, String extras) {
			this.column = column;
			this.extras = extras;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getExtras() {
			return extras;
		}

		public void setExtras(String extras) {
			this.extras = extras;
		}

	}

	public static final OrderByClause EMPTY_ORDER_BY = new OrderByClause();
	
}
