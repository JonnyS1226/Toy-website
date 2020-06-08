package com.academic.calendar.entity;

public class Page {
    //页面要传入当前页码
    private int current = 1;
    //页面要传入最多要显示多少条数据
    private int limit = 10;
    //自己查出来数据的总数（用于计算总页数）
    private int rows;
    //查询路径（用于复用分页链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
        // current * limit - limit
        return (current * limit) - limit;
    }

    /**
     * 计算总页数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0)
        {
            return rows / limit;
        }
        else
        {
            return rows / limit + 1;
        }
    }

    /**
     * 下面两个方法：
     * 从第几页显示到第几页（如显示离当前页最近的两页）
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1? 1 : from;
    }
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total? total : to;
    }
}
