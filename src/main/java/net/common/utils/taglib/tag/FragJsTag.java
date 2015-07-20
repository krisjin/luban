package net.common.utils.taglib.tag;


import net.common.utils.resourceload.ResourceFileURLUtil;
import net.common.utils.taglib.ResourceType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * JavaScript资源标签
 * <p/>
 * User : dongyong.wang@mail-inc.com
 * Date: 12-05-09
 * Time: 下午2:15
 */
public class FragJsTag extends BaseResourceFragTag {

    private static final long serialVersionUID = -505812863133115362L;

    public FragJsTag() {
        super(ResourceType.JAVA_SCRIPT);
    }

    @Override
    protected void renderHtmlBeginTag() throws JspException {

        final JspWriter out = this.pageContext.getOut();
        if (this.getSrc() != null) {
            // 显示外链的javascript标签
            String src = this.getRealSrc(this.getSrc());
            if (src == null) {
                src = this.getSrc();
            }
            final String link = "<script type='text/javascript' src='" + src + "'></script>";
            try {
                out.write(link);
            } catch (IOException e) {
                throw new JspException(e);
            }
        } else {
            // 显示内联标签
            try {
                out.write("<script type='text/javascript'>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }

    @Override
    protected void renderHtmlEndTag() throws JspException {
        if (this.getSrc() != null) {
            return;
        }
        final JspWriter out = this.pageContext.getOut();
        try {
            out.write("</script>");
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    @Override
    protected String getRealSrc(final String src) {
        // 获取真实地址 : 用于测试环境和生产环境的切换
        String realSrc = ResourceFileURLUtil.getJsUrl(this.getSrc(), (HttpServletRequest) this.pageContext.getRequest(), this.getPlat());
        if (realSrc == null) {
            realSrc = src;
        }
        return realSrc;
    }

}
