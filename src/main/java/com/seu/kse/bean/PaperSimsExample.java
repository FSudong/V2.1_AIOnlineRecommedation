package com.seu.kse.bean;

import java.util.ArrayList;
import java.util.List;

public class PaperSimsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PaperSimsExample() {
        oredCriteria = new ArrayList<Criteria>();
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
            criteria = new ArrayList<Criterion>();
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

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(String value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(String value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(String value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(String value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(String value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(String value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLike(String value) {
            addCriterion("pid like", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotLike(String value) {
            addCriterion("pid not like", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<String> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<String> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(String value1, String value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(String value1, String value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPid1IsNull() {
            addCriterion("pid1 is null");
            return (Criteria) this;
        }

        public Criteria andPid1IsNotNull() {
            addCriterion("pid1 is not null");
            return (Criteria) this;
        }

        public Criteria andPid1EqualTo(String value) {
            addCriterion("pid1 =", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1NotEqualTo(String value) {
            addCriterion("pid1 <>", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1GreaterThan(String value) {
            addCriterion("pid1 >", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1GreaterThanOrEqualTo(String value) {
            addCriterion("pid1 >=", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1LessThan(String value) {
            addCriterion("pid1 <", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1LessThanOrEqualTo(String value) {
            addCriterion("pid1 <=", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1Like(String value) {
            addCriterion("pid1 like", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1NotLike(String value) {
            addCriterion("pid1 not like", value, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1In(List<String> values) {
            addCriterion("pid1 in", values, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1NotIn(List<String> values) {
            addCriterion("pid1 not in", values, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1Between(String value1, String value2) {
            addCriterion("pid1 between", value1, value2, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid1NotBetween(String value1, String value2) {
            addCriterion("pid1 not between", value1, value2, "pid1");
            return (Criteria) this;
        }

        public Criteria andPid2IsNull() {
            addCriterion("pid2 is null");
            return (Criteria) this;
        }

        public Criteria andPid2IsNotNull() {
            addCriterion("pid2 is not null");
            return (Criteria) this;
        }

        public Criteria andPid2EqualTo(String value) {
            addCriterion("pid2 =", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2NotEqualTo(String value) {
            addCriterion("pid2 <>", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2GreaterThan(String value) {
            addCriterion("pid2 >", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2GreaterThanOrEqualTo(String value) {
            addCriterion("pid2 >=", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2LessThan(String value) {
            addCriterion("pid2 <", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2LessThanOrEqualTo(String value) {
            addCriterion("pid2 <=", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2Like(String value) {
            addCriterion("pid2 like", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2NotLike(String value) {
            addCriterion("pid2 not like", value, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2In(List<String> values) {
            addCriterion("pid2 in", values, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2NotIn(List<String> values) {
            addCriterion("pid2 not in", values, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2Between(String value1, String value2) {
            addCriterion("pid2 between", value1, value2, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid2NotBetween(String value1, String value2) {
            addCriterion("pid2 not between", value1, value2, "pid2");
            return (Criteria) this;
        }

        public Criteria andPid3IsNull() {
            addCriterion("pid3 is null");
            return (Criteria) this;
        }

        public Criteria andPid3IsNotNull() {
            addCriterion("pid3 is not null");
            return (Criteria) this;
        }

        public Criteria andPid3EqualTo(String value) {
            addCriterion("pid3 =", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3NotEqualTo(String value) {
            addCriterion("pid3 <>", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3GreaterThan(String value) {
            addCriterion("pid3 >", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3GreaterThanOrEqualTo(String value) {
            addCriterion("pid3 >=", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3LessThan(String value) {
            addCriterion("pid3 <", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3LessThanOrEqualTo(String value) {
            addCriterion("pid3 <=", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3Like(String value) {
            addCriterion("pid3 like", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3NotLike(String value) {
            addCriterion("pid3 not like", value, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3In(List<String> values) {
            addCriterion("pid3 in", values, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3NotIn(List<String> values) {
            addCriterion("pid3 not in", values, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3Between(String value1, String value2) {
            addCriterion("pid3 between", value1, value2, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid3NotBetween(String value1, String value2) {
            addCriterion("pid3 not between", value1, value2, "pid3");
            return (Criteria) this;
        }

        public Criteria andPid4IsNull() {
            addCriterion("pid4 is null");
            return (Criteria) this;
        }

        public Criteria andPid4IsNotNull() {
            addCriterion("pid4 is not null");
            return (Criteria) this;
        }

        public Criteria andPid4EqualTo(String value) {
            addCriterion("pid4 =", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4NotEqualTo(String value) {
            addCriterion("pid4 <>", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4GreaterThan(String value) {
            addCriterion("pid4 >", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4GreaterThanOrEqualTo(String value) {
            addCriterion("pid4 >=", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4LessThan(String value) {
            addCriterion("pid4 <", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4LessThanOrEqualTo(String value) {
            addCriterion("pid4 <=", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4Like(String value) {
            addCriterion("pid4 like", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4NotLike(String value) {
            addCriterion("pid4 not like", value, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4In(List<String> values) {
            addCriterion("pid4 in", values, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4NotIn(List<String> values) {
            addCriterion("pid4 not in", values, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4Between(String value1, String value2) {
            addCriterion("pid4 between", value1, value2, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid4NotBetween(String value1, String value2) {
            addCriterion("pid4 not between", value1, value2, "pid4");
            return (Criteria) this;
        }

        public Criteria andPid5IsNull() {
            addCriterion("pid5 is null");
            return (Criteria) this;
        }

        public Criteria andPid5IsNotNull() {
            addCriterion("pid5 is not null");
            return (Criteria) this;
        }

        public Criteria andPid5EqualTo(String value) {
            addCriterion("pid5 =", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5NotEqualTo(String value) {
            addCriterion("pid5 <>", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5GreaterThan(String value) {
            addCriterion("pid5 >", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5GreaterThanOrEqualTo(String value) {
            addCriterion("pid5 >=", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5LessThan(String value) {
            addCriterion("pid5 <", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5LessThanOrEqualTo(String value) {
            addCriterion("pid5 <=", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5Like(String value) {
            addCriterion("pid5 like", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5NotLike(String value) {
            addCriterion("pid5 not like", value, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5In(List<String> values) {
            addCriterion("pid5 in", values, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5NotIn(List<String> values) {
            addCriterion("pid5 not in", values, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5Between(String value1, String value2) {
            addCriterion("pid5 between", value1, value2, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid5NotBetween(String value1, String value2) {
            addCriterion("pid5 not between", value1, value2, "pid5");
            return (Criteria) this;
        }

        public Criteria andPid6IsNull() {
            addCriterion("pid6 is null");
            return (Criteria) this;
        }

        public Criteria andPid6IsNotNull() {
            addCriterion("pid6 is not null");
            return (Criteria) this;
        }

        public Criteria andPid6EqualTo(String value) {
            addCriterion("pid6 =", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6NotEqualTo(String value) {
            addCriterion("pid6 <>", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6GreaterThan(String value) {
            addCriterion("pid6 >", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6GreaterThanOrEqualTo(String value) {
            addCriterion("pid6 >=", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6LessThan(String value) {
            addCriterion("pid6 <", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6LessThanOrEqualTo(String value) {
            addCriterion("pid6 <=", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6Like(String value) {
            addCriterion("pid6 like", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6NotLike(String value) {
            addCriterion("pid6 not like", value, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6In(List<String> values) {
            addCriterion("pid6 in", values, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6NotIn(List<String> values) {
            addCriterion("pid6 not in", values, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6Between(String value1, String value2) {
            addCriterion("pid6 between", value1, value2, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid6NotBetween(String value1, String value2) {
            addCriterion("pid6 not between", value1, value2, "pid6");
            return (Criteria) this;
        }

        public Criteria andPid7IsNull() {
            addCriterion("pid7 is null");
            return (Criteria) this;
        }

        public Criteria andPid7IsNotNull() {
            addCriterion("pid7 is not null");
            return (Criteria) this;
        }

        public Criteria andPid7EqualTo(String value) {
            addCriterion("pid7 =", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7NotEqualTo(String value) {
            addCriterion("pid7 <>", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7GreaterThan(String value) {
            addCriterion("pid7 >", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7GreaterThanOrEqualTo(String value) {
            addCriterion("pid7 >=", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7LessThan(String value) {
            addCriterion("pid7 <", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7LessThanOrEqualTo(String value) {
            addCriterion("pid7 <=", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7Like(String value) {
            addCriterion("pid7 like", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7NotLike(String value) {
            addCriterion("pid7 not like", value, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7In(List<String> values) {
            addCriterion("pid7 in", values, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7NotIn(List<String> values) {
            addCriterion("pid7 not in", values, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7Between(String value1, String value2) {
            addCriterion("pid7 between", value1, value2, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid7NotBetween(String value1, String value2) {
            addCriterion("pid7 not between", value1, value2, "pid7");
            return (Criteria) this;
        }

        public Criteria andPid8IsNull() {
            addCriterion("pid8 is null");
            return (Criteria) this;
        }

        public Criteria andPid8IsNotNull() {
            addCriterion("pid8 is not null");
            return (Criteria) this;
        }

        public Criteria andPid8EqualTo(String value) {
            addCriterion("pid8 =", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8NotEqualTo(String value) {
            addCriterion("pid8 <>", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8GreaterThan(String value) {
            addCriterion("pid8 >", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8GreaterThanOrEqualTo(String value) {
            addCriterion("pid8 >=", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8LessThan(String value) {
            addCriterion("pid8 <", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8LessThanOrEqualTo(String value) {
            addCriterion("pid8 <=", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8Like(String value) {
            addCriterion("pid8 like", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8NotLike(String value) {
            addCriterion("pid8 not like", value, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8In(List<String> values) {
            addCriterion("pid8 in", values, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8NotIn(List<String> values) {
            addCriterion("pid8 not in", values, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8Between(String value1, String value2) {
            addCriterion("pid8 between", value1, value2, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid8NotBetween(String value1, String value2) {
            addCriterion("pid8 not between", value1, value2, "pid8");
            return (Criteria) this;
        }

        public Criteria andPid9IsNull() {
            addCriterion("pid9 is null");
            return (Criteria) this;
        }

        public Criteria andPid9IsNotNull() {
            addCriterion("pid9 is not null");
            return (Criteria) this;
        }

        public Criteria andPid9EqualTo(String value) {
            addCriterion("pid9 =", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9NotEqualTo(String value) {
            addCriterion("pid9 <>", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9GreaterThan(String value) {
            addCriterion("pid9 >", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9GreaterThanOrEqualTo(String value) {
            addCriterion("pid9 >=", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9LessThan(String value) {
            addCriterion("pid9 <", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9LessThanOrEqualTo(String value) {
            addCriterion("pid9 <=", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9Like(String value) {
            addCriterion("pid9 like", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9NotLike(String value) {
            addCriterion("pid9 not like", value, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9In(List<String> values) {
            addCriterion("pid9 in", values, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9NotIn(List<String> values) {
            addCriterion("pid9 not in", values, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9Between(String value1, String value2) {
            addCriterion("pid9 between", value1, value2, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid9NotBetween(String value1, String value2) {
            addCriterion("pid9 not between", value1, value2, "pid9");
            return (Criteria) this;
        }

        public Criteria andPid10IsNull() {
            addCriterion("pid10 is null");
            return (Criteria) this;
        }

        public Criteria andPid10IsNotNull() {
            addCriterion("pid10 is not null");
            return (Criteria) this;
        }

        public Criteria andPid10EqualTo(String value) {
            addCriterion("pid10 =", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10NotEqualTo(String value) {
            addCriterion("pid10 <>", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10GreaterThan(String value) {
            addCriterion("pid10 >", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10GreaterThanOrEqualTo(String value) {
            addCriterion("pid10 >=", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10LessThan(String value) {
            addCriterion("pid10 <", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10LessThanOrEqualTo(String value) {
            addCriterion("pid10 <=", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10Like(String value) {
            addCriterion("pid10 like", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10NotLike(String value) {
            addCriterion("pid10 not like", value, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10In(List<String> values) {
            addCriterion("pid10 in", values, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10NotIn(List<String> values) {
            addCriterion("pid10 not in", values, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10Between(String value1, String value2) {
            addCriterion("pid10 between", value1, value2, "pid10");
            return (Criteria) this;
        }

        public Criteria andPid10NotBetween(String value1, String value2) {
            addCriterion("pid10 not between", value1, value2, "pid10");
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