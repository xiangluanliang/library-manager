package com.guo.domain;

import java.util.ArrayList;
import java.util.List;

public class BookInventoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BookInventoryExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andInventoryIdIsNull() {
            addCriterion("inventory_id is null");
            return (Criteria) this;
        }

        public Criteria andInventoryIdIsNotNull() {
            addCriterion("inventory_id is not null");
            return (Criteria) this;
        }

        public Criteria andInventoryIdEqualTo(Integer value) {
            addCriterion("inventory_id =", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdNotEqualTo(Integer value) {
            addCriterion("inventory_id <>", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdGreaterThan(Integer value) {
            addCriterion("inventory_id >", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("inventory_id >=", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdLessThan(Integer value) {
            addCriterion("inventory_id <", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdLessThanOrEqualTo(Integer value) {
            addCriterion("inventory_id <=", value, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdIn(List<Integer> values) {
            addCriterion("inventory_id in", values, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdNotIn(List<Integer> values) {
            addCriterion("inventory_id not in", values, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdBetween(Integer value1, Integer value2) {
            addCriterion("inventory_id between", value1, value2, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andInventoryIdNotBetween(Integer value1, Integer value2) {
            addCriterion("inventory_id not between", value1, value2, "inventoryId");
            return (Criteria) this;
        }

        public Criteria andBookIdIsNull() {
            addCriterion("book_id is null");
            return (Criteria) this;
        }

        public Criteria andBookIdIsNotNull() {
            addCriterion("book_id is not null");
            return (Criteria) this;
        }

        public Criteria andBookIdEqualTo(Integer value) {
            addCriterion("book_id =", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotEqualTo(Integer value) {
            addCriterion("book_id <>", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdGreaterThan(Integer value) {
            addCriterion("book_id >", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("book_id >=", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdLessThan(Integer value) {
            addCriterion("book_id <", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdLessThanOrEqualTo(Integer value) {
            addCriterion("book_id <=", value, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdIn(List<Integer> values) {
            addCriterion("book_id in", values, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotIn(List<Integer> values) {
            addCriterion("book_id not in", values, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdBetween(Integer value1, Integer value2) {
            addCriterion("book_id between", value1, value2, "bookId");
            return (Criteria) this;
        }

        public Criteria andBookIdNotBetween(Integer value1, Integer value2) {
            addCriterion("book_id not between", value1, value2, "bookId");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesIsNull() {
            addCriterion("total_copies is null");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesIsNotNull() {
            addCriterion("total_copies is not null");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesEqualTo(Integer value) {
            addCriterion("total_copies =", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesNotEqualTo(Integer value) {
            addCriterion("total_copies <>", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesGreaterThan(Integer value) {
            addCriterion("total_copies >", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_copies >=", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesLessThan(Integer value) {
            addCriterion("total_copies <", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesLessThanOrEqualTo(Integer value) {
            addCriterion("total_copies <=", value, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesIn(List<Integer> values) {
            addCriterion("total_copies in", values, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesNotIn(List<Integer> values) {
            addCriterion("total_copies not in", values, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesBetween(Integer value1, Integer value2) {
            addCriterion("total_copies between", value1, value2, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andTotalCopiesNotBetween(Integer value1, Integer value2) {
            addCriterion("total_copies not between", value1, value2, "totalCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesIsNull() {
            addCriterion("available_copies is null");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesIsNotNull() {
            addCriterion("available_copies is not null");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesEqualTo(Integer value) {
            addCriterion("available_copies =", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesNotEqualTo(Integer value) {
            addCriterion("available_copies <>", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesGreaterThan(Integer value) {
            addCriterion("available_copies >", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesGreaterThanOrEqualTo(Integer value) {
            addCriterion("available_copies >=", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesLessThan(Integer value) {
            addCriterion("available_copies <", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesLessThanOrEqualTo(Integer value) {
            addCriterion("available_copies <=", value, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesIn(List<Integer> values) {
            addCriterion("available_copies in", values, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesNotIn(List<Integer> values) {
            addCriterion("available_copies not in", values, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesBetween(Integer value1, Integer value2) {
            addCriterion("available_copies between", value1, value2, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andAvailableCopiesNotBetween(Integer value1, Integer value2) {
            addCriterion("available_copies not between", value1, value2, "availableCopies");
            return (Criteria) this;
        }

        public Criteria andLocationIsNull() {
            addCriterion("location is null");
            return (Criteria) this;
        }

        public Criteria andLocationIsNotNull() {
            addCriterion("location is not null");
            return (Criteria) this;
        }

        public Criteria andLocationEqualTo(String value) {
            addCriterion("location =", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotEqualTo(String value) {
            addCriterion("location <>", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThan(String value) {
            addCriterion("location >", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThanOrEqualTo(String value) {
            addCriterion("location >=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThan(String value) {
            addCriterion("location <", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThanOrEqualTo(String value) {
            addCriterion("location <=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLike(String value) {
            addCriterion("location like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotLike(String value) {
            addCriterion("location not like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationIn(List<String> values) {
            addCriterion("location in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotIn(List<String> values) {
            addCriterion("location not in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationBetween(String value1, String value2) {
            addCriterion("location between", value1, value2, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotBetween(String value1, String value2) {
            addCriterion("location not between", value1, value2, "location");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}