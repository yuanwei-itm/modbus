package com.ruoyi.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * * @param <T> 列表数据类型
 * @author ruoyi
 */
public class TableDataInfo<T> implements Serializable // 1. 类名后加 <T>
{
    private static final long serialVersionUID = 1L;

    /** 总记录数
     * @mock 325*/
    private long total;

    /** 列表数据 */
    private List<T> rows; // 2. 把 ? 改成 T

    /** 消息状态码
     * @mock 200
     *  */
    private int code;

    /** 消息内容
     * @mock 操作成功
     * */
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     * * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) // 3. 构造方法参数也改 T
    {
        this.rows = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<T> getRows() // 4. Getter 返回 T
    {
        return rows;
    }

    public void setRows(List<T> rows) // 5. Setter 参数改 T
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}