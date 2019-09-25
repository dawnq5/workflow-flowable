package com.migu.se.core.workflow.util;


/**
 * 分页工具类
 * @author haow
 *
 */
public class PageUtil {

    public static int PAGE_SIZE = 5;

    public static int[] init(Page<?> page, Integer pageNo,Integer pageSize) {
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        int firstResult = page.getFirst() - 1;
        int maxResults = page.getPageSize();
        return new int[]{firstResult, maxResults};
    }

}
