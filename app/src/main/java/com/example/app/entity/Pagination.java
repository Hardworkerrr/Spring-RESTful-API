package com.example.app.entity;

public class Pagination {
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    public Pagination() {

    }

    public Pagination(long totalElements, int pageNumber, int pageSize) {
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long total) {
        this.totalElements = total;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
