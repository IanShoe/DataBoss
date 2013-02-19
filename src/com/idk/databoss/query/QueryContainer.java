/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.query;

/**
 * Container to facilitate Query building using the reflective system
 * TODO: this guy should have construction methods for the user perhaps.
 * ^ This might solve awesomeness.
 *
 * @author shoemaki
 */
public class QueryContainer {

    private String delete;
    private String from;
    private String innerJoin;
    private String insert;
    private String list;
    private String outerJoin;
    private String select;
    private String update;
    private String value;
    private String where;

    public QueryContainer(String delete, String from, String innerJoin, String list, String outerJoin, String select, String update, String where) {
        this.delete = delete;
        this.from = from;
        this.innerJoin = innerJoin;
        this.list = list;
        this.outerJoin = outerJoin;
        this.select = select;
        this.update = update;
        this.where = where;
    }

    public QueryContainer() {
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getInnerJoin() {
        return innerJoin;
    }

    public void setInnerJoin(String innerJoin) {
        this.innerJoin = innerJoin;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getOuterJoin() {
        return outerJoin;
    }

    public void setOuterJoin(String outerJoin) {
        this.outerJoin = outerJoin;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String buildInsert() {
        return this.insert + " " + this.value;
    }

    public String buildSelect() {
        //TODO: figure out where and joins
        return this.select + " " + this.from + " " + this.innerJoin;
    }
}