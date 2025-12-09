package com.ruoyi.system.domain.vo;

import java.util.List;

// 分页结果VO，仅用于格式封装
public class PageResultVO {
    private Integer pagenum;    // 对应示例的pagenum
    private Integer pagesize;   // 对应示例的pagesize
    private Long total;         // 总条数
    private List<?> list;       // 数据列表（泛型适配任意实体）

    // 构造方法（快速赋值）
    public PageResultVO(Integer pagenum, Integer pagesize, Long total, List<?> list) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.total = total;
        this.list = list;
    }

    // getter/setter（必须，否则JSON序列化会丢失字段）
    public Integer getPagenum() { return pagenum; }
    public void setPagenum(Integer pagenum) { this.pagenum = pagenum; }
    public Integer getPagesize() { return pagesize; }
    public void setPagesize(Integer pagesize) { this.pagesize = pagesize; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public List<?> getList() { return list; }
    public void setList(List<?> list) { this.list = list; }
}